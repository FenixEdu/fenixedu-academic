package ServidorApresentacao.sop;
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import Util.DiaSemana;
import Util.TipoAula;
/**
@author tfc130
*/
public class EditarAulaFormActionTest extends TestCasePresentation {
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EditarAulaFormActionTest.class);

		return suite;
	}
	public void setUp() {
		super.setUp();
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");

	}

	public EditarAulaFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulEditarAula() {
		// define mapping de origem
		setRequestPathInfo("", "/editarAulaForm");
		
		// Preenche campos do formulario
		addRequestParameter(
			"diaSemana",
			new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter("horaInicio",(new Integer(8)).toString());
		addRequestParameter("minutosInicio",(new Integer(0)).toString());
		addRequestParameter(
			"horaFim",
			new Integer(9).toString());
		addRequestParameter(
			"minutosFim",
			new Integer(30).toString());
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter(
			"courseInitials",
			"TFCI");
			
		addRequestParameter("nomeSala", "Ga3");

		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("EditarAula");
		privilegios.add("LerAulasDeDisciplinaExecucao");
		privilegios.add(
			"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);
		try {
			GestorServicos gestor = GestorServicos.manager();
			InfoExecutionCourse iDE = new InfoExecutionCourse(
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
				
			getSession().setAttribute("infoDisciplinaExecucao",iDE);

			
			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = iDE;
			ArrayList infoAulas =
				(ArrayList) gestor.executar(
					userView,
					"LerAulasDeDisciplinaExecucao",
					argsLerAulas);
			getSession().setAttribute("listaAulas", infoAulas);
			InfoLesson infoAula = (InfoLesson) infoAulas.get(0);
			getSession().setAttribute("infoAula", infoAula);

		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}

		// invoca acção
		actionPerform();

		//verifica ausencia de erros
		verifyNoActionErrors();

		// verifica reencaminhamento
		verifyForward("Sucesso");
	}
	public void testUnsuccessfulEditarAula() {
		setRequestPathInfo("", "/editarAulaForm");
		addRequestParameter(
			"diaSemana",
			new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter("horaInicio",(new Integer(8)).toString());
		addRequestParameter("minutosInicio",(new Integer(0)).toString());
		addRequestParameter(
			"horaFim",
			new Integer(9).toString());
		addRequestParameter(
			"minutosFim",
			new Integer(30).toString());
		addRequestParameter("tipoAula",	(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials","TFCI");
		addRequestParameter("nomeSala", "Ga2");
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("EditarAula");
		privilegios.add("LerAulasDeDisciplinaExecucao");
		privilegios.add(
			"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);
		
		try {
			GestorServicos gestor = GestorServicos.manager();
			InfoExecutionCourse iDE = new InfoExecutionCourse(
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
				
			getSession().setAttribute("infoDisciplinaExecucao",iDE);

			
			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = iDE;
			ArrayList infoAulas =
				(ArrayList) gestor.executar(
					userView,
					"LerAulasDeDisciplinaExecucao",
					argsLerAulas);
			getSession().setAttribute("listaAulas", infoAulas);
			InfoLesson infoAula = (InfoLesson) infoAulas.get(0);
			getSession().setAttribute("infoAula", infoAula);

		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}
		actionPerform();
		verifyForwardPath("/naoExecutado.do");

		verifyActionErrors(
			new String[] { "ServidorAplicacao.FenixServiceException" });
	}

}