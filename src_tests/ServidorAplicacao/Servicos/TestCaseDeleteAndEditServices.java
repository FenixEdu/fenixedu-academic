package ServidorAplicacao.Servicos;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author dcs-rjao
 *
 * Created on 24/Fev/2003
 */

public abstract class TestCaseDeleteAndEditServices
	extends TestCaseNeedAuthorizationServices {

	public TestCaseDeleteAndEditServices(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	//	delete non-existing object
	public void testUnsuccessfulExecutionOfDeleteService() {

		Object[] args = getArgumentsOfServiceToBeTestedUnsuccessfuly();

		if (args != null) {
			Object result = null;
			try {
				result =
					ServiceManagerServiceFactory.executeService(
						_userView,
						getNameOfServiceToBeTested(),
						args);
				assertEquals(
					"testUnsuccessfulExecutionOfDeleteService",
					Boolean.FALSE.booleanValue(),
					((Boolean) result).booleanValue());
				System.out.println(
					"testUnsuccessfulExecutionOfDeleteService was SUCCESSFULY runned by class: "
						+ this.getClass().getName());
			} catch (FenixServiceException e) {
				System.out.println(
					"testUnsuccessfulExecutionOfReadService was SUCCESSFULY runned by class: "
						+ this.getClass().getName());
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(
					"testUnsuccessfulExecutionOfDeleteService was UNSUCCESSFULY runned by class: "
						+ this.getClass().getName());
				fail("testUnsuccessfulExecutionOfDeleteService");
			}
		}
	}

	//	delete existing object
	public void testSuccessfulExecutionOfDeleteService() {

		Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();

		if (args != null) {
			Object result = null;
			try {
				result =
					ServiceManagerServiceFactory.executeService(
						_userView,
						getNameOfServiceToBeTested(),
						args);
				assertEquals(
					"testSuccessfulExecutionOfDeleteService",
					Boolean.TRUE.booleanValue(),
					((Boolean) result).booleanValue());
				System.out.println(
					"testSuccessfulExecutionOfDeleteService was SUCCESSFULY runned by class: "
						+ this.getClass().getName());
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(
					"testSuccessfulExecutionOfDeleteService was UNSUCCESSFULY runned by class: "
						+ this.getClass().getName());
				fail("testSuccessfulExecutionOfDeleteService");
			}
		}
	}

	/**
	 * This method must return a String with the name of the service to be tested.
	 */
	protected abstract String getNameOfServiceToBeTested();

	/**
	 * This method must return the service arguments that makes it fail it's execution.
	 * This method must return null if not to be used.
	 */
	protected abstract Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly();

	/**
	 * This method must return the service arguments that makes it execute correctly.
	 * This method must return null if not to be used.
	 */
	protected abstract Object[] getArgumentsOfServiceToBeTestedSuccessfuly();

	/**
	 * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
	 */
	protected boolean needsAuthorization() {
		return true;
	}

}
