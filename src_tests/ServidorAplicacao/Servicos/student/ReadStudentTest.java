/*
 * ReadStudentTest.java
 * JUnit based test
 *
 * Created on 16th of December de 2002, 3:05
 */

package ServidorAplicacao.Servicos.student;

/**
 * 
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servicos.TestCaseReadServices;

public class ReadStudentTest extends TestCaseReadServices {
    public ReadStudentTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    // FIXME: When the service is corrected, correct me too :)
    protected String getNameOfServiceToBeTested() {
        return "ReadStudent";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        Object[] result = { new Integer(99999) };
        return result;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        Object[] result = { new Integer(600) };
        return result;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    protected Object getObjectToCompare() {

        InfoStudent infoStudent = new InfoStudent();
        // FIXME: When the equals for the InfoStudent is corrected it's
        // necessary to set the Type of Degree
        infoStudent.setNumber(new Integer(600));

        return infoStudent;
    }

}