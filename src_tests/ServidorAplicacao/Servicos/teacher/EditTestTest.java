/*
 * Created on 11/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Susana Fernandes
 */
public class EditTestTest extends TestCaseDeleteAndEditServices {

	public EditTestTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "EditTest";
	}
	
	protected boolean needsAuthorization() {
		return true;
	}
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = {new Integer(3), new String("new title"), new String("new information")};
		return args;
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
