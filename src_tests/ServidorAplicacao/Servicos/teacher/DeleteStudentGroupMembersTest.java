/*
 * Created on 03/Set/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author asnr and scpo
 *  
 */
public class DeleteStudentGroupMembersTest extends TestCaseDeleteAndEditServices {

    public DeleteStudentGroupMembersTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteStudentGroupMembers";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        List studentUsernames = new ArrayList();
        studentUsernames.add("user");
        Object[] argsDeleteStudentGroupMembers = { new Integer(25), new Integer(6), studentUsernames };

        return argsDeleteStudentGroupMembers;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        List studentUsernames = new ArrayList();
        studentUsernames.add("15");
        Object[] argsDeleteStudentGroupMembers = { new Integer(25), new Integer(99), studentUsernames };

        return argsDeleteStudentGroupMembers;
    }
}