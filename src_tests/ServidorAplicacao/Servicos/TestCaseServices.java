/*
 * TestCaseServices.java
 *
 * Created on 2003/04/05
 */

package ServidorAplicacao.Servicos;

/**
  * @author Luis Cruz & Sara Ribeiro
  * 
 **/
public abstract class TestCaseServices
	extends TestCaseNeedAuthorizationServices {

	private Object result = null;

	public TestCaseServices(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testSuccessfulExecutionOfReadService() {

		Object[] args = getArgumentsForCallToService();

		if (args != null) {
			try {
				result =
					_gestor.executar(
						_userView,
						getNameOfServiceToBeTested(),
						args);

				if (!verifyServiceResult(result)) {
					fail("Call to servive failed.");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Unexpexted exception running: " + this.getClass().getName());
			}
		}
	}

	/**
	 * This method must return a String with the name of the service to be tested.
	 */
	protected abstract String getNameOfServiceToBeTested();

	/**
	 * This method is used to verify result of call to service.
	 */
	protected abstract boolean verifyServiceResult(Object result);

	/**
	 * This method must return the service arguments that makes it execute correctly.
	 * This method must return null if not to be used.
	 */
	protected abstract Object[] getArgumentsForCallToService();

	/**
	 * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
	 */
	protected boolean needsAuthorization() {
		return false;
	}

}
