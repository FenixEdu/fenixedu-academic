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

public class TurmaTurnoOJBTest extends TestCaseOJB {
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
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readByTurmaTurno method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testreadByTurmaAndTurno() {
    ITurmaTurno turmaTurno = null;
    // read existing TurmaTurno
    try {
      _suportePersistente.iniciarTransaccao();
      // existing
      IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
      assertNotNull(executionYear);
      
      IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
      assertNotNull(executionPeriod);
      
      ICurso degree = cursoPersistente.readBySigla("LEIC");
      assertNotNull(degree);
      
      IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
      assertNotNull(degreeCurricularPlan);
      
      ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
      assertNotNull(executionDegree);
      
      ITurma classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
      assertNotNull(classTemp);
      
      IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
      assertNotNull(executionCourse);
      
      ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
      assertNotNull(shift);
      
      turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift);

	  assertEquals(turmaTurno.getTurno(), shift);
	  assertEquals(turmaTurno.getTurma(), classTemp);
      
      
      // Non Existing
	  shift = _turnoPersistente.readByNameAndExecutionCourse("turno2", executionCourse);
  	  assertNotNull(shift);
  
	  turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift);
	  assertNull(turmaTurno);


      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
    }
  }

  // write new existing turmaTurno
  public void testCreateExistingTurmaTurno() { 

	try{
		
		_suportePersistente.iniciarTransaccao();
		// existing
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
	      
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		assertNotNull(executionPeriod);
	      
		ICurso degree = cursoPersistente.readBySigla("LEIC");
		assertNotNull(degree);
	      
		IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
	      
		ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
		assertNotNull(executionDegree);
	      
		ITurma classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
		assertNotNull(classTemp);
	      
		IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
	      
		ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift);
	      
		// Create Existing 
		
		ITurmaTurno turmaTurno = new TurmaTurno(classTemp, shift);
		_suportePersistente.iniciarTransaccao();
		_turmaTurnoPersistente.lockWrite(turmaTurno);
		_suportePersistente.confirmarTransaccao();
		
		fail("testCreateExistingTurmaTurno");	
	} catch (ExcepcaoPersistencia ex) {
	//all is ok
	}
  }


  // write new non-existing turmaTurno
  public void testCreateNonExistingTurmaTurno() {
	try{
		
		_suportePersistente.iniciarTransaccao();
		// existing
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
	      
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		assertNotNull(executionPeriod);
	      
		ICurso degree = cursoPersistente.readBySigla("LEIC");
		assertNotNull(degree);
	      
		IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
	      
		ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
		assertNotNull(executionDegree);
	      
		ITurma classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
		assertNotNull(classTemp);
	      
		IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
	      
		ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno2", executionCourse);
		assertNotNull(shift);

	    ITurmaTurno turmaTurno = new TurmaTurno(classTemp, shift);
        _turmaTurnoPersistente.lockWrite(turmaTurno);
        _suportePersistente.confirmarTransaccao();

		// Check Insert

		_suportePersistente.iniciarTransaccao();
		turmaTurno = null;
		turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift);
		assertNotNull(turmaTurno);
		_suportePersistente.confirmarTransaccao();


    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingTurmaTurno");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testWriteExistingChangedObject() {
	try{
		
		_suportePersistente.iniciarTransaccao();
		// existing
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
	      
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		assertNotNull(executionPeriod);
	      
		ICurso degree = cursoPersistente.readBySigla("LEIC");
		assertNotNull(degree);
	      
		IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
	      
		ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
		assertNotNull(executionDegree);
	      
		ITurma classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
		assertNotNull(classTemp);
	      
		IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
	      
		ITurno shift1 = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift1);

		ITurmaTurno turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift1);
		assertNotNull(turmaTurno);

		ITurno shift2 = _turnoPersistente.readByNameAndExecutionCourse("turno2", executionCourse);
		assertNotNull(shift2);

		turmaTurno.setTurno(shift2);

		_suportePersistente.confirmarTransaccao();

		// Check Insert

		_suportePersistente.iniciarTransaccao();
		turmaTurno = null;
		turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift1);
		assertNull(turmaTurno);

		turmaTurno = null;
		turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift2);
		assertNotNull(turmaTurno);
		_suportePersistente.confirmarTransaccao();


	} catch (ExcepcaoPersistencia ex) {
	  fail("testCreateNonExistingTurmaTurno");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testDeleteTurmaTurno() {
	ITurmaTurno turmaTurno = null;
	// read existing TurmaTurno
	try {
	  _suportePersistente.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
	  assertNotNull(executionPeriod);
      
	  ICurso degree = cursoPersistente.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  ITurma classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
	  assertNotNull(classTemp);
      
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
	  assertNotNull(shift);
      
	  turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift);
	  assertNotNull(turmaTurno);
      
	  _turmaTurnoPersistente.delete(turmaTurno);

	  _suportePersistente.confirmarTransaccao();
	  
	  
	  _suportePersistente.iniciarTransaccao();
	  turmaTurno = null;
	  turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift);
	  assertNull(turmaTurno);
      
	  _suportePersistente.confirmarTransaccao();
	  
	  
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testDeleteTurmaTurnoByKeys() {
	ITurmaTurno turmaTurno = null;
	// read existing TurmaTurno
	try {
	  _suportePersistente.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
	  assertNotNull(executionPeriod);
      
	  ICurso degree = cursoPersistente.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  ITurma classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
	  assertNotNull(classTemp);
      
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
	  assertNotNull(shift);
      
	  turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift);
	  assertNotNull(turmaTurno);
	  
	  _turmaTurnoPersistente.delete(turmaTurno);
	  _suportePersistente.confirmarTransaccao();

	  _suportePersistente.iniciarTransaccao();
	  turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(classTemp, shift);
	  assertNull(turmaTurno);
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }
  
  /** Test of deleteAll method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      _turmaTurnoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
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
      _suportePersistente.confirmarTransaccao();
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
	  _suportePersistente.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
	  assertNotNull(executionPeriod);
      
	  ICurso degree = cursoPersistente.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  ITurma classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", executionDegree, executionPeriod);
	  assertNotNull(classTemp);
      
      
      List result = _turmaTurnoPersistente.readByClass(classTemp);
      assertNotNull(result);
      assertEquals(result.size(), 1);
      
	  classTemp = _turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("turmaParaTestarInscricoesDeAlunos2", executionDegree, executionPeriod);
	  assertNotNull(classTemp);
	  
	  result = _turmaTurnoPersistente.readByClass(classTemp);
	  assertEquals(result.size(), 5);
      

	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  
  public void testReadClassesWithShift() {
	ITurmaTurno turmaTurno = null;
	// read existing TurmaTurno
	try {
	  _suportePersistente.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  ICurso degree = cursoPersistente.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
	  assertNotNull(shift);
      
	  List result = _turmaTurnoPersistente.readClassesWithShift(shift);
	  assertEquals(result.size(), 1);

	  shift = _turnoPersistente.readByNameAndExecutionCourse("turno455", executionCourse);
	  assertNotNull(shift);
      
	  result = _turmaTurnoPersistente.readClassesWithShift(shift);
	  assertEquals(result.size(), 2);


	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }



}
