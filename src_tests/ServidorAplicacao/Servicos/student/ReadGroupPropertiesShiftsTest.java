/*
 *
 * Created on 03/09/2003
 */

package ServidorAplicacao.Servicos.student;

/**
 * 
 * @author asnr and scpo
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.Servicos.TestCaseReadServices;

public class ReadGroupPropertiesShiftsTest extends TestCaseReadServices {

    public ReadGroupPropertiesShiftsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadGroupPropertiesShiftsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadGroupPropertiesShifts";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] result = { new Integer(6), new Integer(9) };
        return result;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] result = { new Integer(5), new Integer(34) };
        return result;

    }

    protected int getNumberOfItemsToRetrieve() {
        return 3;
    }

    protected Object getObjectToCompare() {

        return null;
    }

}