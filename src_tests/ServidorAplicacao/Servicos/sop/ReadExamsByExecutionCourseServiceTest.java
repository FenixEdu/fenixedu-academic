/*
 * ReadExamsByDayAndBeginningServiceTest.java
 * JUnit based test
 *
 * Created on 2003/03/19
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servicos.TestCaseReadServices;

public class ReadExamsByExecutionCourseServiceTest
	extends TestCaseReadServices {
	public ReadExamsByExecutionCourseServiceTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ReadExamsByExecutionCourseServiceTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadExamsByExecutionCourse";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"Unexisting Course",
				"UC",
				"blob",
				new Double(1),
				new Double(0),
				new Double(0),
				new Double(0),
				new InfoExecutionPeriod("2º semestre",new InfoExecutionYear("2002/2003")));


		Object[] result = { infoExecutionCourse };
		return result;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
			"Redes de Computadores I",
			"RCI",
			"blob",
			new Double(0),
			new Double(0),
			new Double(0),
			new Double(0),
			new InfoExecutionPeriod("2º semestre",new InfoExecutionYear("2002/2003")));

		Object[] result = { infoExecutionCourse };
		return result;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	 */
	protected int getNumberOfItemsToRetrieve() {
		return 2;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
	protected Object getObjectToCompare() {
		return null;
	}

	protected boolean needsAuthorization() {
		return true;
	}

}
