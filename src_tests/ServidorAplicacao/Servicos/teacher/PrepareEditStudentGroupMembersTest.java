package ServidorAplicacao.Servicos.teacher;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.Servicos.TestCaseReadServices;

/**
 * @author asnr and scpo
 *  
 */
public class PrepareEditStudentGroupMembersTest extends TestCaseReadServices {

    public PrepareEditStudentGroupMembersTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(PrepareEditStudentGroupMembersTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "PrepareEditStudentGroupMembers";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] result = { new Integer(26), new Integer(1) };
        return result;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] result = { new Integer(25), new Integer(6) };
        return result;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 4;
    }

    protected Object getObjectToCompare() {
        return null;
    }

}