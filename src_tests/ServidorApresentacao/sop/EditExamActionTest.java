/*
 * CreateExamDATest.java
 *
 * Created on 2003/04/06
 */

package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRole;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.RoleType;
import Util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamActionTest extends TestCasePresentation {

	public EditExamActionTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EditExamActionTest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("sop", "/editExam");
	}

	public void testEdit() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.INFO_EXAMS_KEY,
			getViewExamByDayAndShift());
		session.setAttribute("input", "viewExamsByDayAndShift");

		// indicate method to be called
		addRequestParameter("method", "create");

		// Fill out form
		addRequestParameter("season", "1");
		addRequestParameter("day", "1");
		addRequestParameter("month", "6");
		addRequestParameter("year", "2003");
		addRequestParameter("beginning", "13");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("viewExamsByDayAndShift");
	}

	public void testEditWithAlternativeInputAndNoBeginning() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.INFO_EXAMS_KEY,
			getViewExamByDayAndShift());
		session.setAttribute("input", "viewExamsMap");

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
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("viewExamsMap");
	}

	public void testEditSwitchSeassonToAlreadyScheduledSeason() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.INFO_EXAMS_KEY,
			getViewExamByDayAndShift());
		session.setAttribute("input", "viewExamsByDayAndShift");

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
		String[] errors = { "error.exception.existing" };
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
					"ReadCurrentExecutionPeriod",
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

	private InfoViewExamByDayAndShift getViewExamByDayAndShift() {
		InfoViewExamByDayAndShift infoViewExamByDayAndShift =
			new InfoViewExamByDayAndShift();

		List infoExecutionCourses = new ArrayList();
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"",
				"RCI",
				"",
				null,
				null,
				null,
				null,
				getCurrentExecutionPeriod());
		infoExecutionCourses.add(infoExecutionCourse);

		infoViewExamByDayAndShift.setInfoExecutionCourses(infoExecutionCourses);
		infoViewExamByDayAndShift.setInfoExam(
			new InfoExam(null, null, null, new Season(Season.SEASON1)));

		return infoViewExamByDayAndShift;
	}

}
