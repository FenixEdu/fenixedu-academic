/*
 * CreateExamDATest.java
 *
 * Created on 2003/04/06
 */

package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRole;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.RoleType;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class CreateExamDATest extends TestCasePresentation {

	public CreateExamDATest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CreateExamDATest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("sop", "/createExam");
	}

	public void testPrepare() {
		// indicate method to be called
		addRequestParameter("method", "prepare");
		addRequestParameter("nextPage", "abc123cba321");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("showCreateForm");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		HttpSession session = getSession();

		String nextPage =
			(String) session.getAttribute(SessionConstants.NEXT_PAGE);
		assertNotNull("NEXT_PAGE not in session", nextPage);
		assertEquals(
			"NEXT_PAGE contains unexpected value",
			"abc123cba321",
			nextPage);

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
	}

	public void testCreate() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		InfoExecutionCourse executionCourse =
			new InfoExecutionCourse("", "APR", "", null, null, null, null, getCurrentExecutionPeriod());
		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			executionCourse);
		session.setAttribute(
					SessionConstants.NEXT_PAGE,
					"viewExamsMap");

		// indicate method to be called
		addRequestParameter("method", "create");

		// Fill out form
		addRequestParameter("season", "2");
		addRequestParameter("day", "1");
		addRequestParameter("month", "6");
		addRequestParameter("year", "2003");
		addRequestParameter("beginning", "13");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("viewExamsMap");
	}

	public void testCreateWithOutBeginningAndNextPage() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		InfoExecutionCourse executionCourse =
			new InfoExecutionCourse("", "APR", "", null, null, null, null, getCurrentExecutionPeriod());
		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			executionCourse);

		// indicate method to be called
		addRequestParameter("method", "create");

		// Fill out form
		addRequestParameter("season", "2");
		addRequestParameter("day", "1");
		addRequestParameter("month", "6");
		addRequestParameter("year", "2003");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("Sucess");
	}

	public void testCreateAlreadyScheduledSeason() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		InfoExecutionCourse executionCourse =
			new InfoExecutionCourse("", "APR", "", null, null, null, null, getCurrentExecutionPeriod());
		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			executionCourse);

		// indicate method to be called
		addRequestParameter("method", "create");

		// Fill out form
		addRequestParameter("season", "1");
		addRequestParameter("day", "1");
		addRequestParameter("month", "6");
		addRequestParameter("year", "2003");

		// action perform
		actionPerform();

		// verify that there are no errors
		String[] errors = {"error.exception.existing"}; 
		verifyActionErrors(errors);
		// verify correct Forward
		verifyInputForward();
	}


	private InfoExecutionPeriod getCurrentExecutionPeriod() {
		InfoExecutionPeriod infoExecutionPeriod = null;
		try {
			infoExecutionPeriod =
				(InfoExecutionPeriod) ServiceUtils.executeService(
					getPrivledgedUserView(),
					"ReadActualExecutionPeriod",
					new Object[0]);
		} catch (FenixServiceException e) {
			fail("unexpected exception reading actual execution period: " + e);
		}

		return infoExecutionPeriod;
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

}
