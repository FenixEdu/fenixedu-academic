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
public class ViewClassesFormActionTest extends TestCasePresentation {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ViewClassesFormActionTest.class);

		return suite;
	}

	public void setUp() {
		super.setUp();
		// define ficheiro de configuração Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-publico.xml");
	}

	public ViewClassesFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulViewShiftsFormAction() {
		// define mapping de origem
		setRequestPathInfo("publico", "/viewClasses");

		// coloca o contexto em sessão.
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003"));

		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			infoExecutionPeriod);

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

		Integer curricularYear = new Integer(2);
		getSession().setAttribute(
			SessionConstants.CURRICULAR_YEAR_KEY,
			curricularYear);

		// invoca acção
		actionPerform();

		// verifica reencaminhamento
		verifyForward("Sucess");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}

}