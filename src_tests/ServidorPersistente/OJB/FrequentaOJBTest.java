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

import junit.framework.*;
import java.util.List;
import org.apache.ojb.odmg.OJB;
import org.odmg.QueryException;
import org.odmg.OQLQuery;
import org.odmg.Implementation;
import ServidorPersistente.*;
import Dominio.*;

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
    IFrequenta frequenta = null;
    // read existing Frequenta
    
    IStudent student = null;
    
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(800), licenciatura);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
    }
      
    
    try {
      _suportePersistente.iniciarTransaccao();
      frequenta = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, _disciplinaExecucao1);
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadByAlunoAndDisciplinaExecucao:read existing frequenta", frequenta, _frequenta1);
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
    }
        
    // read unexisting Frequenta
    try {
      _suportePersistente.iniciarTransaccao();
      frequenta = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student,  _disciplinaExecucao2);
      _suportePersistente.confirmarTransaccao();
      assertNull(frequenta);
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByAlunoAndDisciplinaExecucao:fail read unexisting frequenta");
    }
  }

  /** Test of readByAlunoAndDisciplinaExecucao method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testreadByStudentId() {
	List courses = null;
	// read existing Frequenta
	try {
	  _suportePersistente.iniciarTransaccao();
	  courses = _frequentaPersistente.readByStudentId(_aluno1.getNumber());
	  _suportePersistente.confirmarTransaccao();
	  assertNotNull("testReadByStudentId:read courses of existing frequencies", courses);
	  assertTrue("testReadByStudentId:read courses of existing frequencies", !(courses.isEmpty()));
	  assertEquals("testReadByStudentId:read courses of existing frequencies", 1, courses.size());
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
	}
        
	// read unexisting Frequenta
	try {
		_suportePersistente.iniciarTransaccao();
		courses = _frequentaPersistente.readByStudentId(_aluno2.getNumber());
		_suportePersistente.confirmarTransaccao();
		assertNotNull("testReadByStudentId:read courses of existing frequencies", courses);
		assertTrue("testReadByStudentId:read courses of existing frequencies", courses.isEmpty());
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByAlunoAndDisciplinaExecucao:fail read unexisting frequenta");
	}
  }

  // write new existing frequenta
  public void testCreateExistingFrequenta() {
   IStudent student = null;
    
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(800), licenciatura);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
    }

  	
    IFrequenta frequenta = new Frequenta(student, _disciplinaExecucao1);
    try {
      _suportePersistente.iniciarTransaccao();
      _frequentaPersistente.lockWrite(frequenta);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingFrequenta");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }

  // write new non-existing frequenta
  public void testCreateNonExistingFrequenta() {
    IFrequenta frequenta = new Frequenta();
    try {
    	_suportePersistente.iniciarTransaccao();
		IDisciplinaExecucao de =
			_disciplinaExecucaoPersistente
				.readBySiglaAndAnoLectivAndSiglaLicenciatura(
				_disciplinaExecucao2.getSigla(),
				_disciplinaExecucao2.getLicenciaturaExecucao().getAnoLectivo(),
				_disciplinaExecucao2
					.getLicenciaturaExecucao()
					.getCurso()
					.getSigla());
		IStudent aluno = persistentStudent.readByNumero(new Integer(600), licenciatura);
    	_suportePersistente.confirmarTransaccao();
 
    	frequenta.setAluno(aluno);
    	frequenta.setDisciplinaExecucao(de);

      _suportePersistente.iniciarTransaccao();
      _frequentaPersistente.lockWrite(frequenta);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingFrequenta");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testWriteExistingUnchangedObject() {
    // write item already mapped into memory
    
   IStudent student = null;
    
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(800), licenciatura);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
    }

    try {
    	_suportePersistente.iniciarTransaccao();
    	IFrequenta frequenta = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, _disciplinaExecucao1);
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      _frequentaPersistente.lockWrite(frequenta);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testWriteExistingChangedObject() {
    IFrequenta frequenta1 = null;
    IFrequenta frequenta2 = null;
      
      
   IStudent student = null;
    
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(800), licenciatura);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
    }

    // write frequenta already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	IDisciplinaExecucao de =
    		_disciplinaExecucaoPersistente
    			.readBySiglaAndAnoLectivAndSiglaLicenciatura(
    			_disciplinaExecucao2.getSigla(),
    			_disciplinaExecucao2.getLicenciaturaExecucao().getAnoLectivo(),
    			_disciplinaExecucao2
    				.getLicenciaturaExecucao()
    				.getCurso()
    				.getSigla());
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();

      frequenta1 = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, _disciplinaExecucao1);
      frequenta1.setDisciplinaExecucao(de);

      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      frequenta2 = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student,  _disciplinaExecucao2);
      _suportePersistente.confirmarTransaccao();
      assertEquals(frequenta2.getDisciplinaExecucao(), _disciplinaExecucao2);
    } catch (ExcepcaoPersistencia ex) {
        fail("testWriteExistingChangedObject");
      // all is ok
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testDeleteFrequenta() {

    IStudent student = null;
    
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(800), licenciatura);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
    }
    try {
    	_suportePersistente.iniciarTransaccao();
    	IFrequenta frequenta = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, _disciplinaExecucao1);
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      _frequentaPersistente.delete(frequenta);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      frequenta = _frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, _disciplinaExecucao1);
      _suportePersistente.confirmarTransaccao();

      assertEquals(frequenta, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteFrequenta");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.FrequentaOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      _frequentaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

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
    
    
}
