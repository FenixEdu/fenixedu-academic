package ServidorAplicacao.Servicos;

import java.util.Collection;

/**
 * @author dcs-rjao
 *
 * Created on 24/Fev/2003
 */
public abstract class TestCaseReadServices extends TestCaseNeedAuthorizationServices {

	public TestCaseReadServices(String testName) {
		super(testName);
	}
	
	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

//	read non-existing object
	public void testUnsuccessfulExecutionOfReadService() {

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), getArgumentsOfServiceToBeTestedUnsuccessfuly());
			if(result instanceof Collection) {
				assertTrue("testUnsuccessfulExecutionOfReadService", ((Collection) result).isEmpty());
				System.out.println("testUnsuccessfulExecutionOfReadService was SUCCESSFULY runned by class: " + this.getClass().getName());
			} else {
				assertNull("testUnsuccessfulExecutionOfReadService", result);
				System.out.println("testUnsuccessfulExecutionOfReadService was SUCCESSFULY runned by class: " + this.getClass().getName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("testUnsuccessfulExecutionOfReadService was UNSUCCESSFULY runned by class: " + this.getClass().getName());
			fail("testUnsuccessfulExecutionOfReadService");
		}
	}
	
//	read existing object
	public void testSuccessfulExecutionOfReadService() {

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), getArgumentsOfServiceToBeTestedSuccessfuly());
			if(result instanceof Collection) {
				assertEquals("testSuccessfulExecutionOfReadService", getNumberOfItemsToRetrieve(), ((Collection) result).size());
				System.out.println("testSuccessfulExecutionOfReadService was SUCCESSFULY runned by class: " + this.getClass().getName());
			} else {
				assertEquals("testSuccessfulExecutionOfReadService", getObjectToCompare(), result);
				System.out.println("testSuccessfulExecutionOfReadService was SUCCESSFULY runned by class: " + this.getClass().getName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("testSuccessfulExecutionOfReadService was UNSUCCESSFULY runned by class: " + this.getClass().getName());
			fail("testSuccessfulExecutionOfReadService");
		}
	}

	protected abstract String getNameOfServiceToBeTested();
	protected abstract Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly();
	protected abstract Object[] getArgumentsOfServiceToBeTestedSuccessfuly();
	protected abstract int getNumberOfItemsToRetrieve();
	protected abstract Object getObjectToCompare();

	protected boolean needsAuthorization() {
		return false;
	}

}
