package ServidorAplicacao.Servicos;

/**
 * @author dcs-rjao at 21/Fev/2003
 */
abstract public class TestCaseNeedAuthorizationServices extends TestCaseServicos {

	public TestCaseNeedAuthorizationServices(String testName) {
		super(testName);
	}
	
	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testUnauthorizedExecutionOfService() {

		Object serviceArguments[] = { null };

		Object result = null;

		if(needsAuthorization()) {
			try {
				result = _gestor.executar(_userView2, getNameOfServiceToBeTested(), serviceArguments);
				System.out.println("testUnauthorizedExecutionOfService was unsuccessfuly runned by class: " + this.getClass().getName());
				fail(this.getClass().getName() + " : testUnauthorizedExecutionOfService - Service Name: " + getNameOfServiceToBeTested());
			} catch (Exception ex) {
				System.out.println("testUnauthorizedExecutionOfService was successfuly runned by class: " + this.getClass().getName());
				assertNull(this.getClass().getName() + " : testUnauthorizedExecutionOfService", result);
			}
		}
	}
	
	protected abstract String getNameOfServiceToBeTested();
	protected abstract boolean needsAuthorization();
}
