/*
 * TurnoAlunoOJBTest.java
 * JUnit based test
 *
 * Created on 21 de Outubro de 2002, 19:16
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
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.TurnoAluno;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.TipoCurso;

public class TurnoAlunoOJBTest extends TestCaseOJB {
    public TurnoAlunoOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(TurnoAlunoOJBTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readByTurnoAndAluno method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testreadByTurnoAndAluno() {
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
      
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
      
      // Existing
	  ITurnoAluno studentShift = _turnoAlunoPersistente.readByTurnoAndAluno(shift, student);
	  assertNotNull(studentShift);
	  
	  assertEquals(studentShift.getAluno(), student);
	  assertEquals(studentShift.getTurno(), shift);
	  
	  // Non existing

	  executionCourse = null;
	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);

	  shift = null;
	  shift = _turnoPersistente.readByNameAndExecutionCourse("turno4", executionCourse);
	  assertNotNull(shift);
	  
	  studentShift = null;
	  studentShift = _turnoAlunoPersistente.readByTurnoAndAluno(shift, student);
	  assertNull(studentShift);

	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  /** Test of readByTurno method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testReadByTurno() {
	List studentsShifts = null;
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
      
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);

	  // Existing
	  studentsShifts = _turnoAlunoPersistente.readByShift(shift);
	  assertEquals(studentsShifts.size(), 1);
	  

	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
  	  assertNotNull(executionCourse);
  
	  shift = _turnoPersistente.readByNameAndExecutionCourse("turno5", executionCourse);
	  assertNotNull(shift);
	  
	  // Non Existing
	  studentsShifts = _turnoAlunoPersistente.readByShift(shift);
	  assertTrue(studentsShifts.isEmpty());
	  
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurno:fail read unexisting Turno*");
	}

  }

  /** Test of readByStudentIdAndShiftType method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
  public void testreadByStudentIdAndShiftType() {
		// Waiting FIXME

  }

  // write new existing TurnoAluno
  public void testCreateExistingTurnoAluno() { 
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
      
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
      
	  // Existing
	  ITurnoAluno studentShift = _turnoAlunoPersistente.readByTurnoAndAluno(shift, student);
	  assertNotNull(studentShift);
	  
	  ITurnoAluno shiftStudent = new TurnoAluno(shift, student);
	  _turnoAlunoPersistente.lockWrite(shiftStudent);

	  _suportePersistente.confirmarTransaccao();
	  fail("testReadByTurmaAndTurno:fail write existing turmaTurno");
	} catch (ExcepcaoPersistencia ex) {
	  // All is OK
	}
  }

  public void testCreateNonExistingTurmaTurno() {
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
      
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(900), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);

	  // Check if exists
	  ITurnoAluno shiftStudent = _turnoAlunoPersistente.readByTurnoAndAluno(shift, student);
	  assertNull(shiftStudent);


	  shiftStudent = new TurnoAluno(shift, student);
	  _turnoAlunoPersistente.lockWrite(shiftStudent);
	  _suportePersistente.confirmarTransaccao();

	  // Check Insert
	  _suportePersistente.iniciarTransaccao();
	  shiftStudent = null;
	  shiftStudent = _turnoAlunoPersistente.readByTurnoAndAluno(shift, student);
	  assertNotNull(shiftStudent);
	  
	  assertEquals(shiftStudent.getAluno(), student);
	  assertEquals(shiftStudent.getTurno(), shift);
	  
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }


  /** Test of write method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testWriteExistingChangedObject() {
	try {
	  _suportePersistente.iniciarTransaccao();
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  ICurso degree = cursoPersistente.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
      
	  // Existing
	  ITurnoAluno studentShift = _turnoAlunoPersistente.readByTurnoAndAluno(shift, student);
	  assertNotNull(studentShift);
	  
	  // Read new Student
	  IStudent studentTemp = persistentStudent.readByNumero(new Integer(900), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(studentTemp);
	  
	  studentShift.setAluno(studentTemp);
	  _suportePersistente.confirmarTransaccao();
	  

	  // Check Insert
	  
	  _suportePersistente.iniciarTransaccao();

	  studentShift = _turnoAlunoPersistente.readByTurnoAndAluno(shift, student);
	  assertNull(studentShift);
	  
	  studentShift = _turnoAlunoPersistente.readByTurnoAndAluno(shift, studentTemp);
	  assertNotNull(studentShift);
	  
	  assertEquals(studentShift.getAluno(), studentTemp);
	  assertEquals(studentShift.getTurno(), shift);
	  
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testDeleteTurnoAluno() {
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
      
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = _turnoPersistente.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
      
	  // Existing
	  ITurnoAluno studentShift = _turnoAlunoPersistente.readByTurnoAndAluno(shift, student);
	  assertNotNull(studentShift);
	  
	  _turnoAlunoPersistente.delete(studentShift);
	  _suportePersistente.confirmarTransaccao();
	  
	  
	  _suportePersistente.iniciarTransaccao();
	  
	  studentShift = null;
	  studentShift = _turnoAlunoPersistente.readByTurnoAndAluno(shift, student);
	  assertNull(studentShift);
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      _turnoAlunoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();

      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select turnoAluno from " + TurnoAluno.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurnoAluno");
    }

  }
 
}
