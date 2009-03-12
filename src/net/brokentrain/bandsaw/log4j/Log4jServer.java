package net.brokentrain.bandsaw.log4j;


import java.net.ServerSocket;
import java.net.Socket;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.BandsawUtilities;
import net.brokentrain.bandsaw.preferences.Log4jPreferencePage;

import org.apache.log4j.Category;
import org.apache.log4j.LogManager;
import org.apache.log4j.LoggerRepositoryExImpl;
import org.apache.log4j.net.SimpleSocketServer;
import org.apache.log4j.net.SocketNode;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.swt.widgets.Display;


/**
 * @author Brandon
 */
public class Log4jServer extends Thread
{

    private static Log4jServer mLog4jServer;

    private static ServerSocket mServerSocket;
    private static Thread mPrimary;
    private static boolean mServerUp = false;

    private static Category cat = Category.getInstance(SimpleSocketServer.class.getName());
    private static final Object repositorySelectorGuard = new Object();
    private static final LoggerRepositoryExImpl repositoryExImpl = new LoggerRepositoryExImpl(LogManager.getLoggerRepository());

    private static int port;

    static public void init()
    {
        setPrimary(Thread.currentThread());

        LogManager.setRepositorySelector(new RepositorySelector() {

            public LoggerRepository getLoggerRepository() {
                return repositoryExImpl;
            }}, repositorySelectorGuard);

        new DOMConfigurator().configure("server.xml");
    }

    /**
     * Stop the Log4j Socket Server
     * @return boolean
     */
    static public boolean startListener()
    {
        int port = Bandsaw.getDefault().getPreferenceStore().getInt(Log4jPreferencePage.P_PORT);

        Log4jServer.mLog4jServer = new Log4jServer();
        Log4jServer.mLog4jServer.setServerUp(true);
        Log4jServer.mLog4jServer.start();

        BandsawUtilities.getStartAction().setEnabled(false);
        BandsawUtilities.getStopAction().setEnabled(true);
        return true;
    }

    /**
     * Stop the Log4j Socket Server
     */
    static public void stopListener()
    {
        BandsawUtilities.getStartAction().setEnabled(true);
        BandsawUtilities.getStopAction().setEnabled(false);
    }

    /**
     * Add a new message to the table - this is the 
     * real connection between our model and firing
     * off updates to the GUI.
     * @param le The logging event to add
     */
    static synchronized public void newMessage(LoggingEvent le)
    {
        final LoggingEvent thisEvent = le;
        Display.findDisplay(getPrimary()).asyncExec(new Runnable() {
            public void run() {
                LogSet.getInstance().addLoggingEvent(thisEvent);
            }
        });
    }

    /**
     * Kick off the runner thread
     * @see java.lang.Runnable#run()
     */
    public void run()
    {

        ClientConn client_conn = null;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true) {
                Socket socket = serverSocket.accept();
                client_conn = new ClientConn(socket);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {

            // shut down client correctly
            if (client_conn != null) {
                client_conn.setActive(false);
            }
        }
    }

    /**
     * Quick Encapsulation of the client runner
     * - made it seperate so many servers/conns could be active
     *   at once, each in their own thread
     * @author Brandon
     */
    class ClientConn extends Thread
    {

        private Socket socket;

        public ClientConn(Socket s)
        {
            this.socket = s;
        }

        private boolean mActive = false;

        /**
         * Pawn me off so I can handle some requests
         * @see java.lang.Runnable#run()
         */
        public void run() {
            new SocketNode(socket, LogManager.getLoggerRepository());
        }

        /**
         * @return
         */
        public boolean isActive()
        {
            return mActive;
        }

        public void setActive(boolean active) {
            this.mActive = active;
        }
    }

    /**
     * @return
     */
    public boolean isServerUp()
    {
        return mServerUp;
    }

    /**
     * @param aB
     */
    public void setServerUp(boolean aB)
    {
        mServerUp = aB;
    }

    /**
     * @return
     */
    public static Log4jServer getLog4jServer()
    {
        return mLog4jServer;
    }

    /**
     * @return
     */
    public static Thread getPrimary()
    {
        return mPrimary;
    }

    /**
     * @param aThread
     */
    public static void setPrimary(Thread aThread)
    {
        mPrimary = aThread;
    }

}
