/*
 * Created on 04/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.student;

import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author asnr and scpo
 *  
 */
public class UnEnrollStudentInGroupTest extends TestCaseDeleteAndEditServices {

    public UnEnrollStudentInGroupTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "UnEnrollStudentInGroup";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] args = { "user", new Integer(47) };
        return args;
    }
}