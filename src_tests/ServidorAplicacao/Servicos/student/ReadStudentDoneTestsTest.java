/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoDistributedTest;
import DataBeans.InfoSiteDistributedTests;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDoneTestsTest extends TestCaseReadServices {

	/**
	* @param testName
	*/
	public ReadStudentDoneTestsTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadStudentDoneTests";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = {new String("15"), new Integer(26)};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		InfoSiteDistributedTests infoSite = new InfoSiteDistributedTests();
		List distributedTestList = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IDistributedTest distributedTest = new DistributedTest(new Integer(26));
			distributedTest = (IDistributedTest)sp.getIPersistentDistributedTest().readByOId(distributedTest, false);
			sp.confirmarTransaccao();

			InfoDistributedTest infoDistributedTest = Cloner.copyIDistributedTest2InfoDistributedTest(distributedTest);
			distributedTestList.add(infoDistributedTest);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}
		infoSite.setInfoDistributedTests(distributedTestList);
		return infoSite;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}
