/*
 * ItemOJBTest.java
 * JUnit based test
 *
 * Created on 11 de Março de 2003, 11:00
 */

package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
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
 * @authors ss AINDA NAO ESTA PRONTO!
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
		persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
		persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
		persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
		persistentSite = persistentSupport.getIPersistentSite();
		persistentSection = persistentSupport.getIPersistentSection();
		persistentItem = persistentSupport.getIPersistentItem();
		
		persistentSupport.iniciarTransaccao();
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º semestre",executionYear);
		System.out.println("EXECUTION PERIOD"+executionPeriod.getName());
		
		IDisciplinaExecucao executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("PO",executionPeriod);
		System.out.println("DISCIPLINAEXECUCAO "+executionCourse.getNome());
		
		ISite site = persistentSite.readByExecutionCourse(executionCourse);
		System.out.println("SITE "+site.getAnnouncements());
		
		section = persistentSection.readBySiteAndSectionAndName(site,null,"Seccao1dePO");
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
    
  /** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.ItemOJB. */
	public void testReadItemBySectionAndName() {
		IItem item = null;
		
		// read existing Item
		try {
			persistentSupport.iniciarTransaccao();
			item = persistentItem.readBySectionAndName(section,"Item1");
			persistentSupport.confirmarTransaccao();
	  	
	  	} catch (ExcepcaoPersistencia ex) {
			fail("testReadBySectionAndName:fail read existing item");
	  	}
	  	assertEquals("testReadBySectionAndName:read existing item",item.getInformation(),"item1 da seccao1dePO");
		assertEquals("testReadBySectionAndName:read existing item",item.getName(),"Item1");
		assertEquals("testReadBySectionAndName:read existing item",item.getOrder(),new Integer(0));
		assertEquals("testReadBySectionAndName:read existing item",item.getUrgent(),new Integer(1));
		
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
    // FIXME : NOT USED AT THIS TIME
//	public void testVoidToDelete() {
//	}

    

//
//  // write new existing item
//  public void testCreateExistingItem() {
//    IItem item = new Item("2",_seccaoSitio1Topo1,1,"sou a segunda",false);
//    try {
//      persistentSupport.iniciarTransaccao();
//      _itemPersistente.lockWrite(item);
//      persistentSupport.confirmarTransaccao();
//      fail("testCreateExistingItem");
//    } catch (ExcepcaoPersistencia ex) {
//      //all is ok
//    }
//  }
//
//  // write new non-existing item
//  public void testCreateNonExistingItem() {
//    try {
//    	persistentSupport.iniciarTransaccao();
//		ISeccao s =
//			_seccaoPersistente.readBySitioAndSeccaoAndNome(
//				_seccaoSitio1Topo1.getSitio(),
//				_seccaoSitio1Topo1.getSeccaoSuperior(),
//				_seccaoSitio1Topo1.getNome());
//    	persistentSupport.confirmarTransaccao();
//
//    	IItem item = new Item("3", s, 2, "sou a terceira", false);
//
//      persistentSupport.iniciarTransaccao();
//      _itemPersistente.lockWrite(item);
//      persistentSupport.confirmarTransaccao();
//      //      assertTrue(((Item)item).getCodigoInterno() != 0);
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testCreateNonExistingItem"); 
//    }
//  }
//
//  /** Test of write method, of class ServidorPersistente.OJB.ItemOJB. */
//  public void testWriteExistingUnchangedObject() {
//    // write item already mapped into memory
//    try {
//    	persistentSupport.iniciarTransaccao();
//    	IItem item = _itemPersistente.readBySeccaoAndNome(_seccaoSitio1Topo1,"1");
//    	persistentSupport.confirmarTransaccao();
//
//      persistentSupport.iniciarTransaccao();
//      _itemPersistente.lockWrite(item);
//      persistentSupport.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testWriteExistingUnchangedObject");
//    }
//  }
//
//  /** Test of write method, of class ServidorPersistente.OJB.ItemOJB. */
//  public void testWriteExistingChangedObject() {
//    // write item already mapped into memory
//    try {
//      persistentSupport.iniciarTransaccao();
//      IItem item = _itemPersistente.readBySeccaoAndNome(_item2Sitio1Topo1.getSeccao(),"2");
//      item.setOrdem(5);
//      persistentSupport.confirmarTransaccao();
//
//      persistentSupport.iniciarTransaccao();
//      item = _itemPersistente.readBySeccaoAndNome(_item2Sitio1Topo1.getSeccao(),
//                                                  _item2Sitio1Topo1.getNome());
//      persistentSupport.confirmarTransaccao();
//      assertTrue(item.getOrdem() == 5);
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testWriteExistingChangedObject");
//    }
//  }
//
//  /** Test of delete method, of class ServidorPersistente.OJB.ItemOJB. */
//  public void testDeleteItem() {
//    try {
//    	persistentSupport.iniciarTransaccao();
//    	IItem item = _itemPersistente.readBySeccaoAndNome(_seccaoSitio1Topo1,"2");
//    	persistentSupport.confirmarTransaccao();
//
//      persistentSupport.iniciarTransaccao();
//      _itemPersistente.delete(item);
//      persistentSupport.confirmarTransaccao();
//
//      persistentSupport.iniciarTransaccao();
//      item = _itemPersistente.readBySeccaoAndNome(_item2Sitio1Topo1.getSeccao(),
//                                                        _item2Sitio1Topo1.getNome());
//      persistentSupport.confirmarTransaccao();
//
//      assertEquals(item, null);
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testDeleteItem");
//    }
//  }
//
//  /** Test of deleteAll method, of class ServidorPersistente.OJB.ItemOJB. */
//  public void testDeleteAll() {
//    try {
//      persistentSupport.iniciarTransaccao();
//      _itemPersistente.deleteAll();
//      persistentSupport.confirmarTransaccao();
//
//      persistentSupport.iniciarTransaccao();
//
//      List result = null;
//      try {
//        Implementation odmg = OJB.getInstance();
//        OQLQuery query = odmg.newOQLQuery();;
//        String oqlQuery = "select item from " + Item.class.getName();
//        query.create(oqlQuery);
//        result = (List) query.execute();
//      } catch (QueryException ex) {
//        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//      }
//      persistentSupport.confirmarTransaccao();
//      assertNotNull(result);
//      assertTrue(result.isEmpty());
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testDeleteItem");
//    }
//
//  }
    
    
}
