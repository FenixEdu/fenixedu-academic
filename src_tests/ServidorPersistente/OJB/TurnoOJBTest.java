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
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoAula;

public class TurnoOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null;
	ITurnoPersistente persistentShift = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	
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
	try {
		persistentSupport = SuportePersistenteOJB.getInstance();
	} catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		fail("Error");
	}
	persistentShift = persistentSupport.getITurnoPersistente();
	persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testReadByNameAndExecutionCourse() {
    ITurno shift = null;
    IDisciplinaExecucao executionCourse = null;
    try {
    	persistentSupport.iniciarTransaccao();
    	executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
    	assertNotNull(executionCourse);
    	
        // Read Existing
        shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
        assertNotNull(shift);
		assertEquals(shift.getNome(), "turno1");
		assertEquals(shift.getDisciplinaExecucao(), executionCourse);


		// Read non Existing
        shift = null;
		shift = persistentShift.readByNameAndExecutionCourse("turno10", executionCourse);
		assertNull(shift);
        
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNome:fail read existing turno");
    }
  }


  public void testCreateExistingTurno() {
	IDisciplinaExecucao executionCourse = null;

	// Write existing

	try {
		persistentSupport.iniciarTransaccao();

		executionCourse =
			persistentExecutionCourse
				.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
				"TFCI",
				"2002/2003",
				"LEIC");
		assertNotNull(executionCourse);

		ITurno shift =
			new Turno(
				"turno1",
				new TipoAula(TipoAula.TEORICA),
				new Integer(100),
				executionCourse);

		persistentShift.lockWrite(shift);
		persistentSupport.confirmarTransaccao();
		fail("testCreateExistingTurno: Expected an Exception");
	} catch (ExistingPersistentException ex) {
		assertNotNull("testCreateExistingTurno");
	} catch (ExcepcaoPersistencia ex) {
		fail("testCreateExistingTurno: Unexpected Exception");
	}
	// Write non existing
	 
	try {
	   persistentSupport.iniciarTransaccao();

	   ITurno shift = new Turno("turno10", new TipoAula(TipoAula.TEORICA), new Integer(100), executionCourse);
	   
	   persistentShift.lockWrite(shift);
	   persistentSupport.confirmarTransaccao();

	   // Check Insert
	   
	   persistentSupport.iniciarTransaccao();
	   shift = persistentShift.readByNameAndExecutionCourse("turno10", executionCourse);
	   assertNotNull(shift);
	   assertEquals(shift.getNome(), "turno10");
	   assertEquals(shift.getDisciplinaExecucao(), executionCourse);
	   persistentSupport.confirmarTransaccao();

	 } catch (ExcepcaoPersistencia ex) {
		fail("testCreateExistingTurno");
	 }

	 
  }


  /** Test of write method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testWriteExistingChangedObject() {
	ITurno shift = null;
	IDisciplinaExecucao executionCourse = null;
	try {
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
    	
		// Read Existing
		shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift);

		assertEquals(shift.getLotacao(), new Integer(100));
		shift.setLotacao(new Integer(50));
		persistentSupport.confirmarTransaccao();
		
		
		persistentSupport.iniciarTransaccao();
		shift = null;
		shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift);
		
		assertEquals(shift.getNome(), "turno1");
		assertEquals(shift.getDisciplinaExecucao(), executionCourse);
		assertEquals(shift.getLotacao(), new Integer(50));

	  persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNome:fail read existing turno");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testDeleteTurno() {
	ITurno shift = null;
	IDisciplinaExecucao executionCourse = null;
	try {
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
    	
		// Read Existing
		shift = persistentShift.readByNameAndExecutionCourse("turno454", executionCourse);
		assertNotNull(shift);
		
		// Check Lessons
		List lessons = SuportePersistenteOJB.getInstance().getITurnoAulaPersistente().readByShift(shift);
		assertEquals(lessons.size(), 2);
		persistentShift.delete(shift);
		persistentSupport.confirmarTransaccao();
		
		
		// Check Delete
		persistentSupport.iniciarTransaccao();
		ITurno shift2 = null;
		shift2 = persistentShift.readByNameAndExecutionCourse("turno454", executionCourse);
		assertNull(shift2);
		
		// Check deletion of classes
		lessons = null;
		lessons = SuportePersistenteOJB.getInstance().getITurnoAulaPersistente().readByShift(shift);
		assertTrue(lessons.isEmpty());
		
		
		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNome:fail read existing turno");
	}
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testDeleteAll() {
    try {
      persistentSupport.iniciarTransaccao();
      persistentShift.deleteAll();
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

        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select turno from " + Turno.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      persistentSupport.confirmarTransaccao();
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
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
    	
		// Read Existing
		shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
		assertNotNull(shift);

		assertEquals(persistentShift.countAllShiftsOfAllClassesAssociatedWithShift(shift), new Integer(0));

		shift = null;
		shift = persistentShift.readByNameAndExecutionCourse("turno453", executionCourse);
		assertNotNull(shift);

		assertEquals(persistentShift.countAllShiftsOfAllClassesAssociatedWithShift(shift), new Integer(3));


	  persistentSupport.confirmarTransaccao();
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
      persistentSupport.iniciarTransaccao();
	
	  // Read Existing      
      executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
      assertNotNull(executionCourse);
      
      shifts = persistentShift.readByExecutionCourseAndType(executionCourse, new Integer(TipoAula.TEORICA));
      assertEquals("testReadByDisciplinaExecucaoAndType: Existing", shifts.size(), 5);

	  // Read non Existing
	  executionCourse = null;
	  executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("APR", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
      shifts = null;
	  shifts = persistentShift.readByExecutionCourseAndType(executionCourse, new Integer(TipoAula.RESERVA));
	  assertTrue("testReadByDisciplinaExecucaoAndType: Existing", shifts.isEmpty());

	  persistentSupport.confirmarTransaccao();

    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucaoAndType:fail read existing disciplinaExecucao");
    }
  }      
  
  public void testReadByExecutionCourse() {
	List shifts = null;
	IDisciplinaExecucao executionCourse = null;
    
	// read existing disciplinaExecucao
	try {
	  persistentSupport.iniciarTransaccao();
	
	  // Read Existing      
	  executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(executionCourse);
      
	  shifts = persistentShift.readByExecutionCourse(executionCourse);
	  assertEquals("testReadByDisciplinaExecucaoAndType: Existing", shifts.size(), 10);

	  // Read non Existing
	  executionCourse = null;
	  executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("RCII", "2002/2003", "MEEC");
	  assertNotNull(executionCourse);
      
	  shifts = null;
	  shifts = persistentShift.readByExecutionCourse(executionCourse);
	  assertTrue("testReadByDisciplinaExecucaoAndType: Existing", shifts.isEmpty());

	  persistentSupport.confirmarTransaccao();

	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByDisciplinaExecucaoAndType:fail read existing disciplinaExecucao");
	}
  }      
  
  
  
}
