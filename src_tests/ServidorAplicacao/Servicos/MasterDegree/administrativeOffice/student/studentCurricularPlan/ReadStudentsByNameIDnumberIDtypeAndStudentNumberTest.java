package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.student.studentCurricularPlan;


import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;
import Tools.dbaccess;

/**
 * @author David Santos
 * 2/Out/2003
 */

public class ReadStudentsByNameIDnumberIDtypeAndStudentNumberTest extends TestCase {

	protected dbaccess dbAcessPoint = null;
	protected IUserView userView = null;
	protected GestorServicos serviceManager = null;

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
		return "etc/testDataSet.xml";
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

		SuportePersistenteOJB.resetInstance();
		this.serviceManager = GestorServicos.manager();

		String args[] = {"f3667", "pass" , this.getApplication()};

		try {
			this.userView = (IUserView) this.serviceManager.executar(null, "Autenticacao", args);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Authenticating User!");
		}
	}

	protected void tearDown() {
	}

	public void testReadStudentsByNameIDnumberIDtypeAndStudentNumber() {
		List result = null;

		String studentName = "Xxxxxxxxxxxxxxxx Xxxxxxxxx Xxxxxxx Xxxxxxxxx";
		Integer studentNumber = new Integer(5090);
		String idNumber = "13426";
		TipoDocumentoIdentificacao idType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);

		Object args1[] = {studentName, idNumber, idType, studentNumber};

		try {
			result = (List) this.serviceManager.executar(userView, "ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args1);
		} catch (FenixServiceException ex) {
			System.out.println(ex.toString());
			fail("Reading One Student");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Reading One Student");
		}

		assertEquals(result.size(), 1);
		System.out.println("\nOK - One Student Read!");

		Object args2[] = {null, null , null, null};

		try {
			result = (List) this.serviceManager.executar(userView, "ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args2);
		} catch (FenixServiceException ex) {
			System.out.println(ex.toString());
			fail("Reading All Students");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Reading One Student");
		}

		assertEquals(result.size(), 528);
		System.out.println("OK - All Students Read!");
	}
}