package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteSectionTest extends TestCaseDeleteAndEditServices {

	public DeleteSectionTest(String testName) {
		super(testName);		
	}
	
	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "DeleteSection";
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] argsDeleteSection = {new Integer(27), new Integer(6)};
		
		return argsDeleteSection;		
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}