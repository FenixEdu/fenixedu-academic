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
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRole;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.RoleType;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsByDegreeAndCurricularYearActionTest
	extends TestCasePresentation {

	public ViewExamsByDegreeAndCurricularYearActionTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ViewExamsByDegreeAndCurricularYearActionTest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("sop", "/viewExamsByDegreeAndCurricularYear");
	}

	public void testView() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			getCurrentExecutionPeriod());
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			getInfoExecutionDegree());
		session.setAttribute(
			SessionConstants.CURRICULAR_YEAR_KEY,
			new Integer(3));

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("Sucess");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		List infoExecutionCourseAndExamsList =
			(List) session.getAttribute(SessionConstants.INFO_EXAMS_KEY);
		assertNotNull(
			"INFO_EXAMS_KEY not found in session",
			infoExecutionCourseAndExamsList);
		assertEquals(
			"INFO_EXAMS_KEY list size unexpected",
			1,
			infoExecutionCourseAndExamsList.size());
	}

	public void testViewNoExams() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			getCurrentExecutionPeriod());
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			getInfoExecutionDegree());
		session.setAttribute(
			SessionConstants.CURRICULAR_YEAR_KEY,
			new Integer(5));

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("Sucess");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		List infoExecutionCourseAndExamsList =
			(List) session.getAttribute(SessionConstants.INFO_EXAMS_KEY);
		assertNull(
			"INFO_EXAMS_KEY was not suposed to be in session",
			infoExecutionCourseAndExamsList);
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

}
