package net.brokentrain.bandsaw.log4j;

import java.sql.Timestamp;

import org.apache.log4j.spi.LoggingEvent;

public class Log4jDate implements Log4jItem {

    private LoggingEvent le;

    public Log4jDate(LoggingEvent event) {
        le = event;
    }
    /**
     * @see net.brokentrain.bandsaw.log4j.Log4jItem#getText()
     */
    public String getText() {
        return new Timestamp(le.timeStamp).toString();
    }

}
