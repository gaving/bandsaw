package net.brokentrain.bandsaw.views;

import java.io.File;
import java.util.List;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.actions.QuickFilterAction;
import net.brokentrain.bandsaw.actions.ShowDetailAction;
import net.brokentrain.bandsaw.listeners.IMouseListener;
import net.brokentrain.bandsaw.listeners.LifecycleListener;
import net.brokentrain.bandsaw.log4j.ColumnList;
import net.brokentrain.bandsaw.log4j.Log4jServer;
import net.brokentrain.bandsaw.preferences.Log4jColumnsPreferencePage;
import net.brokentrain.bandsaw.preferences.Log4jPreferencePage;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

public class BandsawView extends ViewPart {

    private TableViewer viewer;
    private GridData gridData;
    private Label filterBaseLabel;
    private Label filterLabel;

    Action viewItemAction, copyItemAction, selectAllAction, lineItemAction;

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

        table.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent arg0) {
                if (arg0.keyCode == 'c' && arg0.stateMask == SWT.CTRL) {
                    copyItem();
                }
            }

            public void keyReleased(KeyEvent arg0) {
                // TODO Auto-generated method stub
            }
        });

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

        Log4jServer.init();

        createActions();
        createContextMenu();

        BandsawUtilities.updateTableColumns();

        BandsawUtilities.initColorDefaults();

        BandsawUtilities.resetTableRows();

        BandsawUtilities.updateColors();

        BandsawUtilities.updateTableColumnWidths();

        BandsawUtilities.setViewTitle(table.getParent().getShell().getText());

        hookDoubleClickAction();
    }

    public void createActions() {
        viewItemAction = new Action("View Details") {
            public void run() {
                BandsawUtilities.getShowDetailAction().run();
            }
        };

        // viewItemAction.setImageDescriptor(ImageDescriptor.createFromImage(PaintUtil.iconError));
        viewItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_DEF_VIEW)); 

        lineItemAction = new Action("Jump to file") {
            public void run() {
                jumpToFile();
            }
        };

        // copyItemAction.setImageDescriptor(ImageDescriptor.createFromImage(PaintUtil.iconError));
        lineItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY)); 

        copyItemAction = new Action("Copy") {
            public void run() {
                copyItem();
            }
        };
        // copyItemAction.setImageDescriptor(ImageDescriptor.createFromImage(PaintUtil.iconError));
        copyItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY)); 

        selectAllAction = new Action("Select All") {
            public void run() {
                selectAll();
            }
        };

        // Add selection listener.
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                updateActionEnablement();
            }
        });
    }

   /**
    * Returns the image descriptor with the given relative path.
    */
//   private ImageDescriptor getImageDescriptor(String relativePath) {
//       String iconPath = "icons/";
//       try {
//           Bandsaw plugin = Bandsaw.getDefault();
//           URL installURL = plugin.getDescriptor().getInstallURL();
//           URL url = new URL(installURL, iconPath + relativePath);
//           return ImageDescriptor.createFromURL(url);
//       }
//       catch (MalformedURLException e) {
//           // should not happen
//           return ImageDescriptor.getMissingImageDescriptor();
//       }
//   }

   private void updateActionEnablement() {
       IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
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
        mgr.add(viewItemAction);
        mgr.add(lineItemAction);
        mgr.add(new Separator());
        mgr.add(copyItemAction);
        mgr.add(new Separator());
        mgr.add(selectAllAction);
    }

    private void copyItem() {

        @SuppressWarnings("rawtypes")
            List leList = ((IStructuredSelection)viewer.getSelection()).toList();

        if (leList.isEmpty()) {
            return;
        }

        Clipboard clipboard = new Clipboard(viewer.getControl().getDisplay());
        for (Object obj : leList) {
            LoggingEvent le = (LoggingEvent) obj;

            String renderedMessage = le.getRenderedMessage();
            System.out.println("Copying to clipboard:" + renderedMessage);

            clipboard.setContents(new Object[] { renderedMessage }, new Transfer[] { TextTransfer.getInstance() });
        }
        clipboard.dispose();
    }

    private void jumpToFile() {

        @SuppressWarnings("rawtypes")
        List leList = ((IStructuredSelection)viewer.getSelection()).toList();

        if (leList.isEmpty()) {
            return;
        }

        for (Object obj : leList) {
            LoggingEvent le = (LoggingEvent) obj;

            if (le.locationInformationExists()) {

                LocationInfo locationInfo = le.getLocationInformation();

                String className = locationInfo.getClassName();
                String fileName = locationInfo.getFileName();
                String lineNumber = locationInfo.getLineNumber();

                System.out.println(className);
                System.out.println(fileName);
                System.out.println(lineNumber);

                File fileToOpen = new File(fileName);
                if (fileToOpen.exists() && fileToOpen.isFile()) {

                    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

                    try {
                        IDE.openEditorOnFileStore( page, fileStore );
                    } catch ( PartInitException e ) {
                        //Put your exception handler here if you wish to
                    }
                }
            }
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
        BandsawUtilities.setShowDetailAction(new ShowDetailAction());

        viewer.getTable().addMouseListener(new IMouseListener() {
            public void mouseDoubleClick(MouseEvent e) {
                BandsawUtilities.getShowDetailAction().run();
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
