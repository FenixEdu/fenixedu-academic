package ServidorAplicacao.Servicos;

import java.util.Collection;

import framework.factory.ServiceManagerServiceFactory;

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

		Object[] args = getArgumentsOfServiceToBeTestedUnsuccessfuly();

		if(args!=null){

			Object result = null;
			try {
				result = ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args);
				if(result instanceof Collection) {
					assertTrue("testUnsuccessfulExecutionOfReadService", ((Collection) result).isEmpty());
					System.out.println("testUnsuccessfulExecutionOfReadService was SUCCESSFULY runned by class: " + this.getClass().getName());
				} else {
					assertNull("testUnsuccessfulExecutionOfReadService", result);
					System.out.println("testUnsuccessfulExecutionOfReadService was SUCCESSFULY runned by class: " + this.getClass().getName());
				}
				
			} 
			
			catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("testUnsuccessfulExecutionOfReadService was UNSUCCESSFULY runned by class: " + this.getClass().getName());
				fail("testUnsuccessfulExecutionOfReadService");
			}
	}
	}	
//	read existing object
	public void testSuccessfulExecutionOfReadService() {

		Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();

		if(args == null) {
			args = new Object[]{};
		}
			Object result = null;
			try {
				
				result = ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args);
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
	 * This method must return the number of items the service should return if it executes correctly.
	 * This method must return 0 if not to be used.
	 */
	protected abstract int getNumberOfItemsToRetrieve();

	/**
	 * This method must return an object that equals the object the service should return if it executes correctly.
	 * This method must return null if not to be used.
	 */
	protected abstract Object getObjectToCompare();

	/**
	 * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
	 */
	protected boolean needsAuthorization() {
		return false;
	}

}
