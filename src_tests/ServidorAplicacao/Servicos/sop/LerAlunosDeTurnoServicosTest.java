/*
 * LerAlunosDeTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 22:42
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ShiftKey;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class LerAlunosDeTurnoServicosTest extends TestCaseServicos {
    public LerAlunosDeTurnoServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerAlunosDeTurnoServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // read alunos by unauthorized user
  public void testUnauthorizedReadAlunos() {
    Object argsLerAlunos[] = new Object[1];
    argsLerAlunos[0] = new ShiftKey("turno1", null);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "LerAlunosDeTurno", argsLerAlunos);
        fail("testUnauthorizedReadAlunos");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadAlunos", result);
      }
  }

    // read new existing alunos
  public void testReadExistingAlunos() {
    Object argsLerAlunos[] = new Object[1];
    argsLerAlunos[0] = new ShiftKey("turno1", null);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAlunosDeTurno", argsLerAlunos);
        assertEquals("testLerExistingAlunos", 1, ((List) result).size());
      } catch (Exception ex) {
      	fail("testLerExistingAlunos");
      }
  }

  // read new non-existing alunos
  public void testReadNonExistingAlunos() {
    Object argsLerAlunos[] = new Object[1];
    argsLerAlunos[0] = new ShiftKey("turno3", null);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAlunosDeTurno", argsLerAlunos);
        assertTrue("testLerExistingAlunos", ((List) result).isEmpty());
      } catch (Exception ex) {
      	fail("testLerExistingAlunos");
      }
  }
    
}
