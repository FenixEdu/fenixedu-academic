/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class DeleteExternalActivityCareerTest extends ServiceNeedsAuthenticationTestCase
{

    /**
	 *  
	 */
    public DeleteExternalActivityCareerTest(String testName)
    {
        super(testName);
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testDeleteExternalActivityDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testExpectedDeleteExternalActivityDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "DeleteExternalActivity";
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

    public void testSuccessfull()
    {
        try
        {
            Boolean result = null;
            Object[] args = { new Integer(1)};

            result = (Boolean) gestor.executar(userView, getNameOfServiceToBeTested(), args);

            assertTrue(result.booleanValue());
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Deleting a ExternalActivity of a Teacher " + ex);
        }
    }
}
