/*
 * LerSalasServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 11:15
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import java.util.List;
import ServidorPersistente.*;
import DataBeans.*;
import ServidorAplicacao.Servicos.*;

public class LerSalasServicosTest extends TestCaseServicos {
    public LerSalasServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerSalasServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  // read salas by unauthorized user
  public void testUnauthorizedReadSalas() {
    Object argsLerSalas[] = new Object[0];
    
    Object result = null;     
    try {
      result = _gestor.executar(_userView2, "LerSalas", argsLerSalas);
        fail("testUnauthorizedReadSalas");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadSalas", result);
      }
  }
  
  
  public void testReadAll() {
    Object argsLerSalas[] = new Object[0];
    Object result = null;     
    
   // Nao ha salas na BD
    try {
      _suportePersistente.iniciarTransaccao();    
      _salaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();      
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when setUp");
    }
      
    try {
      result = _gestor.executar(_userView, "LerSalas", argsLerSalas);
      assertEquals("testReadAll: nao ha salas para ler", ((List)result).size(), 0);
      } catch (Exception ex) {
      	fail("testReadAll: nao ha salas para ler");
      }
    
    // Ha 2 Salas na BD
    try {
      _suportePersistente.iniciarTransaccao();    
     _salaPersistente.lockWrite(_sala1);
     _salaPersistente.lockWrite(_sala2);
      _suportePersistente.confirmarTransaccao();      
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when setUp");
    }
    
    try {
      result = _gestor.executar(_userView, "LerSalas", argsLerSalas);
      assertEquals("testReadSalas: ha 2 salas para ler", ((List)result).size(), 2);
      InfoRoom sala1 = new InfoRoom(_sala1.getNome(), _sala1.getEdificio(), _sala1.getPiso(),
                                    _sala1.getTipo(), _sala1.getCapacidadeNormal(),
                                    _sala1.getCapacidadeExame());
      InfoRoom sala2 = new InfoRoom(_sala2.getNome(), _sala2.getEdificio(), _sala2.getPiso(),
                                    _sala2.getTipo(), _sala2.getCapacidadeNormal(),
                                    _sala2.getCapacidadeExame());
      assertTrue("testReadSalas: sala lida e _sala1", ((List)result).contains(sala1));
      assertTrue("testReadSalas: sala lida e _sala2", ((List)result).contains(sala2));
      } catch (Exception ex) {
      	fail("testReadSalas");
      }
  }
      
}
