/*
 * Created on 29/Jul/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author asnr and scpo
 *  
 */
public class DeleteStudentGroupTest extends TestCaseDeleteAndEditServices {

    public DeleteStudentGroupTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteStudentGroup";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] argsDeleteStudentGroup = { new Integer(25), new Integer(7) };

        return argsDeleteStudentGroup;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] argsDeleteStudentGroup = { new Integer(25), new Integer(6) };

        return argsDeleteStudentGroup;
    }
}