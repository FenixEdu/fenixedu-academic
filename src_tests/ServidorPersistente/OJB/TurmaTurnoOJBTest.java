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

import junit.framework.*;
import java.util.List;
import org.apache.ojb.odmg.OJB;
import org.odmg.QueryException;
import org.odmg.OQLQuery;
import org.odmg.Implementation;
import ServidorPersistente.*;
import Dominio.*;

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
      turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(_turma1, _turno1);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByTurmaAndTurno:fail read existing turmaTurno");
    }
    assertEquals("testReadByTurmaAndTurno:read existing aula",turmaTurno,_turmaTurno1);
      
    // read unexisting TurmaTurno
    try {
      _suportePersistente.iniciarTransaccao();
      turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(_turma1, _turno2);
      assertNull(turmaTurno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByTurmaAndTurno:fail read unexisting turmaTurno");
    }
  }

  // write new existing turmaTurno
  public void testCreateExistingTurmaTurno() { 
    ITurmaTurno turmaTurno = new TurmaTurno(_turma1, _turno1);
    try {
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
    ITurmaTurno turmaTurno = null;
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurma turma2 = _turmaPersistente.readByNome(_turma2.getNome());
    	ITurno turno1 = _turnoPersistente.readByNome(_turno1.getNome());
    	_suportePersistente.confirmarTransaccao();

    	turmaTurno = new TurmaTurno(turma2, turno1);

      _suportePersistente.iniciarTransaccao();
      _turmaTurnoPersistente.lockWrite(turmaTurno);
      _suportePersistente.confirmarTransaccao();
      //      assertTrue(((Item)item).getCodigoInterno() != 0);
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingTurmaTurno");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testWriteExistingUnchangedObject() {
    // write item already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurmaTurno turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(_turma1, _turno1);
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      _turmaTurnoPersistente.lockWrite(turmaTurno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testWriteExistingChangedObject() {
    ITurmaTurno turmaTurno1 = null;
    ITurmaTurno turmaTurno2 = null;
      
    // write item already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurno turno2 = _turnoPersistente.readByNome(_turno2.getNome());
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turmaTurno1 = _turmaTurnoPersistente.readByTurmaAndTurno(_turma1, _turno1);
      turmaTurno1.setTurno(turno2);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turmaTurno2 = _turmaTurnoPersistente.readByTurmaAndTurno(_turma1, _turno2);
      _suportePersistente.confirmarTransaccao();

      assertTrue(turmaTurno2.getTurno().equals(_turno2));
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testDeleteTurmaTurno() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurmaTurno turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(_turma1, _turno1);
    	_suportePersistente.confirmarTransaccao();    	
    	
      _suportePersistente.iniciarTransaccao();
      _turmaTurnoPersistente.delete(turmaTurno);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(_turma1, _turno1);
      _suportePersistente.confirmarTransaccao();

      assertEquals(turmaTurno, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurmaTurno");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurmaTurnoOJB. */
  public void testDeleteTurmaTurnoByKeys() {
    try {
      _suportePersistente.iniciarTransaccao();
      _turmaTurnoPersistente.delete(_turma1.getNome(), _turno1.getNome());
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      ITurmaTurno turmaTurno = _turmaTurnoPersistente.readByTurmaAndTurno(_turma1, _turno1);
      _suportePersistente.confirmarTransaccao();

      assertEquals(turmaTurno, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurmaTurno");
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

  public void testReadTurnosDeTurma() {
    try {
      List turnos = null;

      _suportePersistente.iniciarTransaccao();
      ITurma turma1 = _turmaPersistente.readByNome(_turma1.getNome());
      ITurno turno1 = _turnoPersistente.readByNome(_turno1.getNome());
      ITurno turno2 = _turnoPersistente.readByNome(_turno2.getNome());
      _suportePersistente.confirmarTransaccao();      

      ITurmaTurno turmaTurno1 = new TurmaTurno(turma1, turno1);
      ITurmaTurno turmaTurno2 = new TurmaTurno(turma1, turno2);      
        /* Testa metodo qdo nao ha nenhuma turno da turma */
      _suportePersistente.iniciarTransaccao();
      _turmaTurnoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();
      
      _suportePersistente.iniciarTransaccao();
      turnos = _turmaTurnoPersistente.readTurnosDeTurma(_turma1.getNome());
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadTurnosDeTurma: qdo nao nenhum turno da turma na BD", turnos.size(),0);

      /* Testa metodo qdo ha mais do q um turno de uma turma na BD */      
      _suportePersistente.iniciarTransaccao();
      _turmaTurnoPersistente.lockWrite(turmaTurno1);
      _turmaTurnoPersistente.lockWrite(turmaTurno2);
      _suportePersistente.confirmarTransaccao();
      _suportePersistente.iniciarTransaccao();
      turnos = _turmaTurnoPersistente.readTurnosDeTurma(_turma1.getNome());
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadTurnosDeTurma: 1qdo ha 2 turnos da turma na BD", turnos.size(),2);
      assertEquals("testReadTurnosDeTurma: qdo ha 2 turnos da turma na BD", turnos.get(0), _turno1);
      assertEquals("testReadTurnosDeTurma: qdo ha 2 turnos da turma na BD", turnos.get(1), _turno2);
      // Estranhamente o seguinte não funciona neste caso! Mas é o que deveria ser utilizado
      //assertTrue("testReadTurnosDeTurma: 2qdo ha 2 turnos da turma na BD", turnos.contains(_turno1));
      //assertTrue("testReadTurnosDeTurma: 3qdo ha 2 turnos da turma na BD", turnos.contains(_turno2));

    } catch (ExcepcaoPersistencia ex) {
      fail("testReadTurnosDeTurma");
    }
  }

  public void testReadAulasByTurma() {
    try {
      List aulas = null;

      /* Testa metodo qdo ha mais do q um turno de uma turma na BD */
      _suportePersistente.iniciarTransaccao();
      aulas = _turmaTurnoPersistente.readAulasByTurma("turma413");
      _suportePersistente.confirmarTransaccao();

      assertEquals("testReadAulasByTurma: qdo ha 2 aulas da turma na BD", aulas.size(),2);

      _suportePersistente.iniciarTransaccao();
      aulas = _turmaTurnoPersistente.readAulasByTurma("turma414");
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadAulasByTurma: qdo ha 3 aulas da turma na BD", aulas.size(),3);
      
      /* Testa metodo qdo nao ha nenhuma turno da turma */
      _suportePersistente.iniciarTransaccao();
      _turmaTurnoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();
      
      _suportePersistente.iniciarTransaccao();
      aulas = _turmaTurnoPersistente.readTurnosDeTurma("turma413");
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadAulasByTurma: qdo nao nenhum turno da turma na BD", aulas.size(),0);
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadAulasByTurma");
    }
  }    

}
