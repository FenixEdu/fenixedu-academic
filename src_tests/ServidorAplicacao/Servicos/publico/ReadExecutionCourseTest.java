package ServidorAplicacao.Servicos.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
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
public class ReadExecutionCourseTest
	extends TestCaseServicos {

	private InfoExecutionCourse infoExecutionCourse = null;
	private InfoExecutionPeriod infoExecutionPeriod = null;
	private String code = null;
	
	/**
	 * Constructor for SelectClassesTest.
	 */
	public ReadExecutionCourseTest(
		java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(
				ReadExecutionCourseTest.class);

		return suite;
	}
	
	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {

		Object argsReadExecutionCourse[] = new Object[2];

		Object result = null;

		
		// execution course exists in database
		this.prepareTestCase(true);

		argsReadExecutionCourse[0] = this.infoExecutionPeriod;
		argsReadExecutionCourse[1] = this.code;

		try {
			result =
				_gestor.executar(
					_userView,
					"ReadExecutionCourse",
					argsReadExecutionCourse);
			assertTrue(((InfoExecutionCourse)result).equals(infoExecutionCourse));

		} catch (Exception ex) {
			fail("testReadAll: executionCourse with 1 curricularCourses: " + ex);
		}

		// executionCourse does not exist in database
		this.prepareTestCase(false);
		try {
			result =
				_gestor.executar(
					_userView,
					"ReadExecutionCourse",
					argsReadExecutionCourse);
			assertNull(
				"testReadAll: executionCourse does not exist in db",
				result);
		} catch (Exception ex) {
			fail("testReadAll: no curricularCourses of executionCourse: " + ex);
		}
	
	}

	private void prepareTestCase(
		boolean exists) {

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

			this.infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
			
			if (!exists)
				disciplinaExecucaoPersistente.deleteExecutionCourse(executionCourse);

			this.infoExecutionPeriod =
				Cloner.copyIExecutionPeriod2InfoExecutionPeriod(executionPeriod);
			this.code = executionCourse.getSigla();

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
