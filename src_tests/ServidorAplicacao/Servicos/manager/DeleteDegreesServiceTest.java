/*
 * Created on 22/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.List;

import Dominio.Curso;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;
import Util.TipoCurso;

/**
 * @author lmac1
 */
public class DeleteDegreesServiceTest extends TestCaseNeedAuthorizationServices {

	public DeleteDegreesServiceTest(String testName) {
			super(testName);
		}
		
	protected void setUp() {
			System.out.println("ENTRA NO SETUP");
			super.setUp();
		System.out.println("SAI DO SETUP");
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
		
    //	delete non-existing object
	public void testUnsuccessfulExecutionOfDeleteService() {

		List result = new ArrayList();
		Object[] args = { new Curso("NED", "Non Existing Degree", new TipoCurso(1)) };
		try {
			result = (List)_gestor.executar(
											_userView,
											getNameOfServiceToBeTested(),
											args);
			assertNull(result.get(0));
			assertNull(result.get(1));
			System.out.println(
					"testUnsuccessfulExecutionOfDeleteService was SUCCESSFULY runned by class: "
						+ this.getClass().getName());
			} catch (FenixServiceException e) {
				System.out.println(
					"testUnsuccessfulExecutionOfReadService was SUCCESSFULY runned by class: "
						+ this.getClass().getName());
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(
					"testUnsuccessfulExecutionOfDeleteService was UNSUCCESSFULY runned by class: "
						+ this.getClass().getName());
				fail("testUnsuccessfulExecutionOfDeleteService");
			}
		
	}

}
