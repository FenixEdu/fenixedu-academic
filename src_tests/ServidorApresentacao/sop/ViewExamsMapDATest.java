/*
 * CreateExamDATest.java
 *
 * Created on 2003/04/07
 */

package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRole;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.GestorServicos;
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
public class ViewExamsMapDATest extends TestCasePresentation {

	public ViewExamsMapDATest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ViewExamsMapDATest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("sop", "/viewExamsMap");
	}

	public void testView() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.CURRICULAR_YEARS_LIST,
			getCurricularYears());
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			getCurrentExecutionPeriod());
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			getInfoExecutionDegree());

		// indicate method to be called
		addRequestParameter("method", "view");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("viewExamsMap");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		InfoExamsMap infoExamsMap =
			(InfoExamsMap) session.getAttribute(
				SessionConstants.INFO_EXAMS_MAP);
		assertNotNull("INFO_EXAMS_MAP not found in session", infoExamsMap);
		assertTrue(
			"INFO_EXAMS_MAP contains unexpected curricular year list",
			CollectionUtils.isEqualCollection(
				infoExamsMap.getCurricularYears(),
				getCurricularYears()));
		assertNotNull(
			"INFO_EXAMS_MAP does not contain start of exam season",
			infoExamsMap.getStartSeason1());
		assertNotNull(
			"INFO_EXAMS_MAP does not contain end of exam season",
			infoExamsMap.getEndSeason2());
		assertNotNull(
			"INFO_EXAMS_MAP does not contain list of execution courses",
			infoExamsMap.getExecutionCourses());
		assertEquals(
			"INFO_EXAMS_MAP list of execution courses has unexpected size",
			3,
			infoExamsMap.getExecutionCourses().size());
	}

	public void testCreate() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.INFO_EXAMS_MAP,
			getInfoExamsMap());

		// indicate method to be called
		addRequestParameter("method", "create");
		addRequestParameter("indexExecutionCourse", "0");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("createExam");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		Integer curricularYear =
			(Integer) session.getAttribute(
				SessionConstants.CURRICULAR_YEAR_KEY);
		assertNotNull(
			"CURRICULAR_YEAR_KEY not found in session",
			curricularYear);
		assertEquals(
			"INFO_EXAMS_MAP contains unexpected curricular year list",
			new Integer(1),
			curricularYear);

		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) session.getAttribute(
				SessionConstants.EXECUTION_COURSE_KEY);
		assertNotNull(
			"EXECUTION_COURSE_KEY not found in session",
			infoExecutionCourse);
		assertEquals(
			"EXECUTION_COURSE_KEY does not contain expected execution course",
			"AC",
			infoExecutionCourse.getSigla());
	}

	public void testEdit() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			getCurrentExecutionPeriod());

		session.setAttribute(
			SessionConstants.INFO_EXAMS_MAP,
			getInfoExamsMap());

		// indicate method to be called
		addRequestParameter("method", "edit");
		addRequestParameter("executionCourseInitials", "APR");
		addRequestParameter("season", "1");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("editExam");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		ArrayList hoursExpected = Util.getExamShifts();
		ArrayList hoursFromSession =
			(ArrayList) session.getAttribute(SessionConstants.LABLELIST_HOURS);
		assertNotNull("LABLELIST_HOURS not in session", hoursFromSession);
		assertTrue(
			"LABLELIST_HOURS list content is unexpected",
			CollectionUtils.isEqualCollection(hoursExpected, hoursFromSession));

		ArrayList daysOfMonthExpected = Util.getDaysOfMonth();
		ArrayList daysOfMonthFromSession =
			(ArrayList) session.getAttribute(
				SessionConstants.LABLELIST_DAYSOFMONTH);
		assertNotNull(
			"LABLELIST_DAYSOFMONTH not in session",
			daysOfMonthFromSession);
		assertTrue(
			"LABLELIST_DAYSOFMONTH list content is unexpected",
			CollectionUtils.isEqualCollection(
				daysOfMonthExpected,
				daysOfMonthFromSession));

		ArrayList monthsOfYearFromSession =
			(ArrayList) session.getAttribute(
				SessionConstants.LABLELIST_MONTHSOFYEAR);
		assertNotNull(
			"LABLELIST_MONTHSOFYEAR not in session",
			monthsOfYearFromSession);
		assertEquals(
			"LABLELIST_MONTHSOFYEAR list size is unexpected",
			12,
			monthsOfYearFromSession.size());

		ArrayList examSeasonsFromSession =
			(ArrayList) session.getAttribute(
				SessionConstants.LABLELIST_SEASONS);
		assertNotNull(
			"LABLELIST_SEASONS not in session",
			examSeasonsFromSession);
		assertEquals(
			"LABLELIST_SEASONS list size is unexpected",
			2,
			examSeasonsFromSession.size());

		DynaValidatorForm editExamForm =
			(DynaValidatorForm) session.getAttribute("examForm");
		assertNotNull("examForm", editExamForm);
		assertEquals(
			"examForm contains unexpected value for day",
			"30",
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

		InfoViewExamByDayAndShift infoViewExamByDayAndShiftFromSession =
			(InfoViewExamByDayAndShift) session.getAttribute(
				SessionConstants.INFO_EXAMS_KEY);
		assertNotNull(
			"INFO_EXAMS_KEY not found in session",
			infoViewExamByDayAndShiftFromSession);
		assertNotNull(
			"INFO_EXAMS_KEY does not contain info exam",
			infoViewExamByDayAndShiftFromSession.getInfoExam());
		assertNotNull(
			"INFO_EXAMS_KEY does not contain info degree list",
			infoViewExamByDayAndShiftFromSession.getInfoDegrees());
		assertNotNull(
			"INFO_EXAMS_KEY does not contain info execution course list",
			infoViewExamByDayAndShiftFromSession.getInfoExecutionCourses());
		assertEquals(
			"INFO_EXAMS_KEY contains info exam for unexpected season",
			new Season(Season.SEASON1),
			infoViewExamByDayAndShiftFromSession.getInfoExam().getSeason());
		assertEquals(
			"INFO_EXAMS_KEY does not contain info degree list",
			1,
			infoViewExamByDayAndShiftFromSession.getInfoDegrees().size());
		assertEquals(
			"INFO_EXAMS_KEY does not contain info execution course list",
			1,
			infoViewExamByDayAndShiftFromSession.getInfoExecutionCourses().size());

		String input = (String) session.getAttribute("input");
		assertNotNull("input not found in session", input);
		assertEquals("input has unexpected value", "viewExamsMap", input);
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

	private List getCurricularYears() {
		List curricularYears = new ArrayList();
		curricularYears.add(new Integer(1));
		curricularYears.add(new Integer(2));
		curricularYears.add(new Integer(4));

		return curricularYears;
	}

	private InfoExecutionPeriod getCurrentExecutionPeriod() {
		InfoExecutionPeriod infoExecutionPeriod = null;
		try {
			infoExecutionPeriod =
				(InfoExecutionPeriod) ServiceUtils.executeService(
					getPrivledgedUserView(),
					"ReadCurrentExecutionPeriod",
					new Object[0]);
		} catch (FenixServiceException e) {
			fail("unexpected exception reading actual execution period: " + e);
		}

		return infoExecutionPeriod;
	}

	private InfoExecutionDegree getInfoExecutionDegree() {
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			new InfoDegreeCurricularPlan();
		infoDegreeCurricularPlan.setName("plano1");
		InfoDegree infoDegree = new InfoDegree();
		infoDegree.setSigla("LEIC");
		infoDegreeCurricularPlan.setInfoDegree(infoDegree);
		infoExecutionDegree.setInfoDegreeCurricularPlan(
			infoDegreeCurricularPlan);
		infoExecutionDegree.setInfoExecutionYear(
			getCurrentExecutionPeriod().getInfoExecutionYear());

		return infoExecutionDegree;
	}

	private InfoExamsMap getInfoExamsMap() {
		InfoExamsMap infoExamsMap = null;
		GestorServicos gestor = GestorServicos.manager();

		Object[] args =
			{
				getInfoExecutionDegree(),
				getCurricularYears(),
				getCurrentExecutionPeriod()};
		try {
			infoExamsMap =
				(InfoExamsMap) gestor.executar(
					getPrivledgedUserView(),
					"ReadExamsMap",
					args);
		} catch (FenixServiceException e) {
			fail("failed obtaining exams map");
		}

		return infoExamsMap;
	}

}
