package ganymede.views;

import ganymede.Ganymede;
import ganymede.GanymedeUtilities;
import ganymede.actions.QuickFilterAction;
import ganymede.actions.ShowDetailAction;
import ganymede.listeners.IMouseListener;
import ganymede.listeners.LifecycleListener;
import ganymede.log4j.ColumnList;
import ganymede.log4j.Log4jServer;
import ganymede.preferences.Log4jColumnsPreferencePage;
import ganymede.preferences.Log4jPreferencePage;

import org.eclipse.jface.preference.IPreferenceStore;
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

public class GanymedeView extends ViewPart
{
	//private TableViewer viewer;
	//private Action actionStart;
	//private Action actionStop;
	//private Action actionClear;
	//private Action doubleClickAction;
	private Table table;
	private Label filterLabel;

	// private TableColumn[] tc;

	//private static ImageRegistry image_registry;

	//class NameSorter extends ViewerSorter
	//{
	//}

	/**
	 * The constructor.
	 */
	public GanymedeView()
	{
		super();
		Log4jPreferencePage.initializeDefaults();
		GanymedeUtilities.setView(this);
	}

	/**
	 * This is a cal lback that will allow us to create the viewer and
	 * initialize it.
	 */
	public void createPartControl(Composite parent)
	{

		IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();
		
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		
		Label filterBaseLabel = new Label(parent,SWT.NONE);
		filterBaseLabel.setText("Quick Filter: ");
		GridData gridData = new GridData();
		filterBaseLabel.setLayoutData(gridData);
		
		filterLabel = new Label(parent,SWT.NONE);
		GanymedeUtilities.updateTitleBar(null);
		gridData =
			new GridData(GridData.FILL_HORIZONTAL);
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

		table =
			new Table(
				parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION| SWT.BORDER | SWT.VIRTUAL);

		table.setHeaderVisible(true);

		gridData =
			new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);

		GanymedeUtilities.setTable(table);

		ColumnList.getInstance().setColList(
			ColumnList.deSerialize(
				store.getString(Log4jColumnsPreferencePage.P_COLUMNS)));

		// Create ColumnList.COL_COUNT columns
		for (int i = 0; i < ColumnList.getInstance().getColumnCount(); i++)
		{
			new TableColumn(table, SWT.NONE);
		}

		// things that happen to use throughout our life
		getSite().getPage().addPartListener(new LifecycleListener());

		Log4jServer.init(); // needs workspace thread info

		GanymedeUtilities.updateTableColumns(); // init cols

		GanymedeUtilities.initColorDefaults(); // needed for updateColors

		GanymedeUtilities.resetTableRows(); // just in case we are re-opening

		GanymedeUtilities.updateColors(); // needed so color cache is ready

		GanymedeUtilities.updateTableColumnWidths(); // reset col widths

		// set the title in memory
		GanymedeUtilities.setViewTitle(table.getParent().getShell().getText());

		hookDoubleClickAction(); // to bring up details

	}

	private void hookDoubleClickAction()
	{
		GanymedeUtilities.setShowDetailAction(new ShowDetailAction());

		table.addMouseListener(new IMouseListener()
		{
			public void mouseDoubleClick(MouseEvent e)
			{
				GanymedeUtilities.getShowDetailAction().run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{
		if (GanymedeUtilities.isShowing())
		{
			table.getParent().setFocus();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite)
	 */
	public void init(IViewSite site) throws PartInitException
	{
		super.init(site);
		GanymedeUtilities.setSite(site);
	}

	/**
	 * @return Returns the filterLabel.
	 */
	public Label getFilterLabel() {
		return filterLabel;
	}
	/**
	 * @param filterLabel The filterLabel to set.
	 */
	public void setFilterLabel(Label filterLabel) {
		this.filterLabel = filterLabel;
	}
}