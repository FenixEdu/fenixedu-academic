/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteDistributedTests;
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
public class ReadDistributedTestsTest extends TestCaseReadServices {

	public ReadDistributedTestsTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadDistributedTests";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(26)};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		InfoSiteDistributedTests infoSiteDistributedTests =
			new InfoSiteDistributedTests();
		InfoExecutionCourse infoExecutionCourse = null;
		List infoDistributedTestList = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse =
				new ExecutionCourse(new Integer(26));
			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull("executionCourse null", executionCourse);
			IPersistentDistributedTest persistentDistributedTest =
				sp.getIPersistentDistributedTest();
			IDistributedTest distributedTest23 =
				new DistributedTest(new Integer(23));
			distributedTest23 =
				(IDistributedTest) persistentDistributedTest.readByOId(
					distributedTest23,
					false);
			assertNotNull("distributedTest null", distributedTest23);

			IDistributedTest distributedTest24 =
				new DistributedTest(new Integer(24));
			distributedTest24 =
				(IDistributedTest) persistentDistributedTest.readByOId(
					distributedTest24,
					false);
			assertNotNull("distributedTest null", distributedTest24);

			IDistributedTest distributedTest25 =
				new DistributedTest(new Integer(25));
			distributedTest25 =
				(IDistributedTest) persistentDistributedTest.readByOId(
					distributedTest25,
					false);
			assertNotNull("distributedTest null", distributedTest25);
			sp.confirmarTransaccao();
			
			infoExecutionCourse =
				(InfoExecutionCourse) Cloner.get(
					executionCourse);
			InfoDistributedTest infoDistributedTest23 =
				Cloner.copyIDistributedTest2InfoDistributedTest(
					distributedTest23);
			InfoDistributedTest infoDistributedTest24 =
				Cloner.copyIDistributedTest2InfoDistributedTest(
					distributedTest24);
			InfoDistributedTest infoDistributedTest25 =
				Cloner.copyIDistributedTest2InfoDistributedTest(
					distributedTest25);

			infoDistributedTestList.add(infoDistributedTest23);
			infoDistributedTestList.add(infoDistributedTest24);
			infoDistributedTestList.add(infoDistributedTest25);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}

		infoSiteDistributedTests.setExecutionCourse(infoExecutionCourse);
		infoSiteDistributedTests.setInfoDistributedTests(
			infoDistributedTestList);
		SiteView siteView =
			new ExecutionCourseSiteView(
				infoSiteDistributedTests,
				infoSiteDistributedTests);
		return siteView;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}
