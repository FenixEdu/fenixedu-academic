package ServidorPersistente.OJB;

import junit.framework.TestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import Tools.dbaccess;

public class TestCaseOJB extends TestCase {

	private dbaccess dbAcessPoint = null;
	protected ISuportePersistente sp;
	public TestCaseOJB(String testName) {
		super(testName);
	}

	protected void setUp() {
		// The following code backs up the contents of the database
		// and loads the database with the data set required to run
		// the test cases.
		try {
			dbAcessPoint = new dbaccess();
			dbAcessPoint.openConnection();
			dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
			dbAcessPoint.loadDataBase(getDataSetFilePath());
			dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			System.out.println("Setup failed: " + ex);
		}
		
		try {
			SuportePersistenteOJB.resetInstance();
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail ("Setup: getting persistent factory!");
		}
	}

	/**
	* @return
	*/
	protected String getDataSetFilePath() {
		return "etc/testDataSet.xml";
	}

	protected void tearDown() {
		try {
			dbAcessPoint.openConnection();
			dbAcessPoint.loadDataBase("etc/testBackup.xml");
			//dbAcessPoint.loadDataBase("etc/testDataSet.xml");
			dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			System.out.println("Tear down failed: " + ex);
		}
	}

}
