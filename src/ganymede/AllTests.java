package ganymede;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brandon
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ganymede");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(GanymedeTest.class));
        //$JUnit-END$
        return suite;
    }
}
