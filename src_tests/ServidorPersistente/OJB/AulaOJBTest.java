/*
 * AulaOJBTest.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;


/**
 *
 * @author tfc130
 */
import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Implementation;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.Aula;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DiaSemana;
import Util.TipoAula;

public class AulaOJBTest extends TestCaseOJB {
  
  SuportePersistenteOJB persistentSupport = null; 
  ISalaPersistente persistentRoom = null;
  IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
  IAulaPersistente persistentLesson = null;
  IPersistentExecutionPeriod persistentExecutionPeriod = null;

  public AulaOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AulaOJBTest.class);

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
	persistentRoom = persistentSupport.getISalaPersistente();
	persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
	persistentLesson = persistentSupport.getIAulaPersistente();
	persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
	
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readByDiaSemanaAndInicioAndFimAndSala method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testReadByDiaSemanaAndInicioAndFimAndSala() {
    IAula lesson = null;
    ISala room = null;
    IDisciplinaExecucao executionCourse = null;
    
	DiaSemana weekDay = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
    
    Calendar startTime = Calendar.getInstance();
	startTime.set(Calendar.HOUR_OF_DAY, 8);
	startTime.set(Calendar.MINUTE, 00);
	startTime.set(Calendar.SECOND, 00);
	Calendar endTime = Calendar.getInstance();
	endTime.set(Calendar.HOUR_OF_DAY, 9);
	endTime.set(Calendar.MINUTE, 30);
	endTime.set(Calendar.SECOND, 00);

    
	try {
		persistentSupport.iniciarTransaccao();
		room = persistentRoom.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		lesson = persistentLesson.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
		fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read existing aula");
	}

	assertNotNull(lesson);
	assertTrue(lesson.getDiaSemana().getDiaSemana().equals(new Integer(DiaSemana.SEGUNDA_FEIRA)));
	
	assertEquals(lesson.getFim().get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.HOUR_OF_DAY));
	assertEquals(lesson.getFim().get(Calendar.MINUTE), endTime.get(Calendar.MINUTE));
	
	assertEquals(lesson.getInicio().get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.HOUR_OF_DAY));
	assertEquals(lesson.getInicio().get(Calendar.MINUTE), startTime.get(Calendar.MINUTE));
	
	assertEquals(lesson.getTipo().getTipo(), new Integer(TipoAula.TEORICA));
	
	assertEquals(lesson.getSala(), room);
	
	assertEquals(lesson.getDisciplinaExecucao(), executionCourse);
	
	    
    // read unexisting lesson
    try {
      persistentSupport.iniciarTransaccao();
      lesson = persistentLesson.readByDiaSemanaAndInicioAndFimAndSala(new DiaSemana(DiaSemana.DOMINGO), startTime, endTime, room);
      persistentSupport.confirmarTransaccao();
      assertNull("testReadByDiaSemanaAndInicioAndFimAndSala:fail read unexisting aula", lesson);
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read unexisting aula");
    }
  }

  // write new existing lesson
  public void testCreateExistingAula() {
	IAula lesson = new Aula();
	ISala room = null;
	IDisciplinaExecucao executionCourse = null;
    
	DiaSemana weekDay = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
    
	Calendar startTime = Calendar.getInstance();
	startTime.set(Calendar.HOUR_OF_DAY, 8);
	startTime.set(Calendar.MINUTE, 00);
	startTime.set(Calendar.SECOND, 00);
	Calendar endTime = Calendar.getInstance();
	endTime.set(Calendar.HOUR_OF_DAY, 9);
	endTime.set(Calendar.MINUTE, 30);
	endTime.set(Calendar.SECOND, 00);

	try {
		persistentSupport.iniciarTransaccao();
		room = persistentRoom.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
		fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read existing aula" + ex);
	}

	lesson.setDiaSemana(weekDay);
	lesson.setFim(endTime);
	lesson.setInicio(startTime);
	lesson.setSala(room);
	lesson.setDisciplinaExecucao(executionCourse);
	lesson.setTipo(new TipoAula(TipoAula.TEORICA));
	
    try {
      persistentSupport.iniciarTransaccao();
      persistentLesson.lockWrite(lesson);
      persistentSupport.confirmarTransaccao();
      fail("testCreateExistingAula");
    } catch (ExistingPersistentException eex) {
    	// all is ok
    	System.out.println("Caught ExistingPersistentException" + eex);
    } catch (ExcepcaoPersistencia ex) {
      fail("Caught ExcepcaoPersistencia" + ex);
    }
  }

  // write new non-existing aula
  public void testCreateNonExistingAula() {
    IAula lesson = null;
    
	Calendar startTime = Calendar.getInstance();
	startTime.set(Calendar.HOUR_OF_DAY, 8);
	startTime.set(Calendar.MINUTE, 00);
	startTime.set(Calendar.SECOND, 00);
	Calendar endTime = Calendar.getInstance();
	endTime.set(Calendar.HOUR_OF_DAY, 9);
	endTime.set(Calendar.MINUTE, 30);
	endTime.set(Calendar.SECOND, 00);

    
    try {
      persistentSupport.iniciarTransaccao();
      ISala room = persistentRoom.readByName("GA1");
      assertNotNull(room);
      
      IDisciplinaExecucao de = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(de);

	  persistentSupport.confirmarTransaccao();      	  
      	  
      lesson = new Aula(new DiaSemana(DiaSemana.DOMINGO), startTime, endTime, new TipoAula(TipoAula.TEORICA), room, de);

      persistentSupport.iniciarTransaccao();
      persistentLesson.lockWrite(lesson);
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingAula");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testWriteExistingUnchangedObject() {
    // write item already mapped into memory

	IAula lesson = null;
	ISala room = null;
	IDisciplinaExecucao executionCourse = null;
    
	DiaSemana weekDay = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
    
	Calendar startTime = Calendar.getInstance();
	startTime.set(Calendar.HOUR_OF_DAY, 8);
	startTime.set(Calendar.MINUTE, 00);
	startTime.set(Calendar.SECOND, 00);
	Calendar endTime = Calendar.getInstance();
	endTime.set(Calendar.HOUR_OF_DAY, 9);
	endTime.set(Calendar.MINUTE, 30);
	endTime.set(Calendar.SECOND, 00);

    
	try {
		persistentSupport.iniciarTransaccao();
		room = persistentRoom.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		lesson = persistentLesson.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
		fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read existing aula");
	}

	assertNotNull(lesson);

    try {
    	persistentSupport.iniciarTransaccao();
      	persistentLesson.lockWrite(lesson);
      	persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testWriteExistingChangedObject() {

	IAula lesson1 = null;
	IAula lesson2 = null;
	ISala room = null;
	IDisciplinaExecucao executionCourse = null;
    
	DiaSemana weekDay = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
    
	Calendar startTime = Calendar.getInstance();
	startTime.set(Calendar.HOUR_OF_DAY, 8);
	startTime.set(Calendar.MINUTE, 00);
	startTime.set(Calendar.SECOND, 00);
	Calendar endTime = Calendar.getInstance();
	endTime.set(Calendar.HOUR_OF_DAY, 9);
	endTime.set(Calendar.MINUTE, 30);
	endTime.set(Calendar.SECOND, 00);

    
	try {
		persistentSupport.iniciarTransaccao();
		room = persistentRoom.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		lesson1 = persistentLesson.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
		assertNotNull(lesson1);
		lesson1.setDiaSemana(new DiaSemana(DiaSemana.DOMINGO));
	
		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
		fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read existing aula");
	}

	
	try {
		persistentSupport.iniciarTransaccao();
		lesson2 = persistentLesson.readByDiaSemanaAndInicioAndFimAndSala(new DiaSemana(DiaSemana.DOMINGO), startTime, endTime, room);
		assertNotNull(lesson2);
		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
		fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read existing aula");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testDeleteAula() {
	IAula lesson = null;
	ISala room = null;
	IDisciplinaExecucao executionCourse = null;
    
	DiaSemana weekDay = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
    
	Calendar startTime = Calendar.getInstance();
	startTime.set(Calendar.HOUR_OF_DAY, 8);
	startTime.set(Calendar.MINUTE, 00);
	startTime.set(Calendar.SECOND, 00);
	Calendar endTime = Calendar.getInstance();
	endTime.set(Calendar.HOUR_OF_DAY, 9);
	endTime.set(Calendar.MINUTE, 30);
	endTime.set(Calendar.SECOND, 00);

    
	try {
		persistentSupport.iniciarTransaccao();
		room = persistentRoom.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		lesson = persistentLesson.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
		persistentSupport.confirmarTransaccao();
	
		assertNotNull(lesson);

       persistentSupport.iniciarTransaccao();
       persistentLesson.delete(lesson);
       persistentSupport.confirmarTransaccao();

		lesson = null;

       persistentSupport.iniciarTransaccao();
       lesson = persistentLesson.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
       persistentSupport.confirmarTransaccao();

      assertNull("testDeleteAula", lesson);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAula");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testDeleteAll() {
    try {
      persistentSupport.iniciarTransaccao();
      persistentLesson.deleteAll();
      persistentSupport.confirmarTransaccao();

      persistentSupport.iniciarTransaccao();

      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();
        String oqlQuery = "select aula from " + Aula.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      persistentSupport.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAula");
    }

  }

  /** Test of readByDisciplinaExecucao method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testreadByDisciplinaExecucao() {
    List lessons = null;
    // read existing disciplinaExecucao
    
	IDisciplinaExecucao executionCourse1 = null;
	IDisciplinaExecucao executionCourse2 = null;
    
	try {
		persistentSupport.iniciarTransaccao();
		executionCourse1 = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse1);

		executionCourse2 = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("RCII", "2002/2003", "LEEC");
		assertNotNull(executionCourse2);

        lessons = persistentLesson.readByExecutionCourse(executionCourse1);
        assertEquals("testReadByDisciplinaExecucao: Existing", 6, lessons.size());
  
  
  		// read unexisting disciplinaExecucao
    
        lessons = persistentLesson.readByExecutionCourse(executionCourse2);
        assertTrue("testReadByDisciplinaExecucao: Unexisting", lessons.isEmpty());
        persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucao:fail read unexisting disciplinaExecucao");
    }
  }

  public void testReadByRoomAndExecutionPeriod() {
	
	  ISala room = null;
	  IExecutionPeriod executionPeriod = null;
	  List lessons = null;
	  
	  try {
		persistentSupport.iniciarTransaccao();
	  	room = persistentRoom.readByName("GA1");
	  	assertNotNull(room);
	  	
	  	executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
	  	assertNotNull(executionPeriod);
	  	
	  	lessons = persistentLesson.readByRoomAndExecutionPeriod(room, executionPeriod);
	  	assertNotNull(lessons);
	  	assertEquals(lessons.size(), 21);

		room = null;
		room = persistentRoom.readByName("GA2");
		assertNotNull(room);

		lessons = null;
		lessons = persistentLesson.readByRoomAndExecutionPeriod(room, executionPeriod);
		assertNotNull(lessons);
		assertEquals(lessons.size(), 7);


		persistentSupport.confirmarTransaccao();
	  	
	  } catch (ExcepcaoPersistencia ex) {
		fail("testReadByRoomAndExecutionPeriod : fail");
  	  }
  }
  
  public void testReadByExecutionCourseAndLessonType() {
	
	  IDisciplinaExecucao executionCourse = null;
	  List lessons = null;
	  
	  try {
		persistentSupport.iniciarTransaccao();
	  	
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
	  	
		lessons = persistentLesson.readByExecutionCourseAndLessonType(executionCourse, new TipoAula(TipoAula.TEORICA));
		assertNotNull(lessons);
		assertEquals(lessons.size(), 6);
		
		lessons = null;
		lessons = persistentLesson.readByExecutionCourseAndLessonType(executionCourse, new TipoAula(TipoAula.PRATICA));
		assertNotNull(lessons);
		assertEquals(lessons.size(), 0);
		
		persistentSupport.confirmarTransaccao();
	  	
	  } catch (ExcepcaoPersistencia ex) {
		fail("testReadByRoomAndExecutionPeriod : fail");
	  }
  }
  
  public void testReadLessonsInPeriod() {
	
	// FIXME: Test not done...
	
  }
	

  public void testReadLessonsInBroadPeriod() {
  	
  	try {
		persistentSupport.iniciarTransaccao();
		ISala room = persistentRoom.readByName("Ga1");
		assertNotNull(room);
		persistentSupport.confirmarTransaccao();

		IAula lesson = new Aula();
		lesson.setDiaSemana(new DiaSemana(DiaSemana.SEGUNDA_FEIRA));
		lesson.setSala(room);
		
		Calendar startTime = null;
		Calendar endTime = null;

		// Test 1: new lesson is containded is existing one		
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 8);
		startTime.set(Calendar.MINUTE, 30);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 9);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		List result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 1);


		// Test 2: new lesson starts and finishes before existing one		
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 7);
		startTime.set(Calendar.MINUTE, 30);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 9);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 1);


		// Test 3: new lesson starts and finishes at exactly the same time	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 8);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 9);
		endTime.set(Calendar.MINUTE, 30);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 1);


		// Test 4: new lesson starts and finishes after existing one	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 8);
		startTime.set(Calendar.MINUTE, 30);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 10);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 1);


		// Test 5: new lesson starts before existing one and finishes at the same time	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 7);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 8);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 0);

		// Test 6: new lesson starts at the same time and finishes after existing one	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 7);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 8);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 0);




		// Test 7: new lesson starts before existing one and finishes after existing one 	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 7);
		startTime.set(Calendar.MINUTE, 30);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 10);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 1);


		// Test 8: new lesson starts at exactly the same time and finishes before existing one 	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 8);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 9);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 1);


		// Test 9: new lesson starts at exactly the same time and finishes after existing one 	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 8);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 10);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 1);


		// Test 10: new lesson starts after existing one and finishes exactly ate the same time 	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 9);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 9);
		endTime.set(Calendar.MINUTE, 30);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 1);


		// Test 11: new lesson starts before existing one and finishes exactly ate the same time 	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 7);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 9);
		endTime.set(Calendar.MINUTE, 30);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 1);


		// Test 12: new lesson starts and finishes before existing one  	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 7);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 7);
		endTime.set(Calendar.MINUTE, 30);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 0);


		// Test 13: new lesson starts and finishes after existing one  	
		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 10);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 10);
		endTime.set(Calendar.MINUTE, 30);
		endTime.set(Calendar.SECOND, 0);

		lesson.setInicio(startTime);
		lesson.setFim(endTime);

		persistentSupport.iniciarTransaccao();
		result = persistentLesson.readLessonsInBroadPeriod(lesson, null);
		persistentSupport.confirmarTransaccao();
		assertNotNull(result);
		assertEquals(result.size(), 0);
  	} catch (ExcepcaoPersistencia ex) {
		fail("testReadByRoomAndExecutionPeriod : fail");
    }
  }

  public void testReadLessonsInBroadPeriodInAnyRoom() {
  	try {
  		// will contain Query result
  		List lessonsInBroadPeriodInAnyRoom = null;

		// prepare query input
		IAula lesson = new Aula();
		lesson.setDiaSemana(new DiaSemana(DiaSemana.SEGUNDA_FEIRA));
		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, 8);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		lesson.setInicio(start);
		Calendar end = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, 9);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		lesson.setFim(end);

		// execute query
		persistentSupport.iniciarTransaccao();
		lessonsInBroadPeriodInAnyRoom = persistentLesson.readLessonsInBroadPeriodInAnyRoom(lesson);
		persistentSupport.confirmarTransaccao();

		assertNotNull("testReadLessonsInBroadPeriodInAnyRoom: result was null", lessonsInBroadPeriodInAnyRoom);
		assertEquals(4, lessonsInBroadPeriodInAnyRoom.size());

		// prepare query input
		start.set(Calendar.HOUR_OF_DAY, 9);
		lesson.setInicio(start);
		start.set(Calendar.HOUR_OF_DAY, 10);
		start.set(Calendar.MINUTE, 30);
		lesson.setFim(end);

		// execute query
		persistentSupport.iniciarTransaccao();
		lessonsInBroadPeriodInAnyRoom = persistentLesson.readLessonsInBroadPeriodInAnyRoom(lesson);
		persistentSupport.confirmarTransaccao();

		assertNotNull("testReadLessonsInBroadPeriodInAnyRoom: result was null", lessonsInBroadPeriodInAnyRoom);
		assertEquals(2, lessonsInBroadPeriodInAnyRoom.size());

		// prepare query input
		lesson.setDiaSemana(new DiaSemana(DiaSemana.SEXTA_FEIRA));
		start.set(Calendar.HOUR_OF_DAY, 19);
		lesson.setInicio(start);
		start.set(Calendar.HOUR_OF_DAY, 20);
		start.set(Calendar.MINUTE, 0);
		lesson.setFim(end);

		// execute query
		persistentSupport.iniciarTransaccao();
		lessonsInBroadPeriodInAnyRoom = persistentLesson.readLessonsInBroadPeriodInAnyRoom(lesson);
		persistentSupport.confirmarTransaccao();

		assertNotNull("testReadLessonsInBroadPeriodInAnyRoom: result was null", lessonsInBroadPeriodInAnyRoom);
		assertEquals(0, lessonsInBroadPeriodInAnyRoom.size());
	} catch (ExcepcaoPersistencia ex) {
		fail("testReadLessonsInBroadPeriodInAnyRoom: Unexpected Exception :" + ex);
	}
  	
  }

}
