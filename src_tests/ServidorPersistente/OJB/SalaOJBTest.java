/*
 * ItemOJBTest.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ISala;
import Dominio.Sala;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoSala;

public class SalaOJBTest extends TestCaseOJB {
    public SalaOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(SalaOJBTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.SalaOJB. */
  public void testReadByNome() {
    ISala room = null;
    // read existing room
    try {
      persistentSupport.iniciarTransaccao();
      room = persistentRoom.readByName("Ga1");
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNome:fail read existing sala");
    }
    assertEquals("testReadByNome:read existing sala",room.getNome(), "Ga1");
        
    // read unexisting room
    try {
      persistentSupport.iniciarTransaccao();
      room = persistentRoom.readByName("Ga6");
      assertNull(room);
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNome:fail read unexisting sala");
    }
  }

  // write new existing room
  public void testCreateExistingSala() {
    ISala room = new Sala("Ga1", "Pavilhão Central", new Integer(1), new TipoSala(TipoSala.LABORATORIO), new Integer(100), new Integer(50));
    try {
      persistentSupport.iniciarTransaccao();
      persistentRoom.lockWrite(room);
      persistentSupport.confirmarTransaccao();
      fail("testCreateExistingSala: expected an exception");
    } catch (ExistingPersistentException ex) {
      	assertNotNull("Write Existing Room:");
	} catch (ExcepcaoPersistencia ex) {
		fail("Write Existing Room: unexpected exception");
	}
  }

  // write new non-existing room
  public void testCreateNonExistingSala() {
  	ISala room = new Sala("GaXPTO", "Pavilhão Central", new Integer(1), new TipoSala(TipoSala.LABORATORIO), new Integer(100), new Integer(50));
    try {
      persistentSupport.iniciarTransaccao();
      persistentRoom.lockWrite(room);
      persistentSupport.confirmarTransaccao();
      
	  persistentSupport.iniciarTransaccao();
	  room = null;
	  room = persistentRoom.readByName("GaXPTO");
	  assertNotNull(room); 
	  assertEquals(room.getNome(), "GaXPTO");
	  persistentSupport.confirmarTransaccao();
      
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingSala"); 
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.SalaOJB. */
  public void testWriteExistingUnchangedObject() {
    // write sala already mapped into memory
    try {
    	persistentSupport.iniciarTransaccao();
    	ISala room = persistentRoom.readByName("Ga1");
    	persistentSupport.confirmarTransaccao();
    	
    	persistentSupport.iniciarTransaccao();
    	persistentRoom.lockWrite(room);
    	persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
    	fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.SalaOJB. */
  public void testWriteExistingChangedObject() {
    // write room already mapped into memory
    try {
      persistentSupport.iniciarTransaccao();
      ISala room = persistentRoom.readByName("Ga1");
      assertNotNull(room);
      room.setTipo(new TipoSala(TipoSala.PLANA));
      persistentSupport.confirmarTransaccao();

      persistentSupport.iniciarTransaccao();
      room = persistentRoom.readByName("Ga1");
	  assertNotNull(room);
      persistentSupport.confirmarTransaccao();
      
      assertTrue(room.getTipo().equals( new TipoSala(TipoSala.PLANA) ));
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.SalaOJB. */
  public void testDeleteSalaWithLessons() {
    try {
    	persistentSupport.iniciarTransaccao();
    	ISala room = persistentRoom.readByName("Ga1");
    	assertNotNull(room);
    	persistentSupport.confirmarTransaccao();

    	persistentSupport.iniciarTransaccao();
    	persistentRoom.delete(room);
		fail("testDeleteSala");
    	persistentSupport.confirmarTransaccao();

    } catch (ExcepcaoPersistencia ex) {
    	// All is OK
    }
  }

  public void testDeleteSalaWithNoLessons() {
	try {
		
	  ISala room = new Sala("GaXPTO", "Pavilhão Central", new Integer(1), new TipoSala(TipoSala.LABORATORIO), new Integer(100), new Integer(50));
	  persistentSupport.iniciarTransaccao();
	  persistentRoom.lockWrite(room);
	  persistentSupport.confirmarTransaccao();
  
	  persistentSupport.iniciarTransaccao();
	  persistentRoom.delete(room);
	  persistentSupport.confirmarTransaccao();

	  persistentSupport.iniciarTransaccao();
	  room = null;
	  room = persistentRoom.readByName("GaXPTO");
	  assertNull(room);
	  persistentSupport.confirmarTransaccao();

	} catch (ExcepcaoPersistencia ex) {
		fail("testDeleteSala");
	}
   }


  /** Test of deleteAll method, of class ServidorPersistente.OJB.SalaOJB. */
  public void testDeleteAll() {
	//	FIXME: can we delete all rooms ??


//    try {
//      persistentSupport.iniciarTransaccao();
//      persistentRoom.deleteAll();
//      persistentSupport.confirmarTransaccao();
//
//      persistentSupport.iniciarTransaccao();
//      List result = null;
//      try {
//        Implementation odmg = OJB.getInstance();
//        OQLQuery query = odmg.newOQLQuery();;
//        String oqlQuery = "select sala from " + Sala.class.getName();
//        query.create(oqlQuery);
//        result = (List) query.execute();
//      } catch (QueryException ex) {
//        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//      }
//      persistentSupport.confirmarTransaccao();
//      assertNotNull(result);
//      assertTrue(result.isEmpty());
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testDeleteSala");
//    }
//
  }

  
  public void testReadAllExisting() {
    try {
      List salas = null;
      persistentSupport.iniciarTransaccao();
      salas = persistentRoom.readAll();
      persistentSupport.confirmarTransaccao();
      assertEquals(salas.size(), 3);
	} catch (ExcepcaoPersistencia ex) {
		fail("testDeleteSala");
	}
  }
    
	public void testReadSalas() {
			// FIXME: Too many combinations :)
    }  



  
}
