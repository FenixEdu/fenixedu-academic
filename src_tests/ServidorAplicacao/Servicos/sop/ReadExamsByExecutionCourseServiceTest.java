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
import java.util.Calendar;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExam;
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

		return 1;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
	protected Object getObjectToCompare() {
		Calendar beginning = Calendar.getInstance();
		beginning.set(Calendar.YEAR, 2003);
		beginning.set(Calendar.MONTH, Calendar.MARCH);
		beginning.set(Calendar.DAY_OF_MONTH, 19);
		beginning.set(Calendar.HOUR_OF_DAY, 13);
		beginning.set(Calendar.MINUTE, 0);
		beginning.set(Calendar.SECOND, 0);
		Date day = beginning.getTime();
		Calendar end = Calendar.getInstance();
		end.set(Calendar.YEAR, 2003);
		end.set(Calendar.MONTH, Calendar.MARCH);
		end.set(Calendar.DAY_OF_MONTH, 19);
		end.set(Calendar.HOUR_OF_DAY, 15);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
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
		InfoExam infoExam =
			new InfoExam(day, beginning, end, infoExecutionCourse);

		return infoExam;
	}

	protected boolean needsAuthorization() {
		return true;
	}

}
