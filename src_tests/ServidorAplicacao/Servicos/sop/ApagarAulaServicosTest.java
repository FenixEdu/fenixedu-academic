/*
 * ApagarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 15:07
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import DataBeans.*;
import Util.*;
import ServidorAplicacao.Servicos.*;

public class ApagarAulaServicosTest extends TestCaseServicos {
    public ApagarAulaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ApagarAulaServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // delete aula by unauthorized user
  public void testUnauthorizedDeleteAula() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsDeleteAula[] = new Object[1];
    argsDeleteAula[0] = new KeyLesson(_diaSemana1, _inicio, _fim, keySala);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "ApagarAula", argsDeleteAula);
        fail("testUnauthorizedDeleteAula");
      } catch (Exception ex) {
        assertNull("testUnauthorizedDeleteAula", result);
      }
  }

    // delete new existing aula
  public void testDeleteExistingAula() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsDeleteAula[] = new Object[1];
    argsDeleteAula[0] = new KeyLesson(_diaSemana1, _inicio, _fim, keySala);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "ApagarAula", argsDeleteAula);
        assertEquals("testDeleteNonExistingAula", Boolean.TRUE.booleanValue(),
                                                  ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testDeleteNonExistingAula");
      }
  }

    // delete new non-existing aula
  public void testDeleteNonExistingAula() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsDeleteAula[] = new Object[1];
    argsDeleteAula[0] = new KeyLesson(new DiaSemana(DiaSemana.SEXTA_FEIRA),
                                                  _inicio,
                                                  _fim,
                                                  keySala);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "ApagarAula", argsDeleteAula);
        assertEquals("testDeleteNonExistingAula", Boolean.FALSE.booleanValue(),
                                               ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testDeleteNonExistingAula");
      }
  }
  
}
