package ServidorAplicacao.Servicos;

/**
 * @author dcs-rjao
 *
 * Created on 24/Fev/2003
 */
public abstract class TestCaseCreateServices extends TestCaseNeedAuthorizationServices {

	public TestCaseCreateServices(String testName) {
		super(testName);
	}
	
	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

//	create existing object
	public void testUnsuccessfulExecutionOfCreateService() {

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), getArgumentsOfServiceToBeTestedUnsuccessfuly());
			System.out.println("testUnsuccessfulExecutionOfCreateService was UNSUCCESSFULY runned by class: " + this.getClass().getName());
			fail("testUnsuccessfulExecutionOfCreateService");
		} catch (Exception ex) {
			assertNull("testUnsuccessfulExecutionOfCreateService", result);
			System.out.println("testUnsuccessfulExecutionOfCreateService was SUCCESSFULY runned by class: " + this.getClass().getName());
		}
	}
	
//	create non-existing object
	public void testSuccessfulExecutionOfCreateService() {

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), getArgumentsOfServiceToBeTestedSuccessfuly());
			assertEquals("testSuccessfulExecutionOfCreateService", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
			System.out.println("testSuccessfulExecutionOfCreateService was SUCCESSFULY runned by class: " + this.getClass().getName());
		} catch (Exception ex) {
			System.out.println("testSuccessfulExecutionOfCreateService was UNSUCCESSFULY runned by class: " + this.getClass().getName());
			fail("testSuccessfulExecutionOfCreateService");
		}
	}

	protected abstract String getNameOfServiceToBeTested();
	protected abstract Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly();
	protected abstract Object[] getArgumentsOfServiceToBeTestedSuccessfuly();

	protected boolean needsAuthorization() {
		return true;
	}

}
