/*
 * CriarTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 18:43
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import ServidorAplicacao.Servicos.*;
import DataBeans.*;

public class CriarTurmaServicosTest extends TestCaseServicos {
    public CriarTurmaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(CriarTurmaServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  // write new existing turma
  public void testUnauthorizedCreateTurma() {
    Object argsCriarTurma[] = new Object[1];
    InfoDegree iL = new InfoDegree(_curso1.getSigla(),
                                               _curso1.getNome());
    argsCriarTurma[0] = new InfoClass(new String("turma1"),  new Integer(1),new Integer(1), iL);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "CriarTurma", argsCriarTurma);
        fail("testUnauthorizedCreateTurma");
      } catch (Exception ex) {
        assertNull("testUnauthorizedCreateTurma", result);
      }
  }

  // write new existing turma
  public void testCreateExistingTurma() {
    Object argsCriarTurma[] = new Object[1];
    InfoDegree iL = new InfoDegree(_curso1.getSigla(),
                                               _curso1.getNome());
    argsCriarTurma[0] = new InfoClass(new String("turma1"),  new Integer(1),new Integer(1), iL);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "CriarTurma", argsCriarTurma);
        fail("testCreateExistingTurma");
      } catch (Exception ex) {
      	assertNull("testCreateExistingTurma", result);
      }
  }

  // write new non-existing turma
  public void testCreateNonExistingTurma() {
    Object argsCriarTurma[] = new Object[1];
    InfoDegree iL = new InfoDegree(_curso1.getSigla(),
                                               _curso1.getNome());
    argsCriarTurma[0] = new InfoClass(new String("turma2"),  new Integer(1),new Integer(1), iL);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "CriarTurma", argsCriarTurma);
        assertEquals("testCreateNonExistingTurma", Boolean.TRUE.booleanValue(),
                                                   ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testCreateNonExistingTurma");
      }
  }
    
}
