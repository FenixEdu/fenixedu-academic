/*
 * ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriodServiceTest.java
 *
 * Created on 2003/04/05
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExam;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import Util.Season;

public class ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriodServiceTest
	extends TestCaseReadServices {

	public ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriodServiceTest(
		java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(
				ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriodServiceTest
					.class);

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
		return "ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		String executionCourseInitials = "RCI";
		Season season = new Season(Season.SEASON1);
		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod("2º Semestre", infoExecutionYear);

		Object[] result =
			{ executionCourseInitials, season, infoExecutionPeriod };
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
		InfoViewExamByDayAndShift infoViewExamByDayAndShift =
			new InfoViewExamByDayAndShift();

		InfoExam infoExam = new InfoExam();
		infoExam.setSeason(new Season(Season.SEASON1));

		infoViewExamByDayAndShift.setInfoExam(infoExam);


		return infoViewExamByDayAndShift;
	}

	protected boolean needsAuthorization() {
		return true;
	}

}
