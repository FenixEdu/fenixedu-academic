package ServidorApresentacao.sop;
  
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.FormPropertyConfig;
import org.apache.struts.validator.DynaValidatorForm;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
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

		// Necessario para colocar form adicionarAulasForm em sessao
		setRequestPathInfo("/sop", "/adicionarAulasForm");

		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("LerSalas");
		privilegios.add("LerAulasDeDisciplinaExecucao");
		privilegios.add("LerDisciplinasExecucaoDeCursoExecucaoEAnoCurricular");		
		privilegios.add("AdicionarAula");
		privilegios.add("LerTurnosDeDisciplinaExecucao");
		IUserView userView = new UserView("user", privilegios);

		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		DynaActionForm manipularTurnosForm = null;
		
		FormBeanConfig formBeanConfig = new FormBeanConfig();
		formBeanConfig.addFormPropertyConfig(new FormPropertyConfig("indexTurno",Integer.class.getName(),null));
		formBeanConfig.setName("manipularTurnosForm");
		formBeanConfig.setType(DynaValidatorForm.class.getName());
		formBeanConfig.setDynamic(true);		
		try {
			manipularTurnosForm = (DynaActionForm) DynaActionFormClass.createDynaActionFormClass(formBeanConfig).newInstance();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			fail("AdicionarAulasFormActionTest.testSuccessfulAdicionarAulas");
		}
		manipularTurnosForm.set("indexTurno", new Integer(0));
		getSession().setAttribute("manipularTurnosForm", manipularTurnosForm);

		try {
			GestorServicos gestor = GestorServicos.manager();
			
			InfoExecutionCourse infoExecutionCourse =
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
			
			InfoRoom infoRoom = new InfoRoom("Ga2", 
				"Pavilhao Central", 
				new Integer(0),
				new TipoSala(TipoSala.ANFITEATRO),
				new Integer(100),
				new Integer(50));
			
			//create infoShift
			InfoShift infoShift = new InfoShift("turno454", 
				new TipoAula(TipoAula.TEORICA), new Integer(100), infoExecutionCourse); 

			//create infoLesson
			Calendar startTime = Calendar.getInstance();
			startTime.set(Calendar.HOUR_OF_DAY, 8);
			startTime.set(Calendar.MINUTE, 00);
			startTime.set(Calendar.SECOND, 00);

			Calendar endTime = Calendar.getInstance();
			endTime.set(Calendar.HOUR_OF_DAY, 9);
			endTime.set(Calendar.MINUTE, 30);
			endTime.set(Calendar.SECOND, 00);

			InfoLesson infoLesson = null;

			//create lessons			
			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = infoExecutionCourse;
			ArrayList infoLessons = (ArrayList) gestor.executar(userView, "LerAulasDeDisciplinaExecucao", argsLerAulas);
			getSession().setAttribute("infoAulasDeDisciplinaExecucao", infoLessons);

			infoLesson = (InfoLesson) infoLessons.get(0);
			getSession().setAttribute("infoAula", infoLesson);

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

		// verifica reencaminhamento
		verifyForward("Sucesso");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}
}
