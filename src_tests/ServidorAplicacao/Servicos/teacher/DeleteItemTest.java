package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteItemTest extends TestCaseDeleteAndEditServices {

	public DeleteItemTest(String testName) {
		super(testName);		
	}
	
	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "DeleteItem";
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] argsDeleteItem = {new Integer(24), new Integer(2)};
		
		return argsDeleteItem;		
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
