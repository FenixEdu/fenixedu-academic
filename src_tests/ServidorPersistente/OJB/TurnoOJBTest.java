/*
 * TurnoOJBTest.java
 * JUnit based test
 *
 * Created on 15 de Outubro de 2002, 8:59
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

import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.TipoAula;

public class TurnoOJBTest extends TestCaseOJB {
    public TurnoOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(TurnoOJBTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testReadByNameAndExecutionCourse() {
    ITurno shift = null;
    IDisciplinaExecucao executionCourse = null;
    try {
    	_suportePersistente.iniciarTransaccao();
    	executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
    	assertNotNull(executionCourse);
    	
        // Read Existing
        shift = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
        assertNotNull(shift);
		assertEquals(shift.getNome(), "turno1");
		assertEquals(shift.getDisciplinaExecucao(), executionCourse);


		// Read non Existing
        shift = null;
		shift = _turnoPersistente.readByNameAndExecutionCourse("turno10", executionCourse);
		assertNull(shift);
        
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNome:fail read existing turno");
    }
  }


  public void testCreateExistingTurno() {
 	IDisciplinaExecucao executionCourse = null;

	// Write existing

	try {
	   _suportePersistente.iniciarTransaccao();

	   executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	   assertNotNull(executionCourse);

	   ITurno shift = new Turno("turno1", new TipoAula(TipoAula.TEORICA), new Integer(100), executionCourse);
	   
	   _turnoPersistente.lockWrite(shift);
	   _suportePersistente.confirmarTransaccao();
	   fail("testCreateExistingTurno");
	 } catch (ExcepcaoPersistencia ex) {
	   //all is ok
	 }

	// Write non existing
	 
	try {
	   _suportePersistente.iniciarTransaccao();

	   ITurno shift = new Turno("turno10", new TipoAula(TipoAula.TEORICA), new Integer(100), executionCourse);
	   
	   _turnoPersistente.lockWrite(shift);
	   _suportePersistente.confirmarTransaccao();

	   // Check Insert
	   
	   _suportePersistente.iniciarTransaccao();
	   shift = _turnoPersistente.readByNameAndExecutionCourse("turno10", executionCourse);
	   assertNotNull(shift);
	   assertEquals(shift.getNome(), "turno10");
	   assertEquals(shift.getDisciplinaExecucao(), executionCourse);
	   _suportePersistente.confirmarTransaccao();

	 } catch (ExcepcaoPersistencia ex) {
		fail("testCreateExistingTurno");
	 }

	 
  }


  /** Test of write method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testWriteExistingChangedObject() {
	ITurno shift = null;
	IDisciplinaExecucao executionCourse = null;
	try {
		_suportePersistente.iniciarTransaccao();
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
    	
		// Read Existing
		shift = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift);

		assertEquals(shift.getLotacao(), new Integer(100));
		shift.setLotacao(new Integer(50));
		_suportePersistente.confirmarTransaccao();
		
		
		_suportePersistente.iniciarTransaccao();
		shift = null;
		shift = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift);
		
		assertEquals(shift.getNome(), "turno1");
		assertEquals(shift.getDisciplinaExecucao(), executionCourse);
		assertEquals(shift.getLotacao(), new Integer(50));

	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNome:fail read existing turno");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testDeleteTurno() {
	ITurno shift = null;
	IDisciplinaExecucao executionCourse = null;
	try {
		_suportePersistente.iniciarTransaccao();
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
    	
		// Read Existing
		shift = _turnoPersistente.readByNameAndExecutionCourse("turno454", executionCourse);
		assertNotNull(shift);
		
		// Check Lessons
		List lessons = SuportePersistenteOJB.getInstance().getITurnoAulaPersistente().readByShift(shift);
		assertEquals(lessons.size(), 2);
		_turnoPersistente.delete(shift);
		_suportePersistente.confirmarTransaccao();
		
		
		// Check Delete
		_suportePersistente.iniciarTransaccao();
		ITurno shift2 = null;
		shift2 = _turnoPersistente.readByNameAndExecutionCourse("turno454", executionCourse);
		assertNull(shift2);
		
		// Check deletion of classes
		lessons = null;
		lessons = SuportePersistenteOJB.getInstance().getITurnoAulaPersistente().readByShift(shift);
		assertTrue(lessons.isEmpty());
		
		
		_suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNome:fail read existing turno");
	}
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      _turnoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select turno from " + Turno.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurno");
    }

  }
    

  /** Test of querie2 method, of class ServidorPersistente.OJB.TurnoOJB. */

   public void testCountAllShiftsOfAllClassesAssociatedWithShift() {
	ITurno shift = null;
	IDisciplinaExecucao executionCourse = null;
	try {
		_suportePersistente.iniciarTransaccao();
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
    	
		// Read Existing
		shift = _turnoPersistente.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift);

		assertEquals(_turnoPersistente.countAllShiftsOfAllClassesAssociatedWithShift(shift), new Integer(0));

		shift = null;
		shift = _turnoPersistente.readByNameAndExecutionCourse("turno453", executionCourse);
		assertNotNull(shift);

		assertEquals(_turnoPersistente.countAllShiftsOfAllClassesAssociatedWithShift(shift), new Integer(3));


	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNome:fail read existing turno");
	}
  }  

/** Test of readByDisciplinaExecucaoAndType method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testReadByDisciplinaExecucaoAndType() {
    List shifts = null;
    IDisciplinaExecucao executionCourse = null;
    
    // read existing disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
	
	  // Read Existing      
      executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
      assertNotNull(executionCourse);
      
      shifts = _turnoPersistente.readByExecutionCourseAndType(executionCourse, new Integer(TipoAula.TEORICA));
      assertEquals("testReadByDisciplinaExecucaoAndType: Existing", shifts.size(), 4);

	  // Read non Existing
	  executionCourse = null;
	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("APR", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
      shifts = null;
	  shifts = _turnoPersistente.readByExecutionCourseAndType(executionCourse, new Integer(TipoAula.RESERVA));
	  assertTrue("testReadByDisciplinaExecucaoAndType: Existing", shifts.isEmpty());

	  _suportePersistente.confirmarTransaccao();

    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucaoAndType:fail read existing disciplinaExecucao");
    }
  }      
  
  public void testReadByExecutionCourse() {
	List shifts = null;
	IDisciplinaExecucao executionCourse = null;
    
	// read existing disciplinaExecucao
	try {
	  _suportePersistente.iniciarTransaccao();
	
	  // Read Existing      
	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
	  shifts = _turnoPersistente.readByExecutionCourse(executionCourse);
	  assertEquals("testReadByDisciplinaExecucaoAndType: Existing", shifts.size(), 10);

	  // Read non Existing
	  executionCourse = null;
	  executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("RCII", "2002/2003", "LEEC");
	  assertNotNull(executionCourse);
      
	  shifts = null;
	  shifts = _turnoPersistente.readByExecutionCourse(executionCourse);
	  assertTrue("testReadByDisciplinaExecucaoAndType: Existing", shifts.isEmpty());

	  _suportePersistente.confirmarTransaccao();

	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByDisciplinaExecucaoAndType:fail read existing disciplinaExecucao");
	}
  }      
  
  
  
}
