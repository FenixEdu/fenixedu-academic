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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
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

  // read existing section
  
    public void testReadBySiteAndSectionAndName() {
	
	ISection section = null;
	ISite site = null;
	ISection superiorSection = null;
	List inferiorSections = new ArrayList();
	List itens = new ArrayList(); 
	IDisciplinaExecucao executionCourse = null;
	
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,22);
	calendar.set(Calendar.MONTH,Calendar.JANUARY);
	calendar.set(Calendar.YEAR,2003);

    Date date = Date.valueOf("2003-01-22");
 
   try {
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		assertNotNull(executionCourse);
	    
	    site = persistentSite.readByExecutionCourse(executionCourse);
		assertNotNull(site);
	
		section = persistentSection.readBySiteAndSectionAndName(site, null,"Seccao1deTFCI");
		persistentSupport.confirmarTransaccao();
		}
	
	catch (ExcepcaoPersistencia ex) 
	    {
	      fail("testReadBySiteAndSectionAndName:fail read existing section ");
	    }
	
	assertNotNull(section);
		
	assertEquals(section.getName(), "Seccao1deTFCI");
	assertEquals(section.getSectionOrder().intValue(), 0);
	assertEquals(section.getLastModifiedDate(), date);
	assertEquals(section.getSite(), site);
	assertEquals(section.getSuperiorSection(), null);
	assertEquals(section.getInferiorSections(), inferiorSections);
	assertEquals(section.getItems(),itens);
	
	
		
//	inferiorSections.add("subSection1");
//	inferiorSections.add("subSection2");
//	
//	itens.add("item11");
//	itens.add("item12");
//	itens.add("item21");
	










    
	    
	// read unexisting section
	try {
	  persistentSupport.iniciarTransaccao();
	  section = persistentSection.readBySiteAndSectionAndName(site , null , "section5");
	  persistentSupport.confirmarTransaccao();
	  assertNull("testReadBySiteAndSectionAndName:fail read unexisting section", section);
	} catch (ExcepcaoPersistencia ex) {
	  fail("testreadBySiteAndSectionAndName:fail read unexisting section");
	}
	






}



}
