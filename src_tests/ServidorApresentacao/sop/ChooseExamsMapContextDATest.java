/*
 * ChooseExamDayAndShiftDispatchActionTest.java
 *
 * Created on 2003/04/06
 */

package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDATest extends TestCasePresentation {

	public ChooseExamsMapContextDATest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ChooseExamsMapContextDATest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("sop", "/chooseExamsMapContextDA");
	}

	InfoExecutionPeriod infoExecutionPeriodFromSession = null;
	ArrayList semesters = null;
	List curricularYearsListFromSession = null;
	List executionDegreeListFromSession = null;
	ArrayList licenciaturas = null;

	public void testPrepare() {
		// indicate method to be called
		addRequestParameter("method", "prepare");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("chooseExamsMapContext");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------
		HttpSession session = getSession();
		HttpServletRequest request = getRequest();

		infoExecutionPeriodFromSession =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		assertNotNull(
			"INFO_EXECUTION_PERIOD_KEY not in session",
			infoExecutionPeriodFromSession);

		ArrayList semesters = (ArrayList) session.getAttribute("semestres");
		assertNotNull("semestres not in session", semesters);
		assertEquals("semestres list size unexpected", 3, semesters.size());

		List curricularYearsListFromSession =
			(List) session.getAttribute(
				SessionConstants.CURRICULAR_YEAR_LIST_KEY);
		assertNotNull(
			"CURRICULAR_YEAR_LIST_KEY not in session",
			curricularYearsListFromSession);
		assertEquals(
			"CURRICULAR_YEAR_LIST_KEY list size unexpected",
			5,
			curricularYearsListFromSession.size());

		List executionDegreeListFromSession =
			(List) session.getAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);
		assertNotNull(
			"INFO_EXECUTION_DEGREE_LIST_KEY not in session",
			executionDegreeListFromSession);
		assertEquals(
			"INFO_EXECUTION_DEGREE_LIST_KEY list size unexpected",
			2,
			executionDegreeListFromSession.size());

		ArrayList licenciaturas =
			(ArrayList) request.getAttribute(SessionConstants.DEGREES);
		assertNotNull("DEGREES not in session", licenciaturas);
		assertEquals("DEGREES list size unexpected", 3, licenciaturas.size());
	}

	public void testChoose() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY,
			readExecutionDegrees());

		// indicate method to be called
		addRequestParameter("method", "choose");

		// Fill out form
		String[] selectedCurricularYears = { "1", "2", "4" };
		addRequestParameter("selectedCurricularYears", selectedCurricularYears);
		addRequestParameter("index", "0");
		addRequestParameter("year", "2003");
		addRequestParameter("beginning", "13");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("showExamsMap");

		// -------------------------------------------------------------------------
		//   verify session contains expected objects
		// -------------------------------------------------------------------------

		List curricularYears =
			(List) session.getAttribute(SessionConstants.CURRICULAR_YEARS_LIST);
		assertNotNull("CURRICULAR_YEARS_LIST not in session", curricularYears);
		assertEquals(
			"CURRICULAR_YEARS_LIST list size unexpected",
			3,
			curricularYears.size());

		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) session.getAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY);
		assertNotNull("INFO_EXECUTION_DEGREE_KEY not in session", infoExecutionDegree);
		assertEquals("INFO_EXECUTION_DEGREE_KEY was unexpected",
			"Licenciatura de Engenharia Informatica e de Computadores",
			infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
	}


	private List readExecutionDegrees() {
		IUserView userView = null;
		InfoExecutionPeriod infoExecutionPeriod = null;
		try {
			infoExecutionPeriod =
				(InfoExecutionPeriod) ServiceUtils.executeService(
					userView,
					"ReadActualExecutionPeriod",
					new Object[0]);
		} catch (FenixServiceException e) {
			fail("unexpected exception reading actual execution period: " + e);
		}

		Object args[] = { infoExecutionPeriod.getInfoExecutionYear()};

		List executionDegreeList = null;
		try {
			executionDegreeList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadExecutionDegreesByExecutionYear",
					args);
		} catch (FenixServiceException e) {
			fail("unexpected exception reading execution degrees: " + e);
		}

		return executionDegreeList;
	}

}
