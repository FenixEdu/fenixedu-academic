/*
 * LerLicenciaturasServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Novembro de 2002, 4:02
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

public class LerLicenciaturasServicosTest extends TestCaseServicos {
    public LerLicenciaturasServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerLicenciaturasServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  // read salas by unauthorized user
  public void testUnauthorizedReadLicenciaturas() {
    Object argsLerLicenciaturas[] = new Object[0];
    
    Object result = null;     
    try {
      result = _gestor.executar(_userView2, "LerLicenciaturas", argsLerLicenciaturas);
	  assertNotNull("testUnauthorizedReadLicenciaturas", result);
      } catch (Exception ex) {
		fail("testUnauthorizedReadLicenciaturas");
      }
  }

 
  public void testReadAll() {
    Object argsLerLicenciaturas[] = new Object[0];
    Object result = null;
    
   // Nao ha licenciaturas na BD
    try {
      _suportePersistente.iniciarTransaccao();    
      _cursoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();      
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when setUp");
    }

    try {
      result = _gestor.executar(_userView, "LerLicenciaturas", argsLerLicenciaturas);
      assertEquals("testReadAll: nao ha salas para ler", 0, ((List)result).size());
      } catch (Exception ex) {
      	fail("testReadAll: nao ha salas para ler");
      }
    
    // Ha 2 Salas na BD
    try {
      _suportePersistente.iniciarTransaccao();    
     _cursoPersistente.lockWrite(_curso1);
     _cursoPersistente.lockWrite(_curso2);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when setUp");
    }
    
   InfoDegree iL1 = new InfoDegree(_curso1.getSigla(),
                                               _curso1.getNome());
   InfoDegree iL2 = new InfoDegree(_curso2.getSigla(),
                                               _curso2.getNome());
    
    try {
      result = _gestor.executar(_userView, "LerLicenciaturas", argsLerLicenciaturas);
      assertEquals("testReadLicenciaturas: ha 2 licenciaturas para ler", ((List)result).size(), 2);
      assertTrue("testReadLicenciaturas: curso lido e curso1", ((List)result).contains(iL1));
      assertTrue("testReadLicenciaturas: curso lido e curso2", ((List)result).contains(iL2));
      } catch (Exception ex) {
      	fail("testReadLicenciaturas");
      }
  }
      
}
