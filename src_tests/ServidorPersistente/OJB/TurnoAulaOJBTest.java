/*
 * TurnoAulaOJBTest.java
 * JUnit based test
 *
 * Created on 22 de Outubro de 2002, 9:23
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

public class TurnoAulaOJBTest extends TestCaseOJB {
    public TurnoAulaOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(TurnoAulaOJBTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readByTurnoAndAula method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
  public void testreadByTurnoAndAula() {
    ITurnoAula turnoAula = null;
    // read existing TurnoAula
    try {
      _suportePersistente.iniciarTransaccao();
      turnoAula = _turnoAulaPersistente.readByTurnoAndAula(_turno3, _aula1);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByTurnoAndAula:fail read existing turnoAula");
    }
    assertEquals("testReadByTurnoAndAula:read existing TurnoAula",turnoAula,_turnoAula1);
      
    // read unexisting TurnoAula
    try {
      _suportePersistente.iniciarTransaccao();
      turnoAula = _turnoAulaPersistente.readByTurnoAndAula(_turnoInexistente, _aula1);
      assertNull(turnoAula);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByTurnoAndAula:fail read unexisting TurnoAula");
    }
  }

  // write new existing TurnoAula
  public void testCreateExistingTurnoAula() { 
    ITurnoAula turnoAula = new TurnoAula(_turno3, _aula1);
    try {
      _suportePersistente.iniciarTransaccao();
      _turnoAulaPersistente.lockWrite(turnoAula);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingTurnoAula");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }

  // write new non-existing TurnoAula
  public void testCreateNonExistingTurnoAula() {
    ITurnoAula turnoAula = null;
    try {
    	_suportePersistente.iniciarTransaccao();
    	IAula aula = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
    	ITurno turno = _turnoPersistente.readByNome(_turno1.getNome());
    	_suportePersistente.confirmarTransaccao();

    	turnoAula = new TurnoAula(turno, aula);

    	_suportePersistente.iniciarTransaccao();
    	_turnoAulaPersistente.lockWrite(turnoAula);
    	_suportePersistente.confirmarTransaccao();
    	
//      assertTrue(((Item)item).getCodigoInterno() != 0);
    } catch (ExcepcaoPersistencia ex) {
    	fail("testCreateNonExistingTurnoAula");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
  public void testWriteExistingUnchangedObject() {
    // write TurnoAula already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurnoAula turnoAula = _turnoAulaPersistente.readByTurnoAndAula(_turno3, _aula1);
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      _turnoAulaPersistente.lockWrite(turnoAula);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
  public void testWriteExistingChangedObject() {
    ITurnoAula turnoAula1 = null;
    ITurnoAula turnoAula2 = null;
      
    // write TurnoALuno already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	IAula aula1 = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
    	ITurno turno3 = _turnoPersistente.readByNome(_turno3.getNome());
    	ITurno turno5 = _turnoPersistente.readByNome(_turno5.getNome());
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turnoAula1 = _turnoAulaPersistente.readByTurnoAndAula(turno3, aula1);
      turnoAula1.setTurno(turno5);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turnoAula2 = _turnoAulaPersistente.readByTurnoAndAula(turno5, aula1);
      _suportePersistente.confirmarTransaccao();

      assertTrue(turnoAula2.getTurno().equals(turno5));
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
  public void testDeleteTurnoAula() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurnoAula turnoAula = _turnoAulaPersistente.readByTurnoAndAula(_turno3, _aula1);
    	_suportePersistente.confirmarTransaccao();
    	
      _suportePersistente.iniciarTransaccao();
      _turnoAulaPersistente.delete(turnoAula);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turnoAula = _turnoAulaPersistente.readByTurnoAndAula(_turno3, _aula1);
      _suportePersistente.confirmarTransaccao();

      assertEquals(turnoAula, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurnoAula");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      _turnoAulaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select turnoAula from " + TurnoAula.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurnoAula");
    }

  }


    public void testReadAulasDeTurno() {
    try {
      List aulas = null;

      /* Testa metodo qdo ha mais do q uma aula de um turno na BD */
      _suportePersistente.iniciarTransaccao();
      aulas = _turnoAulaPersistente.readByShift(new Turno("455",null, null, null));
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadAulasDeTurno: qdo ha 2 aulas da turma na BD", aulas.size(),2);

        /* Testa metodo qdo nao ha nenhuma aula do turno */
      _suportePersistente.iniciarTransaccao();
      _turnoAulaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();
      
      _suportePersistente.iniciarTransaccao();
      aulas = _turnoAulaPersistente.readByShift(new Turno("turno1",null, null, null));
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadAulasDeTurno: qdo nao nenhuma aula do turno na BD", aulas.size(),0);

    } catch (ExcepcaoPersistencia ex) {
      fail("testReadAulasDeTurno");
    }
  }      
    

  /** Test of delete method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
  public void testDeleteTurnoAulaByKeys() {
//    try {
//      _suportePersistente.iniciarTransaccao();
//      _turnoAulaPersistente.delete(_turno3.getNome(), _aula1.getDiaSemana(), _aula1.getInicio(),
//                                   _aula1.getFim(), _aula1.getSala());
//      _suportePersistente.confirmarTransaccao();
//
//      _suportePersistente.iniciarTransaccao();
//      IAula aula1 = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
//      ITurno turno3 = _turnoPersistente.readByNome(_turno3.getNome());
//      _suportePersistente.confirmarTransaccao();
// 
//      _suportePersistente.iniciarTransaccao();
//      ITurnoAula turnoAula = _turnoAulaPersistente.readByTurnoAndAula(turno3, aula1);
//      _suportePersistente.confirmarTransaccao();
//
//      assertEquals(turnoAula, null);
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testDeleteTurnoAula");
//    }
  }    

  	/** Test of ReadLessonsByStudent method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
	public void testReadLessonsByStudent() {
		try {
			List lessons = null;

			_suportePersistente.iniciarTransaccao();
			lessons = _turnoAulaPersistente.readLessonsByStudent("45498");
			_suportePersistente.confirmarTransaccao();
			assertNotNull("testReadLessonsByStudent", lessons);
			assertEquals("testReadLessonsByStudent", lessons.size(), 2);

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadLessonsByStudent");
		}
	}

	/** Test of ReadLessonsByShift method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
 	public void testReadLessonsByShift() {
//		  try {
//			  List lessons = null;
//
//			  _suportePersistente.iniciarTransaccao();
//			  lessons = _turnoAulaPersistente.readLessonsByShift("turno_apr_teorico1");
//			  _suportePersistente.confirmarTransaccao();
//			  assertNotNull("testReadLessonsByStudent", lessons);
//			  assertEquals("testReadLessonsByStudent", lessons.size(), 1);
//
//		  } catch (ExcepcaoPersistencia ex) {
			  fail("testReadLessonsByStudent");
//		  }
	  }

}
