package ServidorAplicacao.Servicos.teacher;

import java.util.HashMap;

import DataBeans.InfoItem;
import ServidorAplicacao.Servicos.TestCaseCreateServices;

/**
 * @author Fernanda Quitério
 * 
 */
public class InsertItemTest extends TestCaseCreateServices {

	public InsertItemTest(java.lang.String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertItem";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		Integer infoExecutionCourseCode = new Integer(24);
		Integer sectionCode = new Integer(7);
		InfoItem infoItem = new InfoItem("information", "itemName", new Integer(1), null, new Boolean(false));
		Object argsInsertItem[] = {infoExecutionCourseCode, sectionCode, infoItem };
		
			return argsInsertItem;
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