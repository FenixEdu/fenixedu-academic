package ServidorApresentacao.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class PrepareSelectExecutionCourseActionTest
	extends TestCasePresentation {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(PrepareSelectExecutionCourseActionTest.class);

		return suite;
	}

	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuração Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-publico.xml");
	}

	public PrepareSelectExecutionCourseActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulPrepareSelectExecutionCourseAction() {
		// define mapping de origem
		setRequestPathInfo("publico", "/prepareSelectExecutionCourseAction");

		// coloca o contexto em sessão.
		//TODO put curricular year in session

		InfoExecutionDegree infoExecutionDegree =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan(
					"plano1",
					new InfoDegree(
						"LEIC",
						"Licenciatura de Engenharia Informatica e de Computadores")),
				new InfoExecutionYear("2002/2003"));

		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			infoExecutionDegree);

		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			infoExecutionPeriod);

		getSession().setAttribute(SessionConstants.CURRICULAR_YEAR_KEY,new Integer(2));
		// invoca acção
		actionPerform();

		// verifica reencaminhamento
		verifyForward("sucess");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}

}