package ServidorAplicacao.Servicos.teacher;

import java.util.HashMap;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteAnnouncementServiceTest extends TestCaseDeleteAndEditServices {

	public DeleteAnnouncementServiceTest(java.lang.String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "DeleteAnnouncementService";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		Object[] args = { new Integer(27), new Integer(4)};
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}