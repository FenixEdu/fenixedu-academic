/*
 * Created on 6/Nov/2003
 *
 */
package ServidorAplicacao.Servicos.coordinator;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

/**
 *fenix-head
 *ServidorAplicacao.Servicos.coordinator
 * @author João Mota
 *6/Nov/2003
 *
 */
public class AddCoordinatorTestCase extends CoordinatorIsResponsibleByExecutionDegree {

	protected void setUp() {
		super.setUp();
	}

	/**
	 * @param name
	 */
	public AddCoordinatorTestCase(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
	 */
	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
	 */
	protected String[] getAuthenticatedAndUnauthorizedUser() {
		String[] args = { "5592", "pass", getApplication()};
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
	 */
	protected String[] getNotAuthenticatedUser() {
		String[] args = { "jccm", "pass", getApplication()};
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "AddCoordinator";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getAuthorizeArguments() {
		Object[] args = { new Integer(10),new Integer(3)};
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.coordinator.CoordinatorBelongsToExecutionDegree#getNonAuthorizeArguments()
	 */
	protected Object[] getNonAuthorizeArguments() {
		Object[] args = { new Integer(12),new Integer(3)};
		return args;
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/coordinator/testReadCoordinatorsDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/coordinator/testAddCoordinatorDataSet.xml";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testAddCoordinator() {
		Object serviceArguments[] = getAuthorizeArguments();
		try {
			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				"testReadCoordinators was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (NotAuthorizedException ex) {
			ex.printStackTrace();
			System.out.println(
				"testReadCoordinators was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(getNameOfServiceToBeTested() + ": fail testAuthorizedUser");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testReadCoordinators was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.coordinator.CoordinatorIsResponsibleByExecutionDegree#getAuthenticatedResponsibleCoordinatorUserOfExecutionDegree()
	 */
	protected String[] getAuthenticatedNotResponsibleCoordinatorUserOfExecutionDegree() {
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

}
