package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.student.certificate;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;

public class ReadCertificateListTest extends TestCaseReadServices {

    public ReadCertificateListTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadCertificateListTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadCertificateList";
    }

    protected int getNumberOfItemsToRetrieve() {
        return 4;
    }

    protected Object getObjectToCompare() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }
}