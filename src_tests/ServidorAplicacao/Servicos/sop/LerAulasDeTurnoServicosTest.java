/*
 * LerAulasDeTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 22:31
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import java.util.List;
import DataBeans.*;
import ServidorAplicacao.Servicos.*;

public class LerAulasDeTurnoServicosTest extends TestCaseServicos {
    public LerAulasDeTurnoServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerAulasDeTurnoServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // read aulas by unauthorized user
  public void testUnauthorizedReadAulasDeTurno() {
    Object argsLerAulasDeTurno[] = new Object[1];
    argsLerAulasDeTurno[0] = new ShiftKey("turno1", null);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "LerAulasDeTurno", argsLerAulasDeTurno);
        fail("testUnauthorizedReadAulasDeTurno");
      } catch (Exception ex) {
      	assertNull("testUnauthorizedReadAulasDeTurno", result);
      }
  }

    // read new existing aulas de turno
  public void testReadExistingAulasDeTurno() {
    Object argsLerAulasDeTurno[] = new Object[1];
    argsLerAulasDeTurno[0] = new ShiftKey("turno1", null);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeTurno", argsLerAulasDeTurno);
        assertEquals("testReadExistingAulasDeTurno", 1, ((List) result).size());
      } catch (Exception ex) {
      	fail("testReadExistingAulasDeTurno");
      }
  }

    // read new non-existing aulas de turno
  public void testReadNonExistingAulasDeTurno() {
    Object argsLerAulasDeTurno[] = new Object[1];
    argsLerAulasDeTurno[0] = new ShiftKey("turno2", null);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeTurno", argsLerAulasDeTurno);
        assertTrue("testReadExistingAulasDeTurno", ((List) result).isEmpty());
      } catch (Exception ex) {
      	fail("testReadExistingAulasDeTurno");
      }
  }
    
}
