/*
 * Created on 6/Nov/2003
 *
 */
package ServidorAplicacao.Servicos.coordinator;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

/**
 *fenix-head
 *ServidorAplicacao.Servicos.coordinator
 * @author João Mota
 *6/Nov/2003
 *
 */
public abstract class CoordinatorIsResponsibleByExecutionDegree
	extends CoordinatorBelongsToExecutionDegree {
	IUserView userView4 = null;
	/**
	 * @param name
	 */
	public CoordinatorIsResponsibleByExecutionDegree(String name) {
		super(name);
	}
	protected void setUp() {
		super.setUp();
		userView4 = authenticateUser(getAuthenticatedNotResponsibleCoordinatorUserOfExecutionDegree());
	}
	protected abstract String[] getAuthenticatedNotResponsibleCoordinatorUserOfExecutionDegree();
	protected abstract String[] getAuthenticatedAndAuthorizedUser();
	protected abstract String[] getAuthenticatedAndUnauthorizedUser();
	protected abstract String[] getNotAuthenticatedUser();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract Object[] getAuthorizeArguments();
	protected abstract Object[] getNonAuthorizeArguments();
	protected abstract String getDataSetFilePath();
	protected abstract String getApplication();

	public void testCoordinatorIsNotResponsibleCoordinatorOfExecutionDegree() {
		Object serviceArguments[] = getAuthorizeArguments();
		try {
			gestor.executar(userView4, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				"testCoordinatorIsNotResponsibleCoordinatorOfExecutionDegree was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(
				getNameOfServiceToBeTested()
					+ "fail testCoordinatorIsNotResponsibleCoordinatorOfExecutionDegree");
		} catch (NotAuthorizedException ex) {
			System.out.println(
				"testCoordinatorIsNotResponsibleCoordinatorOfExecutionDegree was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (Exception ex) {
			System.out.println(
				"testCoordinatorIsNotResponsibleCoordinatorOfExecutionDegree was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());

		}
	}

}
