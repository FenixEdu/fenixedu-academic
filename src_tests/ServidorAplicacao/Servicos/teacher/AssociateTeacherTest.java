package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ServidorAplicacao.Servicos.TestCaseCreateServices;

/**
 * @author Fernanda Quitério
 */
public class AssociateTeacherTest extends TestCaseCreateServices {

    /**
     * @param testName
     */
    public AssociateTeacherTest(String testName) {
        super(testName);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "AssociateTeacher";

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        Object[] testArgs = { new Integer(26), new Integer(2) };

        return testArgs;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
     */
    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        HashMap args = new HashMap();

        //The Teacher does not exists

        Integer infoExecutionCourseCode = new Integer(26);
        Integer nonExistingTeacherNumber = new Integer(424322343);
        List testArgs1 = new ArrayList();
        testArgs1.add(infoExecutionCourseCode);
        testArgs1.add(nonExistingTeacherNumber);
        args.put("Non-Existing Teacher Test", testArgs1);

        //The teacher already lectures the execution Course
        Integer infoExecutionCourseCode1 = new Integer(26);
        Integer teacherNumber1 = new Integer(1);
        List testArgs2 = new ArrayList();
        testArgs1.add(infoExecutionCourseCode1);
        testArgs1.add(teacherNumber1);

        args.put("The teacher already lectures the execution Course Test", testArgs2);

        return args;
    }

    /**
     * This method must return 'true' if the service needs authorization to be
     * runned and 'false' otherwise.
     */
    protected boolean needsAuthorization() {
        return true;
    }

}