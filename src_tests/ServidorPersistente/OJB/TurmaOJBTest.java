/*
 * TurmaOJBTest.java
 * JUnit based test
 *
 * Created on 15 de Outubro de 2002, 8:42
 */

package ServidorPersistente.OJB;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Implementation;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import Dominio.ITurma;
import Dominio.Turma;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;

public class TurmaOJBTest extends TestCaseOJB {
	public TurmaOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(TurmaOJBTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testReadByNome() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNotNull(classTemp);

			
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
		assertEquals(classTemp.getNome(), "10501");

		// read unexisting Turma
		try {
			_suportePersistente.iniciarTransaccao();
			classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("desc", executionDegree, executionPeriod);
			assertNull(classTemp);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read unexisting turma");
		}
	}

	// write new existing turma
	public void testCreateExistingTurma() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = new Turma("10501", new Integer(1), executionDegree, executionPeriod);

			_turmaPersistente.lockWrite(classTemp);
			_suportePersistente.confirmarTransaccao();
			fail("testCreateExistingTurma");
		} catch (ExcepcaoPersistencia ex) {
			//all is ok
		}
	}

	// write new non-existing turma
	public void testCreateNonExistingTurma() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = null;
			classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10510", executionDegree, executionPeriod);
			assertNull(classTemp);
			_suportePersistente.confirmarTransaccao();

			classTemp = new Turma("10510", new Integer(1), executionDegree, executionPeriod);

			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.lockWrite(classTemp);
			_suportePersistente.confirmarTransaccao();
			
			_suportePersistente.iniciarTransaccao();
			classTemp = null;
			classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10510", executionDegree, executionPeriod);
			assertNotNull(classTemp);
			_suportePersistente.confirmarTransaccao();

			
		} catch (ExcepcaoPersistencia ex) {
			fail("testCreateNonExistingTurma");
		}
	}

	/** Test of write method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testWriteExistingChangedObject() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNotNull(classTemp);

			classTemp.setNome("10510");
			_suportePersistente.confirmarTransaccao();



			_suportePersistente.iniciarTransaccao();

			classTemp = null;
			classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNull(classTemp);

			classTemp = null;
			classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10510", executionDegree, executionPeriod);
			assertNotNull(classTemp);
			assertEquals(classTemp.getNome(), "10510");

			_suportePersistente.confirmarTransaccao();


		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}

	/** Test of delete method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testDeleteTurma() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
			
		Implementation odmg = null;
		OQLQuery query = null;
		String oqlQuery = null;;
		List result = null;
		TurmaTurnoOJB turmaTurnoOJB = null;
		try {
			_suportePersistente.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNotNull(classTemp);
			
			
			// Checks existing shifts

			odmg = OJB.getInstance();
			query = odmg.newOQLQuery();

			try {			
				turmaTurnoOJB = new TurmaTurnoOJB();
				oqlQuery = "select all from " + TurmaTurno.class.getName();
				oqlQuery += " where turma.nome = $1 ";
				oqlQuery += " and turma.executionPeriod.name = $2 ";
				oqlQuery += " and turma.executionPeriod.executionYear.year = $3 ";
				oqlQuery += " and turma.executionDegree.executionYear.year = $4 ";
				oqlQuery += " and turma.executionDegree.curricularPlan.name = $5 ";
				oqlQuery += " and turma.executionDegree.curricularPlan.curso.sigla = $6 ";
	
				query.create(oqlQuery);
				query.bind(classTemp.getNome());
				query.bind(classTemp.getExecutionPeriod().getName());
				query.bind(classTemp.getExecutionPeriod().getExecutionYear().getYear());
				query.bind(classTemp.getExecutionDegree().getExecutionYear().getYear());
				query.bind(classTemp.getExecutionDegree().getCurricularPlan().getName());
				query.bind(classTemp.getExecutionDegree().getCurricularPlan().getCurso().getSigla());			
				result = (List) query.execute();
			} catch (QueryException ex) {
				  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
	
			
			// Delete
			_turmaPersistente.delete(classTemp);
			_suportePersistente.confirmarTransaccao();
			
			_suportePersistente.iniciarTransaccao();

			ITurma classTemp2 = null;
			classTemp2 = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNull(classTemp2);

			
			// Checks Shifts			
			try {		
				turmaTurnoOJB = new TurmaTurnoOJB();
				oqlQuery = "select all from " + TurmaTurno.class.getName();
				oqlQuery += " where turma.nome = $1 ";
				oqlQuery += " and turma.executionPeriod.name = $2 ";
				oqlQuery += " and turma.executionPeriod.executionYear.year = $3 ";
				oqlQuery += " and turma.executionDegree.executionYear.year = $4 ";
				oqlQuery += " and turma.executionDegree.curricularPlan.name = $5 ";
				oqlQuery += " and turma.executionDegree.curricularPlan.curso.sigla = $6 ";
	
				query.create(oqlQuery);
				query.bind(classTemp.getNome());
				query.bind(classTemp.getExecutionPeriod().getName());
				query.bind(classTemp.getExecutionPeriod().getExecutionYear().getYear());
				query.bind(classTemp.getExecutionDegree().getExecutionYear().getYear());
				query.bind(classTemp.getExecutionDegree().getCurricularPlan().getName());
				query.bind(classTemp.getExecutionDegree().getCurricularPlan().getCurso().getSigla());			
				result = (List) query.execute();
			} catch (QueryException ex) {
				  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
	
			_suportePersistente.confirmarTransaccao();
			
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}

	/** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoOJB. */
	public void testDeleteAll() {
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			List result = null;
			try {
				Implementation odmg = OJB.getInstance();
				OQLQuery query = odmg.newOQLQuery();
				;
				String oqlQuery = "select turma from " + Turma.class.getName();
				query.create(oqlQuery);
				result = (List) query.execute();
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			_suportePersistente.confirmarTransaccao();
			assertNotNull(result);
			assertTrue(result.isEmpty());
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteTurma");
		}

	}

	public void testReadAll() {
		try {
			List classes = null;
			
			_suportePersistente.iniciarTransaccao();
			classes = _turmaPersistente.readAll();
			assertEquals(classes.size(), 6);
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
			
			_suportePersistente.iniciarTransaccao();
			classes = _turmaPersistente.readAll();
			assertTrue(classes.isEmpty());
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadAll");
		}
	}

	
	public void testReadByExecutionPeriodAndCurricularYearAndExecutionDegree() {
		List classesList = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classesList = _turmaPersistente.readByExecutionPeriodAndCurricularYearAndExecutionDegree(executionPeriod, new Integer(1), executionDegree);
			assertEquals(classesList.size(), 4);

			classesList = null;
			classesList = _turmaPersistente.readByExecutionPeriodAndCurricularYearAndExecutionDegree(executionPeriod, new Integer(10), executionDegree);
			assertEquals(classesList.size(), 0);

			
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}

	public void testReadByExecutionPeriod() {
		List classesList = null;
		IExecutionPeriod executionPeriod = null;
		IExecutionYear executionYear = null;
		try {
			_suportePersistente.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			classesList = _turmaPersistente.readByExecutionPeriod(executionPeriod);
			assertEquals(classesList.size(), 6);

			executionPeriod = null;
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("3º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			classesList = null;
			classesList = _turmaPersistente.readByExecutionPeriod(executionPeriod);
			assertEquals(classesList.size(), 0);

			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}

	public void testReadByDegreeNameAndDegreeCode() {
		ICurso degree = null;
		List classesList = null;
		try {
			_suportePersistente.iniciarTransaccao();
			
			degree = cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);

			classesList = _turmaPersistente.readByDegreeNameAndDegreeCode(degree.getNome(), degree.getSigla());
			assertEquals(classesList.size(), 4);

			classesList = _turmaPersistente.readByDegreeNameAndDegreeCode("desc", "desc");
			assertEquals(classesList.size(), 0);
			
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}

	public void testReadByExecutionDegree() {
		List classesList = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);
			
			classesList = _turmaPersistente.readByExecutionDegree(executionDegree);
			assertEquals(classesList.size(), 4);
			
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}
}
