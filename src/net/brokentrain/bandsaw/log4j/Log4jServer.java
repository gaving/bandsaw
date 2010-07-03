package net.brokentrain.bandsaw.log4j;

import java.net.ServerSocket;
import java.util.Enumeration;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.BandsawUtilities;
import net.brokentrain.bandsaw.preferences.Log4jPreferencePage;
import net.brokentrain.bandsaw.log4j.CustomAppender;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.LoggerRepositoryExImpl;
import org.apache.log4j.net.SocketAppender;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.swt.widgets.Display;

public class Log4jServer extends Thread {

    private static final Class<Log4jServer> clazz = Log4jServer.class;

    private static Log4jServer mLog4jServer;

    private static Thread mPrimary;
    private static boolean mServerUp = false;

    private static final Object repositorySelectorGuard = new Object();
    private static final LoggerRepositoryExImpl repositoryExImpl = new LoggerRepositoryExImpl(LogManager.getLoggerRepository());

    public static void init() {
        setPrimary(Thread.currentThread());

        LogManager.setRepositorySelector(new RepositorySelector() {

            public LoggerRepository getLoggerRepository() {
                return repositoryExImpl;
            }}, repositorySelectorGuard);

        new DOMConfigurator().configure(clazz.getResource("/cfg/server.xml"));
    }

    /**
     * Stop the Log4j Socket Server
     * @return boolean
     */
    public static boolean startListener() {

        int port = Bandsaw.getDefault().getPreferenceStore().getInt(Log4jPreferencePage.P_PORT);

        /* TODO: Should really work out how to change the port of the custom
         * appender here */
        Logger rootLogger = LogManager.getRootLogger();
        rootLogger.setLevel(Level.toLevel("DEBUG"));

        mServerUp = true;

        BandsawUtilities.getStartAction().setEnabled(false);
        BandsawUtilities.getStopAction().setEnabled(true);

        return true;
    }

    /**
     * Stop the Log4j Socket Server
     */
    public static void stopListener() {
        Logger rootLogger = LogManager.getRootLogger();
        rootLogger.setLevel(Level.toLevel("OFF"));

        mServerUp = false;

        BandsawUtilities.getStartAction().setEnabled(true);
        BandsawUtilities.getStopAction().setEnabled(false);
    }

    /**
     * Add a new message to the table - this is the 
     * real connection between our model and firing
     * off updates to the GUI.
     * @param le The logging event to add
     */
    static synchronized public void newMessage(LoggingEvent le) {
        final LoggingEvent thisEvent = le;
        Display.findDisplay(getPrimary()).asyncExec(new Runnable() {
            public void run() {
                LogSet.getInstance().addLoggingEvent(thisEvent);
            }
        });
    }

    /**
     * @return
     */
    public boolean isServerUp() {
        return mServerUp;
    }

    /**
     * @param aB
     */
    public void setServerUp(boolean aB) {
        mServerUp = aB;
    }

    /**
     * @return
     */
    public static Log4jServer getLog4jServer() {
        return mLog4jServer;
    }

    /**
     * @return
     */
    public static Thread getPrimary() {
        return mPrimary;
    }

    /**
     * @param aThread
     */
    public static void setPrimary(Thread aThread) {
        mPrimary = aThread;
    }

}
