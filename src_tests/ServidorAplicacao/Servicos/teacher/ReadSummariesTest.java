package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteSummaries;
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
public class ReadSummariesTest extends TestCaseReadServices {

	/**
	 * @param testName
	 */
	public ReadSummariesTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadSummaries";

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
		Object[] args = { new Integer(24)};
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

		InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
		InfoExecutionCourse infoExecutionCourse = null;
		InfoSummary infoSummary1 = null;
		InfoSummary infoSummary2 = null;
		List infoSummaries = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentSummary persistentSummary = sp.getIPersistentSummary();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(new Integer(24));
				
			ISummary summary1 = new Summary(new Integer(261));
			ISummary summary2 = new Summary(new Integer(281));
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull("executionCourse null",executionCourse);		
			summary1 = (ISummary) persistentSummary.readByOId(summary1, false);
			summary2 = (ISummary) persistentSummary.readByOId(summary2,false);
			assertNotNull("summary1 null",summary1);
			assertNotNull("summary2 null",summary2);
			sp.confirmarTransaccao();
			infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);
			infoSummary1 = Cloner.copyISummary2InfoSummary(summary1);
			infoSummary2 = Cloner.copyISummary2InfoSummary(summary2);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}
		
		bodyComponent.setExecutionCourse(infoExecutionCourse);
		infoSummaries.add(infoSummary1); 
		infoSummaries.add(infoSummary2); 
		bodyComponent.setInfoSummaries(infoSummaries);
		SiteView siteView =
			new ExecutionCourseSiteView(bodyComponent, bodyComponent);
		return siteView;
	}
	
	protected boolean needsAuthorization() {
			return true;
		}
}