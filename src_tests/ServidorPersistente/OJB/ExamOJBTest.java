/*
 * ExamOJBTest.java
 * JUnit based test
 *
 * Created on 2003/03/19
 */

package ServidorPersistente.OJB;


/**
 *
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.Exam;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Season;

public class ExamOJBTest extends TestCaseOJB {
  
  SuportePersistenteOJB persistentSupport = null;
  IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
  IPersistentExam persistentExam = null;
  IPersistentExecutionPeriod persistentExecutionPeriod = null;
  IPersistentExecutionYear persistentExecutionYear = null;

  public ExamOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ExamOJBTest.class);

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

	persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
	persistentExam = persistentSupport.getIPersistentExam();
	persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
	persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
  
  public void testReadByDayAndBeginningAndExecutionCourse() {
  	Calendar beginning = Calendar.getInstance();
	beginning.set(Calendar.YEAR, 2003);
	beginning.set(Calendar.MONTH, Calendar.MARCH);
	beginning.set(Calendar.DAY_OF_MONTH, 19);
	beginning.set(Calendar.HOUR_OF_DAY, 13);
	beginning.set(Calendar.MINUTE, 0);
	beginning.set(Calendar.SECOND, 0);
	IDisciplinaExecucao executionCourse = null;
	IExecutionPeriod executionPeriod = null;
	IExecutionYear executionYear = null;

	try {
		persistentSupport.iniciarTransaccao();
		executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("RCI", executionPeriod);
		// Make sure test data set is ok
		assertNotNull("testReadByDayAndBeginningAndExecutionCourse: test data has been altered!!!", executionCourse);

		// Read Existing
		IExam exam = persistentExam.readBy(beginning.getTime(), beginning, executionCourse);
		assertNotNull("testReadByDayAndBeginningAndExecutionCourse: expected a result", exam);

		beginning.set(Calendar.YEAR, 2002);

		// Read Non-Existing
		exam = persistentExam.readBy(beginning.getTime(), beginning, executionCourse);
		assertNull("testReadByDayAndBeginningAndExecutionCourse: expected no result", exam);

		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia e) {
		fail("testReadByDayAndBeginningAndExecutionCourse: unexpected exception: " + e);
	}
  }

  public void testReadByDayAndBeginning(){
	Calendar beginning = Calendar.getInstance();
	beginning.set(Calendar.YEAR, 2003);
	beginning.set(Calendar.MONTH, Calendar.MARCH);
	beginning.set(Calendar.DAY_OF_MONTH, 19);
	beginning.set(Calendar.HOUR_OF_DAY, 13);
	beginning.set(Calendar.MINUTE, 0);
	beginning.set(Calendar.SECOND, 0);

	try {
		persistentSupport.iniciarTransaccao();
		// Read Existing
		List exams = persistentExam.readBy(beginning.getTime(), beginning);
		assertEquals("testReadByDayAndBeginning: read existing",7, exams.size());

		beginning.set(Calendar.YEAR, 2002);
		// Read Non-Existing
		exams = persistentExam.readBy(beginning.getTime(), beginning);
		assertEquals("testReadByDayAndBeginning: expected no result",0, exams.size());

		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia e) {
		fail("testReadByDayAndBeginning: unexpected exception: " + e);
	}
  }



  public void testReadByExecutionCourse(){
	Calendar beginning = Calendar.getInstance();
	beginning.set(Calendar.YEAR, 2003);
	beginning.set(Calendar.MONTH, Calendar.MARCH);
	beginning.set(Calendar.DAY_OF_MONTH, 19);
	beginning.set(Calendar.HOUR_OF_DAY, 9);
	beginning.set(Calendar.MINUTE, 0);
	beginning.set(Calendar.SECOND, 0);
	IDisciplinaExecucao executionCourse = null;
	IExecutionPeriod executionPeriod = null;
	IExecutionYear executionYear = null;

	try {
		persistentSupport.iniciarTransaccao();
		executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("RCI", executionPeriod);
		// Make sure test data set is ok
		assertNotNull("testReadByDayAndBeginningAndExecutionCourse: test data has been altered!!!", executionCourse);

		// Read Existing
		List exams = persistentExam.readBy(executionCourse);
		assertEquals("testReadByDayAndBeginningAndExecutionCourse: expected a result",2, exams.size());
		persistentSupport.confirmarTransaccao();
		
		executionCourse.setNome("UnexistingCourse");
		executionCourse.setSigla("UC");
		persistentSupport.iniciarTransaccao();
		// Read Non-Existing
		exams = persistentExam.readBy(executionCourse);
		assertEquals("testReadByDayAndBeginningAndExecutionCourse: expected no result", 0, exams.size());

		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia e) {
		fail("testReadByDayAndBeginningAndExecutionCourse: unexpected exception: " + e);
	}  	
  }



  public void testReadAll() {
	try {
		persistentSupport.iniciarTransaccao();

		List exams = persistentExam.readAll();
		assertNotNull("testReadAll: expected a result", exams);
		assertEquals("testReadAll: expected a diferent number of results", 12, exams.size());

		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia e) {
		fail("testReadAll: unexpected exception: " + e);
	}
  }

  public void testLockWrite() {
	Calendar beginning = Calendar.getInstance();
	beginning.set(Calendar.YEAR, 2003);
	beginning.set(Calendar.MONTH, Calendar.MARCH);
	beginning.set(Calendar.DAY_OF_MONTH, 19);
	beginning.set(Calendar.HOUR_OF_DAY, 9);
	beginning.set(Calendar.MINUTE, 0);
	beginning.set(Calendar.SECOND, 0);
	Calendar end = Calendar.getInstance();
	end.set(Calendar.YEAR, 2003);
	end.set(Calendar.MONTH, Calendar.MARCH);
	end.set(Calendar.DAY_OF_MONTH, 19);
	end.set(Calendar.HOUR_OF_DAY, 12);
	end.set(Calendar.MINUTE, 0);
	end.set(Calendar.SECOND, 0);
	Season season = new Season(Season.SEASON1);
	IDisciplinaExecucao executionCourse = null;
	IExecutionPeriod executionPeriod = null;
	IExecutionYear executionYear = null;
	IExam examFromDB = null;

	try {
		persistentSupport.iniciarTransaccao();
		executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("EP", executionPeriod);
		// Make sure test data set is ok
		assertNotNull("testLockWrite: test data has been altered!!!", executionCourse);
		examFromDB = persistentExam.readBy(beginning.getTime(), beginning, executionCourse);
		assertNotNull("testLockWrite: expected a result", examFromDB);

		// Write Cahnged mapped object
		beginning.set(Calendar.DAY_OF_MONTH, 21);
		examFromDB.setDay(beginning.getTime());
		persistentExam.lockWrite(examFromDB);
		persistentSupport.confirmarTransaccao();
		// Confirm Changes
		persistentSupport.iniciarTransaccao();
		IExam exam = persistentExam.readBy(beginning.getTime(), beginning, executionCourse);
		assertNotNull("testLockWrite: expected a result", exam);
		persistentSupport.confirmarTransaccao();

		persistentSupport.iniciarTransaccao();
		// Write mapped object
		persistentExam.lockWrite(examFromDB);
		persistentSupport.confirmarTransaccao();

		persistentSupport.iniciarTransaccao();		
		// Write new unexisting object
		beginning.set(Calendar.DAY_OF_MONTH, 22);
		season.setSeason(new Integer(Season.SEASON2));
		IExam newExam = new Exam(beginning.getTime(), beginning, end, season, executionCourse);
		persistentExam.lockWrite(newExam);
		persistentSupport.confirmarTransaccao();
		// Confirm Changes
		persistentSupport.iniciarTransaccao();
		exam = persistentExam.readBy(beginning.getTime(), beginning, executionCourse);
		assertNotNull("testLockWrite: expected a result", exam);
		persistentSupport.confirmarTransaccao();
		
		persistentSupport.iniciarTransaccao();
		// Write new unmapped existing object
		beginning.set(Calendar.DAY_OF_MONTH, 22);
		newExam = new Exam(beginning.getTime(), beginning, end, season, executionCourse);
		try {
			persistentExam.lockWrite(newExam);
			fail("testLockWrite: expected an ExistingPersistentException");
		} catch (ExistingPersistentException e) {
			// All is ok!
		}
		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia e) {
		fail("testReadBy: unexpected exception: " + e);
	}
  }


  public void testDelete() {
	try {
		persistentSupport.iniciarTransaccao();
		List exams = persistentExam.readAll();
		persistentSupport.confirmarTransaccao();		
		assertNotNull("testDelete ",exams);
		assertEquals("testDelete ",12,exams.size());
		IExam examToDelete = (IExam)exams.get(0);
				
		//Delete existing
		persistentSupport.iniciarTransaccao();
		persistentExam.delete(examToDelete);
		persistentSupport.confirmarTransaccao();		
		//Confirm deletion
		persistentSupport.iniciarTransaccao();
		exams = persistentExam.readAll();
		persistentSupport.confirmarTransaccao();		
		assertEquals("testDelete: delete existing",11,exams.size());

		//Delete non-existing
		persistentSupport.iniciarTransaccao();
		persistentExam.delete(examToDelete);
		persistentSupport.confirmarTransaccao();		
		//Confirm non-deletion
		persistentSupport.iniciarTransaccao();
		exams = persistentExam.readAll();
		persistentSupport.confirmarTransaccao();
		assertEquals("testDelete: delete non-existing",11,exams.size());
	} catch (ExcepcaoPersistencia e) {
		fail("testReadBy: unexpected exception: " + e);
	}
  }


  public void testDeleteAll() {
	try {
		persistentSupport.iniciarTransaccao();
		List exams = persistentExam.readAll();
		persistentSupport.confirmarTransaccao();		
		assertNotNull("testDeleteAll",exams);
		assertEquals("testDeleteAll",12,exams.size());
				
		//Delete all
		persistentSupport.iniciarTransaccao();
		persistentExam.deleteAll();
		persistentSupport.confirmarTransaccao();		
		//Confirm deletion
		persistentSupport.iniciarTransaccao();
		exams = persistentExam.readAll();
		persistentSupport.confirmarTransaccao();		
		assertEquals("testDelete: delete existing",0,exams.size());
	} catch (ExcepcaoPersistencia e) {
		fail("testReadBy: unexpected exception: " + e);
	}
  }

}
