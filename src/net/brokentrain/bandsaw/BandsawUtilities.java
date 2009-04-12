package net.brokentrain.bandsaw;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ArrayList;

import net.brokentrain.bandsaw.actions.ShowDetailAction;
import net.brokentrain.bandsaw.log4j.ColumnList;
import net.brokentrain.bandsaw.log4j.Log4jCategory;
import net.brokentrain.bandsaw.log4j.Log4jDate;
import net.brokentrain.bandsaw.log4j.Log4jItem;
import net.brokentrain.bandsaw.log4j.Log4jLevel;
import net.brokentrain.bandsaw.log4j.Log4jLineNumber;
import net.brokentrain.bandsaw.log4j.Log4jMessage;
import net.brokentrain.bandsaw.log4j.Log4jNDC;
import net.brokentrain.bandsaw.log4j.Log4jServer;
import net.brokentrain.bandsaw.log4j.Log4jThrowable;
import net.brokentrain.bandsaw.log4j.LogSet;
import net.brokentrain.bandsaw.preferences.ColorPreferencePage;
import net.brokentrain.bandsaw.views.BandsawView;
import net.brokentrain.bandsaw.util.PaintUtil;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewSite;


/**
 * @author Brandon
 */
public class BandsawUtilities {

    private static Table table;

    private static BandsawView mView;

    private static Log4jServer mSocketServer;

    private static ShowDetailAction mShowDetailAction;

    private static Hashtable<String, Color> colors = new Hashtable<String, Color>(5);

    private static IAction mStartAction;

    private static IAction mStopAction;

    private static IViewSite mSite;

    private static boolean mActionsInited = false;

    private static String sViewTitle;

    private static int mServerType = Bandsaw.P_SERVER_TYPE_SOCKET_APPENDER;

