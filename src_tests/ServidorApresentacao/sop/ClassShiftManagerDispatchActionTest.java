/**
 * 
 * Project sop 
 * Package ServidorApresentacao.sop 
 * Created on 16/Jan/2003
 */
package ServidorApresentacao.sop;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.ClassShiftManagerDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *
 */
public class ClassShiftManagerDispatchActionTest extends TestCasePresentation {

	/**
	 * Constructor for ClassShiftManagerDispatchActionTest.
	 * @param arg0
	 */
	public ClassShiftManagerDispatchActionTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ClassShiftManagerDispatchActionTest.class);
		return suite;
	}

	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");

	}

	public void testUnAuthorizedAddClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "addClassShift");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//fills the form
		addRequestParameter("shiftName", "turno1");
		//		Coloca contexto em sessão
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", iL),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			iLE);

		//puts old classview in session
		InfoClass oldClass =
			new InfoClass(
				"10501",
				new Integer(5),
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(SessionConstants.CLASS_VIEW, oldClass);

		//action perform
		actionPerform();

		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedAddClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "addClassShift");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("AdicionarTurno");
		privilegios.add("LerTurnosDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//fills the form
		addRequestParameter("shiftName", "turno5");

		//		Coloca contexto em sessão
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", iL),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			iLE);
		getSession().setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			new InfoExecutionCourse(
				"Trabalho Final de Curso I",
				"TFCI",
				"programa1",
				new Double(0),
				new Double(0),
				new Double(0),
				new Double(0),
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003"))));

		//puts old classview in session
		InfoClass oldClass =
			new InfoClass(
				"10501",
				new Integer(1),
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(SessionConstants.CLASS_VIEW, oldClass);

		//action perform
		actionPerform();

		//verify Forward
		verifyForward("viewClassShiftList");

		//verify that there are errors
		verifyNoActionErrors();

		//verifies correctness of session/request
		assertNotNull(
			getRequest().getAttribute(
				ClassShiftManagerDispatchAction.SHIFT_LIST_ATT));
		assertNotNull(getSession().getAttribute(SessionConstants.CLASS_VIEW));

	}

	public void testUnAuthorizedRemoveClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "removeClassShift");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//fills the form
		addRequestParameter("shiftName", "turno1");

		//		Coloca contexto em sessão
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", iL),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			iLE);

		//puts old classview in session
		InfoClass oldClass =
			new InfoClass(
				"10501",
				new Integer(5),
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(SessionConstants.CLASS_VIEW, oldClass);

		//action perform
		actionPerform();

		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedRemoveClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "removeClassShift");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("RemoverTurno");
		privilegios.add("LerTurnosDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//fills the form
		addRequestParameter("shiftName", "turno1");

		//			Coloca contexto em sessão
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", iL),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			iLE);

		//puts old classview in session
		InfoClass oldClass =
			new InfoClass(
				"10501",
				new Integer(5),
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(SessionConstants.CLASS_VIEW, oldClass);

		//action perform
		actionPerform();

		//verify Forward
		verifyForward("viewClassShiftList");

		//verify that there are errors
		verifyNoActionErrors();

		//verifies correctness of session/request

		assertNotNull(getSession().getAttribute(SessionConstants.CLASS_VIEW));

	}

	public void testUnAuthorizedViewClassShift() {
		//set request path
		//		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//		//sets needed objects to session/request
		//		addRequestParameter("method", "viewClassShiftList");
		//
		//		//coloca credenciais na sessao
		//		HashSet privilegios = new HashSet();
		//		IUserView userView = new UserView("user", privilegios);
		//		getSession().setAttribute(SessionConstants.U_VIEW, userView);
		//
		//		//coloca a class_view na sessão
		//
		//		InfoDegree infoDegree =
		//			new InfoDegree(
		//				_turma1.getLicenciatura().getSigla(),
		//				_turma1.getLicenciatura().getNome());
		//		InfoClass classView =
		//			new InfoClass(
		//				_turma1.getNome(),
		//				_turma1.getSemestre(),
		//				_turma1.getAnoCurricular(),
		//				infoDegree);
		//		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);
		//
		//		//action perform
		//		actionPerform();
		//
		//		//verify that there are errors
		//		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		//		verifyActionErrors(errors);
		//fail("Falta fazer o teste");
	}

	public void testAuthorizedViewClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "viewClassShiftList");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("RemoverTurno");
		privilegios.add("LerTurnosDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//			Coloca contexto em sessão
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", iL),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			iLE);

		//puts old classview in session
		InfoClass oldClass =
			new InfoClass(
				"10501",
				new Integer(5),
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(SessionConstants.CLASS_VIEW, oldClass);

		//action perform
		actionPerform();

		//verify Forward
		verifyForward("viewClassShiftList");

		//verify that there are errors
		verifyNoActionErrors();

		//verifies correctness of session/request
		assertNotNull(
			getRequest().getAttribute(
				ClassShiftManagerDispatchAction.SHIFT_LIST_ATT));
		assertNotNull(getSession().getAttribute(SessionConstants.CLASS_VIEW));

	}

	public void testUnAuthorizedListAvailableShifts() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listAvailableShifts");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//			Coloca contexto em sessão
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", iL),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			iLE);

		//puts old classview in session
		InfoClass oldClass =
			new InfoClass(
				"10501",
				new Integer(5),
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(SessionConstants.CLASS_VIEW, oldClass);

		//action perform
		actionPerform();

		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedListAvailableShifts() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listAvailableShifts");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("LerTurnosDeDisciplinaExecucao");
		privilegios.add("LerTurnosDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//			Coloca contexto em sessão
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", iL),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			iLE);

		//puts execution course to session
		InfoExecutionCourse executionCourse = new InfoExecutionCourse(
		"Trabalho Final de Curso I",
		"TFCI",
		"programa1",
		new Double(0),
		new Double(0),
		new Double(0),
		new Double(0),
		new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		
		getSession().setAttribute(
					SessionConstants.EXECUTION_COURSE_KEY,
		executionCourse);

		//puts old classview in session
		InfoClass oldClass =
			new InfoClass(
				"10501",
				new Integer(5),
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(SessionConstants.CLASS_VIEW, oldClass);

		

		//action perform
		actionPerform();

		//verify Forward
		verifyForward("viewClassShiftList");

		//verify that there are errors
		verifyNoActionErrors();

		//verifies correctness of session/request
		assertNotNull(
			getRequest().getAttribute(
				ClassShiftManagerDispatchAction.SHIFT_LIST_ATT));
		assertNotNull(
			getRequest().getAttribute(
				ClassShiftManagerDispatchAction.AVAILABLE_LIST));

	}
}
