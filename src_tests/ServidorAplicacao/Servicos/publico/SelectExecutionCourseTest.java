package ServidorAplicacao.Servicos.publico;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author jmota
 *
 */
public class SelectExecutionCourseTest extends TestCaseServicos {

	
	private InfoDegree infoDegree = null;
	/**
	 * Constructor for SelectClassesTest.
	 */
	public SelectExecutionCourseTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SelectExecutionCourseTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	public void testReadAll() {

		Object argsSelectExecutionCourses[] = new Object[3];
		Object result = null;

		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		ICursoExecucao executionDegree = null;
		ICurso degree = null;

		try { 
			_suportePersistente.iniciarTransaccao();
			// Insert two new Classes

			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			degreeCurricularPlan = _persistentDegreeCurricularPlan.readByName("plano1");
			assertNotNull(degreeCurricularPlan);
			degree = _cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);			
			executionDegree = _cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);
			
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Read All Failure");
		}
		
		
		// Create the arguments
		InfoExecutionDegree infoExecutionDegree = Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree);
		InfoExecutionPeriod infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(executionPeriod);
		Integer curricularYear = new Integer(1);
		
		argsSelectExecutionCourses[0] = infoExecutionDegree;
		argsSelectExecutionCourses[1] = infoExecutionPeriod; 
		argsSelectExecutionCourses[2] = curricularYear;  


		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_disciplinaExecucaoPersistente.apagarTodasAsDisciplinasExecucao();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectExecutionCourse", argsSelectExecutionCourses);
			assertEquals(
				"testReadAll: no classes to read aqui",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: no classes to read");
		}

		//2 classes in database
		try { 
			_suportePersistente.iniciarTransaccao();
			// Insert two new Classes
			IDisciplinaExecucao executionCourse1 = new DisciplinaExecucao("Disc1", "sigla1", "programa1", new Double(0),new Double(0),new Double(0),new Double(0), executionPeriod);
			IDisciplinaExecucao executionCourse2 = new DisciplinaExecucao("Disc2", "sigla2", "programa2", new Double(0),new Double(0),new Double(0),new Double(0), executionPeriod);
			
			ICurricularCourse curricularCourse = _disciplinaCurricularPersistente.readCurricularCourseByNameCode("Arquitecturas de Computadores","AC");
	
			List associatedCurricularCourses = new ArrayList();
			associatedCurricularCourses.add(curricularCourse);
			
			executionCourse1.setAssociatedCurricularCourses(associatedCurricularCourses);

			_disciplinaExecucaoPersistente.escreverDisciplinaExecucao(executionCourse1);
			_disciplinaExecucaoPersistente.escreverDisciplinaExecucao(executionCourse2);

			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Read All Failure");
		}
	
		try {
			result =
				_gestor.executar(_userView, "SelectExecutionCourse", argsSelectExecutionCourses);
			assertEquals(
				"testReadAll: 1 classes in db",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: 1 classes in db");
		}
	}

}
