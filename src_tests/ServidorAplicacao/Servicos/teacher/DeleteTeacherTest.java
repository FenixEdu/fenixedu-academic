package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.HashMap;

import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Fernanda Quitéio
 */
public class DeleteTeacherTest extends TestCaseDeleteAndEditServices {

    /**
     * @param testName
     */
    public DeleteTeacherTest(String testName) {
        super(testName);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "DeleteTeacher";

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        Object[] testArgs = { new Integer(26), new Integer(1) };

        return testArgs;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] testArgs = { new Integer(24), new Integer(2) };

        return testArgs;

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