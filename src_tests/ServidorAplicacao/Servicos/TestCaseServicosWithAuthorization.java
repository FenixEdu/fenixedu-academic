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
	public void testUnauthorizedExecutionOfService(String serviceName) {

		Object serviceArguments[] = { null };

		Object result = null;
		try {
			result = _gestor.executar(_userView2, getServiceName(), serviceArguments);
			fail("testUnauthorizedExecutionOfService - Service Name: " + serviceName);
		} catch (Exception ex) {
			assertNull("testUnauthorizedExecutionOfService", result);
		}
	}

	public void testUnauthorizedExecutionOfService() {

		Object serviceArguments[] = { null };

		Object result = null;
		try {
			result = _gestor.executar(_userView2, getServiceName(), serviceArguments);
			fail(this.getClass().getName() + " :testUnauthorizedExecutionOfService - Service Name: " + getServiceName());
		} catch (Exception ex) {
			assertNull(this.getClass().getName() + " :testUnauthorizedExecutionOfService", result);
		}
	}
	
	public String getServiceName(){
		return "";
	}
}
