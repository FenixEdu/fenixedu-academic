/*
 * Created on 2/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

/**
 * @author lmac1
 */

public class ReadCurricularCoursesByDegreeCurricularPlanTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadCurricularCoursesByDegreeCurricularPlanTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadCurricularCoursesByDegreeCurricularPlan";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(1) };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 13;
    }
}