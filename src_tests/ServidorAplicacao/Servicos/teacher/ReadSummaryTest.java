package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoSiteSummary;
import DataBeans.InfoSummary;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo
 */
public class ReadSummaryTest extends SummaryBelongsExecutionCourseTestCase {

	/**
	 * @param testName
	 */
	public ReadSummaryTest(String testName) {
		super(testName);
	}

	protected String getDataSetFilePath() {
		return "etc/testReadSummaryDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadSummary";
	}

	protected String[] getAuthorizedUser() {

		String[] args = { "user", "pass", getApplication() };
		return args;
	}

	protected String[] getUnauthorizedUser() {

		String[] args = { "julia", "pass", getApplication() };
		return args;
	}

	protected String[] getNonTeacherUser() {

		String[] args = { "jccm", "pass", getApplication() };
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		Integer executionCourseId = new Integer(24);
		Integer summaryId = new Integer(261);

		Object[] args = { executionCourseId, summaryId };
		return args;
	}

	protected Object[] getTestSummarySuccessfullArguments() {

		Integer executionCourseId = new Integer(24);
		Integer summaryId = new Integer(261);

		Object[] args = { executionCourseId, summaryId };
		return args;
	}

	protected Object[] getTestSummaryUnsuccessfullArguments() {

		Integer executionCourseId = new Integer(25);
		Integer summaryId = new Integer(261);

		Object[] args = { executionCourseId, summaryId };
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfull() {

		try {
			SiteView result = null;

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			result =
				(SiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					getAuthorizeArguments());

			InfoSummary oldInfoSummary = new InfoSummary();
			// TODO: falta criar o sumario para fazer a comparação

			InfoSiteSummary infoSiteSummary =
				(InfoSiteSummary) result.getComponent();
			InfoSummary newInfoSummary = infoSiteSummary.getInfoSummary();
			assertEquals(newInfoSummary, oldInfoSummary);
			// verifica se a base de dados nao foi alterada
			compareDataSet(getDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Reading the Summary of a Site" + ex);
		} catch (Exception ex) {
			fail("Reading the Summary of a Site" + ex);
		}
	}
}