/*
 * LerAulasDeSalaEmSemestreServicosTest.java
 * JUnit based test
 *
 * Created on 29 de Outubro de 2002, 15:49
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.RoomKeyAndSemester;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class LerAulasDeSalaEmSemestreServicosTest extends TestCaseServicos {
    public LerAulasDeSalaEmSemestreServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerAulasDeSalaEmSemestreServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // read aulas by unauthorized user
    // No authorization required
  public void testUnauthorizedReadAulas() {
    Object argsLerAulas[] = new Object[1];
    argsLerAulas[0] = new RoomKeyAndSemester(new Integer(1), new String("Ga1"));

    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "LerAulasDeSalaEmSemestre", argsLerAulas);
		assertNotNull("testUnauthorizedReadAulas", result);
      } catch (Exception ex) {
		fail("testUnauthorizedReadAulas");
      }
  }

  // read new existing aulas
  public void testReadExistingAulas() {
    Object argsLerAulas[] = new Object[1];
    argsLerAulas[0] = new RoomKeyAndSemester(new Integer(1), new String("Ga1"));
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeSalaEmSemestre", argsLerAulas);
        assertEquals("testLerExistingAulas", 3, ((List) result).size());
      } catch (Exception ex) {
        System.out.println("Serviço não executado: " + ex);
      }
  }

    // read new non-existing aulas
  public void testReadNonExistingAulas() {
    Object argsLerAulas[] = new Object[1];
    argsLerAulas[0] = new RoomKeyAndSemester(new Integer(1), new String("Ga6"));

    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeSalaEmSemestre", argsLerAulas);
        assertTrue("testLerNonExistingAulas", ((List) result).isEmpty());
      } catch (Exception ex) {
      	fail("testLerNonExistingAulas");
      }
  }
    
}
