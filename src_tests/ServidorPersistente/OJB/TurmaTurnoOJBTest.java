/*
 * TurmaTurnoOJBTest.java
 * JUnit based test
 *
 * Created on 19 de Outubro de 2002, 15:41
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
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class TurmaTurnoOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null;
	IPersistentExecutionPeriod persistentExecutionPeriod = null;
	IPersistentExecutionYear persistentExecutionYear = null; 
	ICursoPersistente persistentDegree = null;
	ITurmaPersistente persistentClass = null;
	ICursoExecucaoPersistente persistentExecutionDegree = null;
	IPlanoCurricularCursoPersistente persistentDegreeCurricularPlan = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	ITurnoPersistente persistentShift = null;
	ITurmaTurnoPersistente persistentClassShift = null;

	
    public TurmaTurnoOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(TurmaTurnoOJBTest.class);

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
	persistentDegreeCurricularPlan = persistentSupport.getIPlanoCurricularCursoPersistente();
	persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
	persistentShift = persistentSupport.getITurnoPersistente();
	persistentClassShift = persistentSupport.getITurmaTurnoPersistente();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readByTurmaTurno method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testreadByTurmaAndTurno() {
    ITurmaTurno turmaTurno = null;
    // read existing TurmaTurno
    try {
      persistentSupport.iniciarTransaccao();
      // existing
      IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
      assertNotNull(executionYear);
      
      IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
      assertNotNull(executionPeriod);
      
      ICurso degree = persistentDegree.readBySigla("LEIC");
      assertNotNull(degree);
      
      IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
      assertNotNull(degreeCurricularPlan);
      
      ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
      assertNotNull(executionDegree);
      
      ITurma classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
      assertNotNull(classTemp);
      
      IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
      assertNotNull(executionCourse);
      
      ITurno shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
      assertNotNull(shift);
      
      turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift);

	  assertEquals(turmaTurno.getTurno(), shift);
	  assertEquals(turmaTurno.getTurma(), classTemp);
      
      
      // Non Existing
	  shift = persistentShift.readByNameAndExecutionCourse("turno2", executionCourse);
  	  assertNotNull(shift);
  
	  turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift);
	  assertNull(turmaTurno);


      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
    }
  }

  // write new existing turmaTurno
  public void testCreateExistingTurmaTurno() { 

	try{
		
		persistentSupport.iniciarTransaccao();
		// existing
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
	      
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		assertNotNull(executionPeriod);
	      
		ICurso degree = persistentDegree.readBySigla("LEIC");
		assertNotNull(degree);
	      
		IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
	      
		ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
		assertNotNull(executionDegree);
	      
		ITurma classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
		assertNotNull(classTemp);
	      
		IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
	      
		ITurno shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift);
	      
		// Create Existing 
		
		ITurmaTurno turmaTurno = new TurmaTurno(classTemp, shift);
		persistentSupport.iniciarTransaccao();
		persistentClassShift.lockWrite(turmaTurno);
		persistentSupport.confirmarTransaccao();
		
		fail("testCreateExistingTurmaTurno: Expected an Exception");	
	} catch (ExistingPersistentException ex) {
		assertNotNull("testCreateExistingTurmaTurno");
	} catch (ExcepcaoPersistencia ex) {
		fail("testCreateExistingTurmaTurno: Unexpected Exception");
	}
  }


  // write new non-existing turmaTurno
  public void testCreateNonExistingTurmaTurno() {
	try{
		
		persistentSupport.iniciarTransaccao();
		// existing
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
	      
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		assertNotNull(executionPeriod);
	      
		ICurso degree = persistentDegree.readBySigla("LEIC");
		assertNotNull(degree);
	      
		IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
	      
		ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
		assertNotNull(executionDegree);
	      
		ITurma classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
		assertNotNull(classTemp);
	      
		IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
	      
		ITurno shift = persistentShift.readByNameAndExecutionCourse("turno2", executionCourse);
		assertNotNull(shift);

	    ITurmaTurno turmaTurno = new TurmaTurno(classTemp, shift);
        persistentClassShift.lockWrite(turmaTurno);
        persistentSupport.confirmarTransaccao();

		// Check Insert

		persistentSupport.iniciarTransaccao();
		turmaTurno = null;
		turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift);
		assertNotNull(turmaTurno);
		persistentSupport.confirmarTransaccao();


    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingTurmaTurno");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testWriteExistingChangedObject() {
	try{
		
		persistentSupport.iniciarTransaccao();
		// existing
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
	      
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		assertNotNull(executionPeriod);
	      
		ICurso degree = persistentDegree.readBySigla("LEIC");
		assertNotNull(degree);
	      
		IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
	      
		ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
		assertNotNull(executionDegree);
	      
		ITurma classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
		assertNotNull(classTemp);
	      
		IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
	      
		ITurno shift1 = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift1);

		ITurmaTurno turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift1);
		assertNotNull(turmaTurno);

		ITurno shift2 = persistentShift.readByNameAndExecutionCourse("turno2", executionCourse);
		assertNotNull(shift2);

		turmaTurno.setTurno(shift2);

		persistentSupport.confirmarTransaccao();

		// Check Insert

		persistentSupport.iniciarTransaccao();
		turmaTurno = null;
		turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift1);
		assertNull(turmaTurno);

		turmaTurno = null;
		turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift2);
		assertNotNull(turmaTurno);
		persistentSupport.confirmarTransaccao();


	} catch (ExcepcaoPersistencia ex) {
	  fail("testCreateNonExistingTurmaTurno");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testDeleteTurmaTurno() {
	ITurmaTurno turmaTurno = null;
	// read existing TurmaTurno
	try {
	  persistentSupport.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
	  assertNotNull(executionPeriod);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  ITurma classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
	  assertNotNull(classTemp);
      
	  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
	  assertNotNull(shift);
      
	  turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift);
	  assertNotNull(turmaTurno);
      
	  persistentClassShift.delete(turmaTurno);

	  persistentSupport.confirmarTransaccao();
	  
	  
	  persistentSupport.iniciarTransaccao();
	  turmaTurno = null;
	  turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift);
	  assertNull(turmaTurno);
      
	  persistentSupport.confirmarTransaccao();
	  
	  
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testDeleteTurmaTurnoByKeys() {
	ITurmaTurno turmaTurno = null;
	// read existing TurmaTurno
	try {
	  persistentSupport.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
	  assertNotNull(executionPeriod);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  ITurma classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
	  assertNotNull(classTemp);
      
	  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
	  assertNotNull(shift);
      
	  turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift);
	  assertNotNull(turmaTurno);
	  
	  persistentClassShift.delete(turmaTurno);
	  persistentSupport.confirmarTransaccao();

	  persistentSupport.iniciarTransaccao();
	  turmaTurno = persistentClassShift.readByTurmaAndTurno(classTemp, shift);
	  assertNull(turmaTurno);
	  persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }
  
  /** Test of deleteAll method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testDeleteAll() {
    try {
      persistentSupport.iniciarTransaccao();
      persistentClassShift.deleteAll();
      persistentSupport.confirmarTransaccao();

      persistentSupport.iniciarTransaccao();
      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select turmaTurno from " + TurmaTurno.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      persistentSupport.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurmaTurno");
    }

  }

  
  public void testReadByClass() {
	ITurmaTurno turmaTurno = null;
	// read existing TurmaTurno
	try {
	  persistentSupport.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
	  assertNotNull(executionPeriod);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  ITurma classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
	  assertNotNull(classTemp);
      
      
      List result = persistentClassShift.readByClass(classTemp);
      assertNotNull(result);
      assertEquals(result.size(), 1);
      
	  classTemp = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod("turmaParaTestarInscricoesDeAlunos2", executionDegree, executionPeriod);
	  assertNotNull(classTemp);
	  
	  result = persistentClassShift.readByClass(classTemp);
	  assertEquals(result.size(), 5);
      

	  persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  
  public void testReadClassesWithShift() {
	ITurmaTurno turmaTurno = null;
	// read existing TurmaTurno
	try {
	  persistentSupport.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
	  assertNotNull(shift);
      
	  List result = persistentClassShift.readClassesWithShift(shift);
	  assertEquals(result.size(), 1);

	  shift = persistentShift.readByNameAndExecutionCourse("turno455", executionCourse);
	  assertNotNull(shift);
      
	  result = persistentClassShift.readClassesWithShift(shift);
	  assertEquals(result.size(), 2);


	  persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }



}
