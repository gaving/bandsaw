package net.brokentrain.bandsaw.views;

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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class BandsawView extends ViewPart {

    private TableViewer viewer;
    private GridData gridData;
    private Label filterBaseLabel;
    private Label filterLabel;

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

        Table table = viewer.getTable();
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

        //// things that happen to use throughout our life
        getSite().getPage().addPartListener(new LifecycleListener());

        Log4jServer.init(); // needs workspace thread info

        BandsawUtilities.updateTableColumns(); // init cols

        BandsawUtilities.initColorDefaults(); // needed for updateColors

        BandsawUtilities.resetTableRows(); // just in case we are re-opening

        BandsawUtilities.updateColors(); // needed so color cache is ready

        BandsawUtilities.updateTableColumnWidths(); // reset col widths

        // set the title in memory
        BandsawUtilities.setViewTitle(table.getParent().getShell().getText());

        hookDoubleClickAction(); // to bring up details
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
