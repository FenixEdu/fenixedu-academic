package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo
 * 
 */
public class DeleteSummaryTest extends SummaryBelongsExecutionCourseTestCase {

	public DeleteSummaryTest(String testName) {
		super(testName);
	}

	protected String getDataSetFilePath() {
		return "etc/testDeleteSummaryDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/testExpectedDeleteSummaryDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "DeleteSummary";
	}

	protected String[] getAuthorizedUser() {

		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {

		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {

		String[] args = { "jccm", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		Object[] args = { new Integer(24), new Integer(281)};
		return args;
	}

	protected Object[] getTestSummarySuccessfullArguments() {

		Object[] args = { new Integer(24), new Integer(281)};
		return args;
	}

	protected Object[] getTestSummaryUnsuccessfullArguments() {

		Object[] args = { new Integer(25), new Integer(261)};
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfull() {

		try {
			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				getAuthorizeArguments());

			compareDataSet(getExpectedDataSetFilePath());
		}
		catch (FenixServiceException ex) {
			fail("Deleting the Summary of a Site" + ex);
		}
		catch (Exception ex) {
			fail("Deleting the Summary of a Site" + ex);
		}
	}
}
