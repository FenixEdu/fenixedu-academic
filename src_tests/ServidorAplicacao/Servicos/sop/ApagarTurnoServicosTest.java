
/*
 * ApagarTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 18:28
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import DataBeans.*;
import ServidorAplicacao.Servicos.*;

public class ApagarTurnoServicosTest extends TestCaseServicos {
    public ApagarTurnoServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ApagarTurnoServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // unauthorized delete turno
  public void testUnauthorizedDeleteTurno() {
    Object argsDeleteTurno[] = new Object[1];
    argsDeleteTurno[0] = new ShiftKey("turno1", null);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "ApagarTurno", argsDeleteTurno);
        fail("testUnauthorizedDeleteTurno");
      } catch (Exception ex) {
        assertNull("testUnauthorizedDeleteTurno", result);
      }
  }

    // delte existing turno
  public void testDeleteExistingTurno() {
    Object argsDeleteTurno[] = new Object[1];
    argsDeleteTurno[0] = new ShiftKey("turno1", null);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "ApagarTurno", argsDeleteTurno);
        assertEquals("testDeleteExistingTurno", Boolean.TRUE.booleanValue(),
                                                ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testDeleteExistingTurno");
      }
  }

    // delete non-existing turno
  public void testDeleteNonExistingTurno() {
    Object argsDeleteTurno[] = new Object[1];
    argsDeleteTurno[0] = new ShiftKey("turno3", null);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "ApagarTurno", argsDeleteTurno);
        assertEquals("testDeleteNonExistingTurno", Boolean.FALSE.booleanValue(),
                                                   ((Boolean) result).booleanValue()); 
      } catch (Exception ex) {
      	fail("testDeleteNonExistingTurno");
      }
  }
    
}
