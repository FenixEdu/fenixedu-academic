package ServidorAplicacao.Servicos.teacher;

import java.util.Calendar;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.TipoAula;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo
 */
public class InsertSummaryTest extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param testName
	 */
	public InsertSummaryTest(String testName) {
		super(testName);
	}

	protected String getDataSetFilePath() {
		return "etc/testInsertSummaryDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/testExpectedInsertSummaryDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertSummary";
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

		Integer executionCourseId = new Integer(24);
		Integer summaryId = new Integer(262);

		Calendar summaryDate = Calendar.getInstance();
		summaryDate.set(Calendar.DAY_OF_MONTH, 1);
		summaryDate.set(Calendar.MONTH, 2);
		summaryDate.set(Calendar.YEAR, 1999);

		Calendar summaryHour = Calendar.getInstance();
		summaryHour.set(Calendar.HOUR_OF_DAY, 12);
		summaryHour.set(Calendar.MINUTE, 0);

		int tipoAula = TipoAula.DUVIDAS;
		String title = "Titulo";
		String text = "Texto do sumario";

		Object[] args =
			{
				executionCourseId,
				summaryId,
				summaryDate,
				summaryHour,
				new Integer(tipoAula),
				title,
				text };
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

			// verificar as alteracoes da bd
			compareDataSet(getExpectedDataSetFilePath());
		}
		catch (FenixServiceException ex) {
			fail("Insert the Summary of a Site" + ex);
		}
		catch (Exception ex) {
			fail("Insert the Summary of a Site" + ex);
		}
	}
}