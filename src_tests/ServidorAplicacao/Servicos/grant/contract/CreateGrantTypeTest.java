/*
 * Created on 25/Nov/2003
 *  
 */

package ServidorAplicacao.Servicos.grant.contract;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.grant.contract.InfoGrantType;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class CreateGrantTypeTest extends ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
{

    /**
	 * @param testName
	 */
    public CreateGrantTypeTest(String testName)
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
        return "CreateGrantType";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/grant/contract/testCreateGrantTypeDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/grant/contract/testCreateGrantTypeExpectedDataSet.xml";
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

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
    protected Object[] getAuthorizeArguments()
    {
        InfoGrantType infoGrantType = new InfoGrantType();

        infoGrantType.setIndicativeValue(new Double(4.5));
        infoGrantType.setMinPeriodDays(new Integer(30));
        infoGrantType.setMaxPeriodDays(new Integer(90));
        infoGrantType.setName("bolsaFenix");
        infoGrantType.setSigla("BF");
        infoGrantType.setSource("BIST");

        Object[] args = { infoGrantType };
        return args;
    }

    protected Object[] getAuthorizeArgumentsEdit()
    {
        InfoGrantType infoGrantType = new InfoGrantType();

        infoGrantType.setIndicativeValue(new Double(4.5));
        infoGrantType.setMinPeriodDays(new Integer(30));
        infoGrantType.setMaxPeriodDays(new Integer(90));
        infoGrantType.setName("bolsaFenix");
        infoGrantType.setSource("BIST");
        infoGrantType.setIdInternal(new Integer(1));
        infoGrantType.setSigla("M");

        Object[] args = { infoGrantType };
        return args;
    }

    protected Object[] getUnauthorizeArguments(boolean edit)
    {
        InfoGrantType infoGrantType = new InfoGrantType();

        infoGrantType.setSigla("M");
        if (edit)
            infoGrantType.setIdInternal(new Integer(2));

        Object[] args = { infoGrantType };
        return args;
    }

    /** ********** Inicio dos testes ao serviço ************* */

    /*
	 * Grant Type Creation Successfull
	 */
    public void testCreateGrantTypeSuccessfull()
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
                    + " was SUCCESSFULY runned by test: testCreateGrantTypeSuccessfull");
        } catch (FenixServiceException e)
        {
            fail("Creating a new GrantType successfull " + e);
        } catch (Exception e)
        {
            fail("Creating a new GrantType successfull " + e);
        }
    }

    /*
	 * Grant Type Creation Unsuccessfull: existing grant type
	 */
    public void testCreateGrantTypeUnsuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments(false);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
        } catch (NonExistingServiceException e)
        {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantTypeUnsuccessfull");
        } catch (FenixServiceException e)
        {
            fail("Creating a new GrantType unsuccessfull " + e);
        } catch (Exception e)
        {
            fail("Creating a new GrantType unsuccessfull " + e);
        }
    }

    /*
	 * Grant Type Edition Successfull
	 */
    public void testEditGrantTypeSuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArgumentsEdit();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/grant/contract/testEditGrantTypeExpectedDataSet.xml");
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testEditGrantTypeSuccessfull");
        } catch (FenixServiceException e)
        {
            fail("Editing a GrantType successfull " + e);
        } catch (Exception e)
        {
            fail("Editing a GrantType successfull " + e);
        }
    }

    /*
	 * Grant Type Edition Unsuccessfull: existing grant type
	 */
    public void testEditGrantTypeUnsuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments(true);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
        } catch (NonExistingServiceException e)
        {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testEditGrantTypeUnsuccessfull");
        } catch (FenixServiceException e)
        {
            fail("Editing a GrantType unsuccessfull " + e);
        } catch (Exception e)
        {
            fail("Editing a GrantType unsuccessfull " + e);
        }
    }
}