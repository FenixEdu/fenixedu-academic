/*
 * Created on 5/Nov/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author  Luis Egidio, lmre@mega.ist.utl.pt
 * 			Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public abstract class ServiceNeedsStudentAuthenticationTestCase
	extends ServiceTestCase {

	protected IUserView userView = null;
	protected IUserView userView2 = null;
	protected IUserView userView3 = null;

	protected ServiceNeedsStudentAuthenticationTestCase(String name) {
		super(name);
	}

	protected void setUp() {
		super.setUp();
		userView =
			authenticateUser(getAuthenticatedAndAuthorizedUser());
		userView2 =
			authenticateUser(
				getAuthenticatedAndUnauthorizedUser());
		userView3 = authenticateUser(getNotAuthenticatedUser());
	}

	public void testAuthorizedUser() {
		Object serviceArguments[] = getAuthorizeArguments();
		try {
			
				gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					serviceArguments);
			System.out.println(
				"testAuthorizedUser was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (NotAuthorizedException ex) {
			System.out.println(
				"testAuthorizedUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(
				getNameOfServiceToBeTested()
					+ "fail testAuthorizedUser");

		} catch (Exception ex) {
			System.out.println(
				"testAuthorizedUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());

		}

	}

	public void testUnauthorizedUser() {
		Object serviceArguments[] = getAuthorizeArguments();
		try {
		
				gestor.executar(
					userView2,
					getNameOfServiceToBeTested(),
					serviceArguments);

			System.out.println(
				"testUnauthorizedUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(
				getNameOfServiceToBeTested()
					+ "fail testUnauthorizedUser");

		} catch (NotAuthorizedException ex) {
			System.out.println(
				"testUnauthorizedUser was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (Exception ex) {
			System.out.println(
				"testUnauthorizedUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());

		}
	}

	public void testNonStudentUser() {

		Object serviceArguments[] = getAuthorizeArguments();

		try {
			
				gestor.executar(
					userView3,
					getNameOfServiceToBeTested(),
					serviceArguments);

			System.out.println(
				"testNonStudentUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(
				getNameOfServiceToBeTested()
					+ "fail testNonStudentUser");

		} catch (NotAuthorizedException ex) {
			System.out.println(
				"testNonStudentUser was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (Exception ex) {
			System.out.println(
				"testNonStudentUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());

		}
	}

	protected IUserView authenticateUser(String[] arguments) {
		SuportePersistenteOJB.resetInstance();
		String args[] = arguments;

		try {
			return (IUserView) gestor.executar(null, "Autenticacao", args);
		} catch (Exception ex) {
			fail("Authenticating User!" + ex);
			return null;

		}
	}

	protected abstract String getNameOfServiceToBeTested();
	protected abstract String getDataSetFilePath();

	protected abstract String[] getAuthenticatedAndAuthorizedUser();
	protected abstract String[] getAuthenticatedAndUnauthorizedUser();
	protected abstract String[] getNotAuthenticatedUser();

	protected abstract Object[] getAuthorizeArguments();
	protected abstract String getApplication();

}
