/*
 * Created on 25/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoDegree;
import ServidorAplicacao.Servicos.TestCaseReadServices;

/**
 * @author lmac1
 */

public class ReadDegreeServiceTest extends TestCaseReadServices {
	    
	/**
	 * @param testName
	 */
	 public ReadDegreeServiceTest(String testName) {
		super(testName);
	 }

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	 protected String getNameOfServiceToBeTested() {
		return "ReadDegreeService";
	 }
       
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		Object[] args = { new Integer(5) };
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(9) };
		return args;
	}

   /* (non-Javadoc)
	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	*/
	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}
        
   /* (non-Javadoc)
	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	*/	 
	protected Object getObjectToCompare() {			
		return new InfoDegree("MEEC", "Engenharia Electrotecnica e de Computadores");
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