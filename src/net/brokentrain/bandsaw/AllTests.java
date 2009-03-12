package net.brokentrain.bandsaw;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brandon
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for bandsaw");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(BandsawTest.class));
        //$JUnit-END$
        return suite;
    }
}
