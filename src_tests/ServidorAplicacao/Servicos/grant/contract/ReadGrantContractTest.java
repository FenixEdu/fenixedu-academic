/*
 * Created on 18/Dec/2003
 *
 */

package ServidorAplicacao.Servicos.grant.contract;

import DataBeans.grant.contract.InfoGrantContract;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author  Barbosa
 * @author  Pica
 *
 */

public class ReadGrantContractTest extends ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
{

	/**
	 * @param testName
	 */
	public ReadGrantContractTest(java.lang.String testName)
	{
		super(testName);
	}

	/*
	*  (non-Javadoc)
	* @see ServiceNeedsAuthenticationTestCase#getApplication()
	*/
	protected String getApplication()
	{
		return Autenticacao.INTRANET;
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ReadGrantContract";
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/grant/contract/testReadGrantContractDataSet.xml";
	}

	/*
	 *  (non-Javadoc)
	 * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
	 */
	protected String[] getAuthenticatedAndAuthorizedUser()
	{
		String[] args = { "16", "pass", getApplication()};
		return args;
	}
	/*
	 *  (non-Javadoc)
	 * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
	 */
	protected String[] getAuthenticatedAndUnauthorizedUser()
	{
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

	/*
		 *  (non-Javadoc)
		 * @see ServiceNeedsAuthenticationTestCase#getNonAuthenticatedUser()
		 */
	protected String[] getNotAuthenticatedUser()
	{
		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}

	/*
	 *  (non-Javadoc)
	 * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getAuthorizeArguments()
	{
		Integer idInternal = new Integer(2);
		Object[] args = { idInternal };
		return args;
	}

	protected Object[] getUnauthorizeArguments()
	{
		Integer idInternal = new Integer(69);
		Object[] args = { idInternal };
		return args;
	}

	/************  Inicio dos testes ao serviço**************/

	/*
	 * Read a GrantContract Successfull
	 */
	public void testReadGrantContractSuccessfull()
	{
		try
		{
			String[] args = getAuthenticatedAndAuthorizedUser();
			IUserView id = authenticateUser(args);
			Object[] args2 = getAuthorizeArguments();

			InfoGrantContract result =
				(InfoGrantContract) ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

			//Check the read result
			Integer grantContractId = new Integer(2);
			if (!result.getIdInternal().equals(grantContractId))
				fail("Reading a GrantContract Successfull: invalid grant contract read!");

			//Verify unchanged database
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			System.out.println(
				"testReadGrantContractSuccessfull was SUCCESSFULY runned by: "
					+ getNameOfServiceToBeTested());
		} catch (FenixServiceException e)
		{
			fail("Reading a GrantContract Successfull " + e);
		} catch (Exception e)
		{
			fail("Reading a GrantContract Successfull " + e);
		}
	}

	/*
		 * Read a GrantContract Unsuccessfull
		 */
	public void testReadGrantContractUnsuccessfull()
	{
		try
		{
			String[] args = getAuthenticatedAndAuthorizedUser();
			IUserView id = authenticateUser(args);
			Object[] args2 = getUnauthorizeArguments();

			InfoGrantContract result =
				(InfoGrantContract) ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

			//Check the read result
			if (result != null)
				fail("Reading a GrantContract Unsuccessfull: grant contract should not exist!");

			//Verify unchanged database
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			System.out.println(
				"testReadGrantContractUnsuccessfull was SUCCESSFULY runned by: "
					+ getNameOfServiceToBeTested());
		} catch (FenixServiceException e)
		{
			fail("Reading a GrantContract Unsuccessfull " + e);
		} catch (Exception e)
		{
			fail("Reading a GrantContract Unsuccessfull " + e);
		}
	}
}