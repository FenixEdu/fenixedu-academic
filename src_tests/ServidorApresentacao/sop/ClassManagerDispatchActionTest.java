/**
 * 
 * Project sop 
 * Package ServidorApresentacao.sop 
 * Created on 15/Jan/2003
 */
package ServidorApresentacao.sop;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ClassKey;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class ClassManagerDispatchActionTest
	extends TestCasePresentationSopPortal {

	/**
	 * Constructor for ClassManagerDispatchActionTest.
	 * @param arg0
	 */
	public ClassManagerDispatchActionTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ClassManagerDispatchActionTest.class);
		return suite;
	}

	
	protected String getServletConfigFile() {
			return "/WEB-INF/tests/web-sop.xml";
		}
	public void testUnAuthorizedCreateClass() {

		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "createClass");

		setNotAuthorizedUser();

		//fills the form
		addRequestParameter("className", "10501");

		//action perform
		actionPerform();
		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedCreateExistingClass() {
		getSession().setAttribute(
			SessionConstants.SESSION_IS_VALID,
			SessionConstants.SESSION_IS_VALID);

		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "createClass");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();

		setAuthorizedUser();

		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan(
					"plano1",
					new InfoDegree(
						"LEIC",
						"Licenciatura de Engenharia Informatica e de Computadores")),
				new InfoExecutionYear("2002/2003")));
		//fills the form
		addRequestParameter("className", "10501");

		//action perform
		actionPerform();
		//verify that there are  errors		
		String[] errors = { "errors.existClass" };
		verifyActionErrors(errors);
		//verify correct Forward
		verifyInputForward();
	}

	public void testAuthorizedCreateNonExistingClass() {
		getSession().setAttribute(
			SessionConstants.SESSION_IS_VALID,
			SessionConstants.SESSION_IS_VALID);

		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "createClass");

		//coloca credenciais na sessao
		setAuthorizedUser();

		//fills the form
		addRequestParameter("className", "newClassName");
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
		//action perform
		actionPerform();
		//verify that there are no  errors					
		verifyNoActionErrors();
		//verify correct Forward
		verifyInputForward();
	}

	public void testUnAuthorizedPrepareEditClass() {
		getSession().setAttribute(
			SessionConstants.SESSION_IS_VALID,
			SessionConstants.SESSION_IS_VALID);

		//			set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");

		//coloca credenciais na sessao
		setNotAuthorizedUser();

		//fills the form
		addRequestParameter("className", "10501");

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

	public void testUnAuthorizedEditClass() {
		getSession().setAttribute(
			SessionConstants.SESSION_IS_VALID,
			SessionConstants.SESSION_IS_VALID);

		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");
		addRequestParameter("change", "1");

		//coloca credenciais na sessao
		setNotAuthorizedUser();

		//fills the form
		addRequestParameter("className", "xpto");
		//Coloca contexto em sessão
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
				new Integer(1),
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

	public void testAuthorizedPrepareEditClass() {
		getSession().setAttribute(
			SessionConstants.SESSION_IS_VALID,
			SessionConstants.SESSION_IS_VALID);

		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");

		//coloca credenciais na sessao
		setAuthorizedUser();

		//fills the form
		addRequestParameter("className", "10501");
		//Coloca contexto em sessão
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

		//action perform
		actionPerform();
		//verify that there are no errors
		verifyNoActionErrors();
		//verify correct Forward
		verifyInputForward();
	}

	public void testAuthorizedEditClass() {
		getSession().setAttribute(
			SessionConstants.SESSION_IS_VALID,
			SessionConstants.SESSION_IS_VALID);

		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");
		addRequestParameter("change", "1");
		//coloca credenciais na sessao
		setAuthorizedUser();

		//fills the form
		addRequestParameter("className", "xpto");
		//				Coloca contexto em sessão
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

		//		puts old classview in session
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
		//verify that there are no errors
		verifyNoActionErrors();
		//verify correct Forward
		verifyInputForward();
		//verify that the action puts in session/request the required objects
		assertNotNull(SessionConstants.CLASS_VIEW);
		assertNotNull(SessionConstants.LESSON_LIST_ATT);

	}

	public void testAuthorizedEditClassToExisting() {
		getSession().setAttribute(
			SessionConstants.SESSION_IS_VALID,
			SessionConstants.SESSION_IS_VALID);

		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");
		addRequestParameter("change", "1");
		//coloca credenciais na sessao
		setAuthorizedUser();
		//fills the form
		addRequestParameter("className", "turma413");
		//				Coloca contexto em sessão
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

		//		puts old classview in session
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
		verifyActionErrors(new String[] { "error.exception.existing" });

		//verify that the action puts in session/request the required objects
		assertNotNull(SessionConstants.CLASS_VIEW);
		assertNotNull(SessionConstants.LESSON_LIST_ATT);

	}

	public void testUnAuthorizedDeleteClass() {

		//			set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "deleteClass");

		//coloca credenciais na sessao
		setNotAuthorizedUser();
		//fills the form
		addRequestParameter("className", "10501");

		//action perform
		actionPerform();
		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedDeleteClass() {

		//			set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "deleteClass");

		//create execution period
		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			infoExecutionPeriod);

		//create execution degree
		InfoDegree infoDegree =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			new InfoDegreeCurricularPlan("plano1", infoDegree);
		InfoExecutionDegree infoExecutionDegree =
			new InfoExecutionDegree(
				infoDegreeCurricularPlan,
				infoExecutionYear);
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			infoExecutionDegree);

		//privileges
		setAuthorizedUser();

		//fills the form
		addRequestParameter("className", "10501");

		//action perform
		actionPerform();

		//verify that there are errors
		verifyNoActionErrors();
		//		verify correct Forward

		verifyForwardPath("/ClassesManagerDA.do?method=listClasses");
	}

	public void testUnAuthorizedViewClass() {

		//			set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "viewClass");

		//coloca credenciais na sessao
		setNotAuthorizedUser();

		//fills the form
		addRequestParameter("className", "10501");

		//action perform
		actionPerform();
		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);
	}

	public void testAuthorizedViewClass() {

		//		set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "viewClass");

		//coloca credenciais na sessao
		setAuthorizedUser();

		//fills the form
		addRequestParameter("className", "10501");
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

		//action perform
		actionPerform();
		//verify that there are errors
		verifyNoActionErrors();
		//		verify correct Forward

		verifyInputForward();
		//		verify that the action puts in session/request the required objects
		assertNotNull(SessionConstants.CLASS_VIEW);
		assertNotNull(SessionConstants.LESSON_LIST_ATT);
	}

	/**
	 * Method verifySessionAttributes.
	 * @param httpSession
	 * @param attributesList
	 */
	private void verifySessionAttributes(
		HttpSession session,
		List existingAttributesList,
		List nonExistingAttributesList) {
		Enumeration attNames = session.getAttributeNames();

		verifySessionAttributes(session, existingAttributesList, true);
		verifySessionAttributes(session, nonExistingAttributesList, false);
	}
	/**
	 * Method verifySessionAttributes.
	 * @param session
	 * @param existingAttributesList
	 * @param exists
	 */
	private void verifySessionAttributes(
		HttpSession session,
		List list,
		boolean exists) {
		if (list != null) {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				String attName = (String) iterator.next();
				if (!((session.getAttribute(attName) != null) == exists)) {
					String message = " Session contains attribute ";
					if (exists)
						message = "Session doesn't contains attribute ";
					fail(message + attName + ".");
				}
			}
		}

	}

	private InfoClass getInfoTurma(IUserView userView, String className)
		throws Exception {

		ClassKey keyClass = new ClassKey(className);
		Object argsLerTurma[] = { keyClass };
		InfoClass classView =
			(InfoClass) ServiceUtils.executeService(
				userView,
				"LerTurma",
				argsLerTurma);

		return classView;
	}
	
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		
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
