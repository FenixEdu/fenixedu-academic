package ServidorAplicacao.Servicos.teacher;

import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author asnr and scpo
 *  
 */

public class EditStudentGroupShiftTest extends TestCaseDeleteAndEditServices {

    IExecutionCourse executionCourse = null;

    IGroupProperties groupProperties = null;

    /**
     * @param testName
     */
    public EditStudentGroupShiftTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditStudentGroupShift";
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        return null;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        Object[] args = { new Integer(25), new Integer(6), new Integer(34) };
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