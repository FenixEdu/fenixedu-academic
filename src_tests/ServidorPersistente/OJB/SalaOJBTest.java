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


import junit.framework.*;
import java.util.List;
import org.apache.ojb.odmg.OJB;
import org.odmg.QueryException;
import org.odmg.OQLQuery;
import org.odmg.Implementation;
import ServidorPersistente.*;
import Dominio.*;
import Util.*;

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
    ISala sala = null;
    // read existing Sala
    try {
      _suportePersistente.iniciarTransaccao();
      sala = _salaPersistente.readByNome("Ga1");
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNome:fail read existing sala");
    }
    assertEquals("testReadByNome:read existing sala",sala,_sala1);
        
    // read unexisting Sala
    try {
      _suportePersistente.iniciarTransaccao();
      sala = _salaPersistente.readByNome("Ga6");
      assertNull(sala);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNome:fail read unexisting sala");
    }
  }

  // write new existing sala
  public void testCreateExistingSala() {
    ISala sala = new Sala("Ga1", "Pavilhão Central", new Integer(1), _tipoSala, new Integer(100), new Integer(50));
    try {
      _suportePersistente.iniciarTransaccao();
      _salaPersistente.lockWrite(sala);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingSala");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }

  // write new non-existing sala
  public void testCreateNonExistingSala() {
  	ISala sala = new Sala("GaXPTO", "Pavilhão Central", new Integer(1), _tipoSala, new Integer(100), new Integer(50));
    try {
      _suportePersistente.iniciarTransaccao();
      _salaPersistente.lockWrite(sala);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingSala"); 
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.SalaOJB. */
  public void testWriteExistingUnchangedObject() {
    // write sala already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	ISala sala = _salaPersistente.readByNome("Ga1");
    	_suportePersistente.confirmarTransaccao();
    	
    	_suportePersistente.iniciarTransaccao();
    	_salaPersistente.lockWrite(sala);
    	_suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
    	fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.SalaOJB. */
  public void testWriteExistingChangedObject() {
    // write sala already mapped into memory
    try {
      _suportePersistente.iniciarTransaccao();
      ISala sala = _salaPersistente.readByNome("Ga1");
      sala.setTipo(new TipoSala(TipoSala.PLANA));
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      sala = _salaPersistente.readByNome("Ga1");
      _suportePersistente.confirmarTransaccao();
      
      assertTrue(sala.getTipo().equals( new TipoSala(TipoSala.PLANA) ));
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.SalaOJB. */
  public void testDeleteSala() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ISala sala = _salaPersistente.readByNome("Ga1");
    	_suportePersistente.confirmarTransaccao();

    	_suportePersistente.iniciarTransaccao();
    	_salaPersistente.delete(sala);
    	_suportePersistente.confirmarTransaccao();
    	
    	_suportePersistente.iniciarTransaccao();
    	sala = _salaPersistente.readByNome("Ga1");
    	_suportePersistente.confirmarTransaccao();

    	assertNull(sala);
    } catch (ExcepcaoPersistencia ex) {
    	fail("testDeleteSala");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.SalaOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      _salaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select sala from " + Sala.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteSala");
    }

  }

  
  public void testReadAll() {
    try {
      List salas = null;
        /* Testa metodo qdo nao ha salas na BD */
      _suportePersistente.iniciarTransaccao();
      _salaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();
      
      _suportePersistente.iniciarTransaccao();
      salas = _salaPersistente.readAll();
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadAll: qdo nao ha salas na BD", salas.size(),0);

      /* Testa metodo qdo nao mais do q uma sala na BD */      
      _suportePersistente.iniciarTransaccao();
      _salaPersistente.lockWrite(_sala1);
      _salaPersistente.lockWrite(_sala2);      
      _suportePersistente.confirmarTransaccao();
      _suportePersistente.iniciarTransaccao();
      salas = _salaPersistente.readAll();
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadAll: qdo ha duas salas na BD", salas.size(),2);
      assertTrue("testReadAll: qdo ha duas salas na BD", salas.contains(_sala1));
      assertTrue("testReadAll: qdo ha duas salas na BD", salas.contains(_sala2));

    } catch (ExcepcaoPersistencia ex) {
      fail("testReadAll");
    }
  }  
  
  public void testReadByCriteria() {
	try {
		ISala queryRoom = new Sala();
		
	  	List rooms = null;
		/* Testa metodo qdo nao ha salas na BD */
	  	_suportePersistente.iniciarTransaccao();
	  	_salaPersistente.deleteAll();
	  	_suportePersistente.confirmarTransaccao();
      
	  	_suportePersistente.iniciarTransaccao();
	  	rooms = _salaPersistente.readByCriteria(queryRoom);
	  	_suportePersistente.confirmarTransaccao();
	  	assertEquals("testReadByCriteria: no rooms in database", 0, rooms.size());

	  /* Testa metodo qdo nao mais do q uma sala na BD */      
	  _suportePersistente.iniciarTransaccao();
	  _salaPersistente.lockWrite(_sala1);
	  _salaPersistente.lockWrite(_sala2);      
	  _suportePersistente.confirmarTransaccao();
	  _suportePersistente.iniciarTransaccao();
	  queryRoom.setNome(_sala1.getNome());
	  queryRoom.setTipo(_sala1.getTipo());
	  rooms = _salaPersistente.readByCriteria(queryRoom);
	  _suportePersistente.confirmarTransaccao();
	  assertEquals("testReadByCriteria: read by name", 1, rooms.size());
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadAll");
	}
  }  
    
}
