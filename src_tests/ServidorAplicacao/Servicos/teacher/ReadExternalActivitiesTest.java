/*
 * Created on Nov 13, 2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.SiteView;
import DataBeans.teacher.InfoSiteExternalActivities;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadExternalActivitiesTest extends ServiceNeedsAuthenticationTestCase {
    /**
     * @param testName
     */
    public ReadExternalActivitiesTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadExternalActivitiesDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExternalActivities";
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
        Object[] args = { userView.getUtilizador() };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testReadAllWithExternalActivities() {
        try {
            SiteView result = null;

            Object[] args = { userView.getUtilizador() };

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            InfoSiteExternalActivities infoSiteActivities = (InfoSiteExternalActivities) result
                    .getComponent();

            List infoExternalActivities = infoSiteActivities.getInfoExternalActivities();
            assertEquals(infoExternalActivities.size(), 2);
            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("Reading all external activities of a Teacher with external activities " + ex);
        }
    }

    public void testReadAllWithoutExternalActivities() {
        try {
            SiteView result = null;

            String[] args = { "maria", "pass", getApplication() };
            IUserView userView = authenticateUser(args);

            Object[] serviceArgs = { userView.getUtilizador() };

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), serviceArgs);

            InfoSiteExternalActivities infoSiteActivities = (InfoSiteExternalActivities) result
                    .getComponent();

            List infoExternalActivities = infoSiteActivities.getInfoExternalActivities();
            assertTrue(infoExternalActivities.isEmpty());
            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("Reading all external activities of a Teacher without external activities " + ex);
        }
    }
}