/*
 * Created on 05/Nov/2003
 *  
 */

package ServidorAplicacao.Servicos.person.qualification;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */

/**
 * The users that can access to the service are: 1. GrantOwnerManager over a Grant Owner qualification 2.
 * Teacher over his own qualification
 */

public abstract class QualificationServiceNeedsAuthenticationTestCase
	extends ServidorAplicacao.Servicos.ServiceTestCase
{
	protected IUserView userViewGrantOwnerManager = null;
	protected IUserView userViewTeacher = null;
	protected IUserView userViewUnauthorized = null;

	protected QualificationServiceNeedsAuthenticationTestCase(String name)
	{
		super(name);
	}

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp()
	{
		super.setUp();
		userViewGrantOwnerManager = authenticateUser(getAuthorizedUserGrantOwnerManager());
		userViewTeacher = authenticateUser(getAuthorizedUserTeacher());
		userViewUnauthorized = authenticateUser(getUnauthorizedUser());
	}

	/**
	 * Verifies if the service runs to a GrantOwnerManager user (with valid arguments)
	 */
	public void testAuthorizedUserGrantOwnerManager()
	{
		Object serviceArguments[] = getAuthorizeArgumentsGrantOwnerManager();
		try
		{
			gestor.executar(userViewGrantOwnerManager, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by test testAuthorizedUser_GrantOwnerManager.");

		} catch (NotAuthorizedException ex)
		{
			fail(getNameOfServiceToBeTested() + "fail testAuthorizedUser_GrantOwnerManager.");
		} catch (Exception ex)
		{
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	/**
	 * Verifies if the service runs to a Teacher user (with valid arguments)
	 */
	public void testAuthorizedUserTeacher()
	{
		Object serviceArguments[] = getAuthorizeArgumentsTeacher();
		try
		{
			gestor.executar(userViewTeacher, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by test testAuthorizedUser_Teacher.");

		} catch (NotAuthorizedException ex)
		{
			fail(getNameOfServiceToBeTested() + "fail testAuthorizedUser_Teacher.");
		} catch (Exception ex)
		{
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	/**
	 * A non authorized user with valid teacher arguments
	 */
	public void testUnauthorizedUserWithValidArguments1()
	{
		Object serviceArguments[] = getAuthorizeArgumentsTeacher();

		try
		{
			gestor.executar(userViewUnauthorized, getNameOfServiceToBeTested(), serviceArguments);
			fail(
				getNameOfServiceToBeTested()
					+ "fail testUnauthorizedUser(with valid arguments teacher).");

		} catch (NotAuthorizedException ex)
		{
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by test testUnauthorizedUser(with valid arguments teacher). ");
		} catch (Exception ex)
		{
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	/**
	 * A non authorized user with valid grant owner manager arguments
	 */
	public void testUnauthorizedUserWithValidArguments2()
	{
		Object serviceArguments[] = getAuthorizeArgumentsGrantOwnerManager();

		try
		{
			gestor.executar(userViewUnauthorized, getNameOfServiceToBeTested(), serviceArguments);
			fail(
				getNameOfServiceToBeTested()
					+ "fail testUnauthorizedUser(with valid arguments Grant Owner Manager).");

		} catch (NotAuthorizedException ex)
		{
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by test testUnauthorizedUser(with valid arguments Grant Owner Manager). ");
		} catch (Exception ex)
		{
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	protected IUserView authenticateUser(String[] arguments)
	{
		SuportePersistenteOJB.resetInstance();
		String args[] = arguments;
		try
		{
			return (IUserView) gestor.executar(null, "Autenticacao", args);
		} catch (Exception ex)
		{
			fail("Authenticating User!" + ex);
			return null;
		}
	}

	protected abstract String[] getAuthorizedUserGrantOwnerManager();
	protected abstract String[] getAuthorizedUserTeacher();
	protected abstract String[] getUnauthorizedUser();

	protected abstract Object[] getAuthorizeArgumentsGrantOwnerManager();
	protected abstract Object[] getAuthorizeArgumentsTeacher();

	protected abstract String getNameOfServiceToBeTested();
	protected abstract String getDataSetFilePath();
	protected abstract String getApplication();

}
