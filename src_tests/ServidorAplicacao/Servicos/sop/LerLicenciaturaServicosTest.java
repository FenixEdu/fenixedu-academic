/*
 * LerLicenciaturaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Novembro de 2002, 1:28
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import DataBeans.*;
import ServidorAplicacao.Servicos.*;

public class LerLicenciaturaServicosTest extends TestCaseServicos {
    public LerLicenciaturaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerLicenciaturaServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  // read licenciatura by unauthorized user
  // No Authorization is requeired
  public void testUnauthorizedReadLicenciatura() {
    Object argsLerLicenciatura[] = new Object[1];
    argsLerLicenciatura[0] = new String(_curso1.getSigla());

    Object result = null;     
    try {
      result = _gestor.executar(_userView2, "LerLicenciatura", argsLerLicenciatura);
	  fail("testUnauthorizedReadLicenciatura");
      } catch (Exception ex) {
		assertNull("testUnauthorizedReadLicenciatura", result);
      }
  }


  public void testRead() {
    Object argsLerLicenciatura[] = new Object[1];
    argsLerLicenciatura[0] = new String(_curso2.getSigla());

	Object result = null;
    try {
      result = _gestor.executar(_userView, "LerLicenciatura", argsLerLicenciatura);
      assertNull("testRead: nao ha licenciatura para ler", result);
      } catch (Exception ex) {
      	fail("testRead: nao ha licenciatura para ler");
      }

    argsLerLicenciatura[0] = new String(_curso1.getSigla());
    try {
      result = _gestor.executar(_userView, "LerLicenciatura", argsLerLicenciatura);
      InfoDegree iL = new InfoDegree(_curso1.getSigla(),
                                                 _curso1.getNome());
      assertEquals("testReadLicenciatura:", iL, result);
      } catch (Exception ex) {
      	fail("testReadLicenciatura:");
      }
  }
      
}
