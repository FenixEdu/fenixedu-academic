/*
 * Created on 6/Nov/2003
 *
 */
package ServidorAplicacao.Servicos.coordinator;

import java.util.ArrayList;
import java.util.List;

import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

/**
 *fenix-head
 *ServidorAplicacao.Servicos.coordinator
 * @author João Mota
 *6/Nov/2003
 *
 */
public class DeleteCoordinatorsTestCase extends CoordinatorIsResponsibleByExecutionDegree {

	protected void setUp() {
		super.setUp();
	}

	/**
	 * @param name
	 */
	public DeleteCoordinatorsTestCase(String name) {
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
		String[] args = { "3", "pass", getApplication()};
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
		return "RemoveCoordinators";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getAuthorizeArguments() {
		List arg = new ArrayList();
		arg.add(new Integer(3));
		Object[] args = { new Integer(10), arg };
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.coordinator.CoordinatorBelongsToExecutionDegree#getNonAuthorizeArguments()
	 */
	protected Object[] getNonAuthorizeArguments() {
		List arg = new ArrayList();
		arg.add(new Integer(3));
		Object[] args = { new Integer(12), arg };
		return args;
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/coordinator/testAddCoordinatorDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/coordinator/testReadCoordinatorsDataSet.xml";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testDeleteCoordinator() {
		Object serviceArguments[] = getAuthorizeArguments();
		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				"testDeleteCoordinator was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (NotAuthorizedException ex) {
			ex.printStackTrace();
			System.out.println(
				"testDeleteCoordinator was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(getNameOfServiceToBeTested() + ": fail testAuthorizedUser");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testDeleteCoordinator was UNSUCCESSFULY runned by service: "
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
