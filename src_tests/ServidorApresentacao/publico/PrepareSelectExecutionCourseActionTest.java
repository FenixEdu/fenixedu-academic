package ServidorApresentacao.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class PrepareSelectExecutionCourseActionTest
	extends MockStrutsTestCase {

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

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public PrepareSelectExecutionCourseActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulPrepareSelectExecutionCourseAction() {
		// define mapping de origem
		setRequestPathInfo("publico", "/prepareSelectExecutionCourseAction");

		// coloca o contexto em sessão.
		CurricularYearAndSemesterAndInfoExecutionDegree ctx =
			new CurricularYearAndSemesterAndInfoExecutionDegree();
         ctx.setAnoCurricular(new Integer(1));
         ctx.setSemestre(new Integer(1));
         
         InfoExecutionDegree infoExecDegree = new InfoExecutionDegree();
          
		 InfoDegree infoDegree = new InfoDegree();
		 infoDegree.setNome("nomeqq");
		 infoDegree.setSigla("siglaqq");
		 
		 ctx.setInfoLicenciaturaExecucao(infoExecDegree);
		 getSession().setAttribute(SessionConstants.CONTEXT_KEY,ctx);
		 
		// invoca acção
		actionPerform();

		// verifica reencaminhamento
		verifyForward("sucess");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}

}