/*
 * Created on 16/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

/**
 * @author lmac1
 */
public class AssociateExecutionCourseToCurricularCourseTest extends TestCaseManagerInsertAndEditServices {

    public AssociateExecutionCourseToCurricularCourseTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "AssociateExecutionCourseToCurricularCourse";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        Object[] args = { new Integer(24), new Integer(1), new Integer(1) };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        Object[] args = { new Integer(24), new Integer(23), new Integer(1) };
        return args;
    }
}