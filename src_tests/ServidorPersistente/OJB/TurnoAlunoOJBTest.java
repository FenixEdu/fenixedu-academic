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

import junit.framework.*;
import java.util.List;
import org.apache.ojb.odmg.OJB;
import org.odmg.QueryException;
import org.odmg.OQLQuery;
import org.odmg.Implementation;
import ServidorPersistente.*;
import Dominio.*;

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
    ITurnoAluno turnoAluno = null;
    // read existing TurnoAluno
    try {
      _suportePersistente.iniciarTransaccao();
      turnoAluno = _turnoAlunoPersistente.readByTurnoAndAluno(_turno3 ,_aluno1);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByTurnoAndAluno:fail read existing turnoAluno");
    }
    assertEquals("testReadByTurnoAndAluno:read existing TurnoAluno",turnoAluno,_turnoAluno1);
      
    // read unexisting TurnoAluno
    try {
      _suportePersistente.iniciarTransaccao();
      turnoAluno = _turnoAlunoPersistente.readByTurnoAndAluno(_turnoInexistente, _aluno1);
      assertNull(turnoAluno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByTurnoAndAluno:fail read unexisting TurnoAluno");
    }
  }

  /** Test of readByTurno method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testReadByTurno() {
	List alunos = null;
	// read existing Turno*
	try {
	  _suportePersistente.iniciarTransaccao();
	  alunos = _turnoAlunoPersistente.readByTurno(_turno3.getNome());
	  _suportePersistente.confirmarTransaccao();
	  assertEquals("testReadByTurno: Existing", alunos.size(), 1);
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurno:fail read existing turno*");
	}

	// read unexisting Turno*
	try {
	  _suportePersistente.iniciarTransaccao();
	  alunos = _turnoAlunoPersistente.readByTurno(_turnoInexistente.getNome());
	  assertTrue("testReadByTurno: Unexisting", alunos.isEmpty());
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByTurno:fail read unexisting Turno*");
	}
  }

  /** Test of readByStudentIdAndShiftType method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
  public void testreadByStudentIdAndShiftType() {
	ITurno turno = null;
	// read existing TurnoAluno
	try {
	  _suportePersistente.iniciarTransaccao();
	  turno = _turnoAlunoPersistente.readByStudentIdAndShiftType(_aluno1.getNumber(),
	  															 _turno3.getTipo(),
	  															 _turno3.getDisciplinaExecucao().getNome());
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByStudentIdAndShiftType:fail read existing turnoAula");
	}
	assertEquals("testReadByStudentIdAndShiftType:read existing TurnoAula", _turno3, turno);
      
	// read unexisting TurnoAluno
	try {
	  _suportePersistente.iniciarTransaccao();
	  turno = _turnoAlunoPersistente.readByStudentIdAndShiftType(_aluno1.getNumber(),
	  															 _turno2.getTipo(),
	  															 _turno2.getDisciplinaExecucao().getNome());
	  assertNull(turno);
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByStudentIdAndShiftType:fail read unexisting TurnoAula");
	}
  }

  // write new existing TurnoAluno
  public void testCreateExistingTurnoAluno() { 
    try {
      _suportePersistente.iniciarTransaccao();
      _turnoAlunoPersistente.lockWrite(_turnoAluno1);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingTurnoAluno");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }

  // write new non-existing TurnoAluno
  public void testCreateNonExistingTurmaTurno() {
    //ITurnoAluno turnoAluno = new TurnoAluno(_turnoInexistente,_aluno1 );
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurno turno5 = _turnoPersistente.readByNome(_turno5.getNome());
    	IStudent aluno1 = persistentStudent.readByNumero(_aluno1.getNumber(), licenciatura);
    	_suportePersistente.confirmarTransaccao();

    	ITurnoAluno turnoAluno = new TurnoAluno(turno5 , aluno1);

      _suportePersistente.iniciarTransaccao();
      _turnoAlunoPersistente.lockWrite(turnoAluno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingTurnoAluno");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testWriteExistingUnchangedObject() {
    // write TurnoAluno already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurnoAluno turnoAluno = _turnoAlunoPersistente.readByTurnoAndAluno(_turno3 ,_aluno1);
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      _turnoAlunoPersistente.lockWrite(turnoAluno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testWriteExistingChangedObject() {
    ITurnoAluno turnoAluno1 = null;
    ITurnoAluno turnoAluno2 = null;
      
    // write TurnoALuno already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurno turno5 = _turnoPersistente.readByNome(_turno5.getNome());
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turnoAluno1 = _turnoAlunoPersistente.readByTurnoAndAluno(_turno3, _aluno1);
      turnoAluno1.setTurno(turno5);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turnoAluno2 = _turnoAlunoPersistente.readByTurnoAndAluno(_turno5 , _aluno1);
      turnoAluno2 = _turnoAlunoPersistente.readByTurnoAndAluno(_turno5, _aluno1);
      _suportePersistente.confirmarTransaccao();

      assertTrue(turnoAluno2.getTurno().equals(turno5));
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurnoAlunoOJB. */
  public void testDeleteTurnoAluno() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurnoAluno turnoAluno = _turnoAlunoPersistente.readByTurnoAndAluno(_turno3 ,_aluno1);
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      _turnoAlunoPersistente.delete(turnoAluno);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turnoAluno = _turnoAlunoPersistente.readByTurnoAndAluno(_turno3, _aluno1);
      _suportePersistente.confirmarTransaccao();

      assertEquals(turnoAluno, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurnoAluno");
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
