/*
 * LerAulasDeTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 30 de Outubro de 2002, 22:39
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ClassKey;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class LerAulasDeTurmaServicosTest extends TestCaseServicos {
    public LerAulasDeTurmaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerAulasDeTurmaServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // read aulas by unauthorized user
  public void testUnauthorizedReadAulasDeTurma() {
    Object argsLerAulasDeTurma[] = new Object[1];
    argsLerAulasDeTurma[0] = new ClassKey("turma1");
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "LerAulasDeTurma", argsLerAulasDeTurma);
        fail("testUnauthorizedReadAulasDeTurma");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadAulasDeTurma", result);
      }
  }

  // read new existing aulas de turma
  public void testReadExistingAulasDeTurma() {
    Object argsLerAulasDeTurma[] = new Object[1];
    argsLerAulasDeTurma[0] = new ClassKey("turma1");
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeTurma", argsLerAulasDeTurma);
        assertEquals("testReadExistingAulasDeTurma", 1, ((List) result).size());
      } catch (Exception ex) {
      	fail("testReadExistingAulasDeTurma");
      }
  }

    // read new non-existing aulas de turma
  public void testReadNonExistingAulasDeTurma() {
    Object argsLerAulasDeTurma[] = new Object[1];
    argsLerAulasDeTurma[0] = new ClassKey("turma2");
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeTurma", argsLerAulasDeTurma);
        assertTrue("testReadExistingAulasDeTurma", ((List) result).isEmpty());
      } catch (Exception ex) {
      	fail("testReadExistingAulasDeTurma");
      }
  }
    
}
