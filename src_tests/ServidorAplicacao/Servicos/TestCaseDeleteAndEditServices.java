package ServidorAplicacao.Servicos;

/**
 * @author dcs-rjao
 *
 * Created on 24/Fev/2003
 */

public abstract class TestCaseDeleteAndEditServices extends TestCaseNeedAuthorizationServices {

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

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), getArgumentsOfServiceToBeTestedUnsuccessfuly());
			assertEquals("testUnsuccessfulExecutionOfDeleteService", Boolean.FALSE.booleanValue(), ((Boolean) result).booleanValue());
			System.out.println("testUnsuccessfulExecutionOfDeleteService was SUCCESSFULY runned by class: " + this.getClass().getName());
		} catch (Exception ex) {
			System.out.println("testUnsuccessfulExecutionOfDeleteService was UNSUCCESSFULY runned by class: " + this.getClass().getName());
			fail("testUnsuccessfulExecutionOfDeleteService");
		}
	}
	
//	delete existing object
	public void testSuccessfulExecutionOfDeleteService() {

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), getArgumentsOfServiceToBeTestedSuccessfuly());
			assertEquals("testSuccessfulExecutionOfDeleteService", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
			System.out.println("testSuccessfulExecutionOfDeleteService was SUCCESSFULY runned by class: " + this.getClass().getName());
		} catch (Exception ex) {
			System.out.println("testSuccessfulExecutionOfDeleteService was UNSUCCESSFULY runned by class: " + this.getClass().getName());
			fail("testSuccessfulExecutionOfDeleteService");
		}
	}

	protected abstract String getNameOfServiceToBeTested();
	protected abstract Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly();
	protected abstract Object[] getArgumentsOfServiceToBeTestedSuccessfuly();

	protected boolean needsAuthorization() {
		return true;
	}

}
