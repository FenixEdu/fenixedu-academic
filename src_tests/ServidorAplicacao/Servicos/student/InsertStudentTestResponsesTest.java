/*
 * Created on 9/Set/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Susana Fernandes
 */
public class InsertStudentTestResponsesTest
	extends TestCaseDeleteAndEditServices {

	public InsertStudentTestResponsesTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertStudentTestResponses";
	}

	protected boolean needsAuthorization() {
		return true;
	}
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		String[] options = { new String("1")};
		Object[] args = { new String("13"), new Integer(25), options };
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		String[] options = { new String("1")};
		Object[] args = { new String("15"), new Integer(25), options };
		return args;
	}

}
