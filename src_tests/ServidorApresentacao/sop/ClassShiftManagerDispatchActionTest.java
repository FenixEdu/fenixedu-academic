/**
 * 
 * Project sop 
 * Package ServidorApresentacao.sop 
 * Created on 16/Jan/2003
 */
package ServidorApresentacao.sop;

import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.ClassShiftManagerDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *
 */
// TODO verify post conditions of methods test...
public class ClassShiftManagerDispatchActionTest extends TestCasePresentationSopPortal {

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

	
	protected String getServletConfigFile() {
			return "/WEB-INF/web.xml";
		}
	public void testUnAuthorizedAddClassShift() {
				
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "addClassShift");

		setNotAuthorizedUser();
		
		//fills the form
		addRequestParameter("shiftIndex", "0");
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
		String[] errors = { "error.exception.existing" };
		verifyActionErrors(errors);
		
		verifyInputForward();

	}


	 
	public void testAuthorizedAddClassShift() {
		/**
		 * prepare session 
		 */
		setAuthorizedUser();

		InfoDegree infoDegree = new InfoDegree();
		infoDegree.setSigla("LEIC");
		//		Coloca contexto em sessão
		InfoExecutionDegree infoExecutionDegree =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", infoDegree),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			getInfoExecutionPeriod());
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			infoExecutionDegree);

		Integer curricularYear = new Integer(2);
		getSession().setAttribute(
			SessionConstants.CURRICULAR_YEAR_KEY,
			curricularYear);

		InfoExecutionPeriod infoExecutionPeriod = getInfoExecutionPeriod();

		// read executionCourses from executionDegree
		GestorServicos manager = GestorServicos.manager();

		Object args[] =
			{ infoExecutionDegree, infoExecutionPeriod, curricularYear };
		List infoExecutionCourseList = null;
		try {
			infoExecutionCourseList =
				(List) manager.executar(
					getAuthorizedUser(),
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					args);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test services first!");
		}

		assertTrue(
			"InfoExecutionCourseList must be not empty!",
			!infoExecutionCourseList.isEmpty());

		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) infoExecutionCourseList.get(0);

		InfoClass infoClass =
			new InfoClass(
				"10501",
				new Integer(1),
				infoExecutionDegree,
				infoExecutionPeriod);

		getSession().setAttribute(SessionConstants.CLASS_VIEW, infoClass);
		getSession().setAttribute(SessionConstants.U_VIEW, getAuthorizedUser());
		
		
		Object[] argsLerTurnosDeDisciplinaExecucao = { infoExecutionCourse };
		List shiftList = null;
		try{	
			shiftList = (List) manager.executar(getAuthorizedUser(), "LerTurnosDeDisciplinaExecucao", argsLerTurnosDeDisciplinaExecucao);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test services first!");
		}
		
		getSession().setAttribute(
			SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY,
			shiftList);

		
		addRequestParameter("shiftIndex", "1");
		
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		
		addRequestParameter("method", "addClassShift");

		//action perform
		actionPerform();

		//verify that there are errors
		verifyNoActionErrors();

		//verify Forward
		verifyForward("viewClassShiftList");

		//verifies correctness of session/request
		assertNotNull(
			getRequest().getAttribute(
				ClassShiftManagerDispatchAction.SHIFT_LIST_ATT));
		assertNotNull(getSession().getAttribute(SessionConstants.CLASS_VIEW));

	}
	private InfoExecutionPeriod getInfoExecutionPeriod() {
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		infoExecutionPeriod.setName("2º Semestre");
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear("2002/2003");
		infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
		return infoExecutionPeriod;
	}

	public void testUnAuthorizedRemoveClassShift() {
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
		
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "removeClassShift");

		//coloca credenciais na sessao
		setNotAuthorizedUser();
		
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

		
		InfoClass classView =
			new InfoClass(
				"10501",
				new Integer(5),
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);
		
	

		//action perform
		actionPerform();

		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

		verifyForwardPath("/naoAutorizado.do");

	}

	public void testAuthorizedRemoveClassShift() {
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
		
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "removeClassShift");

		//coloca credenciais na sessao
		setAuthorizedUser();

		//			Coloca contexto em sessão
		InfoDegree infoDegree =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree infoExecutionDegree =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", infoDegree),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			infoExecutionDegree);

		//puts old classview in session
		InfoClass infoClass =
			new InfoClass(
				"10501",
				new Integer(5),
				infoExecutionDegree,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(SessionConstants.CLASS_VIEW, infoClass);
		
		GestorServicos manager = GestorServicos.manager();
		
    	Object args[] =
			{ infoClass.getNome(), infoExecutionDegree, getInfoExecutionPeriod()};
		List classInfoShiftList = null;
		try {
			classInfoShiftList =
				(List) manager.executar(
					getAuthorizedUser(),
					"LerTurnosDeTurma",
					args);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test services first!");
		}
		
		assertNotNull("class shift list must be not null!", classInfoShiftList);
		assertTrue("class shift list must be not empty!", !classInfoShiftList.isEmpty());

		getSession().setAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY, classInfoShiftList);

		//fills the form
		addRequestParameter("shiftIndex", "0");

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
		//TODO make this test
	}

	public void testAuthorizedViewClassShift() {
		
		
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "viewClassShiftList");

		//coloca credenciais na sessao

		setAuthorizedUser();

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


		// read executionCourses from executionDegree

		//puts old classview in session
		InfoClass infoClass =
			new InfoClass(
				"10501",
				new Integer(5),
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")));



		getSession().setAttribute(SessionConstants.CLASS_VIEW, infoClass);


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
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
		
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listAvailableShifts");

		//coloca credenciais na sessao
		setNotAuthorizedUser();
		
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
		
		verifyForwardPath("/naoAutorizado.do");

	}

	public void testAuthorizedListAvailableShifts() {
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
		
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listAvailableShifts");

		//coloca credenciais na sessao
		setAuthorizedUser();
		
		

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
		InfoExecutionCourse executionCourse =
			new InfoExecutionCourse(
				"Trabalho Final de Curso I",
				"TFCI",
				"programa1",
				new Double(0),
				new Double(0),
				new Double(0),
				new Double(0),
				getInfoExecutionPeriod());

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
	
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		// 
		return null;
	}
}
