/*
 * ChooseExamDayAndShiftDispatchActionTest.java
 *
 * Created on 2003/04/06
 */

package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.CollectionUtils;

import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.Util;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamDayAndShiftDispatchActionTest
	extends TestCasePresentation {

	public ChooseExamDayAndShiftDispatchActionTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ChooseExamDayAndShiftDispatchActionTest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("sop", "/chooseDayAndShiftForm");
	}

	public void testPrepare() {
		// indicate method to be called
		addRequestParameter("method", "prepare");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("Show Choose Form");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		HttpSession session = getSession();

		ArrayList hoursExpected = Util.getExamShifts();
		ArrayList hoursFromSession =
			(ArrayList) session.getAttribute(SessionConstants.LABLELIST_HOURS);
		assertNotNull("LABLELIST_HOURS not found in session", hoursFromSession);
		assertTrue(
			"LABLELIST_HOURS does not contain expected object",
			CollectionUtils.isEqualCollection(hoursExpected, hoursFromSession));

		ArrayList daysOfMonthExpected = Util.getDaysOfMonth();
		ArrayList daysOfMonthFromSession =
			(ArrayList) session.getAttribute(
				SessionConstants.LABLELIST_DAYSOFMONTH);
		assertNotNull(
			"LABLELIST_DAYSOFMONTH not found in session",
			daysOfMonthFromSession);
		assertTrue(
			"LABLELIST_DAYSOFMONTH does not contain expected object",
			CollectionUtils.isEqualCollection(
				daysOfMonthExpected,
				daysOfMonthFromSession));

		ArrayList monthsOfYearExpected = Util.getMonthsOfYear();
		ArrayList monthsOfYearFromSession =
			(ArrayList) session.getAttribute(
				SessionConstants.LABLELIST_MONTHSOFYEAR);
		assertNotNull(
			"LABLELIST_MONTHSOFYEAR not found in session",
			monthsOfYearFromSession);
		assertEquals(
			"LABLELIST_MONTHSOFYEAR list size was unexpected",
			monthsOfYearExpected.size(),
			monthsOfYearFromSession.size());

		ArrayList yearsExpected = Util.getYears();
		ArrayList yearsFromSession =
			(ArrayList) session.getAttribute(SessionConstants.LABLELIST_YEARS);
		assertNotNull("LABLELIST_YEARS not found in session", yearsFromSession);
		assertTrue(
			"LABLELIST_YEARS does not contain expected object",
			CollectionUtils.isEqualCollection(yearsExpected, yearsFromSession));
	}

	public void testChoose() {
		// indicate method to be called
		addRequestParameter("method", "choose");
		addRequestParameter(SessionConstants.NEXT_PAGE, "viewExams");

		// Fill out form
		addRequestParameter("day", "26");
		addRequestParameter("month", "5");
		addRequestParameter("year", "2003");
		addRequestParameter("beginning", "13");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("viewExams");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		HttpSession session = getSession();

		Calendar examDateAndTimeExpected =
			Calendar.getInstance(
				TimeZone.getTimeZone("GMT"),
				new Locale("pt", "PT"));
		examDateAndTimeExpected.set(Calendar.YEAR, 2003);
		examDateAndTimeExpected.set(Calendar.MONTH, 5);
		examDateAndTimeExpected.set(Calendar.DAY_OF_MONTH, 26);
		examDateAndTimeExpected.set(Calendar.HOUR_OF_DAY, 13);
		examDateAndTimeExpected.set(Calendar.MINUTE, 0);
		examDateAndTimeExpected.set(Calendar.SECOND, 0);
		examDateAndTimeExpected.set(Calendar.MILLISECOND, 0);

		Calendar examDateAndTimeFromSession =
			(Calendar) session.getAttribute(SessionConstants.EXAM_DATEANDTIME);
		assertEquals(
			"EXAM_DATEANDTIME from session was unexpected",
			examDateAndTimeExpected,
			examDateAndTimeFromSession);

		String examDateAndTimeStringExpected = "2003/6/26  às  13 horas";
		String examDateAndTimeStringFromSession =
			(String) session.getAttribute(
				SessionConstants.EXAM_DATEANDTIME_STR);
		assertEquals(
			"EXAM_DATEANDTIME_STR from session was unexpected",
			examDateAndTimeStringExpected,
			examDateAndTimeStringFromSession);
	}

}
