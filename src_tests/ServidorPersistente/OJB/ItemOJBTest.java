/*
 * ItemOJBTest.java
 * JUnit based test
 *
 * Created on 11 de Março de 2003, 11:00
 */

package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Item;
import Dominio.Section;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;


/**
 *
 * @authors asnr e scpo
 */

public class ItemOJBTest extends TestCaseOJB {
	

	ISuportePersistente persistentSupport=null;
	IPersistentExecutionYear persistentExecutionYear=null;
	IPersistentExecutionPeriod persistentExecutionPeriod=null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse=null;
	IPersistentSite persistentSite=null;
	IPersistentSection persistentSection=null;
	IPersistentItem persistentItem=null;
	
	ISection section=null;
	ISection section2=null;
	ISite site =null;
	
  public ItemOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ItemOJBTest.class);
        
    return suite;
  }
    
  protected void setUp() {
	try {
		
		persistentSupport = SuportePersistenteOJB.getInstance();		
		persistentExecutionYear =persistentSupport.getIPersistentExecutionYear();
		persistentExecutionPeriod =persistentSupport.getIPersistentExecutionPeriod();
		persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
		persistentSite = persistentSupport.getIPersistentSite();
		persistentSection = persistentSupport.getIPersistentSection();
		persistentItem = persistentSupport.getIPersistentItem();
		
		persistentSupport.iniciarTransaccao();
		
		IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO","2002/2003","LEEC");
		
		site = persistentSite.readByExecutionCourse(executionCourse);
		
		section = persistentSection.readBySiteAndSectionAndName(site,null,"Seccao1dePO");
		
		
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");

		IExecutionPeriod executionPeriod =persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);

		IDisciplinaExecucao executionCourse2 =persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("TFCI",executionPeriod);

		ISite site2 =persistentSite.readByExecutionCourse(executionCourse2);
				
		section2 =persistentSection.readBySiteAndSectionAndName(site2,null,"Seccao1deTFCI");	
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
    
  /** Test of readBySectionAndName method, of class ServidorPersistente.OJB.ItemOJB. */
	public void testReadItemBySectionAndName() {
		IItem item = null;
		
		// read existing Item
		try {
			persistentSupport.iniciarTransaccao();
			item = persistentItem.readBySectionAndName(section,"Item1dePO");
			persistentSupport.confirmarTransaccao();
	  	
	  	} catch (ExcepcaoPersistencia ex) {
			fail("testReadBySectionAndName:fail read existing item");
	  	}
	  	assertEquals("testReadBySectionAndName:read existing item",item.getInformation(),"item1 da seccao1dePO");
		assertEquals("testReadBySectionAndName:read existing item",item.getName(),"Item1dePO");
		assertEquals("testReadBySectionAndName:read existing item",item.getItemOrder(),new Integer(0));
		assertEquals("testReadBySectionAndName:read existing item",item.getUrgent(),new Boolean (true));
		
	  	// read unexisting Item
	  	try {
			persistentSupport.iniciarTransaccao();
			item = persistentItem.readBySectionAndName(section,"ItemNaoExistente");
			assertNull(item);
			persistentSupport.confirmarTransaccao();
	  } catch (ExcepcaoPersistencia ex) {
		fail("testReadBySectionAndName:fail read unexisting item");
	  }
	}
	
	

	/** Test of readAllItemsBySection method, of class ServidorPersistente.OJB.ItemOJB. */
	public void testReadAllItemsBySection() {
		List items = null;

		// read existing ItemsList
		try {
			persistentSupport.iniciarTransaccao();
			items = persistentItem.readAllItemsBySection(section2);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadBySectionAndName:fail read existing item");
		}
		IItem item1= (IItem) items.get(0);
		assertEquals("testReadAllItemsBySection:read existing item",item1.getInformation(),"item1 da seccao1deTFCI");
		assertEquals("testReadAllItemsBySection:read existing item",item1.getName(),"Item1deTFCI");
		assertEquals("testReadAllItemsBySection:read existing item",item1.getItemOrder(),new Integer(0));
		assertEquals("testReadAllItemsBySection:read existing item",item1.getUrgent(),new Boolean(true));

		IItem item2= (IItem) items.get(1);
		assertEquals("testReadAllItemsBySection:read existing item",item2.getInformation(),"item2 da seccao1deTFCI");
		assertEquals("testReadAllItemsBySection:read existing item",item2.getName(),"Item2deTFCI");
		assertEquals("testReadAllItemsBySection:read existing item",item2.getItemOrder(),new Integer(1));
		assertEquals("testReadAllItemsBySection:read existing item",item2.getUrgent(),new Boolean(false));

		// read unexisting ItemsList
		try {
			persistentSupport.iniciarTransaccao();
			ISection nonExistingSection= new Section("nonExistingSection",site,null);
			List nonExistingItems= persistentItem.readAllItemsBySection(nonExistingSection);
			assertTrue(nonExistingItems.isEmpty());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadAllItemsBySection:fail read unexisting itemsList");
		}
	}
	
	
	

	/** Test of deleteAll method, of class ServidorPersistente.OJB.ItemOJB. */
		
			public void testDeleteAll() {
				List allItems = null;
			
			// delete existing Sites
			try {
				
				persistentSupport.iniciarTransaccao();
				persistentItem.deleteAll();
				persistentSupport.confirmarTransaccao();
			
				persistentSupport.iniciarTransaccao();
				allItems= persistentItem.readAllItemsBySection(section2);
				allItems.addAll(persistentItem.readAllItemsBySection(section));	
				persistentSupport.confirmarTransaccao();
			
				} catch (ExcepcaoPersistencia ex) {
					fail("testDeleteAll:fail deleting all items");
				}
				assertEquals("testDeleteAll:delete all items",0, allItems.size());
			
				
			}
		
		

	/** Test of delete method, of class ServidorPersistente.OJB.ItemOJB. */

		public void testDelete() {
			IItem itemToDelete = null;

			//delete Item

			try {
				persistentSupport.iniciarTransaccao();
				itemToDelete = persistentItem.readBySectionAndName(section2,"Item1deTFCI");
				assertNotNull(itemToDelete);
				persistentItem.delete(itemToDelete);
				persistentSupport.confirmarTransaccao();

			//read deleted Item				 
				persistentSupport.iniciarTransaccao();
				IItem itemDeleted = persistentItem.readBySectionAndName(section2,"Item1deTFCI");
				assertNull(itemDeleted);
				persistentSupport.confirmarTransaccao();

			} catch (ExcepcaoPersistencia ex) {
			 fail("testDelete:fail delete existing item");
		}
	
		}
		


		/** Test of lockWrite method, of class ServidorPersistente.OJB.ItemOJB. */

		public void testLockWrite() {
			
			
			IItem itemToWrite = null;
			
			itemToWrite = new Item("Item2dePO",section,new Integer(1),"ItemInformation",new Boolean(true));
		
			try {
				
			
			//	 write Item
	 
				persistentSupport.iniciarTransaccao();
				persistentItem.lockWrite(itemToWrite);
				persistentSupport.confirmarTransaccao();
			
			

			//read Item
				persistentSupport.iniciarTransaccao();
				IItem itemWritten = persistentItem.readBySectionAndName(section,"Item2dePO");
				assertNotNull(itemWritten);
				persistentSupport.confirmarTransaccao();

			
				} catch (ExcepcaoPersistencia ex) {
			 fail("testLockWrite:fail writting a item");
			}
	

		}
		
}
