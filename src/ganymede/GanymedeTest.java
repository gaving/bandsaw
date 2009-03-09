package ganymede;

import java.net.URL;

import org.apache.log4j.Logger;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Brandon
 */
public class GanymedeTest extends TestCase {

    static private Logger log = Logger.getLogger(GanymedeTest.class);

    /**
     * Constructor for GanymedeTest.
     * @param arg0
     */
    public GanymedeTest(String arg0) {
        super(arg0);
    }

    static public TestSuite suite() {
        TestSuite suite = new TestSuite(GanymedeTest.class);
        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(GanymedeTest.suite());
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConfig() {
        URL a = getClass().getClassLoader().getResource("log4j.xml");
        if ( a == null ) { fail(); }
    }

    public void testLimit() {
        for (int i=0; i<2500; i++) {
            log.debug("Limit Testing : " + i);
        }
        assertTrue(true);
    }

    /**
     * Throw some logs out there 
     */
    public void testLogging() {
        log.debug("debug" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "\n" + "\n" +
                "Is this message", new Exception());
        log.info("info");
        log.warn("warn");
        log.error("error");
        log.fatal("fatal");
    }

}
