/*
 * EditarTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 19:54
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import ServidorAplicacao.Servicos.*;
import DataBeans.*;

public class EditarTurmaServicosTest extends TestCaseServicos {
    public EditarTurmaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(EditarTurmaServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // edit turma by unauthorized user
  public void testUnauthorizedEditTurma() {
    InfoDegree iL = new InfoDegree(_curso1.getSigla(),
                                               _curso1.getNome());
    Object argsEditarTurma[] = new Object[2];
    argsEditarTurma[0] = new ClassKey("turma1");
    argsEditarTurma[1] = new InfoClass("turma1", new Integer(2),new Integer(1), iL);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "EditarTurma", argsEditarTurma);
        fail("testUnauthorizedEditarTurma");
      } catch (Exception ex) {
        assertNull("testUnauthorizedEditarTurma", result);
      }
  }

    // edit new existing turma
  public void testEditExistingTurma() {
    InfoDegree iL = new InfoDegree(_curso1.getSigla(),
                                               _curso1.getNome());
    Object argsEditarTurma[] = new Object[2];
    argsEditarTurma[0] = new ClassKey("turma1");
    argsEditarTurma[1] = new InfoClass("turma1", new Integer(2),new Integer(1), iL);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "EditarTurma", argsEditarTurma);
        assertEquals("testEditNonExistingTurma", Boolean.TRUE.booleanValue(),
                                                 ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testEditNonExistingTurma");
      }
  }

    // edit new non-existing turma
  public void testEditarNonExistingTurma() {
    InfoDegree iL = new InfoDegree(_curso1.getSigla(),
                                               _curso1.getNome());
    Object argsEditarTurma[] = new Object[2];
    argsEditarTurma[0] = new ClassKey("turma2");
    argsEditarTurma[1] = new InfoClass("turma2", new Integer(2),new Integer(1), iL);

    Object result = null; 
      try {
        result = _gestor.executar(_userView, "EditarTurma", argsEditarTurma);
        assertEquals("testEditNonExistingTurma", Boolean.FALSE.booleanValue(),
                                                 ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testEditNonExistingTurma");
      }
  }
    
}
