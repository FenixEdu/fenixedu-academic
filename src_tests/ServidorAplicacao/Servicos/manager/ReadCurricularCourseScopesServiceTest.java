/*
 * Created on 2/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

/**
 * @author lmac1
 */

public class ReadCurricularCourseScopesServiceTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadCurricularCourseScopesServiceTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadCurricularCourseScopes";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(23) };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 6;
    }
}