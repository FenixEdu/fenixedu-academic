package ServidorAplicacao.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author jmota
 *
 */
public class SelectClassesTest extends TestCaseServicos {

	private InfoDegree infoDegree = null;
	/**
	 * Constructor for SelectClassesTest.
	 */
	public SelectClassesTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SelectClassesTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {

		Object argsSelectClasses[] = new Object[1];
		argsSelectClasses[0] = new InfoClass();
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
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

			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			IExecutionPeriod executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(executionPeriod);

			ICurso degree = _cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);
			IPlanoCurricularCurso degreeCurricularPlan =
				_persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);
			ICursoExecucao executionDegree =
				_cursoExecucaoPersistente
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);
			ITurma class1 =
				new Turma(
					"turma1",
					new Integer(2),
					executionDegree,
					executionPeriod);
			ITurma class2 =
				new Turma(
					"turma3",
					new Integer(3),
					executionDegree,
					executionPeriod);
			_turmaPersistente.lockWrite(class1);
			_turmaPersistente.lockWrite(class2);

			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Read All Failure");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadAll: 2 classes in db",
				2,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: 2 classes in db");
		}
	}

	public void testReadByName() {
		Object argsSelectClasses[] = new Object[1];
		InfoClass infoClass = new InfoClass();
		infoClass.setNome("turma1");
		argsSelectClasses[0] = infoClass;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByName: no classes to read",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByName: no classes to read");
		}

		//2 classes in database
		try {
			_suportePersistente.iniciarTransaccao();
			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			IExecutionPeriod executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(executionPeriod);

			ICurso degree = _cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);
			IPlanoCurricularCurso degreeCurricularPlan =
				_persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);
			ICursoExecucao executionDegree =
				_cursoExecucaoPersistente
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);
			ITurma class1 =
				new Turma(
					"turma1",
					new Integer(2),
					executionDegree,
					executionPeriod);
			ITurma class2 =
				new Turma(
					"turma3",
					new Integer(3),
					executionDegree,
					executionPeriod);
			_turmaPersistente.lockWrite(class1);
			_turmaPersistente.lockWrite(class2);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByName: 2 classes in db",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByName: 2 classes in db");
		}
	}

	public void testReadBySemester() {
		Object argsSelectClasses[] = new Object[1];
		InfoClass infoClass = new InfoClass();

		ITurma class1 = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		ICursoExecucao executionDegree = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(executionPeriod);

			degree = _cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);
			degreeCurricularPlan =
				_persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);
			executionDegree =
				_cursoExecucaoPersistente
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);
			class1 =
				_turmaPersistente
					.readByNameAndExecutionDegreeAndExecutionPeriod(
					"10501",
					executionDegree,
					executionPeriod);
			assertNotNull(class1);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}

		infoClass.setSemestre(class1.getSemestre());
		argsSelectClasses[0] = infoClass;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadBySemester: no classes to read",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadBySemester: no classes to read");
		}

		//2 classes in database
		ITurma classTemp1 = null;
		ITurma classTemp2 = null;

		try {
			_suportePersistente.iniciarTransaccao();
			classTemp1 =
				new Turma(
					"turma1",
					new Integer(2),
					executionDegree,
					executionPeriod);
			classTemp2 =
				new Turma(
					"turma3",
					new Integer(3),
					executionDegree,
					executionPeriod);

			classTemp1.setSemestre(class1.getSemestre());
			classTemp2.setSemestre(class1.getSemestre());

			_turmaPersistente.lockWrite(classTemp1);
			_turmaPersistente.lockWrite(classTemp2);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadBySemester: 2 classes in db",
				2,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadBySemester: 2 classes in db");
		}
	}

	public void testReadByCurricularYear() {
		Object argsSelectClasses[] = new Object[1];
		InfoClass infoClass = new InfoClass();

		ITurma class1 = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		ICursoExecucao executionDegree = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(executionPeriod);

			degree = _cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);
			degreeCurricularPlan =
				_persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);
			executionDegree =
				_cursoExecucaoPersistente
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);
			class1 =
				_turmaPersistente
					.readByNameAndExecutionDegreeAndExecutionPeriod(
					"10501",
					executionDegree,
					executionPeriod);
			assertNotNull(class1);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}

		infoClass.setAnoCurricular(class1.getAnoCurricular());
		argsSelectClasses[0] = infoClass;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByCurricularYear: no classes to read",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByCurricularYear: no classes to read");
		}

		//2 classes in database
		ITurma classTemp1 = null;
		ITurma classTemp2 = null;

		try {
			_suportePersistente.iniciarTransaccao();
			classTemp1 =
				new Turma(
					"turma1",
					new Integer(2),
					executionDegree,
					executionPeriod);
			classTemp2 =
				new Turma(
					"turma3",
					new Integer(3),
					executionDegree,
					executionPeriod);

			_turmaPersistente.lockWrite(classTemp1);
			_turmaPersistente.lockWrite(classTemp2);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByCurricularYear: 2 classes in db",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByCurricularYear: 2 classes in db");
		}
	}

	public void testReadByDegree() {
		Object argsSelectClasses[] = new Object[1];
		InfoDegree infoDegree = new InfoDegree();

		ITurma class1 = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		ICursoExecucao executionDegree = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(executionPeriod);

			degree = _cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);
			degreeCurricularPlan =
				_persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);
			executionDegree =
				_cursoExecucaoPersistente
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);
			class1 =
				_turmaPersistente
					.readByNameAndExecutionDegreeAndExecutionPeriod(
					"10501",
					executionDegree,
					executionPeriod);
			assertNotNull(class1);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}

		infoDegree.setNome(degree.getNome());
		infoDegree.setSigla(degree.getSigla());

		InfoClass infoClass = new InfoClass();
		infoClass.setInfoLicenciatura(infoDegree);
		argsSelectClasses[0] = infoClass;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByDegree: no classes to read",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByDegree: no classes to read");
		}

		//2 classes in database
		ITurma classTemp1 = null;
		ITurma classTemp2 = null;

		try {
			_suportePersistente.iniciarTransaccao();
			classTemp1 =
				new Turma(
					"turma1",
					new Integer(2),
					executionDegree,
					executionPeriod);
			classTemp2 =
				new Turma(
					"turma3",
					new Integer(3),
					executionDegree,
					executionPeriod);

			_turmaPersistente.lockWrite(classTemp1);
			_turmaPersistente.lockWrite(classTemp2);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByDegree: 2 classes in db",
				2,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByDegree: 2 classes in db");
		}
	}

}
