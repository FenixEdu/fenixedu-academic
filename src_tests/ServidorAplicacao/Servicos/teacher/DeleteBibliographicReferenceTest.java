package ServidorAplicacao.Servicos.teacher;

import java.util.HashMap;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteBibliographicReferenceTest extends TestCaseDeleteAndEditServices {

	public DeleteBibliographicReferenceTest(java.lang.String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "DeleteBibliographicReference";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		Object[] args = { new Integer(24), new Integer(1)};
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}