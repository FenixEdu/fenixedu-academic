/*
 * Created on 24/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos;

/**
 * @author dcs-rjao
 *
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
			System.out.println("testUnsuccessfulExecutionOfCreateService was unsuccessfuly runned by class: " + this.getClass().getName());
			fail("testUnsuccessfulExecutionOfCreateService");
		} catch (Exception ex) {
			assertNull("testUnsuccessfulExecutionOfCreateService", result);
			System.out.println("testUnsuccessfulExecutionOfCreateService was successfuly runned by class: " + this.getClass().getName());
		}
	}
	
//	create non-existing object
	public void testSuccessfulExecutionOfCreateService() {

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), getArgumentsOfServiceToBeTestedSuccessfuly());
			assertEquals("testSuccessfulExecutionOfCreateService", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
			System.out.println("testSuccessfulExecutionOfCreateService was successfuly runned by class: " + this.getClass().getName());
		} catch (Exception ex) {
			System.out.println("testSuccessfulExecutionOfCreateService was unsuccessfuly runned by class: " + this.getClass().getName());
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
