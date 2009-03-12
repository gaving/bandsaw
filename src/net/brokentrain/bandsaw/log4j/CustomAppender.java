package net.brokentrain.bandsaw.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class CustomAppender extends AppenderSkeleton {

    /**
     * Perform 1-time initialization.
     */
    public static void register() {
        CustomAppender appender = new CustomAppender();
        Logger.getRootLogger().addAppender(appender);
        Logger.getLogger("StackTrace").addAppender(appender);
    }

    /**
     * Constructor with default values.
     */
    public CustomAppender() {
        setLayout(new PatternLayout("%-27d{dd/MMM/yyyy HH:mm:ss Z}%n%n%-5p%n%n%c%n%n%m%n%n"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
        public void append(final LoggingEvent event) {
            Log4jServer.newMessage(event);
        }

    /**
     * {@inheritDoc}
     * @see org.apache.log4j.AppenderSkeleton#close()
     */
    public synchronized void close() {
        closed = true;
    }

    /**
     * {@inheritDoc}
     * @see org.apache.log4j.AppenderSkeleton#requiresLayout()
     */
    public boolean requiresLayout() {
        return true;
    }
}
