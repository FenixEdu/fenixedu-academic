package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 */
public class DeleteSummaryTest extends TestCaseDeleteAndEditServices {

	public DeleteSummaryTest(String testName) {
		super(testName);		
	}
	
	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "DeleteSummary";
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] argsDeleteItem = {new Integer(24),new Integer(261)};
		
		return argsDeleteItem;		
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
