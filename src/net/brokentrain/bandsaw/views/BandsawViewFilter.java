package net.brokentrain.bandsaw.views;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class BandsawViewFilter extends ViewerFilter {

    private String searchString;

    public void setSearchText(String s) {
        this.searchString = ".*" + s + ".*";
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (searchString == null || searchString.length() == 0) {
            return true;
        }

        LoggingEvent e = (LoggingEvent) element;
        if (e.getRenderedMessage().matches(searchString)) {
            return true;
        }

        return false;
    }
}

