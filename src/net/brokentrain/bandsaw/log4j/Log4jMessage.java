package net.brokentrain.bandsaw.log4j;

import org.apache.log4j.spi.LoggingEvent;

public class Log4jMessage implements Log4jItem {

    private LoggingEvent le;

    public Log4jMessage(LoggingEvent event) {
        le = event;
    }

    /**
     * @see net.brokentrain.bandsaw.log4j.Log4jItem#getText()
     */
    public String getText() {
        return le.getRenderedMessage();
    }

}
