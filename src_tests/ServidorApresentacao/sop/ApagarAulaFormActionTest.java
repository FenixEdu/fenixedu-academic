package ServidorApresentacao.sop;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
author tfc130
*/

public class ApagarAulaFormActionTest extends TestCasePresentationSopPortal {
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ApagarAulaFormActionTest.class);

		return suite;
	}

	public void setUp() {
		super.setUp();
	}


	public ApagarAulaFormActionTest(String testName) {
		super(testName);
	}

	private void prepareRequest() {
		//required to put form manipularTurnosForm in request
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);		
		setRequestPathInfo("/sop", "/manipularAulasForm");
		addRequestParameter("indexAula", new Integer(0).toString());
		addRequestParameter("operation", "Apagar Aula");
		actionPerform();		
	}
	
	/**dummy method to prevent super.testUnsuccessfulExecutionOfAction() 
	 * from executing 
	 */
	public void testUnsuccessfulExecutionOfAction() {
	}

	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {

		Map items = new HashMap();
		
		items.put(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
				
		try {
			GestorServicos gestor = GestorServicos.manager();
			InfoExecutionCourse iDE =
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
						new InfoExecutionYear("2002/2003")));
			items.put("infoDisciplinaExecucao", iDE);
			
			Object argsLerSalas[] = new Object[0];
			ArrayList infoSalas =
				(ArrayList) gestor.executar(getAuthorizedUser(), "LerSalas", argsLerSalas);
			getSession().setAttribute("listaSalas", infoSalas);
			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = iDE;
			ArrayList infoAulas =
				(ArrayList) gestor.executar(
					getAuthorizedUser(),
					"LerAulasDeDisciplinaExecucao",
					argsLerAulas);
			items.put("listaAulas", infoAulas);
						
			InfoLesson infoAula = (InfoLesson) infoAulas.get(0);
			items.put("infoAula", infoAula);

		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			fail("Using services at getItemsToPutInSessionForActionToBeTestedSuccessfuly()!");
		}
		
		return items;		
	}
	
	public void testSuccessfulExecutionOfAction() {
		prepareRequest();
		doTest(null, getItemsToPutInSessionForActionToBeTestedSuccessfuly(), getSuccessfulForward(), null, null, null, null);
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/web.xml";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "/sop";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/apagarAula";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "Sucesso";
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