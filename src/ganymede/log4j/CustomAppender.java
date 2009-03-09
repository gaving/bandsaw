package ganymede.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
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

    if (!event.getLevel().isGreaterOrEqual(Level.ERROR)) {
      return;
    }

    Log4jServer.newMessage(event);

    System.out.println(event.getThreadName());
    System.out.println("An the winner is: " + event.getRenderedMessage());
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
