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
import Util.DiaSemana;
import Util.TipoAula;

public class AulaOJBTest extends TestCaseOJB {
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
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readByDiaSemanaAndInicioAndFimAndSala method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testreadByDiaSemanaAndInicioAndFimAndSala() {
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
		_suportePersistente.iniciarTransaccao();
		room = _salaPersistente.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		lesson = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
		_suportePersistente.confirmarTransaccao();
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
      _suportePersistente.iniciarTransaccao();
      lesson = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(new DiaSemana(DiaSemana.DOMINGO), startTime, endTime, room);
      _suportePersistente.confirmarTransaccao();
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
		_suportePersistente.iniciarTransaccao();
		room = _salaPersistente.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		_suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
		fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read existing aula");
	}

	lesson.setDiaSemana(weekDay);
	lesson.setFim(endTime);
	lesson.setInicio(startTime);
	lesson.setSala(room);
		
	
    try {
      _suportePersistente.iniciarTransaccao();
      _aulaPersistente.lockWrite(lesson);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingAula");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
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
      _suportePersistente.iniciarTransaccao();
      ISala room = _salaPersistente.readByName("GA1");
      assertNotNull(room);
      
      IDisciplinaExecucao de = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
	  assertNotNull(de);

	  _suportePersistente.confirmarTransaccao();      	  
      	  
      lesson = new Aula(new DiaSemana(DiaSemana.DOMINGO), startTime, endTime, new TipoAula(TipoAula.TEORICA), room, de);

      _suportePersistente.iniciarTransaccao();
      _aulaPersistente.lockWrite(lesson);
      _suportePersistente.confirmarTransaccao();
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
		_suportePersistente.iniciarTransaccao();
		room = _salaPersistente.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		lesson = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
		_suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
		fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read existing aula");
	}

	assertNotNull(lesson);

    try {
    	_suportePersistente.iniciarTransaccao();
      	_aulaPersistente.lockWrite(lesson);
      	_suportePersistente.confirmarTransaccao();
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
		_suportePersistente.iniciarTransaccao();
		room = _salaPersistente.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		lesson1 = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
		assertNotNull(lesson1);
		lesson1.setDiaSemana(new DiaSemana(DiaSemana.DOMINGO));
	
		_suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
		fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read existing aula");
	}

	
	try {
		_suportePersistente.iniciarTransaccao();
		lesson2 = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(new DiaSemana(DiaSemana.DOMINGO), startTime, endTime, room);
		assertNotNull(lesson2);
		_suportePersistente.confirmarTransaccao();
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
		_suportePersistente.iniciarTransaccao();
		room = _salaPersistente.readByName("GA1");
		assertNotNull(room);
		
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
		lesson = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
		_suportePersistente.confirmarTransaccao();
	
		assertNotNull(lesson);

       _suportePersistente.iniciarTransaccao();
       _aulaPersistente.delete(lesson);
       _suportePersistente.confirmarTransaccao();

		lesson = null;

       _suportePersistente.iniciarTransaccao();
       lesson = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
       _suportePersistente.confirmarTransaccao();

      assertNull("testDeleteAula", lesson);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAula");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      _aulaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();

      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select aula from " + Aula.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
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
		_suportePersistente.iniciarTransaccao();
		executionCourse1 = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse1);

		executionCourse2 = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("RCII", "2002/2003", "LEEC");
		assertNotNull(executionCourse2);

        lessons = _aulaPersistente.readByExecutionCourse(executionCourse1);
        assertEquals("testReadByDisciplinaExecucao: Existing", 5, lessons.size());
  
  
  		// read unexisting disciplinaExecucao
    
        lessons = _aulaPersistente.readByExecutionCourse(executionCourse2);
        assertTrue("testReadByDisciplinaExecucao: Unexisting", lessons.isEmpty());
        _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucao:fail read unexisting disciplinaExecucao");
    }
  }

  public void testReadByRoomAndExecutionPeriod() {
	
	  ISala room = null;
	  IExecutionPeriod executionPeriod = null;
	  List lessons = null;
	  
	  try {
		_suportePersistente.iniciarTransaccao();
	  	room = _salaPersistente.readByName("GA1");
	  	assertNotNull(room);
	  	
	  	executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
	  	assertNotNull(executionPeriod);
	  	
	  	lessons = _aulaPersistente.readByRoomAndExecutionPeriod(room, executionPeriod);
	  	assertNotNull(lessons);
	  	assertEquals(lessons.size(), 21);

		room = null;
		room = _salaPersistente.readByName("GA2");
		assertNotNull(room);

		lessons = null;
		lessons = _aulaPersistente.readByRoomAndExecutionPeriod(room, executionPeriod);
		assertNotNull(lessons);
		assertEquals(lessons.size(), 6);


		_suportePersistente.confirmarTransaccao();
	  	
	  } catch (ExcepcaoPersistencia ex) {
		fail("testReadByRoomAndExecutionPeriod : fail");
  	  }
  }
  
  public void testReadByExecutionCourseAndLessonType() {
	
	  IDisciplinaExecucao executionCourse = null;
	  List lessons = null;
	  
	  try {
		_suportePersistente.iniciarTransaccao();
	  	
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
	  	
		lessons = _aulaPersistente.readByExecutionCourseAndLessonType(executionCourse, new TipoAula(TipoAula.TEORICA));
		assertNotNull(lessons);
		assertEquals(lessons.size(), 5);
		
		lessons = null;
		lessons = _aulaPersistente.readByExecutionCourseAndLessonType(executionCourse, new TipoAula(TipoAula.PRATICA));
		assertNotNull(lessons);
		assertEquals(lessons.size(), 0);
		
		_suportePersistente.confirmarTransaccao();
	  	
	  } catch (ExcepcaoPersistencia ex) {
		fail("testReadByRoomAndExecutionPeriod : fail");
	  }
  }
  
  public void testReadLessonsInPeriod() {
	
	// FIXME: 
	
  }
	
  
  
}
