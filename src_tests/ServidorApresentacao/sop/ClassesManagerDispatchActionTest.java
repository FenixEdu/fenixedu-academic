/**
 * 
 * Project sop 
 * Package ServidorApresentacao.sop 
 * Created on 15/Jan/2003
 */
package ServidorApresentacao.sop;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.ClassesManagerDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoCurso;

/**
 * @author João Mota
 *
 */
public class ClassesManagerDispatchActionTest extends TestCasePresentationSopPortal {

	/**
	 * Constructor for ClassesManagerDispatchAction.
	 * @param arg0
	 */
	public ClassesManagerDispatchActionTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ClassesManagerDispatchActionTest.class);
		return suite;
	}

	
	protected String getServletConfigFile() {
			return "/WEB-INF/web.xml";
		}
	public void testUnAuthorizedListClasses() {
		
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
				
		//set request path
		setRequestPathInfo("sop", "/ClassesManagerDA");
		
		addRequestParameter("method","listClasses");
		
		
		//sets needed objects to session/request
		setNotAuthorizedUser();
		
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
		
		verifyForwardPath("/naoAutorizado.do");
		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedListClasses() {
		
				
		//set request path
		setRequestPathInfo("sop", "/ClassesManagerDA");
		
		addRequestParameter("method","listClasses");
		
		//sets needed objects to session/request
		setAuthorizedUser();
		
		//		Coloca contexto em sessão
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores", TipoCurso.LICENCIATURA_STRING);
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
					SessionConstants.CURRICULAR_YEAR_KEY,
					new Integer(1));

		//action perform
		actionPerform();
		//verify that there are no errors
		verifyNoActionErrors();
		//verify forward
		verifyForward("listClasses");
		//verify that all that should be in session is
		GestorServicos gestor = GestorServicos.manager();
		Object argsLerTurmas[] =
			{
				iLE,
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003")),
				new Integer(1)};

		try {
			List classesList =
				(List) gestor.executar(
					SessionUtils.getUserView(getRequest()),
					"LerTurmas",
					argsLerTurmas);
			if (classesList != null && !classesList.isEmpty()) {
				assertEquals(
					classesList,
					getRequest().getAttribute(ClassesManagerDispatchAction.CLASS_LIST_KEY));
			}
		} catch (Exception e) {
		}

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
		
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		
		return null;
	}

}
