/*
 * Created on 1/Set/2003
 */
package ServidorAplicacao.Servicos.manager;


/**
 * @author lmac1
 */

public class ReadExecutionDegreesByDegreeCurricularPlanServiceTest extends TestCaseManagerReadServices {
	    
	/**
	 * @param testName
	 */
	 public ReadExecutionDegreesByDegreeCurricularPlanServiceTest(String testName) {
		super(testName);
	 }

	 protected String getNameOfServiceToBeTested() {
		return "ReadExecutionDegreesByDegreeCurricularPlan";
	 }
		
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(1) };
		return args;
	}
		
	protected int getNumberOfItemsToRetrieve() {
		return 1;
	}
}