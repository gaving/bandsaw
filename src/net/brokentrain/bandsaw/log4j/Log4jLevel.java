package net.brokentrain.bandsaw.log4j;

import org.apache.log4j.spi.LoggingEvent;

public class Log4jLevel implements Log4jItem {

    private LoggingEvent le;

    public Log4jLevel(LoggingEvent event) {
        le = event;
    }

    /**
     * @see net.brokentrain.bandsaw.log4j.LogItem#getText()
     */
    public String getText() {
        return le.getLevel().toString();
    }

}
