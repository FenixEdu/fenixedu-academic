package ServidorAplicacao.Servicos.teacher;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteSummary;
import DataBeans.InfoSummary;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * @author Susana Fernandes
 */
public class ReadSummaryTest extends TestCaseReadServices {

	/**
	 * @param testName
	 */
	public ReadSummaryTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadSummary";

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(24), new Integer(261)};
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	 */
	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
	protected Object getObjectToCompare() {

		InfoSiteSummary bodyComponent = new InfoSiteSummary();
		InfoExecutionCourse infoExecutionCourse = null;
		InfoSummary infoSummary = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentSummary persistentSummary = sp.getIPersistentSummary();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(new Integer(24));
			ISummary summary = new Summary(new Integer(261));

			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull("executionCourse null", executionCourse);
			summary = (ISummary) persistentSummary.readByOId(summary, false);
			assertNotNull("summary null", summary);
			sp.confirmarTransaccao();
			infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);
			infoSummary = Cloner.copyISummary2InfoSummary(summary);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}
		bodyComponent.setExecutionCourse(infoExecutionCourse);
		bodyComponent.setInfoSummary(infoSummary);

		SiteView siteView =
			new ExecutionCourseSiteView(bodyComponent, bodyComponent);
		return siteView;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}