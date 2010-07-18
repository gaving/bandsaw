package net.brokentrain.bandsaw.util;

import java.util.HashMap;
import java.util.Iterator;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.actions.LockAction;
import net.brokentrain.bandsaw.log4j.ColumnList;
import net.brokentrain.bandsaw.log4j.Log4jCategory;
import net.brokentrain.bandsaw.log4j.Log4jDate;
import net.brokentrain.bandsaw.log4j.Log4jItem;
import net.brokentrain.bandsaw.log4j.Log4jLevel;
import net.brokentrain.bandsaw.log4j.Log4jLineNumber;
import net.brokentrain.bandsaw.log4j.Log4jMessage;
import net.brokentrain.bandsaw.log4j.Log4jNDC;
import net.brokentrain.bandsaw.log4j.Log4jThrowable;
import net.brokentrain.bandsaw.notification.BandsawNotification;
import net.brokentrain.bandsaw.preferences.Log4jPreferencePage;
import net.brokentrain.bandsaw.views.BandsawView;
import net.brokentrain.bandsaw.views.BandsawViewLabelProvider;
import net.brokentrain.bandsaw.views.BandsawViewModelProvider;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;

public class BandsawUtilities {

    private static Table table;

    private static TableViewer tableViewer;

    private static BandsawView mView;

    private static IViewSite mSite;

    public static HashMap<Integer, String> getColumnLabels() {
        HashMap<Integer, String> columnLabels = new HashMap<Integer, String>();
        columnLabels.put(Log4jItem.LEVEL, Log4jItem.LABEL_LEVEL);
        columnLabels.put(Log4jItem.CATEGORY, Log4jItem.LABEL_CATEGORY);
        columnLabels.put(Log4jItem.MESSAGE, Log4jItem.LABEL_MESSAGE);
        columnLabels.put(Log4jItem.LINE_NUMBER, Log4jItem.LABEL_LINE_NUMBER);
        columnLabels.put(Log4jItem.DATE, Log4jItem.LABEL_DATE);
        columnLabels.put(Log4jItem.NDC, Log4jItem.LABEL_NDC);
        columnLabels.put(Log4jItem.THROWABLE, Log4jItem.LABEL_THROWABLE);
        return columnLabels;
    }

    /**
     * Get the string label
     *
     * @param col
     * @return String
     */
    public static String getLabelText(int col) {
        switch (col) {
        case Log4jItem.LEVEL:
            return Log4jItem.LABEL_LEVEL;
        case Log4jItem.CATEGORY:
            return Log4jItem.LABEL_CATEGORY;
        case Log4jItem.MESSAGE:
            return Log4jItem.LABEL_MESSAGE;
        case Log4jItem.LINE_NUMBER:
            return Log4jItem.LABEL_LINE_NUMBER;
        case Log4jItem.DATE:
            return Log4jItem.LABEL_DATE;
        case Log4jItem.NDC:
            return Log4jItem.LABEL_NDC;
        case Log4jItem.THROWABLE:
            return Log4jItem.LABEL_THROWABLE;
        default:
            return Log4jItem.LABEL_UNKNOWN;
        }
    }

    /**
     * This is used to convert a string column label to it's int counterpart
     *
     * @param colLabel
     *            The column label
     * @return int
     */
    public static int convertColumnToInt(String colLabel) {
        if (colLabel.equals(Log4jItem.LABEL_LEVEL)) {
            return Log4jItem.LEVEL;
        } else if (colLabel.equals(Log4jItem.LABEL_CATEGORY)) {
            return Log4jItem.CATEGORY;
        } else if (colLabel.equals(Log4jItem.LABEL_MESSAGE)) {
            return Log4jItem.MESSAGE;
        } else if (colLabel.equals(Log4jItem.LABEL_LINE_NUMBER)) {
            return Log4jItem.LINE_NUMBER;
        } else if (colLabel.equals(Log4jItem.LABEL_DATE)) {
            return Log4jItem.DATE;
        } else if (colLabel.equals(Log4jItem.LABEL_NDC)) {
            return Log4jItem.NDC;
        } else if (colLabel.equals(Log4jItem.LABEL_THROWABLE)) {
            return Log4jItem.THROWABLE;
        } else {
            return Log4jItem.UNKNOWN;
        }
    }

    public static Log4jItem Log4jItemFactory(int type, LoggingEvent e) {
        switch (type) {
        case Log4jItem.LEVEL:
            return new Log4jLevel(e);
        case Log4jItem.CATEGORY:
            return new Log4jCategory(e);
        case Log4jItem.MESSAGE:
            return new Log4jMessage(e);
        case Log4jItem.LINE_NUMBER:
            return new Log4jLineNumber(e);
        case Log4jItem.DATE:
            return new Log4jDate(e);
        case Log4jItem.NDC:
            return new Log4jNDC(e);
        case Log4jItem.THROWABLE:
            return new Log4jThrowable(e);
        default:
            return null;
        }
    }

    public static void setTableViewer(TableViewer tableViewer) {
        BandsawUtilities.tableViewer = tableViewer;
    }

    public static void setTable(Table table) {
        BandsawUtilities.table = table;
    }

    public static TableViewer getViewer() {
        return BandsawUtilities.tableViewer;
    }

