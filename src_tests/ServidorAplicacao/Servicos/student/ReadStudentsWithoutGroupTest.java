/*
 *
 * Created on 03/09/2003
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author asnr and scpo
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.Servicos.TestCaseReadServices;

public class ReadStudentsWithoutGroupTest extends TestCaseReadServices {

	public ReadStudentsWithoutGroupTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadStudentsWithoutGroupTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadStudentsWithoutGroup";
	}
	
	//WHEN ALL STUDENTS HAVE GROUPS
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] result = { new Integer(3), "user" };
		return result;

	}

	protected int getNumberOfItemsToRetrieve() {
		return 4;
	}

	protected Object getObjectToCompare() {

		return null;
	}

}
