/*
 * ApagarTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 18:19
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import DataBeans.*;
import ServidorAplicacao.Servicos.*;

public class ApagarTurmaServicosTest extends TestCaseServicos {
    public ApagarTurmaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ApagarTurmaServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  // unauthorized delete turma
  public void testUnauthorizedDeleteTurma() {
    Object argsDeleteTurma[] = new Object[1];
    argsDeleteTurma[0] = new ClassKey("turma1");
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "ApagarTurma", argsDeleteTurma);
        fail("testUnauthorizedDeleteTurma");
      } catch (Exception ex) {
        assertNull("testUnauthorizedDeleteTurma", result);
      }
  }

  // delte existing turma
  public void testDeleteExistingTurma() {
    Object argsDeleteTurma[] = new Object[1];
    argsDeleteTurma[0] = new ClassKey("turma1");
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "ApagarTurma", argsDeleteTurma);
        assertEquals("testDeleteExistingTurma", Boolean.TRUE.booleanValue(),
                                                ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testDeleteExistingTurma");
      }
  }

  // delete non-existing turma
  public void testDeleteNonExistingTurma() {
    Object argsDeleteTurma[] = new Object[1];
    argsDeleteTurma[0] = new ClassKey("turma2");
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "ApagarTurma", argsDeleteTurma);
        assertEquals("testDeleteNonExistingTurma", Boolean.FALSE.booleanValue(),
                                                   ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testDeleteNonExistingTurma");
      }
  }
    
}
