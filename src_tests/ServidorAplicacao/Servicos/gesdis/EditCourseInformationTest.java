/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servicos.gesdis;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class EditCourseInformationTest
	extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param testName
	 */
	public EditCourseInformationTest(String name) {
		super(name);
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/gesdis/testEditCourseInformationDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/gesdis/testExpectedEditCourseInformationDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "EditCourseInformation";
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {

		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser() {

		String[] args = { "jorge", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser() {

		String[] args = { "jccm", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		Integer executionCourseId = new Integer(24);
		String courseReport = new String("novo relatorio da disciplina");

		Object[] args = { executionCourseId, courseReport };
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfull() {

		try {

			Boolean result = null;

			String[] args = getAuthenticatedAndAuthorizedUser();
			IUserView userView = authenticateUser(args);

			result =
				(Boolean) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					getAuthorizeArguments());

			assertTrue(result.booleanValue());
			// verifica as alteracoes da base de dados
			compareDataSetUsingExceptedDataSetTablesAndColumns(
				getExpectedDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Editing a Course Information " + ex);
		} catch (Exception ex) {
			fail("Editing a Course Information " + ex);
		}
	}
}
