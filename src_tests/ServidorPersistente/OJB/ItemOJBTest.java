/*
 * ItemOJBTest.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 *
 * @author ars
 */
public class ItemOJBTest extends TestCaseOJB {
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
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
    
    // FIXME : NOT USED AT THIS TIME
	public void testVoidToDelete() {
	}

    
  /** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.ItemOJB. */
//  public void testReadBySeccaoAndNome() {
//    IItem item = null;
//    // read existing Item
//    try {
//      persistentSupport.iniciarTransaccao();
//      item = _itemPersistente.readBySeccaoAndNome(_seccaoSitio1Topo1,"1");
//      persistentSupport.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadBySeccaoAndNome:fail read existing item");
//    }
//    assertEquals("testReadBySeccaoAndNome:read existing item",item,_item1Sitio1Topo1);
//        
//    // read unexisting Item
//    try {
//      persistentSupport.iniciarTransaccao();
//      item = _itemPersistente.readBySeccaoAndNome(_seccaoSitio1Topo1,"6");
//      assertNull(item);
//      persistentSupport.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadBySeccaoAndNome:fail read unexisting item");
//    }
//  }
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
