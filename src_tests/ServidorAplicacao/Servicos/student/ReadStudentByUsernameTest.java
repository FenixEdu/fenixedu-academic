/*
 * ReadStudentTest.java
 * JUnit based test
 *
 * Created on 07th of January de 2003, 23:11
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.student;

/**
 * 
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;

public class ReadStudentByUsernameTest extends TestCaseReadServices {
    public ReadStudentByUsernameTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentByUsernameTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentByUsername";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        Object[] result = { "userdesc" };
        return result;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] result = { "user" };
        return result;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    protected Object getObjectToCompare() {
        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setNumber(new Integer(600));
        // FIXME: When the equals for the InfoStudent is corrected , the
        // degreeType has to be set
        return infoStudent;
    }

}