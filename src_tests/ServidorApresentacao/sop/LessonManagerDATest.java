/*
 * CreateExamDATest.java
 *
 * Created on 2003/04/07
 */

package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRole;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.RoleType;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class LessonManagerDATest extends TestCasePresentation {

	public LessonManagerDATest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LessonManagerDATest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("sop", "/criarAulaForm");
	}

	public void testCreate() {
		HttpSession session = getSession();

		// Prepare session
		session.setAttribute(SessionConstants.U_VIEW, getPrivledgedUserView());
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			getCurrentExecutionPeriod());
		session.setAttribute(
			SessionConstants.CURRICULAR_YEAR_KEY,
			new Integer(4));
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			getInfoExecutionDegree());
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			getCurrentExecutionPeriod());

		// indicate method to be called
		addRequestParameter("operation", "Criar");

		// Fill out form
		addRequestParameter("horaInicio", "10");
		addRequestParameter("minutosInicio", "0");
		addRequestParameter("horaFim", "11");
		addRequestParameter("minutosFim", "0");
		addRequestParameter("courseInitials", "APR");
		addRequestParameter("nomeSala", "Ga1");
		addRequestParameter("diaSemana", "2");
		addRequestParameter("tipoAula", "1");

		// action perform
		actionPerform();

		// verify that there are no errors
		verifyNoActionErrors();
		// verify correct Forward
		verifyForward("Criar");
	}

	// TODO : testeCreate alternative cenarios
	// TODO : testStoreChanges
	// TODO : testChangeRoom
	// TODO : testChooseRoom
	

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
