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
import Dominio.IExam;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;

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
		List exams = persistentExam.readBy(beginning, beginning);
		assertEquals("testReadByDayAndBeginning: read existing",6, exams.size());

		beginning.set(Calendar.YEAR, 2002);
		// Read Non-Existing
		exams = persistentExam.readBy(beginning, beginning);
		assertEquals("testReadByDayAndBeginning: expected no result",0, exams.size());

		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia e) {
		fail("testReadByDayAndBeginning: unexpected exception: " + e);
	}
  }

  public void testReadAll() {
	try {
		persistentSupport.iniciarTransaccao();

		List exams = persistentExam.readAll();
		assertNotNull("testReadAll: expected a result", exams);
		assertEquals("testReadAll: expected a diferent number of results", 11, exams.size());

		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia e) {
		fail("testReadAll: unexpected exception: " + e);
	}
  }

  public void testDelete() {
	try {
		persistentSupport.iniciarTransaccao();
		List exams = persistentExam.readAll();
		persistentSupport.confirmarTransaccao();		
		assertNotNull("testDelete ",exams);
		assertEquals("testDelete ",11,exams.size());
		IExam examToDelete = (IExam)exams.get(0);
				
		//Delete existing
		persistentSupport.iniciarTransaccao();
		persistentExam.delete(examToDelete);
		persistentSupport.confirmarTransaccao();		
		//Confirm deletion
		persistentSupport.iniciarTransaccao();
		exams = persistentExam.readAll();
		persistentSupport.confirmarTransaccao();		
		assertEquals("testDelete: delete existing",10,exams.size());

		//Delete non-existing
		persistentSupport.iniciarTransaccao();
		persistentExam.delete(examToDelete);
		persistentSupport.confirmarTransaccao();		
		//Confirm non-deletion
		persistentSupport.iniciarTransaccao();
		exams = persistentExam.readAll();
		persistentSupport.confirmarTransaccao();
		assertEquals("testDelete: delete non-existing",10,exams.size());
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
		assertEquals("testDeleteAll",11,exams.size());
				
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
