/*
 * LerSalasServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 11:15
 */

package ServidorAplicacao.Servicos.publico;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.ISala;
import Dominio.Sala;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.TipoSala;

public class SelectRoomsTest extends TestCaseServicos {
    public SelectRoomsTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(SelectRoomsTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  public void testReadAll() {
    Object argsSelectRooms[] = new Object[1];
	argsSelectRooms[0] = new InfoRoom();
    Object result = null;     
    
   // No Rooms in Database
    try {
      _suportePersistente.iniciarTransaccao();    
      _salaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();      
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when setUp");
    }
      
    try {
      result = _gestor.executar(_userView, "SelectRooms", argsSelectRooms);
      assertEquals("testReadAll: no rooms to read", 0, ((List)result).size());
      } catch (Exception ex) {
      	fail("testReadAll: no rooms to read");
      }
    
    // 2 Rooms in Database
    
	ISala room1 = new Sala("sala1", "Edificio Central", new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(30), new Integer(20));
	ISala room2 = new Sala("sala2", "Edificio Central", new Integer(2), new TipoSala(TipoSala.LABORATORIO), new Integer(40), new Integer(20));
    
    try {
      _suportePersistente.iniciarTransaccao();    
      _salaPersistente.lockWrite(room1);
      _salaPersistente.lockWrite(room2);
      _suportePersistente.confirmarTransaccao();      
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when setUp");
    }
    
    try {
      result = _gestor.executar(_userView, "SelectRooms", argsSelectRooms);
      assertEquals("testReadSalas: 2 rooms to be read", 2, ((List)result).size());
	  InfoRoom infoRoom1 = Cloner.copyRoom2InfoRoom(room1);
	  InfoRoom infoRoom2 = Cloner.copyRoom2InfoRoom(room2);

      assertTrue("testReadSalas: 2 rooms to be read", ((List)result).contains(infoRoom1));
      assertTrue("testReadSalas: 2 rooms to be read", ((List)result).contains(infoRoom2));
      } catch (Exception ex) {
      	fail("testReadSalas");
      }
  }
      
  public void testReadByName() {
	Object argsSelectRooms[] = new Object[1];

	ISala room1 = new Sala("sala1", "Edificio Central", new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(30), new Integer(20));

	try {
	  _suportePersistente.iniciarTransaccao();    
	  _salaPersistente.lockWrite(room1);
	  _suportePersistente.confirmarTransaccao();      
	} catch (ExcepcaoPersistencia excepcao) {
	  fail("Exception when setUp");
	}


	argsSelectRooms[0] = new InfoRoom(room1.getNome(), null, null, null, null, null);
	Object result = null;     
    
	try {
	  result = _gestor.executar(_userView, "SelectRooms", argsSelectRooms);
	  assertEquals("testReadSalas: 1 rooms to be read", 1, ((List)result).size());
	  
	  InfoRoom infoRoom1 = Cloner.copyRoom2InfoRoom(room1);
	  assertTrue("testReadSalas: 1 rooms to be read", ((List)result).contains(infoRoom1));
	  } catch (Exception ex) {
		fail("testReadSalas");
	  }
  }

}