/*
 * Created on 11/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Susana Fernandes
 */
public class EditTestQuestionTest extends TestCaseDeleteAndEditServices {

	public EditTestQuestionTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "EditTestQuestion";
	}

	protected boolean needsAuthorization() {
		return true;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(3), new Integer(6), new Integer(3), new Integer(4)};
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
