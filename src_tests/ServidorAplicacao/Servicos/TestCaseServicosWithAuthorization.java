package ServidorAplicacao.Servicos;

/**
 * @author dcs-rjao at 21/Fev/2003
 */
public class TestCaseServicosWithAuthorization extends TestCaseServicos {

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
			result = _gestor.executar(_userView2, serviceName, serviceArguments);
			fail("testUnauthorizedExecutionOfService - Service Name: " + serviceName);
		} catch (Exception ex) {
			assertNull("testUnauthorizedExecutionOfService", result);
		}
	}
}
