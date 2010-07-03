package net.brokentrain.bandsaw.preferences;

import java.util.Iterator;
import java.util.Vector;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.log4j.ColumnList;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class Log4jColumnsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    public static final String P_COLUMNS = "P_COLUMNS";
    private List columnList;
    private Button btnRemove;
    private Button btnMoveUp;
    private Button btnAdd;
    private Combo cmboAddList;
    private Button btnMoveDown;
    private Label label;

    Vector<Integer> columns = new Vector<Integer>();

    public void init(IWorkbench workbench) {
        setPreferenceStore(Bandsaw.getDefault().getPreferenceStore());
    }

    protected Control createContents(Composite parent) {

        Composite entryTable = new Composite(parent, SWT.NULL);

        //Create a data that takes up the extra space in the dialog .
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        entryTable.setLayoutData(data);

        GridLayout layout = new GridLayout(2, false);
        entryTable.setLayout(layout);

        GridData gData;

        label = new Label(entryTable, SWT.NONE);
        label.setText("Table Columns");
        gData = new GridData(GridData.FILL_BOTH);
        gData.horizontalSpan = 2;
        label.setLayoutData(gData);

        // LIST
        columnList = new List(entryTable, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        gData = new GridData(GridData.FILL_BOTH);
        gData.horizontalSpan = 1;
        gData.verticalSpan = 3;
        columnList.setLayoutData(gData);

        // MOVE UP
        btnMoveUp = new Button(entryTable, SWT.PUSH | SWT.CENTER);
        btnMoveUp.setText("Move Up"); //$NON-NLS-1$
        btnMoveUp.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                int index = columnList.getSelectionIndex();
                ColumnList.getInstance().moveUp(index);
                refreshList();
                columnList.select(index - 1);
            }
        });
        gData = new GridData();
        btnMoveUp.setLayoutData(gData);

        // MOVE DOWN
        btnMoveDown = new Button(entryTable, SWT.PUSH | SWT.CENTER);
        btnMoveDown.setText("Move Down"); //$NON-NLS-1$
        btnMoveDown.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                int index = columnList.getSelectionIndex();
                ColumnList.getInstance().moveDown(index);
                refreshList();
                columnList.select(index + 1);
            }
        });
        gData = new GridData();
        btnMoveDown.setLayoutData(gData);

        // REMOVE
        btnRemove = new Button(entryTable, SWT.PUSH | SWT.CENTER);
        btnRemove.setText("Remove"); //$NON-NLS-1$
        btnRemove.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                int index = columnList.getSelectionIndex();
                ColumnList.getInstance().remove(columnList.getSelectionIndex());
                refreshList();
                columnList.select(index - 1);
            }
        });
        gData = new GridData();
        btnRemove.setLayoutData(gData);

        // LIST
        cmboAddList = new Combo(entryTable, SWT.DROP_DOWN | SWT.READ_ONLY);
        for (String label : BandsawUtilities.getColumnLabels()) {
            cmboAddList.add(label);
        }
        cmboAddList.select(0);
        gData = new GridData();
        gData.horizontalSpan = 2;
        cmboAddList.setLayoutData(gData);

        // ADD IT BUTTON
        btnAdd = new Button(entryTable, SWT.PUSH | SWT.CENTER);
        btnAdd.setText("Add Column"); //$NON-NLS-1$
        btnAdd.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                int col = BandsawUtilities.convertColumnToInt(cmboAddList.getText());
                ColumnList.getInstance().add(col);
                refreshList();
            }
        });

        gData = new GridData();
        btnAdd.setLayoutData(gData);

        refreshList(); // initial

        return null;
    }

    /**
     * Refresh the main list
     */
    protected void refreshList() {
        columnList.removeAll();
        Iterator<Integer> i = ColumnList.getInstance().getList();
        while (i.hasNext()) {
            Integer col = i.next();
            columnList.add(BandsawUtilities.getLabelText(col.intValue()));
        }
    }

    private int[] loadDefaultValues() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        return ColumnList.deSerialize(store.getDefaultString(P_COLUMNS));
    }

    private void storeValues() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        store.setValue(P_COLUMNS,
                ColumnList.serialize(ColumnList.getInstance().getCols()));
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    protected void performApply() {
        storeValues();
        BandsawUtilities.updateTableColumns();
        super.performApply();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults() {
        // store.setValue(P_COLUMNS, ColumnList.serialize(loadDefaultValues()));
        // ColumnList.getInstance().add(col);
        // ColumnList.getInstance().setColList(loadDefaultValues());
            ColumnList.getInstance().clear();
        for (int value : loadDefaultValues()) {
            System.out.println(value);
            ColumnList.getInstance().add(value);
        }
        refreshList();
        // storeValues();
        super.performDefaults();
    }


    /**
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    public boolean performOk() {
        storeValues();
        BandsawUtilities.updateTableColumns();
        return super.performOk();
    }

}