    public static Table getTable() {
        return BandsawUtilities.table;
    }

    /**
     * Sets the defaults. This needs to be called before you can update widths
     * or save any
     */
    public static void setTableColumnWidthDefaults() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        store.setDefault("colWidth." + Log4jItem.LEVEL, 75);
        store.setDefault("colWidth." + Log4jItem.CATEGORY, 75);
        store.setDefault("colWidth." + Log4jItem.DATE, 75);
        store.setDefault("colWidth." + Log4jItem.LINE_NUMBER, 75);
        store.setDefault("colWidth." + Log4jItem.MESSAGE, 75);
        store.setDefault("colWidth." + Log4jItem.NDC, 75);
        store.setDefault("colWidth." + Log4jItem.THROWABLE, 75);
        store.setDefault("colWidth." + Log4jItem.UNKNOWN, 75);
    }

    /**
     * Saves column widths
     */
    public static void saveTableColumnWidths() {
        if (isShowing()) {
            setTableColumnWidthDefaults();
            IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
            TableColumn[] tc = getTable().getColumns();
            ColumnList list = ColumnList.getInstance();
            Iterator<Integer> iter = list.getList();
            for (int i = 0; i < list.getColumnCount(); i++) {
                int val = (iter.next()).intValue();
                store.setValue("colWidth." + val, tc[i].getWidth());
            }
        }
    }

    /**
     * Used to refresh their widths [on startup usually]
     */
    public static void updateTableColumnWidths() {
        if (isShowing()) {
            setTableColumnWidthDefaults();
            IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
            TableColumn[] tc = getTable().getColumns();
            ColumnList list = ColumnList.getInstance();
            Iterator<Integer> iter = list.getList();
            for (int i = 0; i < list.getColumnCount(); i++) {
                int val = (iter.next()).intValue();
                int width = store.getInt("colWidth." + val);
                tc[i].setWidth(width);
            }
        }
    }

    public static void updateTableColumns() {
        if (isShowing()) {
            TableColumn[] tc = getTable().getColumns();
            ColumnList list = ColumnList.getInstance();
            Iterator<Integer> iter = list.getList();
            for (int i = 0; i < list.getColumnCount(); i++) {
                int val = (iter.next()).intValue();
                tc[i].setText(BandsawUtilities.getLabelText(val));
                updateTableColumnWidths();
            }
        }
    }

    public static String handleNull(String string) {
        if (string == null) {
            return "";
        } else {
            return string;
        }
    }

    public static String getColumnText(Object aLoggingEvent, int aColumnIndex) {
        if (aLoggingEvent instanceof LoggingEvent) {
            LoggingEvent le = (LoggingEvent) aLoggingEvent;
            return ColumnList.getInstance().getText(aColumnIndex, le);
        }

        return "Not a valid logging event";
    }

    /**
     * @return
     */
    public static BandsawView getView() {
        return mView;
    }

    /**
     * @param aView
     */
    public static void setView(BandsawView aView) {
        mView = aView;
    }

    /**
     * Show a message box
     *
     * @param message
     */
    public static void showMessage(String message) {
        MessageDialog.openInformation(getTable().getParent().getShell(),
                "Bandsaw", message + "...");
    }

    public static void updateColors() {
        BandsawViewLabelProvider.initColors();

        if (isShowing()) {
            tableViewer.refresh();
        }
    }

    /**
     * @return
     */
    public static boolean isShowing() {
        Table table = BandsawUtilities.getTable();
        return (table != null && !table.isDisposed());
    }

    /**
     * @return
     */
    public static IViewSite getSite() {
        return mSite;
    }

    /**
     * @param aSite
     */
    public static void setSite(IViewSite aSite) {
        mSite = aSite;
    }

    /**
     * @param le
     */
    @SuppressWarnings("restriction")
    public static void addTableItem(LoggingEvent le) {
        BandsawViewModelProvider events = BandsawViewModelProvider
                .getInstance();
        events.getEvents().add(le);
        getViewer().refresh();

        if (!LockAction.isLocked()) {
            getViewer().reveal(le);
        }
        
        updateContentDescription();        

        boolean notification = Bandsaw.getDefault().getPreferenceStore()
                .getBoolean(Log4jPreferencePage.P_SHOW_NOTIFICATIONS);
        if (notification) {
            BandsawNotification popup = new BandsawNotification();
            popup.setLoggingEvent(le);
            popup.create();
            popup.open();
        }
        
        // tableViewer.getTable().pack();
        // for (TableColumn column : tableViewer.getTable().getColumns())
        // column.pack();
    }


    public static void updateContentDescription() {
        getView().setContentDescription(getViewer().getTable().getItemCount() + " items");
    }

    public static void updateStatus(String message) {
        IActionBars bars = ((IViewSite) getSite()).getActionBars();
        bars.getStatusLineManager().setMessage(message);
    }

    /* TODO Replace this with proper sql validator */
    public static boolean isSQL(String statement) {
        statement = statement.trim().toUpperCase();
        return (statement.startsWith("SELECT") ||
        (statement.startsWith("UPDATE")) ||
        (statement.startsWith("INSERT")) ||
        (statement.startsWith("CREATE")) ||
        (statement.startsWith("DROP")));
    }
}
