package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.student.studentCurricularPlan;


import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;
import Util.TipoDocumentoIdentificacao;

/**
 * @author David Santos
 * 2/Out/2003
 */

public class ReadStudentsByNameIDnumberIDtypeAndStudentNumberTest extends TestCase {

	protected dbaccess dbAcessPoint = null;
	protected IUserView authorisedUserView = null;
	protected IUserView notAuthorisedUserView = null;

	public ReadStudentsByNameIDnumberIDtypeAndStudentNumberTest(String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadStudentsByNameIDnumberIDtypeAndStudentNumberTest.class);
		return suite;
	}

	protected String getApplication(){
		return Autenticacao.INTRANET;
	}

	protected String getDataSetFilePath() {
		return "etc/testDataSetForReadStudentsByNameIDnumberIDtypeAndStudentNumberTest.xml";
	}

	protected String getDBBackupDataSetFilePath() {
		return "etc/dbBackup.xml";
	}

	protected void setUp() {

		try {
			dbAcessPoint = new dbaccess();
			dbAcessPoint.setDbName("fenix");
			dbAcessPoint.openConnection();
//			dbAcessPoint.backUpDataBaseContents(this.getDBBackupDataSetFilePath());
			dbAcessPoint.loadDataBase(getDataSetFilePath());
			dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Loading Database With Test Data Set!");
		}

		SuportePersistenteOJB.resetInstance();

		String args1[] = {"posGrad", "pass" , this.getApplication()};
		String args2[] = {"alunoGrad", "pass" , this.getApplication()};

		try {
			this.authorisedUserView = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", args1);
			this.notAuthorisedUserView = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", args2);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Authenticating User");
		}
	}

	protected void tearDown() {
//		try {
//			dbAcessPoint.openConnection();
//			dbAcessPoint.loadDataBase(this.getDBBackupDataSetFilePath());
//			dbAcessPoint.closeConnection();
//		} catch (Exception ex) {
//			System.out.println(ex.toString());
//			fail("Loading Database With DB Backup Data Set!");
//		}
	}

	public void testFindingOneStudent() {

		System.out.print("testFindingOneStudent: ");

		List result = null;

		String studentName = "ALUNO POSGRAD 1";
		Integer studentNumber = new Integer(600);
		String idNumber = "6000";
		TipoDocumentoIdentificacao idType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);

		Object args[] = {studentName, idNumber, idType, studentNumber};

		try {
			result = (List) ServiceManagerServiceFactory.executeService(this.authorisedUserView, "ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args);
		} catch (FenixServiceException ex) {
			System.out.println(ex.toString());
			fail("Finding One Student");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Finding One Student");
		}

		assertEquals(result.size(), 1);
		assertEquals(((InfoStudent) result.get(0)).getNumber(), studentNumber);
		assertEquals(((InfoStudent) result.get(0)).getInfoPerson().getNome(), studentName);
		assertEquals(((InfoStudent) result.get(0)).getInfoPerson().getNumeroDocumentoIdentificacao(), idNumber);
		assertEquals(((InfoStudent) result.get(0)).getInfoPerson().getTipoDocumentoIdentificacao(), idType);
		assertTrue(result.get(0) instanceof InfoStudent);

		System.out.println("OK!");
	}

	public void testFindingOneOrMoreStudents() {

		System.out.print("testFindingOneOrMoreStudents: ");

		List result = null;

		String studentName = "ALUNO%";

		Object args[] = {studentName, null, null, null};

		try {
			result = (List) ServiceManagerServiceFactory.executeService(this.authorisedUserView, "ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args);
		} catch (FenixServiceException ex) {
			System.out.println(ex.toString());
			fail("Finding One Or More Students");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Finding One Or More Students");
		}

		assertEquals(result.size(), 2);
		assertTrue(result.get(0) instanceof InfoStudent);
		assertTrue(result.get(1) instanceof InfoStudent);

		System.out.println("OK!");
	}

	public void testFindingNoStudents() {

		System.out.print("testFindingNoStudents: ");

		List result = null;

		String studentName = "DAVID%";
		Integer studentNumber = new Integer(5090);
		String idNumber = "13426";
		TipoDocumentoIdentificacao idType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);

		Object args[] = {studentName, idNumber, idType, studentNumber};

		try {
			result = (List) ServiceManagerServiceFactory.executeService(this.authorisedUserView, "ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args);
		} catch (FenixServiceException ex) {
			System.out.println(ex.toString());
			fail("Finding No Students");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Finding No Students");
		}

		assertEquals(result.size(), 0);

		System.out.println("OK!");
	}

	public void testNotMasterDegreeAdminOfficeEmployee() {

		System.out.print("testNotMasterDegrreAdminOfficeEmployee: ");

		String studentName = "ALUNO POSGRAD 1";
		Integer studentNumber = new Integer(600);
		String idNumber = "6000";
		TipoDocumentoIdentificacao idType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);

		Object args[] = {studentName, idNumber, idType, studentNumber};

		try {
			ServiceManagerServiceFactory.executeService(this.notAuthorisedUserView, "ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args);
		} catch (NotAuthorizedException ex) {
			System.out.println("OK!");
		} catch (FenixServiceException ex) {
			System.out.println(ex.toString());
			fail("Not Master Degree Administrative Office Employee");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Not Master Degree Administrative Office Employee");
		}
	}

	public void testNotMasterDegreeStudent() {

		System.out.print("testNotMasterDegreeStudent: ");

		List result = null;

		String studentName = "ALUNO GRAD";
		Integer studentNumber = new Integer(800);
		String idNumber = "8000";
		TipoDocumentoIdentificacao idType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);

		Object args[] = {studentName, idNumber, idType, studentNumber};

		try {
			result = (List) ServiceManagerServiceFactory.executeService(this.authorisedUserView, "ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args);
		} catch (FenixServiceException ex) {
			System.out.println(ex.toString());
			fail("Not Master Degree Student");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Not Master Degree Student");
		}

		assertEquals(result.size(), 0);

		System.out.println("OK!");
	}

}