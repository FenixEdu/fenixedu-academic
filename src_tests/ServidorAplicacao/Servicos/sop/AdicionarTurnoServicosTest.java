/*
 * AdicionarTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 12:54
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import DataBeans.*;
import ServidorAplicacao.Servicos.*;

public class AdicionarTurnoServicosTest extends TestCaseServicos {
    public AdicionarTurnoServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AdicionarTurnoServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  // write turmaTurno by unauthorized user
  public void testUnauthorizedCreateTurmaTurno() {
    Object argsCriarTurmaTurno[] = new Object[1];
    argsCriarTurmaTurno[0] = new ClassAndShiftKeys("turma1", "turno1");
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "AdicionarTurno", argsCriarTurmaTurno);
        fail("testUnauthorizedCreateTurmaTurno");
      } catch (Exception ex) {
        assertNull("testUnauthorizedCreateTurmaTurno", result);
      }
  }

  // write new existing turmaTurno
  public void testCreateExistingTurmaTurno() {
    Object argsCriarTurmaTurno[] = new Object[1];
    argsCriarTurmaTurno[0] = new ClassAndShiftKeys("turma1", "turno1");
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "AdicionarTurno", argsCriarTurmaTurno);
        fail("testCreateExistingTurmaTurno");
      } catch (Exception ex) {
        assertNull("testCreateExistingTurmaTurno", result);
      }
  }

  // write new non-existing turmaTurno
  public void testCreateNonExistingTurmaTurno() {
    Object argsCriarTurmaTurno[] = new Object[1];
    argsCriarTurmaTurno[0] = new ClassAndShiftKeys(_turma1.getNome(), _turno2.getNome());
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "AdicionarTurno", argsCriarTurmaTurno);
        assertEquals("testCreateNonExistingTurmaTurno", Boolean.TRUE.booleanValue(),
                                                        ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testCreateNonExistingTurmaTurno");
      }
  }
  
}
