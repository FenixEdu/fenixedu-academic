/*
 * SectionOJBTest.java
 * JUnit based test
 *
 * Created on 11 de March de 2003, 11:09
 */

package ServidorPersistente.OJB;


/**
 *
 * @author lmac1
 */

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.ISection;
import Dominio.ISite;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;

public class SectionOJBTest extends TestCaseOJB {
  
  SuportePersistenteOJB persistentSupport = null; 
  IPersistentSite persistentSite = null;
  IPersistentSection persistentSection = null;
  IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
  
  ISite site = null;
  ISection superiorSection = null;
  ISection supSection2 = null;
  IDisciplinaExecucao executionCourse = null;
	
  
  public SectionOJBTest(String testName) {
	super(testName);
  }
    
  public static void main(String[] args) {
	junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
	TestSuite suite = new TestSuite(SectionOJBTest.class);

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
	persistentSite = persistentSupport.getIPersistentSite();
	persistentSection = persistentSupport.getIPersistentSection();
	persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
  }
    
  protected void tearDown() {
	super.tearDown();
  }
  
  /** Test of readBySiteAndSectionAndName() method, of class ServidorPersistente.OJB.SectionOJB.*/

  
  
	public void testReadBySiteAndSectionAndName() {
	
		ISection section = null;	
		
	//	read existing section without superiorSection

	try {
		 persistentSupport.iniciarTransaccao();

		 executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		 assertNotNull(executionCourse);
		 site = persistentSite.readByExecutionCourse(executionCourse);
		 assertNotNull(site);
		 section = persistentSection.readBySiteAndSectionAndName(site, superiorSection,"Seccao1deTFCI");

		 persistentSupport.confirmarTransaccao();
		 }
	
	 catch (ExcepcaoPersistencia ex) 
		 {
		   fail("testReadBySiteAndSectionAndName:fail read existing section ");
		 }
		
		 assertNotNull(section);
		
		 assertEquals(section.getName(), "Seccao1deTFCI");
		 assertEquals(section.getSite(), site);
		 assertEquals(section.getSuperiorSection(), superiorSection);
		 
	
	
	//	read existing section with superiorSection
	
   try {
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "LEEC");
		assertNotNull(executionCourse);
	    
		site = persistentSite.readByExecutionCourse(executionCourse);
		assertNotNull(site);
	
		superiorSection = persistentSection.readBySiteAndSectionAndName(site, null,"Seccao1dePO");	
		assertNotNull(superiorSection);

		section = persistentSection.readBySiteAndSectionAndName(site, superiorSection,"SubSeccao2dePO");

		persistentSupport.confirmarTransaccao();
		}
	
	catch (ExcepcaoPersistencia ex) 
		{
		  fail("testReadBySiteAndSectionAndName:fail read existing section ");
		}
		
		assertNotNull(section);
		
		assertEquals(section.getName(), "SubSeccao2dePO");
		assertEquals(section.getSite(), site);
		assertEquals(section.getSuperiorSection(), superiorSection);
	
    
     
	// read unexisting section (unexisting name)
	try {
			persistentSupport.iniciarTransaccao();
	  
			executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
			assertNotNull(executionCourse);
	    
			site = persistentSite.readByExecutionCourse(executionCourse);
			assertNotNull(site);
	
			section = persistentSection.readBySiteAndSectionAndName(site , null , "section5");
			persistentSupport.confirmarTransaccao();
			assertNull("testReadBySiteAndSectionAndName:fail read unexisting section", section);
		} catch (ExcepcaoPersistencia ex) {
		fail("testreadBySiteAndSectionAndName:fail read unexisting section");
		}
	
	
   //	read unexisting section (section doesnt belong to the site)
   try {
		 persistentSupport.iniciarTransaccao();
		 section = persistentSection.readBySiteAndSectionAndName(site , null , "seccao1dePO");
		 persistentSupport.confirmarTransaccao();
		 assertNull("testReadBySiteAndSectionAndName:fail read unexisting section", section);
	   } catch (ExcepcaoPersistencia ex) {
		 fail("testreadBySiteAndSectionAndName:fail read unexisting section");
	   }


	   //	read unexisting section (section doesnt belong to the superiorSection)
	try {
		 persistentSupport.iniciarTransaccao();			 
		 supSection2= persistentSection.readBySiteAndSectionAndName(site, null,"seccao1deTFCI");
	     assertNotNull(supSection2);
		
		 executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "LEEC");
		 assertNotNull(executionCourse);
	    
		 site = persistentSite.readByExecutionCourse(executionCourse);
		 assertNotNull(site);
	
