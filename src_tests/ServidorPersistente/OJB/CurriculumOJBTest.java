/*
 * Created on 11/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;

/**
 * @author jmota
 */
public class CurriculumOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null; 
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	IPersistentCurriculum persistentCurriculum = null;
	IPersistentExecutionPeriod persistentExecutionPeriod = null;
	IPersistentExecutionYear persistentExecutionYear = null;
	/**
	 * @param testName
	 */
	public CurriculumOJBTest(String testName) {
		super(testName);
	
	}
	
	  public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	  }
    
	  public static Test suite() {
		TestSuite suite = new TestSuite(CurriculumOJBTest.class);

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
		persistentCurriculum = persistentSupport.getIPersistentCurriculum();
		persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
	    persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
	  }
    
	  protected void tearDown() {
		super.tearDown();
	  }  
	  
	  
	public void testReadCurriculumByExecutionCourse(){
		IDisciplinaExecucao executionCourse=null;
		ICurriculum curriculum=null;
		IExecutionYear executionYear= null;
		IExecutionPeriod executionPeriod = null;
		
//		Read Existing Execution Course	
		 try{
			 persistentSupport.iniciarTransaccao();
			 executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");
			 assertNotNull(executionCourse);
			 persistentSupport.confirmarTransaccao();
		
		 } catch (ExcepcaoPersistencia ex) {
			 fail("    -> Failed Reading Existing Execution Course");
		 }

//		Read Existing Curriculum
		 try{
			 persistentSupport.iniciarTransaccao();
			 curriculum=persistentCurriculum.readCurriculumByExecutionCourse(executionCourse);
			 assertNotNull(curriculum);
			 persistentSupport.confirmarTransaccao();
		
		 } catch (ExcepcaoPersistencia ex) {
			 fail("    -> Failed Reading Existing Curriculum");
		 }
		 assertTrue(curriculum.getExecutionCourse().equals(executionCourse));
		 assertTrue(curriculum.getGeneralObjectives().equals("bla"));
		 assertTrue(curriculum.getOperacionalObjectives().equals("bla"));
		 assertTrue(curriculum.getProgram().equals("bla"));
		 
//		Read non-Existing Curriculum
//			Read Existing Execution Course	
		 try{
			 persistentSupport.iniciarTransaccao();
			 executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			 executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre",executionYear);
			 executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("TFCII",executionPeriod);
			
			 assertNotNull(executionCourse);
			 persistentSupport.confirmarTransaccao();
		
		 } catch (ExcepcaoPersistencia ex) {
			 fail("    -> Failed Reading Existing Execution Course");
		 }
		 try{
			 persistentSupport.iniciarTransaccao();
			 curriculum=persistentCurriculum.readCurriculumByExecutionCourse(executionCourse);
			 assertNull(curriculum);
			 persistentSupport.confirmarTransaccao();
		
		 } catch (ExcepcaoPersistencia ex) {
			 fail("    -> Failed Reading NON Existing Curriculum");
		 }
		 
	}
	  
	  
	  
}
