/*
 * EditarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 19:58
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class EditarSalaServicosTest extends TestCaseServicos {
    public EditarSalaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(EditarSalaServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // edit sala by unauthorized user
  public void testUnauthorizedEditSala() {
    Object argsEditarSala[] = new Object[2];
    argsEditarSala[0] = new RoomKey(new String("Ga1"));
    argsEditarSala[1] = new InfoRoom(new String("Ga1"), new String("Pavilhão Central"), new Integer(1),
                        _tipoSala, new Integer(150), new Integer(25));
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "EditarSala", argsEditarSala);
        fail("testUnauthorizedEditarSala");
      } catch (Exception ex) {
        assertNull("testUnauthorizedEditarSala", result);
      }
  }

    // edit new existing sala
  public void testEditExistingSala() {
    Object argsEditarSala[] = new Object[2];
    argsEditarSala[0] = new RoomKey(new String("Ga1"));
    argsEditarSala[1] = new InfoRoom(new String("Ga1"), new String("Pavilhão Central"), new Integer(1),
                        _tipoSala, new Integer(150), new Integer(25));
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "EditarSala", argsEditarSala);
        assertEquals("testEditNonExistingSala", Boolean.TRUE.booleanValue(),
                                                ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testEditNonExistingSala");
      }
  }

    // edit new non-existing sala
  public void testEditarNonExistingSala() {
    Object argsEditarSala[] = new Object[2];
    argsEditarSala[0] = new RoomKey(new String("Ga6"));
    argsEditarSala[1] = new InfoRoom(new String("Ga6"), new String("Pavilhão Central"), new Integer(1),
                        _tipoSala, new Integer(150), new Integer(25));
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "EditarSala", argsEditarSala);
        assertEquals("testEditNonExistingSala", Boolean.FALSE.booleanValue(),
                                                ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testEditNonExistingSala");
      }
  }
    
}
