/*
 * Created on 15/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesByExecutionPeriodTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadExecutionCoursesByExecutionPeriodTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionCoursesByExecutionPeriod";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(1) };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 10;
    }
}