package ServidorAplicacao.Servicos.teacher;

import java.util.Calendar;

import DataBeans.InfoSiteSummary;
import DataBeans.InfoSummary;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import Util.TipoAula;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo
 */
public class EditSummaryTest extends SummaryBelongsExecutionCourseTestCase {

	/**
	 * @param testName
	 */
	public EditSummaryTest(String testName) {
		super(testName);
	}

	protected String getDataSetFilePath() {
		return "etc/testEditSummaryDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "EditSummary";
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
		Integer summaryId = new Integer(261);

		Calendar summaryDate = Calendar.getInstance();
		summaryDate.set(Calendar.DAY_OF_MONTH, 1);
		summaryDate.set(Calendar.MONTH, 2);
		summaryDate.set(Calendar.YEAR, 1999);

		Calendar summaryHour = Calendar.getInstance();
		summaryHour.set(Calendar.HOUR_OF_DAY, 12);
		summaryHour.set(Calendar.MINUTE, 0);

		int tipoAula = TipoAula.DUVIDAS;
		String title = "Novo Titulo";
		String text = "Novo texto do sumario";

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

	protected Object[] getTestSummarySuccessfullArguments() {

		Integer executionCourseId = new Integer(24);
		Integer summaryId = new Integer(261);

		Calendar summaryDate = Calendar.getInstance();
		summaryDate.set(Calendar.DAY_OF_MONTH, 1);
		summaryDate.set(Calendar.MONTH, 2);
		summaryDate.set(Calendar.YEAR, 1999);

		Calendar summaryHour = Calendar.getInstance();
		summaryHour.set(Calendar.HOUR_OF_DAY, 12);
		summaryHour.set(Calendar.MINUTE, 0);

		int tipoAula = TipoAula.DUVIDAS;
		String title = "Novo Titulo";
		String text = "Novo texto do sumario";

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

	protected Object[] getTestSummaryUnsuccessfullArguments() {

		Integer executionCourseId = new Integer(25);
		Integer summaryId = new Integer(281);
		
		Calendar summaryDate = Calendar.getInstance();
		summaryDate.set(Calendar.DAY_OF_MONTH, 1);
		summaryDate.set(Calendar.MONTH, 2);
		summaryDate.set(Calendar.YEAR, 1999);
		
		Calendar summaryHour = Calendar.getInstance();
		summaryHour.set(Calendar.HOUR_OF_DAY, 12);
		summaryHour.set(Calendar.MINUTE, 0);

		int tipoAula = TipoAula.DUVIDAS;
		String title = "Novo Titulo";
		String text = "Novo texto do sumario";
		
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
			SiteView result = null;
	
			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			result =
				(SiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					getAuthorizeArguments());

			InfoSummary oldInfoSummary = new InfoSummary();
			// TODO: falta alterar o sumario para fazer a comparação

			InfoSiteSummary infoSiteSummary =
				(InfoSiteSummary) result.getComponent();
			InfoSummary newInfoSummary = infoSiteSummary.getInfoSummary();
			assertEquals(newInfoSummary, oldInfoSummary);
			// verificar as alteracoes da bd
			compareDataSet(getDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Editing the Summary of a Site" + ex);
		} catch (Exception ex) {
			fail("Editing the Summary of a Site" + ex);
		}
	}
}