		 section = persistentSection.readBySiteAndSectionAndName(site , supSection2 , "seccao1dePO");
		 persistentSupport.confirmarTransaccao();
		 assertNull("testReadBySiteAndSectionAndName:fail read unexisting section", section);
		} catch (ExcepcaoPersistencia ex) {
		  fail("testreadBySiteAndSectionAndName:fail read unexisting section");
	    }

	}


	/** Test of readBySiteAndSection() method, of class ServidorPersistente.OJB.SectionOJB.*/

	public void testReadBySiteAndSection() {

	List inferiorSections = null;
	
	
		//read existing inferiorSections without superiorSection
	
		try {
		 persistentSupport.iniciarTransaccao();

		 executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		 assertNotNull(executionCourse);
		 site = persistentSite.readByExecutionCourse(executionCourse);
		 assertNotNull(site);
		 inferiorSections = persistentSection.readBySiteAndSection(site, superiorSection);

		 persistentSupport.confirmarTransaccao();
		 }
	
	 catch (ExcepcaoPersistencia ex) 
		 {
		   fail("testReadBySiteAndSection:fail read existing inferiorSections ");
		 }
		
		 assertNotNull(inferiorSections);
		
		
		 assertEquals(((ISection) inferiorSections.get(0)).getName(), "Seccao1deTFCI");
		 assertEquals(((ISection) inferiorSections.get(0)).getSite(), site);
		 assertEquals(((ISection) inferiorSections.get(0)).getSuperiorSection(), superiorSection);
		 
		 System.out.println("Acabei a 1ª parte do teste");
	
	

	
	//	read existing inferiorSections with superiorSection
	
   try {
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "LEEC");
		assertNotNull(executionCourse);
	    
		site = persistentSite.readByExecutionCourse(executionCourse);
		assertNotNull(site);
	
		superiorSection = persistentSection.readBySiteAndSectionAndName(site, null,"Seccao1dePO");	
		assertNotNull(superiorSection);
		
		inferiorSections = persistentSection.readBySiteAndSection(site, superiorSection);
		persistentSupport.confirmarTransaccao();
		}
	
	catch (ExcepcaoPersistencia ex) 
		{
		  fail("testReadBySiteAndSection:fail read existing inferiorSections ");
		}
			
		assertNotNull(inferiorSections);
		
		assertEquals(((ISection) inferiorSections.get(0)).getName(), "SubSeccao1dePO");
		assertEquals(((ISection) inferiorSections.get(0)).getSite(), site);
		assertEquals(((ISection) inferiorSections.get(0)).getSuperiorSection(), superiorSection);
		
		
		assertEquals(((ISection) inferiorSections.get(1)).getName(), "SubSeccao2dePO");
		assertEquals(((ISection) inferiorSections.get(1)).getSite(), site);
		assertEquals(((ISection) inferiorSections.get(1)).getSuperiorSection(), superiorSection);
	
		System.out.println("Acabei a 2ª parte do teste");
     
/*
   //	read unexisting inferiorSections (the site doesnt have inferiorSections)
   try {
		 persistentSupport.iniciarTransaccao();

//	//FAZER SITIO SEM SECCOES////////////////////
	    
	executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
	assertNotNull(executionCourse);
	    
	site = persistentSite.readByExecutionCourse(executionCourse);
	assertNotNull(site);

		 inferiorSections = persistentSection.readBySiteAndSection(site , null);
		 persistentSupport.confirmarTransaccao();
		 assertNull("testReadBySiteAndSection:fail read unexisting inferiorSections", inferiorSections);
	   } catch (ExcepcaoPersistencia ex) {
		 fail("testreadBySiteAndSection:fail read unexisting inferiorSections");
	   }

	   //	read unexisting section (section doesnt belong to the superiorSection)
	try {
		 persistentSupport.iniciarTransaccao();			 
		////FAZER SITIO com SECCOES////////////////////
	     executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "LEEC");
		 assertNotNull(executionCourse);
	    
		 site = persistentSite.readByExecutionCourse(executionCourse);
		 assertNotNull(site);
		///OUTRA SECCAO SUPERIOR///////////////////////
		 supSection2= persistentSection.readBySiteAndSectionAndName(site, null,"seccao1deTFCI");
		 assertNotNull(supSection2);
		
		 executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "LEEC");
		 assertNotNull(executionCourse);
	   
		 site = persistentSite.readByExecutionCourse(executionCourse);
		 assertNotNull(site);
	
		 inferiorSections = persistentSection.readBySiteAndSection(site , supSection2);
		 persistentSupport.confirmarTransaccao();
		 assertNull("testReadBySiteAndSection:fail read unexisting inferiorSections", inferiorSections);
		} catch (ExcepcaoPersistencia ex) {
		  fail("testreadBySiteAndSection:fail read unexisting inferiorSections");
		}
*/
	}
}

