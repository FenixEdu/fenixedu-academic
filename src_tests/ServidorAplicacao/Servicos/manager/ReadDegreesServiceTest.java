/*
 * Created on 23/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import ServidorAplicacao.Servicos.TestCaseReadServices;

/**
 * @author lmac1
 */

public class ReadDegreesServiceTest extends TestCaseReadServices {
	    
	/**
	 * @param testName
	 */
	 public ReadDegreesServiceTest(String testName) {
		super(testName);
	 }

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	 protected String getNameOfServiceToBeTested() {
		return "ReadDegreesService";
	 }
       
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		return null;
	}

   /* (non-Javadoc)
	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	*/
	protected int getNumberOfItemsToRetrieve() {
		return 5;
	}
        
	/* (non-Javadoc)
		 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
		 */
		 
	protected Object getObjectToCompare() {			
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServicos#getArgsForAuthorizedUser()
	 */
	protected String[] getArgsForAuthorizedUser() {
		return new String[] {"manager", "pass", getApplication()};
	}
	
	protected boolean needsAuthorization() {
			return true;
	}

}