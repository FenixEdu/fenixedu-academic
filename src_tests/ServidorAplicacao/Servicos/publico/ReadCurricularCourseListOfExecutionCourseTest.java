package ServidorAplicacao.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author tfc130
 *
 */
public class ReadCurricularCourseListOfExecutionCourseTest
	extends TestCaseServicos {

	private InfoExecutionCourse infoExecutionCourse = null;

	/**
	 * Constructor for SelectClassesTest.
	 */
	public ReadCurricularCourseListOfExecutionCourseTest(
		java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(
				ReadCurricularCourseListOfExecutionCourseTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {

		Object argsReadCurricularCourseListOfExecutionCourse[] =
			new Object[1];
		Object result = null;
		//execution course with 1 curricularCourses associated
		this.prepareTestCase(true);
		argsReadCurricularCourseListOfExecutionCourse[0] =
			this.infoExecutionCourse;
			
		System.out.println(infoExecutionCourse);
		try {
			result =
				_gestor.executar(
					_userView,
					"ReadCurricularCourseListOfExecutionCourse",
					argsReadCurricularCourseListOfExecutionCourse);
			assertEquals(
				"testReadAll: 1 curricularCourses of executionCourse",
				1,
				((List) result).size());

		} catch (Exception ex) {
			fail("testReadAll: executionCourse with 1 curricularCourses: " + ex);
		}
		
		// Empty database - no curricularCourses of selected executionCourse
		this.prepareTestCase(false);
		try {
			result =
				_gestor.executar(
					_userView,
					"ReadCurricularCourseListOfExecutionCourse",
					argsReadCurricularCourseListOfExecutionCourse);
			assertEquals(
				"testReadAll: no curricularCourses of executionCourse",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: no curricularCourses of executionCourse: " + ex);
		}
	}

	private void prepareTestCase(
		boolean hasCurricularCourses) {
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
			IPersistentExecutionCourse disciplinaExecucaoPersistente =
				sp.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse =
				disciplinaExecucaoPersistente
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			assertNotNull(executionCourse);
			
			if (!hasCurricularCourses) {
				PersistenceBroker pb = PersistenceBrokerFactory.defaultPersistenceBroker();
				sp.getIPersistentCurricularCourse().deleteAll();
				pb.removeFromCache(executionCourse);
				//executionCourse.setAssociatedCurricularCourses(null);
			}
			
			this.infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			try {
				sp.cancelarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("ligarSuportePersistente: cancelarTransaccao: " + ex);
			}
			System.out.println("44");
			fail("ligarSuportePersistente: confirmarTransaccao: " + excepcao);
		}
	}

}
