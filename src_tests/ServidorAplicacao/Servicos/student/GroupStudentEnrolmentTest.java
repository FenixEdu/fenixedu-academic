/*
 * Created on 04/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.student;

import java.util.HashMap;

import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseCreateServices;

/**
 * @author asnr and scpo
 *  
 */
public class GroupStudentEnrolmentTest extends TestCaseCreateServices {

    /**
     * @param testName
     */
    public GroupStudentEnrolmentTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "GroupStudentEnrolment";
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] args = { new Integer(6), "user" };
        return args;

    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(8), "15" };
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