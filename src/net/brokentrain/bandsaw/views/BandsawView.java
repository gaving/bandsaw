package net.brokentrain.bandsaw.views;

import java.util.Iterator;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.actions.CopyAction;
import net.brokentrain.bandsaw.actions.JumpAction;
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
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
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
    private Composite labelArea;

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

        labelArea = new Composite(parent, SWT.NONE);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.exclude = true;
        labelArea.setLayout(new GridLayout(2, false));
        labelArea.setLayoutData(gridData);
        labelArea.setVisible(false);

        final Text searchText = new Text(labelArea, SWT.BORDER | SWT.SEARCH | SWT.CANCEL | SWT.ICON_CANCEL | SWT.ICON_SEARCH);
        // searchText.setText("type filter text");
        searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                ViewerFilter[] filters = viewer.getFilters();
                for (ViewerFilter filter : filters) {
                    ((BandsawViewFilter) filter).setSearchText(searchText.getText());
                }
                viewer.refresh();
            }
        });

        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

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

        ColumnList list = ColumnList.getInstance();
        Iterator<Integer> iter = list.getList();
        for (int i = 0; i < ColumnList.getInstance().getColumnCount(); i++) {

            int val = (iter.next()).intValue();

            final int index = i;
            final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
            final TableColumn column = viewerColumn.getColumn();

            column.setText(BandsawUtilities.getLabelText(val));
            column.setResizable(true);
            column.setMoveable(true);
            column.setWidth(100);
            column.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    ((BandsawViewSorter) viewer.getSorter()).setColumn(index);
                    int dir = viewer.getTable().getSortDirection();
                    if (viewer.getTable().getSortColumn() == column) {
                        dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
                    } else {
                        dir = SWT.DOWN;
                    }
                    viewer.getTable().setSortDirection(dir);
                    viewer.getTable().setSortColumn(column);
                    viewer.refresh();
                }
            });
        }

        viewer.setContentProvider(new BandsawViewContentProvider());
        viewer.setLabelProvider(new BandsawViewLabelProvider());
        viewer.setSorter(new BandsawViewSorter());
        viewer.addFilter(new BandsawViewFilter());

        labelArea.pack();
        parent.pack();

        viewer.setInput(BandsawViewModelProvider.getInstance().getEvents());

        getSite().getPage().addPartListener(new LifecycleListener());

        activateContext();

        Log4jServer.init();

        createActions();
        createContextMenu();
        hookGlobalActions();

 //       BandsawUtilities.updateTableColumns();

        BandsawUtilities.initColorDefaults();

        BandsawUtilities.resetTableRows();

        BandsawUtilities.updateColors();

//        BandsawUtilities.updateTableColumnWidths();

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

    public void toggleFilter() {
        labelArea.setVisible(!labelArea.isVisible());
        ((GridData) labelArea.getLayoutData()).exclude = !labelArea.isVisible();
        labelArea.getParent().layout();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite)
     */
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        BandsawUtilities.setSite(site);
    }
}
