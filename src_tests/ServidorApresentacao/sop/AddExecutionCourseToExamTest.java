/*
 * AddExecutionCourseToExamTest.java
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
import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRole;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.RoleType;
import Util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class AddExecutionCourseToExamTest extends TestCasePresentation {

	public AddExecutionCourseToExamTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(AddExecutionCourseToExamTest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("sop", "/addExecutionCourseToExam");
	}

	public void testAddExecutionCourseToExam() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			new InfoExecutionCourse(
				"",
				"APR",
				"",
				null,
				null,
				null,
				null,
				getCurrentExecutionPeriod()));
		session.setAttribute(
			SessionConstants.INFO_VIEW_EXAM,
			getViewExamByDayAndShift());

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("Sucess");
	}

	public void testAddExecutionCourseWithAlreadyScheduledExamForCorrespondingSeasonToExam() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			new InfoExecutionCourse(
				"",
				"RCI",
				"",
				null,
				null,
				null,
				null,
				getCurrentExecutionPeriod()));
		session.setAttribute(
			SessionConstants.INFO_VIEW_EXAM,
			getViewExamByDayAndShift());

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

	private InfoViewExamByDayAndShift getViewExamByDayAndShift() {
		InfoViewExamByDayAndShift infoViewExamByDayAndShift =
			new InfoViewExamByDayAndShift();

		List infoExecutionCourses = new ArrayList();
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"",
				"AC",
				"",
				null,
				null,
				null,
				null,
				getCurrentExecutionPeriod());
		infoExecutionCourses.add(infoExecutionCourse);

		infoViewExamByDayAndShift.setInfoExecutionCourses(infoExecutionCourses);
		infoViewExamByDayAndShift.setInfoExam(
			new InfoExam(null, null, null, new Season(Season.SEASON2)));

		return infoViewExamByDayAndShift;
	}

}
