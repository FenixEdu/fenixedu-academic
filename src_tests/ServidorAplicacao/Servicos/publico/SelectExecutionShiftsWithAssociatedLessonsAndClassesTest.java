package ServidorAplicacao.Servicos.publico;

import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISala;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DiaSemana;

/**
 * @author tfc130
 *
 */
public class SelectExecutionShiftsWithAssociatedLessonsAndClassesTest
	extends TestCaseServicos {

	private InfoExecutionCourse infoExecutionCourse = null;

	/**
	 * Constructor for SelectClassesTest.
	 */
	public SelectExecutionShiftsWithAssociatedLessonsAndClassesTest(
		java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(
				SelectExecutionShiftsWithAssociatedLessonsAndClassesTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {

		Object argsSelectExecutionShiftsWithAssociatedLessonsAndClasses[] =
			new Object[1];

		Object result = null;

		//execution course with 10 shifts with 1st shift with 1 associated lessons and 1 associated classes
		this.prepareTestCase(1);

		argsSelectExecutionShiftsWithAssociatedLessonsAndClasses[0] =
			this.infoExecutionCourse;

		try {
			result =
				_gestor.executar(
					_userView,
					"SelectExecutionShiftsWithAssociatedLessonsAndClasses",
					argsSelectExecutionShiftsWithAssociatedLessonsAndClasses);
			assertEquals(
				"testReadAll: 10 shift in db with 1st shift with 1 lesson and 1 class",
				10,
				((List) result).size());
			assertEquals(
				"testReadAll: 1 lessons of 1st shift of executionCourse",
				1,
				(
					(InfoShiftWithAssociatedInfoClassesAndInfoLessons)
						((List) result)
					.get(0))
					.getInfoLessons()
					.size());
			assertEquals(
				"testReadAll: 1 classes of 1st shift of executionCourse",
				1,
				(
					(InfoShiftWithAssociatedInfoClassesAndInfoLessons)
						((List) result)
					.get(0))
					.getInfoClasses()
					.size());
		} catch (Exception ex) {
			fail("testReadAll: 1 shift in db with 1 lesson and 1 class: " + ex);
		}

		//10 shifts in database with no associated lessons and no associated classes 
		this.prepareTestCase(2);
		try {
			result =
				_gestor.executar(
					_userView,
					"SelectExecutionShiftsWithAssociatedLessonsAndClasses",
					argsSelectExecutionShiftsWithAssociatedLessonsAndClasses);
			assertEquals(
				"testReadAll: 1 shift in db with no lessons and no classes",
				10,
				((List) result).size());
			assertEquals(
				"testReadAll: 0 lessons of shift in db",
				0,
				(
					(InfoShiftWithAssociatedInfoClassesAndInfoLessons)
						((List) result)
					.get(0))
					.getInfoLessons()
					.size());
			assertEquals(
				"testReadAll: 0 classes of shift in db",
				0,
				(
					(InfoShiftWithAssociatedInfoClassesAndInfoLessons)
						((List) result)
					.get(0))
					.getInfoClasses()
					.size());
		} catch (Exception ex) {
			fail("testReadAll: 1 shift in db with no lessons and no classes");
		}

		// Empty database - no shifts of selected executionCourse
		this.prepareTestCase(3);
		try {
			result =
				_gestor.executar(
					_userView,
					"SelectExecutionShiftsWithAssociatedLessonsAndClasses",
					argsSelectExecutionShiftsWithAssociatedLessonsAndClasses);
			assertEquals(
				"testReadAll: no shiftsWithAssociatedLessonsAndClasses to read aqui",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail(
				"testReadAll: no shiftsWithAssociatedLessonsAndClasses to read: "
					+ ex);
		}
	}

	private void prepareTestCase(int testCase) {

		ISuportePersistente sp = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			ICursoPersistente cursoPersistente = sp.getICursoPersistente();
			ICurso degree = cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);

			IPersistentDegreeCurricularPlan planoCurricularCursoPersistente =
				sp.getIPersistentDegreeCurricularPlan();
			IDegreeCurricularPlan degreeCurricularPlan =
				planoCurricularCursoPersistente.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);

			IPersistentExecutionYear persistenExecutionYear =
				sp.getIPersistentExecutionYear();
			IExecutionYear executionYear =
				persistenExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);

			ICursoExecucaoPersistente cursoExecucaoPersistente =
				sp.getICursoExecucaoPersistente();
			ICursoExecucao executionDegree =
				cursoExecucaoPersistente
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);

			IPersistentExecutionPeriod persistentExecutionPeriod =
				sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(executionPeriod);

			IDisciplinaExecucaoPersistente disciplinaExecucaoPersistente =
				sp.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				disciplinaExecucaoPersistente
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			assertNotNull(executionCourse);

			this.infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);

			ITurnoPersistente turnoPersistente = sp.getITurnoPersistente();
//			ITurno shift =
//				new Turno(
//					"turno",
//					new TipoAula(TipoAula.TEORICA),
//					new Integer(100),
//					executionCourse);

			ISalaPersistente salaPersistente = sp.getISalaPersistente();
			ISala room1 = salaPersistente.readByName("Ga1");
			assertNotNull(room1);

			IAulaPersistente aulaPersistente = sp.getIAulaPersistente();
			DiaSemana diaSemana = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
			Calendar inicio = Calendar.getInstance();
			inicio.set(Calendar.HOUR_OF_DAY, 8);
			inicio.set(Calendar.MINUTE, 0);
			inicio.set(Calendar.SECOND, 0);
			Calendar fim = Calendar.getInstance();
			fim.set(Calendar.HOUR_OF_DAY, 9);
			fim.set(Calendar.MINUTE, 30);
			fim.set(Calendar.SECOND, 0);

			IAula lesson =
				aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(
					diaSemana,
					inicio,
					fim,
					room1,
					executionPeriod);
			assertNotNull(lesson);

			ITurnoAulaPersistente turnoAulaPersistente =
				sp.getITurnoAulaPersistente();

			ITurmaTurnoPersistente turmaTurnoPersistente =
				sp.getITurmaTurnoPersistente();

	//		ITurmaPersistente turmaPersistente = sp.getITurmaPersistente();
//			ITurma class0 =
//				turmaPersistente
//					.readByNameAndExecutionDegreeAndExecutionPeriod(
//					"turmaParaTestarInscricoesDeAlunos2",
//					executionDegree,
//					executionPeriod);

			if (testCase == 2) {
				//System.out.println("###### Turnos sem aulas e sem turmas associadas");
				turnoAulaPersistente.deleteAll();
				turmaTurnoPersistente.deleteAll();
			}

			if (testCase == 3) {
				//System.out.println("###### Nao tem turnos");
				turnoPersistente.deleteAll();
			}
			sp.confirmarTransaccao();

		} catch (ExcepcaoPersistencia excepcao) {
			try {
				sp.cancelarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("ligarSuportePersistente: cancelarTransaccao: " + ex);
			}
			fail("ligarSuportePersistente: confirmarTransaccao: " + excepcao);
		}
	}

}
