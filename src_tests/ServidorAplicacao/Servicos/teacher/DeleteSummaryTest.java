package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo
 *  
 */
public class DeleteSummaryTest extends SummaryBelongsExecutionCourseTest {

    public DeleteSummaryTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testDeleteSummaryDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedDeleteSummaryDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteSummary";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {

        String[] args = { "jccm", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {

        Object[] args = { new Integer(24), new Integer(281) };
        return args;
    }

    protected Object[] getTestSummaryUnsuccessfullArguments() {

        Object[] args = { new Integer(25), new Integer(261) };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {

        try {

            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
        } catch (FenixServiceException ex) {
            fail("Deleting the Summary of a Site" + ex);
        } catch (Exception ex) {
            fail("Deleting the Summary of a Site" + ex);
        }
    }
}