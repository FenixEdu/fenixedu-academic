/*
 * Created on 15/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

/**
 * @author lmac1
 */
public class DissociateExecutionCourseTest extends TestCaseManagerInsertAndEditServices {

    public DissociateExecutionCourseTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "DissociateExecutionCourse";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(24), new Integer(1) };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        Object[] args = { new Integer(24), new Integer(111) };
        return args;
    }

}