/*
 * Created on 27/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

/**
 * @author lmac1
 */
public class ReadExecutionCourseTeachersTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadExecutionCourseTeachersTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionCourseTeachers";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(24) };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 2;
    }
}