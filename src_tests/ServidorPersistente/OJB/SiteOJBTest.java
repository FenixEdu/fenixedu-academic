/*
 * Created on 12/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorPersistente.OJB;


import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISite;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;



/**
 *
 * @authors ss AINDA NAO ESTA PRONTO!
 */

public class SiteOJBTest extends TestCaseOJB{
	
	ISuportePersistente persistentSupport=null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse=null;
	IPersistentExecutionYear persistentExecutionYear=null;
	IPersistentExecutionPeriod persistentExecutionPeriod=null;
	IPersistentSite persistentSite=null;
	IDisciplinaExecucao executionCourse=null;
	IExecutionYear executionYear=null;
	IExecutionPeriod executionPeriod=null;
	
	
  public SiteOJBTest(java.lang.String testName) {
	super(testName);
  }
    
  public static void main(java.lang.String[] args) {
	junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
	TestSuite suite = new TestSuite(SiteOJBTest.class);
        
	return suite;
  }

  protected void setUp() {
	  try {
		
		  persistentSupport = SuportePersistenteOJB.getInstance();		
		  persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
		  persistentSite = persistentSupport.getIPersistentSite();
		  persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
		  persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
	  
	  } catch (ExcepcaoPersistencia e) {
		  e.printStackTrace();
		  fail("Error");
	  }	
	
	  super.setUp();
	}
    
	protected void tearDown() {
	  super.tearDown();
	}
	
	
	/** Test of readByExecutionCourse method, of class ServidorPersistente.OJB.SiteOJB. */
		
		public void testReadByExecutionCourse() {
			ISite site = null;
			
		// read existing execution Course
			
		try{
			 persistentSupport.iniciarTransaccao();
		 	 executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");	
		 	 assertNotNull(executionCourse);
		 	 persistentSupport.confirmarTransaccao();
		
			 } catch (ExcepcaoPersistencia ex) {
		 	fail("    -> Failed Reading Existing Execution Course");
		 }
		
		
		// read existing Site
		try {
				
				persistentSupport.iniciarTransaccao();
				site = persistentSite.readByExecutionCourse(executionCourse);
				persistentSupport.confirmarTransaccao();
	  	
				
			} catch (ExcepcaoPersistencia ex) {
				fail("testReadByExecutionCourse:fail read existing site");
			}
			assertEquals("testReadByExecutionCourse:read existing site",site.getExecutionCourse(),executionCourse);
			assertEquals("testReadByExecutionCourse:read existing site",site.getInitialSection(),null);
			
				
//				delete Site
			 try{
				 persistentSupport.iniciarTransaccao();
				 site=persistentSite.readByExecutionCourse(executionCourse);
				 assertNotNull(site);
				 persistentSite.delete(site);
				 persistentSupport.confirmarTransaccao();
				

//				read non-Existing Site				 
				 persistentSupport.iniciarTransaccao();
				 site=persistentSite.readByExecutionCourse(executionCourse);
				 assertNull(site);
				 persistentSupport.confirmarTransaccao();
		
			 } catch (ExcepcaoPersistencia ex) {
				 fail("    -> Failed Reading NON Existing Site");
			 }
		 
		}
	  			
}