/*
 * TestCaseRequeiersAuthorizationServices.java
 *
 * Created on 2003/04/06
 */

package ServidorAplicacao.Servicos;


/**
  * @author Luis Cruz & Sara Ribeiro
  * 
 **/
public abstract class TestCaseRequeiersAuthorizationServices extends TestCaseServices {

	public TestCaseRequeiersAuthorizationServices(String testName) {
		super(testName);
	}

	public void testUnauthorizedExecutionOfService() {
		result = null;
		try {
			result = gestor.executar(userViewNotAuthorized, getNameOfServiceToBeTested(), args);
			fail("Service was run with unauthorized user view.");
		} catch (Exception ex) {
			assertNull("Execution of Service with unauthorized user view returned a result: " + result, result);
		}
	}

}