/*
 * Created on 04/Set/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Dominio.IExecutionCourse;
import ServidorAplicacao.Servicos.TestCaseCreateServices;

/**
 * @author asnr and scpo
 *  
 */
public class GroupEnrolmentTest extends TestCaseCreateServices {

    IExecutionCourse executionCourse = null;

    /**
     * @param testName
     */
    public GroupEnrolmentTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "GroupEnrolment";
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

        List studentCodes = new ArrayList();
        studentCodes.add(new Integer(10));
        studentCodes.add(new Integer(6));
        Object[] args = { new Integer(5), new Integer(9), new Integer(2), studentCodes, "15" };
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
        HashMap hashMap = new HashMap();

        List args = new ArrayList();

        args.add(0, new Integer(5));
        args.add(1, new Integer(34));
        args.add(2, null);
        args.add(3, "15");
        hashMap.put("Não respeita as propriedades do grupo", args);

        //hashMap.put("O grupo com esse número já existe na base de dados ouou
        // os elementos", args);

        return hashMap;
    }

}