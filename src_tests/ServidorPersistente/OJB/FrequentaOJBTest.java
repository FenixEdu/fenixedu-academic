/*
 * FrequentaOJBTest.java
 * JUnit based test
 *
 * Created on 20 de Outubro de 2002, 15:53
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

import Dominio.Frequenta;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.TipoCurso;

public class FrequentaOJBTest extends TestCaseOJB {
    public FrequentaOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(FrequentaOJBTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readByAlunoAndDisciplinaExecucao method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testreadByAlunoAndDisciplinaExecucao() {
    IFrequenta attend = null;
    IStudent student = null;
    IDisciplinaExecucao executionCourse = null;
    
    
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);	        
	
	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);

      attend = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, executionCourse);
	  assertNotNull(attend);
	        
      _suportePersistente.confirmarTransaccao();
	  assertEquals(attend.getAluno(), student);
	  assertEquals(attend.getDisciplinaExecucao(), executionCourse);
      
      
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
    }
        
    // read unexisting Frequenta
    try {
      _suportePersistente.iniciarTransaccao();
      
	  executionCourse = null;
	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
   	  assertNotNull(executionCourse);

      
      attend = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student,  executionCourse);
      _suportePersistente.confirmarTransaccao();
      assertNull(attend);
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByAlunoAndDisciplinaExecucao:fail read unexisting frequenta");
    }
  }

  /** Test of readByAlunoAndDisciplinaExecucao method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testreadByStudentId() {

	// FIXME: Must read by Username


//	List courses = null;
//	// read existing Frequenta
//	try {
//	  _suportePersistente.iniciarTransaccao();
//	  courses = _frequentaPersistente.readByStudentId(_aluno1.getNumber());
//	  _suportePersistente.confirmarTransaccao();
//	  assertNotNull("testReadByStudentId:read courses of existing frequencies", courses);
//	  assertTrue("testReadByStudentId:read courses of existing frequencies", !(courses.isEmpty()));
//	  assertEquals("testReadByStudentId:read courses of existing frequencies", 1, courses.size());
//	} catch (ExcepcaoPersistencia ex) {
//	  fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
//	}
//        
//	// read unexisting Frequenta
//	try {
//		_suportePersistente.iniciarTransaccao();
//		courses = _frequentaPersistente.readByStudentId(_aluno2.getNumber());
//		_suportePersistente.confirmarTransaccao();
//		assertNotNull("testReadByStudentId:read courses of existing frequencies", courses);
//		assertTrue("testReadByStudentId:read courses of existing frequencies", courses.isEmpty());
//	} catch (ExcepcaoPersistencia ex) {
//	  fail("testReadByAlunoAndDisciplinaExecucao:fail read unexisting frequenta");
//	}
  }

  // write new existing frequenta
  public void testCreateExistingFrequenta() {
   IStudent student = null;
   IDisciplinaExecucao executionCourse = null;
       
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
      assertNotNull(student);

	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      _suportePersistente.confirmarTransaccao();

      IFrequenta attend = new Frequenta(student, executionCourse);
      
      _suportePersistente.iniciarTransaccao();
      _frequentaPersistente.lockWrite(attend);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingFrequenta");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }

//  // write new non-existing frequenta
  public void testCreateNonExistingFrequenta() {
    IFrequenta attend = new Frequenta();
	IStudent student = null;
	IDisciplinaExecucao executionCourse = null;
	
    try {
    	_suportePersistente.iniciarTransaccao();
    	
		student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
		assertNotNull(student);
    	
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
		assertNotNull(executionCourse);

    	_suportePersistente.confirmarTransaccao();
 
    	attend.setAluno(student);
    	attend.setDisciplinaExecucao(executionCourse);

      _suportePersistente.iniciarTransaccao();
      _frequentaPersistente.lockWrite(attend);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingFrequenta");
    }
  }


  /** Test of write method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testWriteExistingChangedObject() {
	IFrequenta attend = null;
	IStudent student = null;
	IDisciplinaExecucao executionCourse = null;
    
    
	try {
	  _suportePersistente.iniciarTransaccao();
	  	student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  	assertNotNull(student);	        
	
	  	executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  	assertNotNull(executionCourse);

	  	attend = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, executionCourse);
	  	assertNotNull(attend);
	        
	    _suportePersistente.confirmarTransaccao();
	  	  
	  	assertEquals(attend.getAluno(), student);
	  	assertEquals(attend.getDisciplinaExecucao(), executionCourse);
      
            
	    _suportePersistente.iniciarTransaccao();
	  	attend = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, executionCourse);
	  	assertNotNull(attend);

	  	IDisciplinaExecucao executionCourse1 = null;
	  	executionCourse1 = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
	  	assertNotNull(executionCourse1);
	
	  	attend.setDisciplinaExecucao(executionCourse1);
	    _suportePersistente.confirmarTransaccao();


	  // Check changes
	  
	  _suportePersistente.iniciarTransaccao();
	  IFrequenta attendTemp = null;
	  attendTemp = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, executionCourse1);
	  assertNotNull(attendTemp);
	  assertEquals(attendTemp.getAluno(), student);
	  assertEquals(attendTemp.getDisciplinaExecucao(), executionCourse1);

	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testDeleteFrequenta() {

    IStudent student = null;
    
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  IDisciplinaExecucao executionCourse = null;
	  
	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);

	  IFrequenta attend = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, executionCourse);
	  assertNotNull(attend);

      _frequentaPersistente.delete(attend);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      IFrequenta attendTemp = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, executionCourse);
      _suportePersistente.confirmarTransaccao();

      assertNull(attendTemp);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteFrequenta");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testDeleteAll() {
	IStudent student = null;
    
	try {
		
	  // Check that something exists
	  _suportePersistente.iniciarTransaccao();
	  student = persistentStudent.readByNumero(new Integer(800), new TipoCurso(TipoCurso.LICENCIATURA));
	  IDisciplinaExecucao executionCourse = null;
	  
	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);

	  IFrequenta attend = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, executionCourse);
	  assertNotNull(attend);
	  _suportePersistente.confirmarTransaccao();

	  // Delete All
      _suportePersistente.iniciarTransaccao();
      _frequentaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();


	  // Check Deletion
      _suportePersistente.iniciarTransaccao();
      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select frequenta from " + Frequenta.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAllFrequenta");
    }

  }

  public void testReadByExecutionCourse() {

	try {
	  _suportePersistente.iniciarTransaccao();
	
	  // Check one that exists  
	  IDisciplinaExecucao executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);

	  List attendList = _frequentaPersistente.readByExecutionCourse(executionCourse);
	  assertNotNull(attendList);
	  assertEquals(attendList.size(), 1);


	  // Check one that doesn't exist  

	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "LEEC");
      assertNotNull(executionCourse);
		
	  attendList = null;
	  attendList = _frequentaPersistente.readByExecutionCourse(executionCourse);
	  assertNotNull(attendList);
	  assertEquals(attendList.size(), 0);

	  _suportePersistente.confirmarTransaccao();

	} catch (ExcepcaoPersistencia ex) {
	  fail("testDeleteFrequenta");
	}
  }


    
    
}
