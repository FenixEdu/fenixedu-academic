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
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.TurnoAluno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public class TurnoAlunoOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null;
	IPersistentExecutionYear persistentExecutionYear = null; 
	ICursoPersistente persistentDegree = null;
	ICursoExecucaoPersistente persistentExecutionDegree = null;
	IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	ITurnoPersistente persistentShift = null;
	IPersistentStudent persistentStudent = null;
	ITurnoAlunoPersistente persistentStudentShift = null;

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
	try {
		persistentSupport = SuportePersistenteOJB.getInstance();
	} catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		fail("Error");
	}
	persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
	persistentDegree = persistentSupport.getICursoPersistente();
	persistentExecutionDegree = persistentSupport.getICursoExecucaoPersistente();
	persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
	persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
	persistentShift = persistentSupport.getITurnoPersistente();
	persistentStudent = persistentSupport.getIPersistentStudent();
	persistentStudentShift = persistentSupport.getITurnoAlunoPersistente();

  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readByTurnoAndAluno method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testreadByTurnoAndAluno() {
	try {
	  persistentSupport.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "MEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
      
      // Existing
	  ITurnoAluno studentShift = persistentStudentShift.readByTurnoAndAluno(shift, student);
	  assertNotNull(studentShift);
	  
	  assertEquals(studentShift.getAluno(), student);
	  assertEquals(studentShift.getTurno(), shift);
	  
	  // Non existing

	  executionCourse = null;
	  executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);

	  shift = null;
	  shift = persistentShift.readByNameAndExecutionCourse("turno4", executionCourse);
	  assertNotNull(shift);
	  
	  studentShift = null;
	  studentShift = persistentStudentShift.readByTurnoAndAluno(shift, student);
	  assertNull(studentShift);

	  persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  /** Test of readByTurno method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testReadByTurno() {
	List studentsShifts = null;
	try {
	  persistentSupport.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "MEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);

	  // Existing
	  studentsShifts = persistentStudentShift.readByShift(shift);
	  assertEquals(studentsShifts.size(), 1);
	  

	  executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
  	  assertNotNull(executionCourse);
  
	  shift = persistentShift.readByNameAndExecutionCourse("turno5", executionCourse);
	  assertNotNull(shift);
	  
	  // Non Existing
	  studentsShifts = persistentStudentShift.readByShift(shift);
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
	  persistentSupport.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "MEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
      
	  // Existing
	  ITurnoAluno studentShift = persistentStudentShift.readByTurnoAndAluno(shift, student);
	  assertNotNull(studentShift);
	  
	  ITurnoAluno shiftStudent = new TurnoAluno(shift, student);
	  persistentStudentShift.lockWrite(shiftStudent);

	  persistentSupport.confirmarTransaccao();
	  fail("testReadByTurmaAndTurno:fail write existing turmaTurno");
	} catch (ExistingPersistentException ex) {
		assertNotNull("testCreateExistingTurnoAluno:" + ex);
	} catch (ExcepcaoPersistencia ex) {
		fail("testCreateExistingTurnoAluno: Unexpected Excpetion");
	}
  }

  public void testCreateNonExistingTurmaTurno() {
	try {
	  persistentSupport.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "MEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(900), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);

	  // Check if exists
	  ITurnoAluno shiftStudent = persistentStudentShift.readByTurnoAndAluno(shift, student);
	  assertNull(shiftStudent);


	  shiftStudent = new TurnoAluno(shift, student);
	  persistentStudentShift.lockWrite(shiftStudent);
	  persistentSupport.confirmarTransaccao();

	  // Check Insert
	  persistentSupport.iniciarTransaccao();
	  shiftStudent = null;
	  shiftStudent = persistentStudentShift.readByTurnoAndAluno(shift, student);
	  assertNotNull(shiftStudent);
	  
	  assertEquals(shiftStudent.getAluno(), student);
	  assertEquals(shiftStudent.getTurno(), shift);
	  
	  persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }


  /** Test of write method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testWriteExistingChangedObject() {
	try {
	  persistentSupport.iniciarTransaccao();
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "MEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
      
	  // Existing
	  ITurnoAluno studentShift = persistentStudentShift.readByTurnoAndAluno(shift, student);
	  assertNotNull(studentShift);
	  
	  // Read new Student
	  IStudent studentTemp = persistentStudent.readByNumero(new Integer(900), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(studentTemp);
	  
	  studentShift.setAluno(studentTemp);
	  persistentSupport.confirmarTransaccao();
	  

	  // Check Insert
	  
	  persistentSupport.iniciarTransaccao();

	  studentShift = persistentStudentShift.readByTurnoAndAluno(shift, student);
	  assertNull(studentShift);
	  
	  studentShift = persistentStudentShift.readByTurnoAndAluno(shift, studentTemp);
	  assertNotNull(studentShift);
	  
	  assertEquals(studentShift.getAluno(), studentTemp);
	  assertEquals(studentShift.getTurno(), shift);
	  
	  persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testDeleteTurnoAluno() {
	try {
	  persistentSupport.iniciarTransaccao();
	  // existing
	  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
	  assertNotNull(executionYear);
      
	  ICurso degree = persistentDegree.readBySigla("LEIC");
	  assertNotNull(degree);
      
	  IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
	  assertNotNull(degreeCurricularPlan);
      
	  ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNotNull(executionDegree);
      
	  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "MEEC");
	  assertNotNull(executionCourse);
      
	  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno3", executionCourse);
	  assertNotNull(shift);
	  
	  IStudent student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
      
	  // Existing
	  ITurnoAluno studentShift = persistentStudentShift.readByTurnoAndAluno(shift, student);
	  assertNotNull(studentShift);
	  
	  persistentStudentShift.delete(studentShift);
	  persistentSupport.confirmarTransaccao();
	  
	  
	  persistentSupport.iniciarTransaccao();
	  
	  studentShift = null;
	  studentShift = persistentStudentShift.readByTurnoAndAluno(shift, student);
	  assertNull(studentShift);
	  persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
	}
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testDeleteAll() {
    try {
      persistentSupport.iniciarTransaccao();
      persistentStudentShift.deleteAll();
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
        String oqlQuery = "select turnoAluno from " + TurnoAluno.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      persistentSupport.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurnoAluno");
    }

  }
 
}
