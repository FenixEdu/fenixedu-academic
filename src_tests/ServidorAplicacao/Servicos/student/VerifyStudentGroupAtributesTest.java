/*
 * Created on 04/Set/2003
 */
package ServidorAplicacao.Servicos.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ServidorAplicacao.Servicos.TestCaseCreateServices;

/**
 * @author asnr and scpo
 */
public class VerifyStudentGroupAtributesTest extends TestCaseCreateServices {

    /**
     * @param testName
     */
    public VerifyStudentGroupAtributesTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "VerifyStudentGroupAtributes";
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] args = { new Integer(11), "sousa" };
        return args;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        return null;
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
        args.add(0, new Integer(15));
        args.add(1, "sousa");
        hashMap.put("O grupo nao existe", args);

        args.add(0, new Integer(11));
        args.add(1, "user");
        hashMap.put("Ja esta inscrito", args);

        args.add(0, new Integer(11));
        args.add(1, "sousa");
        hashMap.put("Ja atingiu a capacidade maxima do grupo!", args);

        return hashMap;
    }

}