/*
 * SuportePersistenteOJBTest.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author ars
 */
public class SuportePersistenteOJBTest extends TestCaseOJB {
    
    public SuportePersistenteOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(SuportePersistenteOJBTest.class);
        
        return suite;
    }
}
