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

public class ReadEnroledExecutionCoursesTest extends TestCaseReadServices {

    public ReadEnroledExecutionCoursesTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadEnroledExecutionCoursesTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadEnroledExecutionCourses";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] result = { "jorge" };
        return result;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] result = { "user" };
        return result;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    protected Object getObjectToCompare() {
        return null;
    }

}