package net.brokentrain.bandsaw.log4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.log4j.spi.LoggingEvent;

public class Log4jDate implements Log4jItem {

    private LoggingEvent le;
    
    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public Log4jDate(LoggingEvent event) {
        le = event;
    }

    /**
     * @see net.brokentrain.bandsaw.log4j.Log4jItem#getText()
     */
    public String getText() {
        return format.format(new Timestamp(le.timeStamp));
    }

}
