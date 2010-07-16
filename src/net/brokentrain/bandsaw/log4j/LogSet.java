package net.brokentrain.bandsaw.log4j;

import java.util.Collection;
import java.util.Vector;

import net.brokentrain.bandsaw.actions.LockAction;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.apache.log4j.spi.LoggingEvent;

public class LogSet {

    private Vector<LoggingEvent> mAllLogs = new Vector<LoggingEvent>();

    private Vector<LoggingEvent> mShowingLogs = new Vector<LoggingEvent>();

    private Vector<LoggingEvent> mHiddenLogs = new Vector<LoggingEvent>();

    private FilterSet mFilterset = new FilterSet();

    private static LogSet mInstance = null;

    private LogSet() {
    }

    public static LogSet getInstance() {
        if (mInstance == null) {
            mInstance = new LogSet();
        }
        return mInstance;
    }

    /**
     * Add a log4j event to the set and alert listeners
     * 
     * @param le
     */
    public void addLoggingEvent(LoggingEvent le) {
        if (getFilterset().isValidForShow(le)) {
            if (BandsawUtilities.isShowing()) {
                BandsawUtilities.addTableItem(le);
            }
        }
    }

    /**
     * Returns the filterset.
     * 
     * @return FilterSet
     */
    public FilterSet getFilterset() {
        return mFilterset;
    }

    /**
     * Sets the filterset.
     * 
     * @param filterset
     *            The filterset to set
     */
    public void setFilterset(FilterSet filterset) {
        this.mFilterset = filterset;
    }

    public void clear() {
        mAllLogs.clear();
        mShowingLogs.clear();
        mHiddenLogs.clear();
    }

    public LoggingEvent getLoggingEventShowingAt(int idx) {
        return mShowingLogs.get(idx);
    }

    public Collection<LoggingEvent> getValidLogs() {
        Vector<LoggingEvent> rSet = new Vector<LoggingEvent>();

        if (!LockAction.isLocked()) {
            rSet.addAll(mHiddenLogs);
        }
        rSet.addAll(mShowingLogs);

        return rSet;
    }

    public int getValidLogCount() {
        return mShowingLogs.size();
    }
}
