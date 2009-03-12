package net.brokentrain.bandsaw.log4j;

import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Brandon
 */
public class Log4jNDC implements Log4jItem {

    private LoggingEvent le;

    public Log4jNDC(LoggingEvent event) {
        le = event;
    }

    /**
     * @see net.brokentrain.bandsaw.log4j.Log4jItem#getText()
     */
    public String getText() {
        return le.getNDC();
    }

}
