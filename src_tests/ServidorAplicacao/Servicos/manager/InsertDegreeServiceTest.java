/*
 * Created on 28/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoDegree;
import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;
import Util.TipoCurso;

/**
 * @author lmac1
 */

public class InsertDegreeServiceTest extends TestCaseNeedAuthorizationServices {

	public InsertDegreeServiceTest(String testName) {
			super(testName);
		}
		
	protected void setUp() {
			super.setUp();
		}

	protected void tearDown() {
			super.tearDown();
		}
		
	protected String getNameOfServiceToBeTested() {
			return "InsertDegreeService";
		}
		
	protected boolean needsAuthorization() {
			return true;
		}
		
	protected String[] getArgsForAuthorizedUser() {
			return new String[] {"manager", "pass", getApplication()};
		}
		
	//	insert degree with code already existing in DB
	public void testUnsuccessfulExecutionOfInsertServiceCodeExists() {
			InfoDegree infoDegree = new InfoDegree("MEEC", "Teste do codigo existente");
			Object[] args = { infoDegree };
			Object result = null;
			try {
					result =
						_gestor.executar(
							_userView,
							getNameOfServiceToBeTested(),
							args);
					List comparatorArgument = new ArrayList();
					comparatorArgument.add("MEEC");
					comparatorArgument.add(null);
					assertEquals(
						"testUnsuccessfulExecutionOfInsertServiceCodeExists",
						comparatorArgument,
						result);
					System.out.println(
						"testUnsuccessfulExecutionOfInsertServiceCodeExists was SUCCESSFULY runned by class: "
							+ this.getClass().getName());
			} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(
						"testUnsuccessfulExecutionOfInsertServiceCodeExists was UNSUCCESSFULY runned by class: "
							+ this.getClass().getName());
					fail("testUnsuccessfulExecutionOfInsertServiceCodeExists");
			}
		}
		
//	insert degree with name already existing in DB
	public void testUnsuccessfulExecutionOfInsertServiceNameExists() {
		InfoDegree infoDegree = new InfoDegree("TNE", "Licenciatura de Engenharia Informatica e de Computadores");
		Object[] args = { infoDegree };
		Object result = null;
		try {
				result =
						_gestor.executar(
						_userView,
						getNameOfServiceToBeTested(),
						args);
				List comparatorArgument = new ArrayList();
				comparatorArgument.add(null);
				comparatorArgument.add("Licenciatura de Engenharia Informatica e de Computadores");
				assertEquals(
					"testUnsuccessfulExecutionOfInsertServiceNameExists",
					comparatorArgument,
					result);
				System.out.println(
					"testUnsuccessfulExecutionOfInsertServiceNameExists was SUCCESSFULY runned by class: "
						+ this.getClass().getName());
		} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(
					"testUnsuccessfulExecutionOfInsertServiceNameExists was UNSUCCESSFULY runned by class: "
						+ this.getClass().getName());
				fail("testUnsuccessfulExecutionOfInsertServiceNameExists");
		}
	}
		
//	really insert degree in DB
	public void testSuccessfulExecutionOfInsertService() {
		InfoDegree infoDegree = new InfoDegree("ICS", "Inserir Com Sucesso", new TipoCurso(1));
		Object[] args = { infoDegree };
		Object result = null;
		try {
				result =
					_gestor.executar(
						_userView,
						getNameOfServiceToBeTested(),
						args);
				assertNull(
					"testSuccessfulExecutionOfInsertService",
					result);
				System.out.println(
					"testSuccessfulExecutionOfInsertService was SUCCESSFULY runned by class: "
						+ this.getClass().getName());
		} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(
					"testSuccessfulExecutionOfInsertService was UNSUCCESSFULY runned by class: "
						+ this.getClass().getName());
				fail("testSuccessfulExecutionOfInsertService");
		}
	}
}
