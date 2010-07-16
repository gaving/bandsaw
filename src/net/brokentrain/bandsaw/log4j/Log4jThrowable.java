package net.brokentrain.bandsaw.log4j;

import org.apache.log4j.spi.LoggingEvent;

public class Log4jThrowable implements Log4jItem {

    private LoggingEvent le;

    public Log4jThrowable(LoggingEvent event) {
        le = event;
    }

    /**
     * @see net.brokentrain.bandsaw.log4j.Log4jItem#getText()
     */
    public String getText() {
        if (le.getThrowableInformation() != null) {
            return "X"; // TODO: use image instead
        } else {
            return "";
        }
    }

}
