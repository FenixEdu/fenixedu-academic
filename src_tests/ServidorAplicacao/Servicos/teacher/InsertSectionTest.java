package ServidorAplicacao.Servicos.teacher;

import java.util.HashMap;

import ServidorAplicacao.Servicos.TestCaseCreateServices;

/**
 * @author Fernanda Quitério
 * 
 */
public class InsertSectionTest extends TestCaseCreateServices {

	public InsertSectionTest(java.lang.String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertSection";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(24), new Integer(7), new String("sectionName"), new Integer(1)};
//		Object[] args = { new Integer(26), null, new String("sectionName"), new Integer(1)};
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;

	}

	/**
	 * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
	 */
	protected boolean needsAuthorization() {
		return true;
	}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}