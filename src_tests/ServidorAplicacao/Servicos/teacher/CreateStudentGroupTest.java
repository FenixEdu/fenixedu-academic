/*
 * Created on 03/Set/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Dominio.IExecutionCourse;
import ServidorAplicacao.Servicos.TestCaseCreateServices;

/**
 * @author asnr and scpo
 *  
 */
public class CreateStudentGroupTest extends TestCaseCreateServices {

    IExecutionCourse executionCourse = null;

    /**
     * @param testName
     */
    public CreateStudentGroupTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "CreateStudentGroup";
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        Object[] args = { new Integer(25), new Integer(1), new Integer(3), new Integer(9), null };
        return args;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        List studentCodes = new ArrayList();
        studentCodes.add(new String("14"));
        studentCodes.add(new String("15"));
        Object[] args = { new Integer(25), new Integer(5), new Integer(3), new Integer(9), studentCodes };
        return args;

    }

    /**
     * This method must return 'true' if the service needs authorization to be
     * runned and 'false' otherwise.
     */
    protected boolean needsAuthorization() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
     */
    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

}