    public static ArrayList<String> getColumnLabels() {
        ArrayList<String> columnLabels = new ArrayList<String>();
        columnLabels.add(Log4jItem.LABEL_LEVEL);
        columnLabels.add(Log4jItem.LABEL_CATEGORY);
        columnLabels.add(Log4jItem.LABEL_MESSAGE);
        columnLabels.add(Log4jItem.LABEL_LINE_NUMBER);
        columnLabels.add(Log4jItem.LABEL_DATE);
        columnLabels.add(Log4jItem.LABEL_NDC);
        columnLabels.add(Log4jItem.LABEL_THROWABLE);
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

    public static void setTable(Table table) {
        BandsawUtilities.table = table;
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
            Iterator iter = list.getList();
            for (int i = 0; i < list.getColumnCount(); i++) {
                int val = ((Integer) iter.next()).intValue();
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
            Iterator iter = list.getList();
            for (int i = 0; i < list.getColumnCount(); i++) {
                int val = ((Integer) iter.next()).intValue();
                int width = store.getInt("colWidth." + val);
                tc[i].setWidth(width);
            }
        }
    }

    public static void updateTableColumns() {
        if (isShowing()) {
            TableColumn[] tc = getTable().getColumns();
            ColumnList list = ColumnList.getInstance();
            Iterator iter = list.getList();
            for (int i = 0; i < list.getColumnCount(); i++) {
                int val = ((Integer) iter.next()).intValue();
                tc[i].setText(BandsawUtilities.getLabelText(val));
                updateTableColumnWidths();
            }

            resetTableRows();
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
     * The filter has been updated, so we need to refresh display
     */
    public static void filterUpdated() {
        LogSet.getInstance().revalidateAll();
        resetTableRows();
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
     * @return
     */
    public static Log4jServer getSocketServer() {
        return mSocketServer;
    }

    /**
     * @param aServer
     */
    public static void setSocketServer(Log4jServer aServer) {
        mSocketServer = aServer;
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

    /**
     * @return
     */
    public static ShowDetailAction getShowDetailAction() {
        return mShowDetailAction;
    }

    /**
     * @param aAction
     */
    public static void setShowDetailAction(ShowDetailAction aAction) {
        mShowDetailAction = aAction;
    }

    public static void updateColors() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();

        colors.clear();
        colors.put(Level.DEBUG.toString(), new Color(Display.getCurrent(),
                    PreferenceConverter.getColor(store,
                        ColorPreferencePage.DEBUG_COLOR_KEY)));
        colors.put(Level.INFO.toString(), new Color(Display.getCurrent(),
                    PreferenceConverter.getColor(store,
                        ColorPreferencePage.INFO_COLOR_KEY)));
        colors.put(Level.WARN.toString(), new Color(Display.getCurrent(),
                    PreferenceConverter.getColor(store,
                        ColorPreferencePage.WARN_COLOR_KEY)));
        colors.put(Level.ERROR.toString(), new Color(Display.getCurrent(),
                    PreferenceConverter.getColor(store,
                        ColorPreferencePage.ERROR_COLOR_KEY)));
        colors.put(Level.FATAL.toString(), new Color(Display.getCurrent(),
                    PreferenceConverter.getColor(store,
                        ColorPreferencePage.FATAL_COLOR_KEY)));

        // reset display
        if (isShowing()) {
            LogSet logSet = LogSet.getInstance();
            Collection validLogs = logSet.getValidLogs();
            TableItem[] items = getTable().getItems();
            int idx = 0;
            for (Iterator logIter = validLogs.iterator(); logIter.hasNext(); idx++) {
                LoggingEvent le = (LoggingEvent) logIter.next();
                items[idx].setForeground(BandsawUtilities.getColor(le
                            .getLevel()));
            }
        }
    }

    public static Color getColor(Level level) {
        return (Color) colors.get(level.toString());
    }

    public static void initColorDefaults() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.DEBUG_COLOR_KEY, new RGB(0, 0, 0));
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.INFO_COLOR_KEY, new RGB(0, 255, 0));
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.WARN_COLOR_KEY, new RGB(255, 128, 0));
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.ERROR_COLOR_KEY, new RGB(255, 0, 0));
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.FATAL_COLOR_KEY, new RGB(255, 0, 0));
    }

    /**
     * Is the view active? Use this in preference pages to see if you need to
     * call back to update view.
     * 
     * @return
     */
    public static boolean isShowing() {
        Table table = BandsawUtilities.getTable();
        return (table != null && !table.isDisposed());
    }

    /**
     * 
     */
    public static void resetTableRows() {
        // refresh display
        if (isShowing()) {
            boolean visible = getTable().getVisible();
            getTable().setVisible(false);
            LogSet logSet = LogSet.getInstance();
            Collection validLogs = logSet.getValidLogs();
            int itemCount = getTable().getItemCount();
            TableItem[] items = getTable().getItems();
            TableItem thisItem;
            int idx = 0;
            for (Iterator logIter = validLogs.iterator(); logIter.hasNext(); idx++) {
                LoggingEvent le = (LoggingEvent) logIter.next();
                if (idx < itemCount) // reuse if possible
                {
                    thisItem = items[idx];
                } else {
                    thisItem = createTableItem(idx);
                }

                int colCount = getTable().getColumnCount();
                for (int colIdx = 0; colIdx < colCount; colIdx++) {
                    String text = BandsawUtilities.getColumnText(le, colIdx);
                    thisItem.setText(colIdx, text);
                    thisItem.setForeground(BandsawUtilities.getColor(le
                                .getLevel()));
                }
            }

            // get rid of any now open spaces
            if (idx < itemCount) {
                getTable().remove(idx, itemCount - 1);
            }

            getTable().setVisible(visible);
        }
    }

    /**
     * @return
     */
    private static TableItem createTableItem(int index) {
        return new TableItem(getTable(), SWT.NONE, index);
    }

    /**
     * @return
     */
    public static IAction getStartAction() {
        if (!isActionsInited()) {
            initActions();
        }
        return mStartAction;
    }

    /**
     * @return
     */
    public static IAction getStopAction() {
        if (!isActionsInited()) {
            initActions();
        }
        return mStopAction;
    }

    /**
     * @param aAction
     */
    public static void setStartAction(IAction aAction) {
        mStartAction = aAction;
    }

    /**
     * @param aAction
     */
    public static void setStopAction(IAction aAction) {
        mStopAction = aAction;
    }

    /**
     * 
     */
    public static void initActions() {
        setActionsInited(true);
        BandsawUtilities.setStartAction(((ActionContributionItem) getSite()
                    .getActionBars().getToolBarManager().find(
                        "Ganymede.StartAction")).getAction());

        BandsawUtilities.setStopAction(((ActionContributionItem) getSite()
                    .getActionBars().getToolBarManager()
                    .find("Ganymede.StopAction")).getAction());

        Log4jServer instance = Log4jServer.getLog4jServer();
        if (instance != null && instance.isServerUp()) {
            getStartAction().setEnabled(false);
            getStopAction().setEnabled(true);
        } else {
            getStartAction().setEnabled(true);
            getStopAction().setEnabled(false);
        }
    }

    /**
     * @return
     */
    public static boolean isActionsInited() {
        return mActionsInited;
    }

    /**
     * @param aB
     */
    public static void setActionsInited(boolean aB) {
        mActionsInited = aB;
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
     * @return
     */
    public static String getViewTitle() {
        return sViewTitle;
    }

    /**
     * @param aString
     */
    public static void setViewTitle(String aString) {
        sViewTitle = aString;
    }

    /**
     * @param le
     */
    public static void addTableItem(LoggingEvent le) {
        TableItem tableItem = createTableItem(0);

        tableItem.setImage(0, PaintUtil.getIcon(le.getLevel().toInt()));
        tableItem.setForeground(BandsawUtilities.getColor(le.getLevel()));

        for (int i = 0; i < getTable().getColumnCount(); i++) {
            tableItem.setText(i, getColumnText(le, i));
        }

        table.setSelection(0);
    }

    public static void updateTitleBar(String filterText) {
        if (filterText == null || filterText.equals("")) {
            filterText = "No Filter";
        }
        BandsawUtilities.getView().getFilterLabel().setText(filterText);
    }

    public static int getServerType() {
        return mServerType;
    }

    public static void setServerType(int serverType) {
        mServerType = serverType;

        Log4jServer.stopListener();
        Log4jServer.startListener();
    }

}
