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
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurma;
import Dominio.Turma;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class TurmaOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentExecutionPeriod persistentExecutionPeriod = null;
	IPersistentExecutionYear persistentExecutionYear = null;
	ICursoPersistente persistentDegree = null;
	ITurmaPersistente persistentClass = null;
	ICursoExecucaoPersistente persistentExecutionDegree = null;
	IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;

	
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
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
		persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
		persistentDegree = persistentSupport.getICursoPersistente();
		persistentClass = persistentSupport.getITurmaPersistente();
		persistentExecutionDegree = persistentSupport.getICursoExecucaoPersistente();
		persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testReadByNome() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNotNull(classTemp);

			
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
		assertEquals(classTemp.getNome(), "10501");

		// read unexisting Turma
		try {
			persistentSupport.iniciarTransaccao();
			classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("desc", executionDegree, executionPeriod);
			assertNull(classTemp);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read unexisting turma");
		}
	}

	// write new existing turma
	public void testCreateExistingTurma() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = new Turma("10501", new Integer(1), executionDegree, executionPeriod);

			persistentClass.lockWrite(classTemp);
			persistentSupport.confirmarTransaccao();
			fail("testCreateExistingTurma: Expexted an Exception");
		} catch (ExistingPersistentException ex) {
			assertNotNull("testCreateExistingTurma");
		} catch (ExcepcaoPersistencia ex) {
			fail("testCreateExistingTurma: Unexpected Exception");
		}
	}

	// write new non-existing turma
	public void testCreateNonExistingTurma() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = null;
			classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10510", executionDegree, executionPeriod);
			assertNull(classTemp);
			persistentSupport.confirmarTransaccao();

			classTemp = new Turma("10510", new Integer(1), executionDegree, executionPeriod);

			persistentSupport.iniciarTransaccao();
			persistentClass.lockWrite(classTemp);
			persistentSupport.confirmarTransaccao();
			
			persistentSupport.iniciarTransaccao();
			classTemp = null;
			classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10510", executionDegree, executionPeriod);
			assertNotNull(classTemp);
			persistentSupport.confirmarTransaccao();

			
		} catch (ExcepcaoPersistencia ex) {
			fail("testCreateNonExistingTurma");
		}
	}

	/** Test of write method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testWriteExistingChangedObject() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNotNull(classTemp);

			persistentClass.readByOId(classTemp, true);
			classTemp.setNome("10510");
			persistentSupport.confirmarTransaccao();



			persistentSupport.iniciarTransaccao();

			classTemp = null;
			classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNull(classTemp);

			classTemp = null;
			classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10510", executionDegree, executionPeriod);
			assertNotNull(classTemp);
			assertEquals(classTemp.getNome(), "10510");

			persistentSupport.confirmarTransaccao();


		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}

	/** Test of delete method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testDeleteTurma() {
		ITurma classTemp = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
			
		Implementation odmg = null;
		OQLQuery query = null;
		String oqlQuery = null;;
		List result = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNotNull(classTemp);
			
			
			// Checks existing shifts

			odmg = OJB.getInstance();

			///////////////////////////////////////////////////////////////////
			// Added Code due to Upgrade from OJB 0.9.5 to OJB rc1
			///////////////////////////////////////////////////////////////////
			Database db = odmg.newDatabase();

			try {
				db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
			} catch (ODMGException e) {
				e.printStackTrace();
			}
			///////////////////////////////////////////////////////////////////
			// End of Added Code
			///////////////////////////////////////////////////////////////////

			query = odmg.newOQLQuery();

			try {			
				
				oqlQuery = "select all from " + TurmaTurno.class.getName();
				oqlQuery += " where turma.nome = $1 ";
				oqlQuery += " and turma.executionPeriod.name = $2 ";
				oqlQuery += " and turma.executionPeriod.executionYear.year = $3 ";
				oqlQuery += " and turma.executionDegree.executionYear.year = $4 ";
				oqlQuery += " and turma.executionDegree.curricularPlan.name = $5 ";
				oqlQuery += " and turma.executionDegree.curricularPlan.degree.sigla = $6 ";
	
				query.create(oqlQuery);
				query.bind(classTemp.getNome());
				query.bind(classTemp.getExecutionPeriod().getName());
				query.bind(classTemp.getExecutionPeriod().getExecutionYear().getYear());
				query.bind(classTemp.getExecutionDegree().getExecutionYear().getYear());
				query.bind(classTemp.getExecutionDegree().getCurricularPlan().getName());
				query.bind(classTemp.getExecutionDegree().getCurricularPlan().getDegree().getSigla());			
				result = (List) query.execute();
				assertNotNull(result);
			} catch (QueryException ex) {
				  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
	
			
			// Delete
			persistentClass.delete(classTemp);
			persistentSupport.confirmarTransaccao();
			
			persistentSupport.iniciarTransaccao();

			ITurma classTemp2 = null;
			classTemp2 = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
			assertNull(classTemp2);

			
			// Checks Shifts			
			try {		
				oqlQuery = "select all from " + TurmaTurno.class.getName();
				oqlQuery += " where turma.nome = $1 ";
				oqlQuery += " and turma.executionPeriod.name = $2 ";
				oqlQuery += " and turma.executionPeriod.executionYear.year = $3 ";
				oqlQuery += " and turma.executionDegree.executionYear.year = $4 ";
				oqlQuery += " and turma.executionDegree.curricularPlan.name = $5 ";
				oqlQuery += " and turma.executionDegree.curricularPlan.degree.sigla = $6 ";
	
				query.create(oqlQuery);
				query.bind(classTemp.getNome());
				query.bind(classTemp.getExecutionPeriod().getName());
				query.bind(classTemp.getExecutionPeriod().getExecutionYear().getYear());
				query.bind(classTemp.getExecutionDegree().getExecutionYear().getYear());
				query.bind(classTemp.getExecutionDegree().getCurricularPlan().getName());
				query.bind(classTemp.getExecutionDegree().getCurricularPlan().getDegree().getSigla());			
				result = (List) query.execute();
				assertNotNull(result);
			} catch (QueryException ex) {
				  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
	
			persistentSupport.confirmarTransaccao();
			
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}

	/** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoOJB. */
	public void testDeleteAll() {
		try {
			persistentSupport.iniciarTransaccao();
			persistentClass.deleteAll();
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			List result = null;
			try {
				Implementation odmg = OJB.getInstance();

				///////////////////////////////////////////////////////////////////
				// Added Code due to Upgrade from OJB 0.9.5 to OJB rc1
				///////////////////////////////////////////////////////////////////
				Database db = odmg.newDatabase();

				try {
					db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
				} catch (ODMGException e) {
					e.printStackTrace();
				}
				///////////////////////////////////////////////////////////////////
				// End of Added Code
				///////////////////////////////////////////////////////////////////

				OQLQuery query = odmg.newOQLQuery();
				String oqlQuery = "select turma from " + Turma.class.getName();
				query.create(oqlQuery);
				result = (List) query.execute();
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			persistentSupport.confirmarTransaccao();
			assertNotNull(result);
			assertTrue(result.isEmpty());
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteTurma");
		}

	}

	public void testReadAll() {
		try {
			List classes = null;
			
			persistentSupport.iniciarTransaccao();
			classes = persistentClass.readAll();
			assertEquals(classes.size(), 6);
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			persistentClass.deleteAll();
			persistentSupport.confirmarTransaccao();
			
			persistentSupport.iniciarTransaccao();
			classes = persistentClass.readAll();
			assertTrue(classes.isEmpty());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadAll");
		}
	}

	
	public void testReadByExecutionPeriodAndCurricularYearAndExecutionDegree() {
		List classesList = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);

			classesList = persistentClass.readByExecutionPeriodAndCurricularYearAndExecutionDegree(executionPeriod, new Integer(1), executionDegree);
			assertEquals(classesList.size(), 4);

			classesList = null;
			classesList = persistentClass.readByExecutionPeriodAndCurricularYearAndExecutionDegree(executionPeriod, new Integer(10), executionDegree);
			assertEquals(classesList.size(), 0);

			
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}
	//TODO: test readbynameandexecutionyearandexecutionperiod
	public void testReadByExecutionPeriod() {
		List classesList = null;
		IExecutionPeriod executionPeriod = null;
		IExecutionYear executionYear = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			classesList = persistentClass.readByExecutionPeriod(executionPeriod);
			assertEquals(classesList.size(), 6);

			executionPeriod = null;
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("3º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			classesList = null;
			classesList = persistentClass.readByExecutionPeriod(executionPeriod);
			assertEquals(classesList.size(), 0);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}

	public void testReadByDegreeNameAndDegreeCode() {
		ICurso degree = null;
		List classesList = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			classesList = persistentClass.readByDegreeNameAndDegreeCode(degree.getNome(), degree.getSigla());
			assertEquals(classesList.size(), 4);

			classesList = persistentClass.readByDegreeNameAndDegreeCode("desc", "desc");
			assertEquals(classesList.size(), 0);
			
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}

	public void testReadByExecutionDegree() {
		List classesList = null;
		ICursoExecucao executionDegree = null;
		IExecutionPeriod executionPeriod = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICurso degree = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
			assertNotNull(executionDegree);
			
			classesList = persistentClass.readByExecutionDegree(executionDegree);
			assertEquals(classesList.size(), 4);
			
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
	}
}
