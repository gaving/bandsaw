package ganymede.log4j;

import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Brandon
 */
public class Log4jCategory implements Log4jItem {

    private LoggingEvent le;

    public Log4jCategory(LoggingEvent event) {
        le = event;
    }

    /**
     * @see ganymede.log4j.Log4jItem#getText()
     */
    public String getText() {
        return le.getLoggerName();
    }

}
