/*
 * LerSalasServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 11:15
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerSalasServicosTest extends TestCaseReadServices {
    public LerSalasServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerSalasServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  protected String getNameOfServiceToBeTested() {
	  return "LerSalas";
  }

  protected int getNumberOfItemsToRetrieve(){
	  return 3;
  }
  protected Object getObjectToCompare(){
	  return null;
  }
	
  protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

	  return null;
  }
	

  protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
	ISuportePersistente sp = null;
	try {
		sp = SuportePersistenteOJB.getInstance();
		sp.iniciarTransaccao();
		//sp.getISalaPersistente().deleteAll(); method deleted - too dangerous
		sp.confirmarTransaccao();
	} catch (ExcepcaoPersistencia excepcao) {
	  fail("Exception when setUp");
	}
	return null;
  }



  

      
}
