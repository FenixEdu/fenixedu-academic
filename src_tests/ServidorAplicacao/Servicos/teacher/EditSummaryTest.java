package ServidorAplicacao.Servicos.teacher;

import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
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

	protected String getExpectedDataSetFilePath() {
		return "etc/testExpectedEditSummaryDataSet.xml";
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

		ISummary summary = null;

		try {

			summary = new Summary(summaryId);
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentSummary persistentSummary = sp.getIPersistentSummary();
			summary = (ISummary) persistentSummary.readByOId(summary, false);
			sp.confirmarTransaccao();
		}
		catch (ExcepcaoPersistencia ex) {
			fail(
				"Editing the summary: failed reading the summary to edit "
					+ ex);
		}

		summary.setTitle("Novo Titulo");
		summary.setSummaryText("Novo texto do sumario");
		summary.setSummaryType(new TipoAula(TipoAula.DUVIDAS));

		Object[] args =
			{
				executionCourseId,
				summaryId,
				summary.getSummaryDate(),
				summary.getSummaryHour(),
				summary.getSummaryType(),
				summary.getTitle(),
				summary.getSummaryText()};
		return args;
	}

	protected Object[] getTestSummarySuccessfullArguments() {

		Integer executionCourseId = new Integer(24);
		Integer summaryId = new Integer(261);

		ISummary summary = null;

		try {

			summary = new Summary(summaryId);
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentSummary persistentSummary = sp.getIPersistentSummary();
			summary = (ISummary) persistentSummary.readByOId(summary, false);
			sp.confirmarTransaccao();
		}
		catch (ExcepcaoPersistencia ex) {
			fail(
				"Editing the summary: failed reading the summary to edit "
					+ ex);
		}

		summary.setTitle("Novo Titulo");
		summary.setSummaryText("Novo texto do sumario");
		summary.setSummaryType(new TipoAula(TipoAula.DUVIDAS));

		Object[] args =
			{
				executionCourseId,
				summaryId,
				summary.getSummaryDate(),
				summary.getSummaryHour(),
				summary.getSummaryType(),
				summary.getTitle(),
				summary.getSummaryText()};
		return args;
	}

	protected Object[] getTestSummaryUnsuccessfullArguments() {

		Integer executionCourseId = new Integer(25);
		Integer summaryId = new Integer(281);

		ISummary summary = null;

		try {

			summary = new Summary(summaryId);
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentSummary persistentSummary = sp.getIPersistentSummary();
			summary = (ISummary) persistentSummary.readByOId(summary, false);
			sp.confirmarTransaccao();
		}
		catch (ExcepcaoPersistencia ex) {
			fail(
				"Editing the summary: failed reading the summary to edit "
					+ ex);
		}

		summary.setTitle("Novo Titulo");
		summary.setSummaryText("Novo texto do sumario");
		summary.setSummaryType(new TipoAula(TipoAula.DUVIDAS));

		Object[] args =
			{
				executionCourseId,
				summaryId,
				summary.getSummaryDate(),
				summary.getSummaryHour(),
				summary.getSummaryType(),
				summary.getTitle(),
				summary.getSummaryText()};
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
			fail("Editing the Summary of a Site" + ex);
		}
		catch (Exception ex) {
			fail("Editing the Summary of a Site" + ex);
		}
	}
}