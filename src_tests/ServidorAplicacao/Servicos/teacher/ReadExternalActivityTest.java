/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.SiteView;
import DataBeans.teacher.InfoExternalActivity;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadExternalActivityTest extends ServiceNeedsAuthenticationTestCase
{

    /**
	 * @param testName
	 */
    public ReadExternalActivityTest(String testName)
    {
        super(testName);
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testReadExternalActivityDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadExternalActivity";
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {

        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser()
    {

        String[] args = { "julia", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser()
    {

        String[] args = { "jccm", "pass", getApplication()};
        return args;
    }

    protected Object[] getAuthorizeArguments()
    {
        Object[] args = { new Integer(1)};
        return args;
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testSucessfull()
    {
        try
        {
            SiteView result = null;
            Object[] args = { new Integer(1)};

            result = (SiteView) gestor.executar(userView, getNameOfServiceToBeTested(), args);

            InfoExternalActivity infoExternalActivity = (InfoExternalActivity) result.getComponent();

            assertTrue(infoExternalActivity.getIdInternal().equals(args[0]));
            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Reading a ExternalActivity of a Teacher" + ex);
        }
    }
}
