package net.brokentrain.bandsaw.views;

import java.util.Iterator;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.actions.CopyAction;
import net.brokentrain.bandsaw.actions.JumpAction;
import net.brokentrain.bandsaw.actions.QuickFilterAction;
import net.brokentrain.bandsaw.actions.ShowDetailAction;
import net.brokentrain.bandsaw.listeners.IMouseListener;
import net.brokentrain.bandsaw.listeners.LifecycleListener;
import net.brokentrain.bandsaw.log4j.ColumnList;
import net.brokentrain.bandsaw.log4j.Log4jServer;
import net.brokentrain.bandsaw.preferences.Log4jColumnsPreferencePage;
import net.brokentrain.bandsaw.preferences.Log4jPreferencePage;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

public class BandsawView extends ViewPart {

    private TableViewer viewer;
    private GridData gridData;
    private Label filterBaseLabel;
    private Label filterLabel;

    Action viewItemAction, copyItemAction, deleteItemAction, selectAllAction, jumpAction;

    /**
     * The constructor.
     */
    public BandsawView() {
        super();
        Log4jPreferencePage.initializeDefaults();
        BandsawUtilities.setView(this);
    }

    public void createPartControl(final Composite parent) {

        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();

        GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);

        Composite labelArea = new Composite(parent, SWT.NONE);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.exclude = false;
        labelArea.setLayout(new GridLayout(2, false));
        labelArea.setLayoutData(gridData);

        filterBaseLabel = new Label(labelArea,SWT.NONE);
        filterBaseLabel.setText("Quick Filter: ");
        gridData = new GridData();
        filterBaseLabel.setLayoutData(gridData);

        filterLabel = new Label(labelArea,SWT.NONE);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        filterLabel.setLayoutData(gridData);

        filterLabel.addMouseListener(new MouseListener() {
            public void mouseDoubleClick(MouseEvent e) {
                new QuickFilterAction().run(null);
            }

            public void mouseDown(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            public void mouseUp(MouseEvent e) {
                // TODO Auto-generated method stub
            }
        });

        BandsawUtilities.updateTitleBar(null);

        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        viewer.setContentProvider(new BandsawViewContentProvider());
        viewer.setLabelProvider(new BandsawViewLabelProvider());
        viewer.setSorter(new BandsawViewSorter());
        viewer.setInput(getViewSite());

        gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
        gridData.horizontalSpan = 3;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;

        final Table table = viewer.getTable();
        table.setLayoutData(gridData);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        BandsawUtilities.setTable(table);
        BandsawUtilities.setTableViewer(viewer);

        ColumnList.getInstance().setColList(
                ColumnList.deSerialize(
                    store.getString(Log4jColumnsPreferencePage.P_COLUMNS)));

        // Create ColumnList.COL_COUNT columns
        for (int i = 0; i < ColumnList.getInstance().getColumnCount(); i++) {
            new TableColumn(table, SWT.NONE);
        }

        getSite().getPage().addPartListener(new LifecycleListener());

        activateContext();

        Log4jServer.init();

        createActions();
        createContextMenu();
        hookGlobalActions();

        BandsawUtilities.updateTableColumns();

        BandsawUtilities.initColorDefaults();

        BandsawUtilities.resetTableRows();

        BandsawUtilities.updateColors();

        BandsawUtilities.updateTableColumnWidths();

        BandsawUtilities.setViewTitle(table.getParent().getShell().getText());

        hookDoubleClickAction();
    }

    private void hookGlobalActions() {
        IActionBars bars = getViewSite().getActionBars();
        bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyItemAction);
    }

    /**
     * Activate a context that this view uses. It will be tied to this view
     * activation events and will be removed when the view is disposed.
     * Copied from org.eclipse.ui.examples.contributions.InfoView.java
     */
    private void activateContext() {
        IContextService contextService = (IContextService) getSite()
        .getService(IContextService.class);
        contextService.activateContext("net.brokentrain.view.context");
    }

    public void createActions() {

        IHandlerService hs = (IHandlerService)getSite().getService(IHandlerService.class);

        jumpAction = new JumpAction("Go to");
        jumpAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
        jumpAction.setActionDefinitionId("net.brokentrain.commands.jump");
        hs.activateHandler("net.brokentrain.commands.jump", new ActionHandler(jumpAction));

        viewItemAction = new Action("View") {
            public void run() {
                new ShowDetailAction().run();
            }
        };
        viewItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_DEF_VIEW));
        viewItemAction.setActionDefinitionId("net.brokentrain.commands.view");
        hs.activateHandler("net.brokentrain.commands.view", new ActionHandler(viewItemAction));

        copyItemAction = new CopyAction("Copy");
        copyItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        copyItemAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_COPY);

        deleteItemAction = new Action("Delete") {
            public void run() {
                deleteItem();
            }
        };
        deleteItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        deleteItemAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_DELETE);

        selectAllAction = new Action("Select All") {
            public void run() {
                selectAll();
            }
        };
        selectAllAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_SELECT_ALL);

        // Add selection listener.
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                updateActionEnablement();
            }
        });
    }

   private void updateActionEnablement() {
       IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
       jumpAction.setEnabled(sel.size() > 0);
       viewItemAction.setEnabled(sel.size() > 0);
       deleteItemAction.setEnabled(sel.size() > 0);
       copyItemAction.setEnabled(sel.size() > 0);
   }

    private void createContextMenu() {
        // Create menu manager.
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(mgr);
            }
        });

        // Create menu.
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);

        // Register menu for extension.
        getSite().registerContextMenu(menuMgr, viewer);
    }

    private void fillContextMenu(IMenuManager mgr) {
        mgr.add(jumpAction);
        mgr.add(viewItemAction);
        mgr.add(new Separator());
        mgr.add(copyItemAction);
        mgr.add(deleteItemAction);
        mgr.add(selectAllAction);
    }

    private void deleteItem() {
        IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
        Iterator<?> iter = sel.iterator();
        while (iter.hasNext()) {
            LoggingEvent loggingEvent = (LoggingEvent)iter.next();
            viewer.remove(loggingEvent);
        }
    }

    /**
     * Select all items.
     */
    private void selectAll() {
        ((Table) viewer.getTable()).selectAll();
        updateActionEnablement();
    }

    private void hookDoubleClickAction() {
        viewer.getTable().addMouseListener(new IMouseListener() {
            public void mouseDoubleClick(MouseEvent e) {
                jumpAction.run();
            }
        });
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        if (BandsawUtilities.isShowing()) {
            viewer.getTable().getParent().setFocus();
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite)
     */
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        BandsawUtilities.setSite(site);
    }

    /**
     * @param filterLabel The filterLabel to set.
     */
    public void updateFilterLabel(String text) {
        GridData labelData = (GridData) this.filterBaseLabel.getParent().getLayoutData();
        if (text == null || text.equals("")) {
            labelData.exclude = true;
            this.filterBaseLabel.setVisible(false);
            this.filterLabel.setVisible(false);
            this.filterLabel.getParent().setVisible(false);
        } else {
            labelData.exclude = false;
            this.filterBaseLabel.setVisible(true);
            this.filterLabel.setVisible(true);
            this.filterLabel.setText(text);
            this.filterLabel.getParent().setVisible(true);
        }
        this.filterBaseLabel.getParent().getParent().layout();
    }
}
