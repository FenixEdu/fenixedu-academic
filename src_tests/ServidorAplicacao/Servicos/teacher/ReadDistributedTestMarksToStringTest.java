/*
 * Created on 18/Fev/2004
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadDistributedTestMarksToStringTest extends ServiceNeedsAuthenticationTestCase {
    public ReadDistributedTestMarksToStringTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadDistributedTestMarks2StringDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadDistributedTestMarksToString";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "D3673", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {

        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer executionCourseId = new Integer(34033);
        Integer distributedTestId = new Integer(190);
        Object[] args = { executionCourseId, distributedTestId };
        return args;
    }

    protected Object[] getAuthorizeArgumentsForOneTest() {
        Integer executionCourseId = new Integer(34033);
        Integer distributedTestId = new Integer(190);
        Object[] args = { executionCourseId, distributedTestId };

        return args;
    }

    protected Object[] getAuthorizeArgumentsForTwoTests() {
        Integer executionCourseId = new Integer(34033);
        String[] distributedTestId = { "190", "191" };
        Object[] args = { executionCourseId, distributedTestId };

        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {
        try {
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
            Object[] args1 = getAuthorizeArgumentsForOneTest();
            String result1 = (String) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args1);
            Object[] args2 = getAuthorizeArgumentsForTwoTests();
            String result2 = (String) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args2);

            String result2Compare1 = new String(
                    "Número\tNome\tP1\tNota\n46730\tXxxxxxxxxxxxxxxx Xxxxxxxxx Xxxxxxx Xxxxxxxxx\t-0,33\t0\n48178\tXxxxxxxxxxxxxxxx Xxxxxxxxx Xxxxxxx Xxxxxxxxx\t-0,33\t0\n48184\tXxxxxxxxxxxxxxxx Xxxxxxxxx Xxxxxxx Xxxxxxxxx\t0\t0\n");

            String result2Compare2 = new String(
                    "Número\tNome\tFicha 1\tFicha 2\t\n46730\tXxxxxxxxxxxxxxxx Xxxxxxxxx Xxxxxxx Xxxxxxxxx\t0\t0\t\n48178\tXxxxxxxxxxxxxxxx Xxxxxxxxx Xxxxxxx Xxxxxxxxx\t0\t0\t\n48184\tXxxxxxxxxxxxxxxx Xxxxxxxxx Xxxxxxx Xxxxxxxxx\t0\tNA\t");

            assertEquals(result2Compare1, result1);
            assertEquals(result2Compare2, result2);

        } catch (FenixServiceException ex) {
            fail("ReadDistributedTestMarksToStringTest " + ex);
        } catch (Exception ex) {
            fail("ReadDistributedTestMarksToStringTest " + ex);
        }
    }
}