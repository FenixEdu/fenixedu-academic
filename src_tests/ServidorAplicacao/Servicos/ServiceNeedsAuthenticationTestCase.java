/*
 * Created on 7/Out/2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servicos;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis & Nuno
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class ServiceNeedsAuthenticationTestCase
	extends ServiceTestCase {

	protected IUserView userView = null;
	protected IUserView userView2 = null;
	protected IUserView userView3 = null;

	protected ServiceNeedsAuthenticationTestCase(String name) {
		super(name);
	}

	protected void setUp() {
		super.setUp();
		userView = authenticateUser(getAuthorizedUser());
		userView2 = authenticateUser(getUnauthorizedUser());
		userView3 = authenticateUser(getNonTeacherUser());

	}

	protected void testAuthorizedUser() {

		Object serviceArguments[] = getAuthorizeArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					serviceArguments);
			System.out.println(
				"testAuthorizedUser was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (NotAuthorizedException ex) {
			fail(getNameOfServiceToBeTested() + "fail testAuthorizedUser");

		} catch (Exception ex) {
			fail(
				"Não foi possivel correr o serviço"
					+ getNameOfServiceToBeTested());

		}

	}

	protected void testUnauthorizedUser() {
		Object serviceArguments[] = getAuthorizeArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView2,
					getNameOfServiceToBeTested(),
					serviceArguments);
			fail(getNameOfServiceToBeTested() + "fail testUnauthorizedUser");

		} catch (NotAuthorizedException ex) {

			System.out.println(
				"testUnauthorizedUser was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (Exception ex) {
			fail(
				"Não foi possivel correr o serviço"
					+ getNameOfServiceToBeTested());

		}
	}

	protected void testNonTeacherUser() {

		Object serviceArguments[] = getAuthorizeArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView3,
					getNameOfServiceToBeTested(),
					serviceArguments);
			fail(getNameOfServiceToBeTested() + "fail testUnauthorizedUser");

		} catch (NotAuthorizedException ex) {

			System.out.println(
				"testUnauthorizedUser was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (Exception ex) {
			fail(
				"Não foi possivel correr o serviço"
					+ getNameOfServiceToBeTested());

		}
	}

	protected IUserView authenticateUser(String[] arguments) {
		SuportePersistenteOJB.resetInstance();
		String args[] = arguments;

		try {
			return (IUserView) gestor.executar(
				null,
				"Autenticacao",
				args);
		} catch (Exception ex) {
			fail("Authenticating User!" + ex);
			return null;

		}
	}

	protected abstract String[] getAuthorizedUser();
	protected abstract String[] getUnauthorizedUser();
	protected abstract String[] getNonTeacherUser();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract Object[] getAuthorizeArguments();
	protected abstract String getDataSetFilePath();

}
