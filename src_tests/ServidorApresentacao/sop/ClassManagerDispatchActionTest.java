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
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *
 */
public class ClassManagerDispatchActionTest extends TestCasePresentation {
	
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

	public void setUp() {
		super.setUp();
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");

	}

	public void testUnAuthorizedCreateClass() {
		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "createClass");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

		//fills the form
		addRequestParameter("className", "10501");

		//action perform
		actionPerform();
		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedCreateExistingClass() {
		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "createClass");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("CriarTurma");
		privilegios.add("LerTurma");
		privilegios.add("LerAulasDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);
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
		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "createClass");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("CriarTurma");
		privilegios.add("LerTurma");
		privilegios.add("LerAulasDeTurma");

		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

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
		//			set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

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
		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");
		addRequestParameter("change", "1");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

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
		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("EditarTurma");
		privilegios.add("LerTurma");
		privilegios.add("LerAulasDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

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
		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");
		addRequestParameter("change", "1");
		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("EditarTurma");
		privilegios.add("LerTurma");
		privilegios.add("LerAulasDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

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
		//set request path
		setRequestPathInfo("sop", "/ClassManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "editClass");
		addRequestParameter("change", "1");
		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("EditarTurma");
		privilegios.add("LerTurma");
		privilegios.add("LerAulasDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

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
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

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
		InfoExecutionYear infoExecutionYear =  new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		getSession().setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

		//create execution degree
		InfoDegree infoDegree = new InfoDegree("LEIC", "Licenciatura de Engenharia Informatica e de Computadores");
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1", infoDegree);
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
		getSession().setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);
				
		//privileges
		HashSet privileges = new HashSet();
		privileges.add("LerTurma");		
		privileges.add("ApagarTurma");
		IUserView userView = new UserView("user", privileges);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

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
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

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
		HashSet privilegios = new HashSet();
		privilegios.add("LerTurma");
		privilegios.add("LerAulasDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

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
}
