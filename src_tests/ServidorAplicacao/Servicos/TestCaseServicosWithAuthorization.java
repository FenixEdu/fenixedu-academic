package ServidorAplicacao.Servicos;

/**
 * @author dcs-rjao at 21/Fev/2003
 */
abstract public class TestCaseServicosWithAuthorization extends TestCaseServicos {

	public TestCaseServicosWithAuthorization(String testName) {
		super(testName);
	}
	
	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// Unauthorized execution of services
//	public void testUnauthorizedExecutionOfService(String serviceName) {
//
//		Object serviceArguments[] = { null };
//
//		Object result = null;
//		try {
//			result = _gestor.executar(_userView2, serviceName, serviceArguments);
//			fail("testUnauthorizedExecutionOfService - Service Name: " + serviceName);
//		} catch (Exception ex) {
//			assertNull("testUnauthorizedExecutionOfService", result);
//		}
//	}

	public void testUnauthorizedExecutionOfService() {

		Object serviceArguments[] = { null };

		Object result = null;
		try {
			result = _gestor.executar(_userView2, getNameOfServiceToBeTested(), serviceArguments);
			fail(this.getClass().getName() + " : testUnauthorizedExecutionOfService - Service Name: " + getNameOfServiceToBeTested());
		} catch (Exception ex) {
			assertNull(this.getClass().getName() + " : testUnauthorizedExecutionOfService", result);
		}
		System.out.println("testUnauthorizedExecutionOfService runned by class: " + this.getClass().getName());
	}
	
	protected abstract String getNameOfServiceToBeTested();
}
