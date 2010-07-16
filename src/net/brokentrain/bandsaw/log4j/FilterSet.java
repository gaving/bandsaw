package net.brokentrain.bandsaw.log4j;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.spi.LoggingEvent;

public class FilterSet {

    private ArrayList<Filter> filters = new ArrayList<Filter>(1);

    private Filter mQuickFilter;

    public FilterSet() {
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void removeFilter(Filter filter) {
        for (Filter f : filters) {
            if (filter.equals(f)) {
                filters.remove(f);
                return;
            }
        }
    }

    public Filter[] getFilters() {
        return filters.toArray(new Filter[0]);
    }

    public Iterator<Filter> iterator() {
        return filters.iterator();
    }

    public boolean isValidForShow(LoggingEvent le) {
        for (Filter filter : filters) {
            if (!filter.isValid(le)) {
                return false;
            }
        }

        // check quick filter
        if (getQuickFilter() != null) {
            if (!getQuickFilter().isValid(le, true)) {
                return false;
            }
        }

        return true;
    }

    /**
     * How many filters do we have, useful for filling in arrays of values
     * elsewhere
     * 
     * @return int
     */
    public int getCount() {
        return filters.size();
    }

    /**
     * @return
     */
    public Filter getQuickFilter() {
        return mQuickFilter;
    }

    /**
     * @param aFilter
     */
    public void setQuickFilter(Filter aFilter) {
        mQuickFilter = aFilter;
    }

}
