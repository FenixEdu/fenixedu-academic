package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.student.studentCurricularPlan;


import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ServidorAplicacao.GestorServicos;
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
	protected IUserView userView = null;
	protected GestorServicos serviceManager = null;

	public ReadPosGradStudentCurricularPlansTest(String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadPosGradStudentCurricularPlansTest.class);
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

		String args[] = {"julia", "pass" , this.getApplication()};

		try {
			this.userView = (IUserView) this.serviceManager.executar(null, "Autenticacao", args);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Authenticating User!");
		}
	}


	
	protected void tearDown() {
	}

	public void testReadPosGradStudentCurricularPlans() {
		List result = null;

		System.out.println("test Case 1-ReadPosGradStudentCurricularPlans- with valid data");		
		Integer studentIdNumber = new Integer(11);
		
		
		Object args1[] = {studentIdNumber};

		try {
			result = (List) this.serviceManager.executar(userView, "ReadPosGradStudentCurricularPlans", args1);
		} catch (FenixServiceException ex) {
			System.out.println(ex.toString());
			fail("Reading the Curricular Plans of a PosGrad Student");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("Reading the Curricular Plans of a PosGrad Student");
		}
		assertEquals(result.size(), 1);
		System.out.println("\nOK - One student curricular plan Read!");
		System.out.println("test Case 2-ReadPosGradStudentCurricularPlans - non-existing student");	
		Object args2[] = {new Integer(50)};

		try {
			result = (List) this.serviceManager.executar(userView, "ReadPosGradStudentCurricularPlans", args2);
			fail("Reading the Curricular Plans of a PosGrad Student with non-existing-studentId");
		} catch (InvalidArgumentsServiceException ex) {
			System.out.println("\nOK - proper exception thrown!");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			fail("test Case 2-wrong exception thrown");
		}

		System.out.println("test Case 3-ReadPosGradStudentCurricularPlans - graduation Student");	
		Object args3[] = {new Integer(8)};

				try {
					result = (List) this.serviceManager.executar(userView, "ReadPosGradStudentCurricularPlans", args3);
					fail("test case 3- exception not thrown");
				} catch (NotAuthorizedException ex) {
					System.out.println("\nOK - proper exception thrown!");					
				} catch (Exception ex) {
					System.out.println(ex.toString());
					fail("test Case 3-wrong exception thrown");
				}
	}
}