/*
 * Created on 11/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Susana Fernandes
 */
public class InsertTestQuestionTest extends TestCaseDeleteAndEditServices {

	public InsertTestQuestionTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "InsertTestQuestion";
	}
	
	protected boolean needsAuthorization() {
		return true;
	}
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(3), new Integer(1), new Integer(4), new Integer(5)};
		return args;
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

}
