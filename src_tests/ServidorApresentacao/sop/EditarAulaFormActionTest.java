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
import Util.DiaSemana;
import Util.TipoAula;
/**
@author tfc130
*/
public class EditarAulaFormActionTest extends TestCasePresentationSopPortal {
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EditarAulaFormActionTest.class);

		return suite;
	}
	public void setUp() {
		super.setUp();

	}

	public EditarAulaFormActionTest(String testName) {
		super(testName);
	}


	private void prepareSuccessfulRequest() {
		
		
		// Preenche campos do formulario
		addRequestParameter("diaSemana",new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter("horaInicio","20");
		addRequestParameter("minutosInicio","0");
		addRequestParameter("horaFim","21");
		addRequestParameter("minutosFim","30");
		addRequestParameter("tipoAula",	(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials","TFCI");
		addRequestParameter("nomeSala", "GA1");
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
				

			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = iDE;
			ArrayList infoAulas =
				(ArrayList) gestor.executar(
					userView,
					"LerAulasDeDisciplinaExecucao",
					argsLerAulas);
			items.put("listaAulas", infoAulas);
			InfoLesson infoAula = (InfoLesson) infoAulas.get(0);
			items.put("infoAula", infoAula);
	
		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}
			
		return items;		
	}

	public void testSuccessfulExecutionOfAction() {
		prepareSuccessfulRequest();		
		doTest(null, getItemsToPutInSessionForActionToBeTestedSuccessfuly(), getSuccessfulForward(), null, null, null, null);
	}


	private void prepareUnsuccessfulRequest_ExistingLesson() {
		// Preenche campos do formulario
		addRequestParameter("diaSemana",new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter("horaInicio","8");
		addRequestParameter("minutosInicio","00");
		addRequestParameter("horaFim","9");
		addRequestParameter("minutosFim","30");
		addRequestParameter("tipoAula",	(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials","TFCI");
		addRequestParameter("nomeSala", "GA2");
	}

	private void prepareUnsuccessfulRequest_InterceptingLesson() {
		// Preenche campos do formulario
		addRequestParameter("diaSemana",new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter("horaInicio","8");
		addRequestParameter("minutosInicio","30");
		addRequestParameter("horaFim","9");
		addRequestParameter("minutosFim","0");
		addRequestParameter("tipoAula",	(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials","TFCI");
		addRequestParameter("nomeSala", "GA2");
	}

	private void prepareUnsuccessfulRequest_InvalidTimeInterval() {
		// Preenche campos do formulario
		addRequestParameter("diaSemana",new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter("horaInicio","21");
		addRequestParameter("minutosInicio","0");
		addRequestParameter("horaFim","20");
		addRequestParameter("minutosFim","0");
		addRequestParameter("tipoAula",	(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials","TFCI");
		addRequestParameter("nomeSala", "GA1");
	}

	public void testUnsuccessfulExecutionOfActionExistingLesson() {
		prepareUnsuccessfulRequest_ExistingLesson();
		doTest(null, getItemsToPutInSessionForActionToBeTestedSuccessfuly(), null, getUnsuccessfulForwardPath(), null, null, getActionErrors_ExistingLesson());
	}

	public void testUnsuccessfulExecutionOfActionInterceptingLesson() {
		prepareUnsuccessfulRequest_InterceptingLesson();
		doTest(null, getItemsToPutInSessionForActionToBeTestedSuccessfuly(), null, getUnsuccessfulForwardPath(), null, null, getActionErrors_InterceptingLesson());				
	}

	public void testUnsuccessfulExecutionOfAction() {
		prepareUnsuccessfulRequest_InvalidTimeInterval();
		doTest(null, getItemsToPutInSessionForActionToBeTestedSuccessfuly(), null, getUnsuccessfulForwardPath(), null, null, getActionErrors_InvalidTimeInterval());				
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/tests/web-sop.xml";
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
		return "/editarAulaForm";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "Sucesso";
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getUnsuccessfulForwardPath()
	 */
	protected String getUnsuccessfulForwardPath() {
		return "/editarAula.jsp";
	}

	
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getActionErrors()
	 * 
	 */
	protected String[] getActionErrors_ExistingLesson() {
		return new String[] {"error.exception.existing"};
	}
	
	protected String[] getActionErrors_InterceptingLesson() {
		return new String[] {"error.exception.intercepting.lesson"};
	}

	protected String[] getActionErrors_InvalidTimeInterval() {
		return new String[] {"errors.lesson.invalid.time.interval"};
	}

	
}