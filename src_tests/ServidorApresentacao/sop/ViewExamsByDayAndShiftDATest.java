/*
 * CreateExamDATest.java
 *
 * Created on 2003/04/06
 */

package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoRole;
import DataBeans.InfoViewExam;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.RoleType;
import Util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsByDayAndShiftDATest extends TestCasePresentation {

	public ViewExamsByDayAndShiftDATest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ViewExamsByDayAndShiftDATest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("sop", "/viewExamsDayAndShiftForm");
	}

	public void testView() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.EXAM_DATEANDTIME,
			getDateAndTime());

		// indicate method to be called
		addRequestParameter("method", "view");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("View Exams");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		Integer availableRoomOccupation =
			(Integer) session.getAttribute(
				SessionConstants.AVAILABLE_ROOM_OCCUPATION);
		assertNotNull(
			"AVAILABLE_ROOM_OCCUPATION not found in session",
			availableRoomOccupation);
		assertEquals(
			"AVAILABLE_ROOM_OCCUPATION contains unexpected value",
			150,
			availableRoomOccupation.intValue());

		List infoExams =
			(List) session.getAttribute(SessionConstants.LIST_EXAMSANDINFO);
		assertNotNull("LIST_EXAMSANDINFO not found in session", infoExams);
		assertEquals(
			"LIST_EXAMSANDINFO contains unexpected number of elements",
			2,
			infoExams.size());
	}

	public void testDelete() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.EXAM_DATEANDTIME,
			getDateAndTime());
		session.setAttribute(
			SessionConstants.LIST_EXAMSANDINFO,
			getExamsAndInfoList());

		// indicate method to be called
		addRequestParameter("method", "delete");
		addRequestParameter("indexExam", "0");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("Deleted Exam");
	}

	public void testEdit() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(
			SessionConstants.LIST_EXAMSANDINFO,
			getExamsAndInfoList());

		// indicate method to be called
		addRequestParameter("method", "edit");
		addRequestParameter("indexExam", "0");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("Edit Exam");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		ArrayList hoursExpected = Util.getExamShifts();
		ArrayList hoursFromSession =
			(ArrayList) session.getAttribute(SessionConstants.LABLELIST_HOURS);
		assertNotNull("LABLELIST_HOURS not found in session", hoursFromSession);
		assertEquals(
			"LABLELIST_HOURS does not contain expected value",
			hoursExpected,
			hoursFromSession);

		ArrayList daysOfMonthExpected = Util.getDaysOfMonth();
		ArrayList daysOfMonthFromSession =
			(ArrayList) session.getAttribute(
				SessionConstants.LABLELIST_DAYSOFMONTH);
		assertNotNull(
			"LABLELIST_DAYSOFMONTH not found in session",
			daysOfMonthFromSession);
		assertEquals(
			"LABLELIST_DAYSOFMONTH does not contain expected value",
			daysOfMonthExpected,
			daysOfMonthFromSession);

		ArrayList monthsOfYearFromSession =
			(ArrayList) session.getAttribute(
				SessionConstants.LABLELIST_MONTHSOFYEAR);
		assertNotNull(
			"LABLELIST_MONTHSOFYEAR not found in session",
			monthsOfYearFromSession);
		assertEquals(
			"LABLELIST_MONTHSOFYEAR does not contain expected value",
			12,
			monthsOfYearFromSession.size());

		ArrayList examSeasonsFromSession =
			(ArrayList) session.getAttribute(
				SessionConstants.LABLELIST_SEASONS);
		assertNotNull(
			"LABLELIST_SEASONS not found in session",
			examSeasonsFromSession);
		assertEquals(
			"LABLELIST_SEASONS does not contain expected value",
			2,
			examSeasonsFromSession.size());

		DynaValidatorForm editExamForm =
			(DynaValidatorForm) session.getAttribute("examForm");
		assertNotNull("examForm", editExamForm);
		assertEquals(
			"examForm contains unexpected value for day",
			"26",
			editExamForm.get("day"));
		assertEquals(
			"examForm contains unexpected value for month",
			"5",
			editExamForm.get("month"));
		assertEquals(
			"examForm contains unexpected value for year",
			"2003",
			editExamForm.get("year"));
		assertEquals(
			"examForm contains unexpected value for beginning",
			"13",
			editExamForm.get("beginning"));

		String input = (String) session.getAttribute("input");
		assertNotNull("input not found in session", input);
		assertEquals(
			"input does not contain expected value",
			"viewExamsByDayAndShift",
			input);

		InfoViewExamByDayAndShift infoViewExamByDayAndShift =
			(InfoViewExamByDayAndShift) session.getAttribute(
				SessionConstants.INFO_EXAMS_KEY);
		assertNotNull(
			"INFO_EXAMS_KEY not found in session",
			infoViewExamByDayAndShift);
		assertNotNull(
			"INFO_EXAMS_KEY does not contain an exam",
			infoViewExamByDayAndShift.getInfoExam());
		assertNotNull(
			"INFO_EXAMS_KEY does not contain a list of execution courses",
			infoViewExamByDayAndShift.getInfoExecutionCourses());
		assertNotNull(
			"INFO_EXAMS_KEY does not contain a list of degrees",
			infoViewExamByDayAndShift.getInfoDegrees());
		assertEquals(
			"INFO_EXAMS_KEY does not contain exam with expected season",
			new Season(Season.SEASON1),
			infoViewExamByDayAndShift.getInfoExam().getSeason());
		assertEquals(
			"INFO_EXAMS_KEY contains a list of execution courses of unexpected size",
			2,
			infoViewExamByDayAndShift.getInfoExecutionCourses().size());
		assertEquals(
			"INFO_EXAMS_KEY contains a list of degrees of unexpected size",
			2,
			infoViewExamByDayAndShift.getInfoDegrees().size());
	}

	public void testAddExecutionCourse() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(
			SessionConstants.LIST_EXAMSANDINFO,
			getExamsAndInfoList());

		// indicate method to be called
		addRequestParameter("method", "addExecutionCourse");
		addRequestParameter("indexExam", "0");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("Add Execution Course");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		InfoViewExamByDayAndShift infoViewExamsFromSession =
			(InfoViewExamByDayAndShift) session.getAttribute(
				SessionConstants.INFO_VIEW_EXAM);
		assertNotNull(
			"INFO_VIEW_EXAM was not found in session",
			infoViewExamsFromSession);
		assertEquals(
			"INFO_VIEW_EXAM not of expected season",
			new Season(Season.SEASON1),
			infoViewExamsFromSession.getInfoExam().getSeason());
		assertNotNull(
			"INFO_VIEW_EXAM contains empty execution course list",
			infoViewExamsFromSession.getInfoExecutionCourses());
		assertEquals(
			"INFO_VIEW_EXAM contains exam of unexpected season",
			2,
			infoViewExamsFromSession.getInfoExecutionCourses().size());

		String input = (String) session.getAttribute("input");
		assertNotNull("input not found in session", "viewExamsByDayAndShift");
		assertEquals(
			"input does not contain expected value",
			"viewExamsByDayAndShift",
			input);
	}

	private UserView getPrivledgedUserView() {
		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TIME_TABLE_MANAGER);
		roles.add(infoRole);

		UserView userView = new UserView("user", null);
		userView.setRoles(roles);

		return userView;
	}

	private Calendar getDateAndTime() {
		Calendar dateAndTime = Calendar.getInstance();

		dateAndTime.set(Calendar.DAY_OF_MONTH, 26);
		dateAndTime.set(Calendar.MONTH, Calendar.JUNE);
		dateAndTime.set(Calendar.YEAR, 2003);
		dateAndTime.set(Calendar.HOUR_OF_DAY, 13);
		dateAndTime.set(Calendar.MINUTE, 0);
		dateAndTime.set(Calendar.SECOND, 0);
		dateAndTime.set(Calendar.MILLISECOND, 0);

		return dateAndTime;
	}

	private List getExamsAndInfoList() {
		List examsAndInfoList = null;

		Object args[] = { getDateAndTime(), getDateAndTime()};
		try {
			InfoViewExam infoViewExams =
				(InfoViewExam) ServiceUtils.executeService(
					getPrivledgedUserView(),
					"ReadExamsByDayAndBeginning",
					args);
			examsAndInfoList = infoViewExams.getInfoViewExamsByDayAndShift();
		} catch (FenixServiceException e) {
			fail("failed obtaining exams and info list");
		}

		return examsAndInfoList;
	}

}
