/*
 * Created on 12/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.gesdis;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformationTest extends ServiceNeedsAuthenticationTestCase
{

    public ReadCourseInformationTest(String testName)
    {
        super(testName);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
    protected String getNameOfServiceToBeTested()
    {
        return "ReadCourseInformation";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
	 */
    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/gesdis/testReadCourseInformationDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/gesdis/testExpectedReadCourseInformationDataSet.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "jorge", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "jccm", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
    protected Object[] getAuthorizeArguments()
    {
        Integer executionCourseId = new Integer(24);

        Object[] args = { executionCourseId };
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            gestor.executar(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

            // TODO: verificar os dados do siteView?????'
            // verifica as alteracoes da base de dados
            compareDataSetUsingExceptedDataSetTablesAndColumns(getDataSetFilePath());
        } catch (FenixServiceException ex)
        {
            fail("Reading a Course Information " + ex);
        } catch (Exception ex)
        {
            fail("Reading a Course Information " + ex);
        }
    }

}
