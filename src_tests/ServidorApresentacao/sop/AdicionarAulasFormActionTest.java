package ServidorApresentacao.sop;
  
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoAula;
import Util.TipoSala;

/**
 * AdicionarAulasFormActionTest.java
 * 
 * @author Ivo Brandão
 */
public class AdicionarAulasFormActionTest extends TestCasePresentation {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(AdicionarAulasFormActionTest.class);

		return suite;
	}
	
	public void setUp() throws Exception {
		super.setUp();
		
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");
	}

	public AdicionarAulasFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulAdicionarAulas() {

		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("VerTurno");
		privilegios.add("LerAlunosDeTurno");
		privilegios.add("LerTurnosDeDisciplinaExecucao");

		// Necessario para colocar form manipularTurnosForm em sessao
		setRequestPathInfo("/sop", "/manipularTurnosForm");
		addRequestParameter("indexTurno", new Integer(0).toString());
		actionPerform();

		// coloca credenciais na sessao
		privilegios = new HashSet();
		privilegios.add("LerSalas");
		privilegios.add("LerAulasDeDisciplinaExecucao");
		privilegios.add("LerDisciplinasExecucaoDeCursoExecucaoEAnoCurricular");
		privilegios.add("AdicionarAula");
		privilegios.add("LerTurnosDeDisciplinaExecucao");
		IUserView userView = new UserView("user", privilegios);

		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		// define mapping de origem
		setRequestPathInfo("/sop", "/adicionarAulasForm");

		try {
			GestorServicos gestor = GestorServicos.manager();

			InfoExecutionCourse infoExecutionCourse =
				new InfoExecutionCourse(
					"Trabalho Final de Curso II",
					"TFCII",
					"programa1",
					new Double(1.5),
					new Double(2),
					new Double(1.5),
					new Double(2),
					new InfoExecutionPeriod(
						"2º Semestre",
						new InfoExecutionYear("2002/2003")));
			
			InfoRoom infoRoom = new InfoRoom("Ga2", 
				"Pavilhao Central", 
				new Integer(0),
				new TipoSala(TipoSala.ANFITEATRO),
				new Integer(100),
				new Integer(50));
			
			//create infoShift
			InfoShift infoShift = new InfoShift("turno_adicionar_aula", 
				new TipoAula(TipoAula.TEORICA), new Integer(100), infoExecutionCourse); 

			//create lessons			
			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = infoExecutionCourse;
			ArrayList infoLessons = (ArrayList) gestor.executar(userView, "LerAulasDeDisciplinaExecucao", argsLerAulas);
			getSession().setAttribute("infoAulasDeDisciplinaExecucao", infoLessons);

			//create shifts
			Object argsLerTurnos[] = new Object[1];
			argsLerTurnos[0] = infoExecutionCourse;
			ArrayList infoShifts = (ArrayList) gestor.executar(userView, "LerTurnosDeDisciplinaExecucao", argsLerTurnos);
			getSession().setAttribute("infoTurnosDeDisciplinaExecucao", infoShifts);

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
}
