/*
 * Created on 05/Nov/2003
 *  
 */

package ServidorAplicacao.Servicos.person;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */

/*
 * NOTA: Por agora existem apenas dois utilizadores que podem aceder a este
 * serviço: 1-Grant Owner Manager a alterar una qualificação de um BOLSEIRO!
 * 2-Docente a alterar uma qualificação dele próprio Testes aos pré-filtros
 */

public abstract class QualificationServiceNeedsAuthenticationTestCase
	extends ServidorAplicacao.Servicos.ServiceTestCase {

	protected IUserView userViewGrantOwnerManager = null;
	protected IUserView userViewTeacher = null;
	protected IUserView userViewUnauthorized = null;

	protected QualificationServiceNeedsAuthenticationTestCase(String name) {
		super(name);
	}

	public void setUp() {
		super.setUp();
		userViewGrantOwnerManager =
			authenticateUser(getAuthorizedUserGrantOwnerManager());
		userViewTeacher = authenticateUser(getAuthorizedUserTeacher());
		userViewUnauthorized = authenticateUser(getUnauthorizedUser());
	}

	/*
	 * Verifica se o serviço corre para um utilizador GrantOwnerManager, com
	 * dados válidos.
	 */
	public void testAuthorizedUserGrantOwnerManager() {

		Object serviceArguments[] = getAuthorizeArgumentsGrantOwnerManager();
		try {

			gestor.executar(
				userViewGrantOwnerManager,
				getNameOfServiceToBeTested(),
				serviceArguments);
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by test testAuthorizedUser_GrantOwnerManager.");

		} catch (NotAuthorizedException ex) {
			fail(
				getNameOfServiceToBeTested()
					+ "fail testAuthorizedUser_GrantOwnerManager.");
		} catch (Exception ex) {
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	/*
	 * Verifica se o serviço corre para um utilizador Teacher, com dados
	 * válidos.
	 */
	public void testAuthorizedUserTeacher() {

		Object serviceArguments[] = getAuthorizeArgumentsTeacher();
		try {
			gestor.executar(
				userViewTeacher,
				getNameOfServiceToBeTested(),
				serviceArguments);
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by test testAuthorizedUser_Teacher.");

		} catch (NotAuthorizedException ex) {
			fail(
				getNameOfServiceToBeTested()
					+ "fail testAuthorizedUser_Teacher.");
		} catch (Exception ex) {
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	/*
	 * Um utilizador não autorizado (com os argumentos validos de Teacher e de
	 * GrantOwnerManager).
	 */
	public void testUnauthorizedUser() {
		Object serviceArguments1[] = getAuthorizeArgumentsTeacher();
		Object serviceArguments2[] = getAuthorizeArgumentsGrantOwnerManager();

		//Teste 1: argumentos validos para professor
		try {
			gestor.executar(
				userViewUnauthorized,
				getNameOfServiceToBeTested(),
				serviceArguments1);
			fail(
				getNameOfServiceToBeTested()
					+ "fail testUnauthorizedUser(with valid arguments teacher).");

		} catch (NotAuthorizedException ex) {
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by test testUnauthorizedUser(with valid arguments teacher). ");
		} catch (Exception ex) {
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}

		//Teste 2: argumentos válidos para grant owner
		try {
			gestor.executar(
				userViewUnauthorized,
				getNameOfServiceToBeTested(),
				serviceArguments2);
			fail(
				getNameOfServiceToBeTested()
					+ "fail testUnauthorizedUser(with valid arguments Grant Owner Manager).");

		} catch (NotAuthorizedException ex) {
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by test testUnauthorizedUser(with valid arguments Grant Owner Manager). ");
		} catch (Exception ex) {
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

	protected abstract String[] getAuthorizedUserGrantOwnerManager();
	protected abstract String[] getAuthorizedUserTeacher();
	protected abstract String[] getUnauthorizedUser();

	protected abstract Object[] getAuthorizeArgumentsGrantOwnerManager();
	protected abstract Object[] getAuthorizeArgumentsTeacher();

	protected abstract String getNameOfServiceToBeTested();
	protected abstract String getDataSetFilePath();
	protected abstract String getApplication();

}
