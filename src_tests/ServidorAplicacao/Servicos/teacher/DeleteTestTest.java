/*
 * Created on 11/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Susana Fernandes
 */
public class DeleteTestTest extends TestCaseDeleteAndEditServices {

    public DeleteTestTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteTest";
    }

    protected boolean needsAuthorization() {
        return true;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(26), new Integer(3) };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] args = { new Integer(26), new Integer(0) };
        return args;
    }
}