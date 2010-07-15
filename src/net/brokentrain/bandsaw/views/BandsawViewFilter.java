package net.brokentrain.bandsaw.views;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class BandsawViewFilter extends ViewerFilter {

    private String searchString;

    public void setSearchText(String s) {
        // Search must be a substring of the existing value
        this.searchString = ".*" + s + ".*";
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (searchString == null || searchString.length() == 0) {
            return true;
        }
        LoggingEvent p = (LoggingEvent) element;
        // Log4jItem event = BandsawUtilities.Log4jItemFactory(propertyIndex, event1);
        if (p.getRenderedMessage().matches(searchString)) {
            return true;
        }

        // if (p.getFirstName().matches(searchString)) {
            // return true;
        // }
        // if (p.getLastName().matches(searchString)) {
            // return true;
        // }

        return false;
    }
}

