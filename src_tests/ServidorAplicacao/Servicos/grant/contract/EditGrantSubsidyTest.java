/*
 * Created on 22/Jan/2004
 *  
 */

package ServidorAplicacao.Servicos.grant.contract;

import DataBeans.grant.contract.InfoGrantSubsidy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditGrantSubsidyTest
    extends ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
{

    /**
     * @param testName
     */
    public EditGrantSubsidyTest(String testName)
    {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication()
    {
        return Autenticacao.INTRANET;
    }

    protected String getNameOfServiceToBeTested()
    {
        return "EditGrantSubsidy";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/grant/contract/testEditGrantSubsidyDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/grant/contract/testEditGrantSubsidyExpectedDataSet.xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "16", "pass", getApplication()};
        return args;
    }
    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "julia", "pass", getApplication()};
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getNonAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "fiado", "pass", getApplication()};
        return args;
    }

    protected Object[] getArguments()
    {
        InfoGrantSubsidy infoGrantSubsidy = null;
        Object[] args = { infoGrantSubsidy };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments()
    {
       
        Object[] args = getArguments();
        return args;
    }

    protected Object[] getAuthorizeArgumentsEdit()
    {
        Object[] args = {};
        return args;
    }

    protected Object[] getUnauthorizeArgumentsEdit()
    {
        Object[] args = {};
        return args;
    }

    /** ********** Inicio dos testes ao serviço ************* */

    /*
     * Grant Subsidy Creation Successfull
     */
    public void testCreateNewGrantSubsidySuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantSubsidySuccessfull");
        } catch (FenixServiceException e)
        {
            fail("Creating a new GrantSubsidy " + e);
        } catch (Exception e)
        {
            fail("Creating a new GrantSubsidy " + e);
        }
    }
}