package net.brokentrain.bandsaw.log4j;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public class Log4jLineNumber implements Log4jItem {

    private LoggingEvent le;

    public Log4jLineNumber(LoggingEvent event) {
        le = event;
    }

    /**
     * @see net.brokentrain.bandsaw.log4j.Log4jItem#getText()
     */
    public String getText() {
        LocationInfo location = le.getLocationInformation();
        if ( location == null ) {
            return "";
        } else {
            return location.getLineNumber();
        }
    }

}
