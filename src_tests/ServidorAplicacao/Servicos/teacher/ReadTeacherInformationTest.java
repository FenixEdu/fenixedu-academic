/*
 * Created on 7/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.teacher.InfoSiteTeacherInformation;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadTeacherInformationTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param testName
     */
    public ReadTeacherInformationTest(String name) {
        super(name);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadTeacherInformationDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadTeacherInformation";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "jorge", "pass", getApplication() };
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

    public void testSuccessfull() {

        try {
            SiteView result = null;

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), getAuthorizeArguments());

            InfoSiteTeacherInformation infoSiteTeacherInformation = (InfoSiteTeacherInformation) result
                    .getComponent();
            InfoTeacher infoTeacher = infoSiteTeacherInformation.getInfoTeacher();
            assertEquals(userView.getUtilizador(), infoTeacher.getInfoPerson().getUsername());
            // verifica as alteracoes da base de dados
            compareDataSetUsingExceptedDataSetTablesAndColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("Reading a teacher Information " + ex);
        }
    }
}