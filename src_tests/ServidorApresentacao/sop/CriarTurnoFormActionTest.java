package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoAula;

/**
 * @author tfc130
 */
public class CriarTurnoFormActionTest extends TestCasePresentationSopPortal {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CriarTurnoFormActionTest.class);

		return suite;
	}

	protected String getServletConfigFile() {
		return "/WEB-INF/web.xml";
	}
	public CriarTurnoFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulCriarTurno() {

		// define mapping de origem
		setRequestPathInfo("sop", "/criarTurnoForm");

		// Preenche campos do formulario
		addRequestParameter("nome", "turnoNovoxpto");
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials", "TFCI");
		addRequestParameter("lotacao", (new Integer(100).toString()));

		// coloca credenciais na sessao
		setAuthorizedUser();
		try {
			InfoDegree iL =
				new InfoDegree(
					"LEIC",
					"Licenciatura de Engenharia Informatica e de Computadores");
			InfoExecutionDegree iLE =
				new InfoExecutionDegree(
					new InfoDegreeCurricularPlan("plano1", iL),
					new InfoExecutionYear("2002/2003"));

			Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] =
				{
					 new CurricularYearAndSemesterAndInfoExecutionDegree(
						new Integer(5),
						new Integer(1),
						iLE)};
			getSession().setAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY,
				iLE);

			InfoExecutionPeriod executionPeriod =
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003"));
			getSession().setAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY,
				executionPeriod);

			getSession().setAttribute(
				SessionConstants.CURRICULAR_YEAR_KEY,
				new Integer(2));

			GestorServicos gestor = GestorServicos.manager();
			ArrayList iDE =
				(ArrayList) gestor.executar(
					getAuthorizedUser(),
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);
			getSession().setAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY,
				iDE);
		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}

		// invoca acaoo
		actionPerform();

		// verifica reencaminhamento
		verifyForward("Sucesso");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}

	public void testUnsuccessfulCriarTurno() {

		setRequestPathInfo("sop", "/criarTurnoForm");
		addRequestParameter("nome", "turno1");
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials", "TFCI");
		addRequestParameter("lotacao", (new Integer(100).toString()));

		// coloca credenciais na sessao
		setAuthorizedUser();
		try {
			InfoDegree iL =
				new InfoDegree(
					"LEIC",
					"Licenciatura de Engenharia Informatica e de Computadores");
			InfoExecutionDegree iLE =
				new InfoExecutionDegree(
					new InfoDegreeCurricularPlan("plano1", iL),
					new InfoExecutionYear("2002/2003"));

			Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] =
				{
					 new CurricularYearAndSemesterAndInfoExecutionDegree(
						new Integer(5),
						new Integer(1),
						iLE)};
			getSession().setAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY,
				iLE);

			InfoExecutionPeriod executionPeriod =
				new InfoExecutionPeriod(
					"2º Semestre",
					new InfoExecutionYear("2002/2003"));
			getSession().setAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY,
				executionPeriod);

			getSession().setAttribute(
				SessionConstants.CURRICULAR_YEAR_KEY,
				new Integer(2));

			GestorServicos gestor = GestorServicos.manager();
			ArrayList iDE =
				(ArrayList) gestor.executar(
					getAuthorizedUser(),
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);
			getSession().setAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY,
				iDE);
		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}

		actionPerform();
		verifyInputForward();

		verifyActionErrors(new String[] { "error.exception.existing" });
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