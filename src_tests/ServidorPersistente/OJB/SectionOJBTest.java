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
import Dominio.Section;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;

public class SectionOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentSite persistentSite = null;
	IPersistentSection persistentSection = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	IPersistentItem persistentItem = null;

	ISite site = null;
	ISection superiorSection = null;
	ISection supSection2 = null;
	IDisciplinaExecucao executionCourse = null;
	IDisciplinaExecucao executionCourse2 = null;
	IDisciplinaExecucao executionCourse3 = null;

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

			persistentSite = persistentSupport.getIPersistentSite();
			persistentSection = persistentSupport.getIPersistentSection();
			persistentItem = persistentSupport.getIPersistentItem();
			persistentExecutionCourse =
				persistentSupport.getIDisciplinaExecucaoPersistente();
			persistentSupport.iniciarTransaccao();
			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			executionCourse2 =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"PO",
					"2002/2003",
					"LEEC");
			executionCourse3 =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCII",
					"2002/2003",
					"LEEC");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		
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
			
			assertNotNull(executionCourse2);
		    
			site = persistentSite.readByExecutionCourse(executionCourse2);
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
			
			 assertNotNull(executionCourse2);
		    
			 site = persistentSite.readByExecutionCourse(executionCourse2);
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
			assertNotNull(executionCourse2);
		    
			site = persistentSite.readByExecutionCourse(executionCourse2);
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
		     
	
	   //	read unexisting sections (the site doesnt have sections)
	   try {
			 persistentSupport.iniciarTransaccao();
	
			assertNotNull(executionCourse3);
		   	site = persistentSite.readByExecutionCourse(executionCourse3);
			assertNotNull(site);
	
			inferiorSections = persistentSection.readBySiteAndSection(site , null);
		
			System.out.println("seccoes"+inferiorSections);
		
			persistentSupport.confirmarTransaccao();
			
			assertTrue(inferiorSections.isEmpty());
		  } catch (ExcepcaoPersistencia ex) {
			 fail("testreadBySiteAndSection:fail read unexisting sections");
	       }
	
	
		   //	read unexisting section (section doesnt belong to the superiorSection)
		try {
			 persistentSupport.iniciarTransaccao();			 
			
			 assertNotNull(executionCourse);
			 site = persistentSite.readByExecutionCourse(executionCourse);
			 assertNotNull(site);
			 supSection2= persistentSection.readBySiteAndSectionAndName(site, null,"seccao1deTFCI");
			 assertNotNull(supSection2);
			 inferiorSections = persistentSection.readBySiteAndSection(site , supSection2);
			 persistentSupport.confirmarTransaccao();
			 assertTrue(inferiorSections.isEmpty());
			} catch (ExcepcaoPersistencia ex) {
			  fail("testreadBySiteAndSection:fail read unexisting inferiorSections");
			}
	
		}

	/** Test of delete() method, of class ServidorPersistente.OJB.SectionOJB.*/

		public void testDelete() {
			ISite site = null;
			ISection section = null;
	
			
			 //delete section without inferior sections
			try {
				
				 persistentSupport.iniciarTransaccao();
				 assertNotNull(executionCourse2);
				 site = persistentSite.readByExecutionCourse(executionCourse2);
				 assertNotNull(site);
				 superiorSection = persistentSection.readBySiteAndSectionAndName(site,null,"seccao1dePO");
				 assertNotNull(superiorSection);
				 section = persistentSection.readBySiteAndSectionAndName(site,superiorSection,"subSeccao1dePO");
				 assertNotNull(section);
				 persistentSection.delete(section);
	 		     persistentSupport.confirmarTransaccao();
	
						 
				 persistentSupport.iniciarTransaccao();
				 ISection deletedSection = persistentSection.readBySiteAndSectionAndName(site,superiorSection,"subSeccao1dePO");
			     assertNull(deletedSection);
				 persistentSupport.confirmarTransaccao();
	
			} catch (ExcepcaoPersistencia ex) {
			 fail("testDelete:fail deleting a section without inferior sections");
			}
			
			//delete section with inferior sections and itens
		   try {
				
			    persistentSupport.iniciarTransaccao();
			    assertNotNull(executionCourse2);
			    site = persistentSite.readByExecutionCourse(executionCourse2);
			    assertNotNull(site);
			    section = persistentSection.readBySiteAndSectionAndName(site,null,"seccao1dePO");
			    assertNotNull(section);
			    persistentSection.delete(section);
			    persistentSupport.confirmarTransaccao();
	
		  				 
			    persistentSupport.iniciarTransaccao();		    
			    List deletedInferiorSections = persistentSection.readBySiteAndSection(site, section);
			    assertTrue(deletedInferiorSections.isEmpty());
			    List itens = persistentItem.readAllItemsBySection(section);
			    assertTrue(itens.isEmpty());
			    ISection deletedSection = persistentSection.readBySiteAndSectionAndName(site,null,"seccao1dePO");      
			    assertNull(deletedSection);
		
				persistentSupport.confirmarTransaccao();
	
					
	
		   } catch (ExcepcaoPersistencia ex) {
			fail("testDelete:fail deleting a section with inferior sections");
	  	   }
	 	}

	/** Test of lockWrite method, of class ServidorPersistente.OJB.SectionOJB. */

	public void testLockWrite() {
		ISite site = null;
		ISection sectionToWrite = null;

		
		//write section without superior section 

		try {
			

			// write section		
			persistentSupport.iniciarTransaccao();
			site = persistentSite.readByExecutionCourse(executionCourse);
			assertNotNull(site);
			sectionToWrite = new Section("xptopjhadsfkhjksaghjksa", site, null);
			persistentSection.lockWrite(sectionToWrite);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
					fail("testLockWrite:fail writting a section without superior section");
				}
			try {	 
			 //read section
			persistentSupport.iniciarTransaccao();
			ISection writtenSection = persistentSection.readBySiteAndSectionAndName(site, null,"xptopjhadsfkhjksaghjksa");
			assertEquals("testLockWrite: written section not equal to read section",sectionToWrite, writtenSection);
			persistentSupport.confirmarTransaccao();
			
		} catch (ExcepcaoPersistencia ex) {
			fail("testLockWrite:fail writting a section without superior section");
		}

//		write section with superior section 
				try {
					ISection superiorSection = null;
				persistentSupport.iniciarTransaccao();
				site = persistentSite.readByExecutionCourse(executionCourse);
				assertNotNull("testLockWrite:failed reading site",site);
				superiorSection = persistentSection.readBySiteAndSectionAndName(site,null,"xptopjhadsfkhjksaghjksa");
				assertNotNull("testLockWrite:failed reading section",superiorSection);
				sectionToWrite = new Section("blablablasection", site, superiorSection);
				// write section
				persistentSection.lockWrite(sectionToWrite);
				persistentSupport.confirmarTransaccao();
				} catch (ExcepcaoPersistencia ex) {
								fail("testLockWrite:fail writting a section with superior section");
								}		
				try {  
				  //read section
					persistentSupport.iniciarTransaccao();
					superiorSection = persistentSection.readBySiteAndSectionAndName(site,null,"xptopjhadsfkhjksaghjksa");
					assertNotNull("testLockWrite:failed reading section",superiorSection);
					ISection writtenSection = persistentSection.readBySiteAndSectionAndName(site, superiorSection,"blablablasection");
					assertNotNull("testLockWrite:failed reading section",writtenSection);
					
					assertEquals("testLockWrite: written section not equal to read section",sectionToWrite, writtenSection);
					persistentSupport.confirmarTransaccao();		
		
				} catch (ExcepcaoPersistencia ex) {
			 	fail("testLockWrite:fail writting a section with superior section");
			   	}

	}

	/** Test of deleteAll method, of class ServidorPersistente.OJB.SectionOJB. */

			public void testDeleteAll() {
				List allSections = null;
				
			// delete existing Sections
			try {
				
				persistentSupport.iniciarTransaccao();
				persistentSection.deleteAll();
				persistentSupport.confirmarTransaccao();
			
				persistentSupport.iniciarTransaccao();
				allSections = persistentSection.readAll();	
				persistentSupport.confirmarTransaccao();
			
				} catch (ExcepcaoPersistencia ex) {
					fail("testDeleteAll:fail deleting all sections");
				}
				assertEquals("testDeleteAll:delete all sections",0, allSections.size());
			}				

}