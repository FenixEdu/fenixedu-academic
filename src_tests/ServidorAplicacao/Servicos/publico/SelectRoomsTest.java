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
import junit.framework.*;
import java.util.List;
import ServidorPersistente.*;
import DataBeans.*;
import ServidorAplicacao.Servicos.*;

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
    try {
      _suportePersistente.iniciarTransaccao();    
     _salaPersistente.lockWrite(_sala1);
     _salaPersistente.lockWrite(_sala2);
      _suportePersistente.confirmarTransaccao();      
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when setUp");
    }
    
    try {
      result = _gestor.executar(_userView, "SelectRooms", argsSelectRooms);
      assertEquals("testReadSalas: 2 rooms to be read", 2, ((List)result).size());
      InfoRoom sala1 = new InfoRoom(_sala1.getNome(), _sala1.getEdificio(), _sala1.getPiso(),
                                    _sala1.getTipo(), _sala1.getCapacidadeNormal(),
                                    _sala1.getCapacidadeExame());
      InfoRoom sala2 = new InfoRoom(_sala2.getNome(), _sala2.getEdificio(), _sala2.getPiso(),
                                    _sala2.getTipo(), _sala2.getCapacidadeNormal(),
                                    _sala2.getCapacidadeExame());
      assertTrue("testReadSalas: 2 rooms to be read", ((List)result).contains(sala1));
      assertTrue("testReadSalas: 2 rooms to be read", ((List)result).contains(sala2));
      } catch (Exception ex) {
      	fail("testReadSalas");
      }
  }
      
  public void testReadByName() {
	Object argsSelectRooms[] = new Object[1];
	argsSelectRooms[0] = new InfoRoom(_sala1.getNome(), null, null, null, null, null);
	Object result = null;     
    
	try {
	  result = _gestor.executar(_userView, "SelectRooms", argsSelectRooms);
	  assertEquals("testReadSalas: 1 rooms to be read", 1, ((List)result).size());
	  InfoRoom sala1 = new InfoRoom(_sala1.getNome(), _sala1.getEdificio(), _sala1.getPiso(),
									_sala1.getTipo(), _sala1.getCapacidadeNormal(),
									_sala1.getCapacidadeExame());
	  assertTrue("testReadSalas: 1 rooms to be read", ((List)result).contains(sala1));
	  } catch (Exception ex) {
		fail("testReadSalas");
	  }
  }

}