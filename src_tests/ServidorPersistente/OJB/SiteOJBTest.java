/*
 * Created on 12/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorPersistente.OJB;


import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.DisciplinaExecucao;
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
 * @authors asnr e scpo
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
		  persistentSupport.iniciarTransaccao();
		  persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
		  persistentSite = persistentSupport.getIPersistentSite();
		  persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
		  persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
		  executionCourse =persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
							"PO",
							"2002/2003",
							"LEEC");
		persistentSupport.confirmarTransaccao();
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

		try {
			persistentSupport.iniciarTransaccao();
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
		assertEquals(
			"testReadByExecutionCourse:read existing site",
			executionCourse,
			site.getExecutionCourse());
		assertEquals(
			"testReadByExecutionCourse:read existing site",
			null,
			site.getInitialSection());


		//read non-existing Site
		try {
			persistentSupport.iniciarTransaccao();

			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");

			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);

			IDisciplinaExecucao nonExistingExecutionCourse =
				new DisciplinaExecucao(
					"nonExistingExecutionCourse",
					"NEEC",
					"",
					new Double(1),
					new Double(2),
					new Double(0),
					new Double(0),
					executionPeriod);
			site = persistentSite.readByExecutionCourse(nonExistingExecutionCourse);
			assertNull(site);
			persistentSupport.confirmarTransaccao();


		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading NON Existing Site");
		}

	}





	/** Test of readAll method, of class ServidorPersistente.OJB.SiteOJB. */
		
		public void testReadAll() {
			List allSites = null;
			
		// read existing Sites
		try {
				
			persistentSupport.iniciarTransaccao();
			allSites = persistentSite.readAll();
			persistentSupport.confirmarTransaccao();
	  		
				
			} catch (ExcepcaoPersistencia ex) {
				fail("testReadAll:fail reading all sites");
			}
			assertEquals("testReadAll:read all sites",10, allSites.size());
			
			
			
			
		}





	/** Test of deleteAll method, of class ServidorPersistente.OJB.SiteOJB. */
		
		public void testDeleteAll() {
			List allSites = null;
			
		// delete existing Sites
		try {
				
			persistentSupport.iniciarTransaccao();
			persistentSite.deleteAll();
			persistentSupport.confirmarTransaccao();
			
			persistentSupport.iniciarTransaccao();
			allSites= persistentSite.readAll();	
			persistentSupport.confirmarTransaccao();
			
			} catch (ExcepcaoPersistencia ex) {
				fail("testDeleteAll:fail deleting all sites");
			}
			assertEquals("testDeleteAll:delete all sites",0, allSites.size());
			
				
		}
		
		

	/** Test of delete method, of class ServidorPersistente.OJB.SiteOJB. */

	public void testDelete() {
		ISite siteToDelete = null;

		//delete Site

		try {
			persistentSupport.iniciarTransaccao();
			siteToDelete = persistentSite.readByExecutionCourse(executionCourse);
			assertNotNull(siteToDelete);
			persistentSite.delete(siteToDelete);
			persistentSupport.confirmarTransaccao();

		//read deleted Site				 
			persistentSupport.iniciarTransaccao();
			ISite siteDeleted = persistentSite.readByExecutionCourse(executionCourse);
			assertNull(siteDeleted);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
		 fail("testDelete:fail delete existing site");
	}
	
	}
		


	/** Test of lockWrite method, of class ServidorPersistente.OJB.SiteOJB. */

	public void testLockWrite() {
		ISite siteToWrite = null;

		
		  // first delete Site 
		try {
			persistentSupport.iniciarTransaccao();
			siteToWrite = persistentSite.readByExecutionCourse(executionCourse);
			assertNotNull(siteToWrite);
			persistentSite.delete(siteToWrite);
			persistentSupport.confirmarTransaccao();
		

		// write Site
		 
			persistentSupport.iniciarTransaccao();
			ISite siteDeleted=persistentSite.readByExecutionCourse(executionCourse);
			assertNull(siteDeleted);
			persistentSite.lockWrite(siteToWrite);
			persistentSupport.confirmarTransaccao();

		//read Site
			persistentSupport.iniciarTransaccao();
			ISite siteWritten = persistentSite.readByExecutionCourse(executionCourse);
			assertEquals(siteToWrite,siteWritten);
			persistentSupport.confirmarTransaccao();


		} catch (ExcepcaoPersistencia ex) {
		 fail("testLockWrite:fail writting a site");
	}
	
	}
	  			
	


}
