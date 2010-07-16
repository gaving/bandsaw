package net.brokentrain.bandsaw.views;

import net.brokentrain.bandsaw.log4j.Log4jItem;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class BandsawViewSorter extends ViewerSorter {
    private int propertyIndex;
    private static final int DESCENDING = 1;

    private int direction = DESCENDING;

    public BandsawViewSorter() {
        this.propertyIndex = 0;
        direction = DESCENDING;
    }

    public void setColumn(int column) {
        if (column == this.propertyIndex) {
            direction = 1 - direction;
        } else {
            this.propertyIndex = column;
            direction = DESCENDING;
        }
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        LoggingEvent event1 = (LoggingEvent) e1;
        LoggingEvent event2 = (LoggingEvent) e2;
        Log4jItem item1 = BandsawUtilities.Log4jItemFactory(propertyIndex,
                event1);
        Log4jItem item2 = BandsawUtilities.Log4jItemFactory(propertyIndex,
                event2);
        int rc = item1.getText().compareTo(item2.getText());
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }
}
