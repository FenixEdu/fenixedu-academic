package ServidorAplicacao
	.Servicos
	.MasterDegree
	.administrativeOffice
	.student
	.studentCurricularPlan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.dbunit.Assertion;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;

/**
 * @author João Mota
 * 2/Out/2003
 */

public class ReadPosGradStudentCurricularPlansTest extends TestCase {

	protected dbaccess dbAcessPoint = null;
	//protected IUserView userView = null;

	public ReadPosGradStudentCurricularPlansTest(String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ReadPosGradStudentCurricularPlansTest.class);
		return suite;
	}

	protected String getApplication() {
		return Autenticacao.INTRANET;
	}

	protected String getDataSetFilePath() {
		return "etc/testDataSetForReadPosGradStudentCurricularPlansTest.xml";
	}

	protected void setUp() {

		try {
			dbAcessPoint = new dbaccess();
			dbAcessPoint.openConnection();
			dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
			dbAcessPoint.loadDataBase(getDataSetFilePath());
			dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Loading Database With Test Data Set!");
		}

	}

	protected IUserView authenticateUser(
		String userName,
		String passwd,
		String application) {
		SuportePersistenteOJB.resetInstance();

		String args[] = { userName, passwd, application };

		try {
			return (IUserView) ServiceManagerServiceFactory.executeService(
				null,
				"Autenticacao",
				args);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Authenticating User!");
			return null;

		}
	}

	protected void tearDown() {
		try {
			dbAcessPoint = new dbaccess();
			dbAcessPoint.openConnection();
			dbAcessPoint.loadDataBase("etc/testBackup.xml");
			dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Loading Database With backup Data Set!");
		}
	}

	public void compareDataSet(String fileName) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(new File(fileName));
		} catch (FileNotFoundException e2) {
			fail("File not found");
		}
		IDataSet expectedDataSet = null;
		try {
			expectedDataSet = new FlatXmlDataSet(fileReader);
		} catch (DataSetException e3) {
			fail("DataSetException");
		} catch (IOException e3) {
			fail("IOException");
		}
		try {
			dbAcessPoint = new dbaccess();
			dbAcessPoint.openConnection();
			dbAcessPoint.backUpDataBaseContents("etc/testTemp.xml");
			dbAcessPoint.loadDataBase(getDataSetFilePath());
			dbAcessPoint.closeConnection();
		} catch (Exception e1) {
			System.out.println(e1.toString());
			fail("Exporting Database to Data Set!");
		}
		try {
			fileReader = new FileReader(new File("etc/testTemp.xml"));
		} catch (FileNotFoundException e4) {
			fail("File not found");
		}
		IDataSet currentDataSet = null;
		try {
			currentDataSet = new FlatXmlDataSet(fileReader);
		} catch (DataSetException e5) {
			fail("DataSetException");
		} catch (IOException e5) {
			fail("IOException");
		}
		try {
			Assertion.assertEquals(currentDataSet, expectedDataSet);
		} catch (Exception e) {
			fail("Asserting that DB state is the one expected");
		}

	}

	public void testReadPosGradStudentCurricularPlans() {
		List result = null;

		System.out.println(
			"test Case 1-ReadPosGradStudentCurricularPlans- with valid data");
		Integer studentIdNumber = new Integer(11);

		Object args1[] = { studentIdNumber };
		System.out.println("user->"+authenticateUser("julia", "pass", this.getApplication()));
		try {
			result =
				(List) ServiceManagerServiceFactory.executeService(
					authenticateUser("julia", "pass", this.getApplication()),
					"ReadPosGradStudentCurricularPlans",
					args1);
		} catch (FenixServiceException ex) {
			System.out.println(ex.toString());
			fail("Reading the Curricular Plans of a PosGrad Student-FenixServiceException");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Reading the Curricular Plans of a PosGrad Student-Exception");
		}
		assertEquals(result.size(), 1);
		assertTrue(result.get(0) instanceof InfoStudentCurricularPlan);
		compareDataSet(getDataSetFilePath());
		System.out.println("test case 1 ok!");
		System.out.println(
			"test Case 2-ReadPosGradStudentCurricularPlans - non-existing student");
		Object args2[] = { new Integer(50)};

		try {
			result =
				(List) ServiceManagerServiceFactory.executeService(
					authenticateUser("julia", "pass", this.getApplication()),
					"ReadPosGradStudentCurricularPlans",
					args2);
			fail("Reading the Curricular Plans of a PosGrad Student with non-existing-studentId");
		} catch (InvalidArgumentsServiceException ex) {
			System.out.println("test case 2 - proper exception thrown!");
			compareDataSet(getDataSetFilePath());
			System.out.println("test case 2 ok!");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("test Case 2-wrong exception thrown");
		}

		System.out.println(
			"test Case 3-ReadPosGradStudentCurricularPlans - graduation Student");
		Object args3[] = { new Integer(8)};

		try {
			result =
				(List) ServiceManagerServiceFactory.executeService(
					authenticateUser("julia", "pass", this.getApplication()),
					"ReadPosGradStudentCurricularPlans",
					args3);
			fail("test case 3- exception not thrown");
		} catch (NotAuthorizedException ex) {
			System.out.println("test case 3 - proper exception thrown!");
			compareDataSet(getDataSetFilePath());
			System.out.println("test case 3 ok!");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("test Case 3-wrong exception thrown");
		}

		System.out.println(
			"test Case 4-ReadPosGradStudentCurricularPlans - user with no authorization");
		Object args4[] = { new Integer(11)};

		try {
			result =
				(List) ServiceManagerServiceFactory.executeService(
					authenticateUser("3", "pass", this.getApplication()),
					"ReadPosGradStudentCurricularPlans",
					args4);
			fail("test case 4- exception not thrown");
		} catch (NotAuthorizedException ex) {
			System.out.println("test case 4 - proper exception thrown!");
			compareDataSet(getDataSetFilePath());
			System.out.println("test case 4 ok!");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("test Case 4-wrong exception thrown");
		}
	}
}