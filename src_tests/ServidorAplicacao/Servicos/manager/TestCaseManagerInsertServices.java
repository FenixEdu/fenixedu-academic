/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.HashMap;

import ServidorAplicacao.Servicos.TestCaseCreateServices;

/**
 * @author lmac1
 */
public abstract class TestCaseManagerInsertServices extends TestCaseCreateServices {

	public TestCaseManagerInsertServices(String testName) {
		super(testName);
	}

	public void testSuccessfulExecutionOfCreateService() {

		Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();
		
		Object result = null;
		if (args != null) {
			try {
				result = _gestor.executar(_userView, getNameOfServiceToBeTested(), args);
				System.out.println("RESULTADO DO SERVICO COM SUCESSO"+result);
				System.out.println("testSuccessfulExecutionOfCreateService was SUCCESSFULY run by class: " + this.getClass().getName());
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("testSuccessfulExecutionOfCreateService was UNSUCCESSFULY run by class: " + this.getClass().getName());
				fail("testSuccessfulExecutionOfCreateService");
			}
		}
	}
	
	protected abstract String getNameOfServiceToBeTested();

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly(){
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly(){
		return null;
	}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly(){
		return null;
	}
	
	protected String[] getArgsForAuthorizedUser() {
			return new String[] {"manager", "pass", getApplication()};
		}
	

}
