/*
 * Created on 03/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseCreateServices;

/**
 * @author asnr and scpo
 *  
 */
public class InsertStudentGroupMembersTest extends TestCaseCreateServices {

    IExecutionCourse executionCourse = null;

    /**
     * @param testName
     */
    public InsertStudentGroupMembersTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "InsertStudentGroupMembers";
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        List studentCodes = new ArrayList();
        studentCodes.add(new Integer(10));
        studentCodes.add(new Integer(11));
        Object[] args = { new Integer(25), new Integer(99), studentCodes };
        return args;

    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        List studentCodes = new ArrayList();
        studentCodes.add(new Integer(10));
        studentCodes.add(new Integer(11));
        Object[] args = { new Integer(25), new Integer(7), studentCodes };
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