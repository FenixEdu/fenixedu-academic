package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;

/**
 * @author Fernanda Quitério
 * 25/06/2003
 */
public class EvaluationOJBTest extends TestCaseOJB {
  
  SuportePersistenteOJB persistentSupport = null;
  IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
  IPersistentEvaluation persistentEvaluation = null;
  IPersistentExecutionPeriod persistentExecutionPeriod = null;
  IPersistentExecutionYear persistentExecutionYear = null;

  public EvaluationOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(EvaluationOJBTest.class);

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
	persistentEvaluation = persistentSupport.getIPersistentEvaluation();
	persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
	persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
  
  public void testReadAll() {
	try {
		persistentSupport.iniciarTransaccao();

		List evaluations = persistentEvaluation.readAll();
		assertNotNull("testReadAll: expected a result", evaluations);
		assertEquals("testReadAll: expected a diferent number of results", 11, evaluations.size());

		persistentSupport.confirmarTransaccao();
	} catch (ExcepcaoPersistencia e) {
		fail("testReadAll: unexpected exception: " + e);
	}
  }

  public void testDeleteAll() {
	try {
		persistentSupport.iniciarTransaccao();
		List evaluations = persistentEvaluation.readAll();
		persistentSupport.confirmarTransaccao();		
		assertNotNull("testDeleteAll",evaluations);
		assertEquals("testDeleteAll",11,evaluations.size());
				
		//Delete all
		persistentSupport.iniciarTransaccao();
		persistentEvaluation.deleteAll();
		persistentSupport.confirmarTransaccao();		
		//Confirm deletion
		persistentSupport.iniciarTransaccao();
		evaluations = persistentEvaluation.readAll();
		persistentSupport.confirmarTransaccao();		
		assertEquals("testDelete: delete existing",0,evaluations.size());
	} catch (ExcepcaoPersistencia e) {
		fail("testReadBy: unexpected exception: " + e);
	}
  }
}