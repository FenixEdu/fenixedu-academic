/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteDistributedTest;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.DistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IDistributedTest;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTestTest extends TestCaseReadServices {

	public ReadDistributedTestTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadDistributedTest";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(26), new Integer(25)};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		InfoSiteDistributedTest bodyComponent = new InfoSiteDistributedTest();
		InfoExecutionCourse infoExecutionCourse = null;
		InfoDistributedTest infoDistributedTest = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IExecutionCourse executionCourse =
				new ExecutionCourse(new Integer(26));
			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull("executionCourse null", executionCourse);
			IPersistentDistributedTest persistentDistributedTest =
				sp.getIPersistentDistributedTest();
			IDistributedTest distributedTest =
				new DistributedTest(new Integer(25));
			distributedTest =
				(IDistributedTest) persistentDistributedTest.readByOId(
					distributedTest,
					false);
			assertNotNull("test null", distributedTest);
			sp.confirmarTransaccao();

			infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);
			infoDistributedTest =
				Cloner.copyIDistributedTest2InfoDistributedTest(
					distributedTest);

		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}

		bodyComponent.setExecutionCourse(infoExecutionCourse);
		bodyComponent.setInfoDistributedTest(infoDistributedTest);
		SiteView siteView =
			new ExecutionCourseSiteView(bodyComponent, bodyComponent);
		return siteView;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}
