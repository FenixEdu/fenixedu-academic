/*
 * ReadStudentLessonsTest.java JUnit based test
 *
 * Created on January 4th, 2003, 23:35
 */

package ServidorAplicacao.Servicos.student;

/**
 * 
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servicos.TestCaseReadServices;

public class ReadStudentLessonsTest extends TestCaseReadServices {

    public ReadStudentLessonsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentLessonsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentLessons";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        InfoStudent infoStudent = new InfoStudent();
        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setUsername("desc");
        infoStudent.setInfoPerson(infoPerson);
        Object[] result = { infoStudent };
        return result;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        InfoStudent infoStudent = new InfoStudent();
        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setUsername("45498");
        infoStudent.setInfoPerson(infoPerson);
        Object[] result = { infoStudent };
        return result;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 2;
    }

    protected Object getObjectToCompare() {
        return null;
    }

}