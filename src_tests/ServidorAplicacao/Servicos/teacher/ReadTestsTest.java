/*
 * Created on 12/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteTests;
import DataBeans.InfoTest;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadTestsTest extends TestCaseReadServices {
	/**
	* @param testName
	*/
	public ReadTestsTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadTests";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(25)};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		InfoSiteTests bodyComponent = new InfoSiteTests();
		InfoExecutionCourse infoExecutionCourse = null;
		List infoTestList = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(new Integer(25));
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull("executionCourse null", executionCourse);
			IPersistentTest persistentTest = sp.getIPersistentTest();
			ITest test = new Test(new Integer(7));
			test = (ITest) persistentTest.readByOId(test, false);
			assertNotNull("test null", test);

			sp.confirmarTransaccao();
			infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);
			InfoTest infoTest = Cloner.copyITest2InfoTest(test);
			infoTestList.add(infoTest);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}

		bodyComponent.setExecutionCourse(infoExecutionCourse);
		bodyComponent.setInfoTests(infoTestList);
		SiteView siteView =
			new ExecutionCourseSiteView(bodyComponent, bodyComponent);
		return siteView;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}
