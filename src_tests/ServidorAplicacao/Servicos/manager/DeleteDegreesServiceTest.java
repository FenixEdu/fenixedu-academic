/*
 * Created on 22/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;

/**
 * @author lmac1
 */

public class DeleteDegreesServiceTest extends TestCaseNeedAuthorizationServices {

	public DeleteDegreesServiceTest(String testName) {
			super(testName);
		}
		
	protected void setUp() {
			super.setUp();
		}

	protected void tearDown() {
			super.tearDown();
		}
		
	protected String getNameOfServiceToBeTested() {
			return "DeleteDegreesService";
		}
		
	protected boolean needsAuthorization() {
			return true;
		}
		
	protected String[] getArgsForAuthorizedUser() {
			return new String[]{"manager", "pass", getApplication()};
		}
		
    //	delete existing object
	public void testSuccessfulExecutionOfDeleteServiceObjectExists() {

		    Integer[] entry = { new Integer(12) };
			List idList = Arrays.asList(entry);
			Object[] args = { idList };

			Object result = null;
			try {
					result =
						_gestor.executar(
							_userView,
							getNameOfServiceToBeTested(),
							args);
					List comparatorArgument = new ArrayList();
					assertEquals(
						"testSuccessfulExecutionOfDeleteServiceObjectExists",
						comparatorArgument,
						result);
					System.out.println(
						"testSuccessfulExecutionOfDeleteServiceObjectExists was SUCCESSFULY runned by class: "
							+ this.getClass().getName());
			} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(
						"testSuccessfulExecutionOfDeleteServiceObjectExists was UNSUCCESSFULY runned by class: "
							+ this.getClass().getName());
					fail("testSuccessfulExecutionOfDeleteService");
			}
		}
		
//	delete non-existing object
	public void testSuccessfulExecutionOfDeleteServiceNoObject() {

			Integer[] entry = { new Integer(5) };
			List idList = Arrays.asList(entry);
			Object[] args = { idList };

			Object result = null;
			try {
					result =
						_gestor.executar(
							_userView,
							getNameOfServiceToBeTested(),
							args);
					List comparatorArgument = new ArrayList();
					assertEquals(
						"testSuccessfulExecutionOfDeleteServiceNoObject",
						comparatorArgument,
						result);
					System.out.println(
						"testSuccessfulExecutionOfDeleteServiceNoObject was SUCCESSFULY runned by class: "
							+ this.getClass().getName());
			} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(
						"testSuccessfulExecutionOfDeleteServiceNoObject was UNSUCCESSFULY runned by class: "
							+ this.getClass().getName());
					fail("testSuccessfulExecutionOfDeleteServiceNoObject");
			}
		}
		
//	try to delete object that can´t be deleted
	public void testUnSuccessfulExecutionOfDeleteService() {

			Integer[] entry = { new Integer(10) };
			List idList = Arrays.asList(entry);
			Object[] args = { idList };

			Object result = null;
			try {
					result =
						_gestor.executar(
							_userView,
							getNameOfServiceToBeTested(),
							args);
					List comparatorArgument = new ArrayList(1);
					comparatorArgument.add("Informatica e Computadores");
					assertEquals(
						"testUnSuccessfulExecutionOfDeleteService",
						comparatorArgument,
						result);
					System.out.println(
						"testUnSuccessfulExecutionOfDeleteService was SUCCESSFULY runned by class: "
							+ this.getClass().getName());
			} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(
						"testUnSuccessfulExecutionOfDeleteService was UNSUCCESSFULY runned by class: "
							+ this.getClass().getName());
					fail("testUnSuccessfulExecutionOfDeleteService");
			}
		}

}
