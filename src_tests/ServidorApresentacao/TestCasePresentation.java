/*
 * Created on 18/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao;

/**
 * @author jmota
 */

import servletunit.struts.MockStrutsTestCase;
import Tools.dbaccess;
public class TestCasePresentation extends MockStrutsTestCase {

	private dbaccess _dbAcessPoint = null;

	public TestCasePresentation(String testName) {
		super(testName);
	}

	public void setUp(){
		
		try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Setting up!");
		}
		
		//		The following code backs up the contents of the database
		// and loads the database with the data set required to run
		// the test cases.
		try {
			_dbAcessPoint = new dbaccess();
			_dbAcessPoint.openConnection();
			_dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
			_dbAcessPoint.loadDataBase("etc/testDataSet.xml");
			_dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			System.out.println("Setup failed: " + ex);
			fail("Setting up!");
		}

	}

	protected void tearDown() {
		try {
			_dbAcessPoint.openConnection();
			_dbAcessPoint.loadDataBase("etc/testBackup.xml");
			_dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			System.out.println("Tear down failed: " + ex);
		}
	}

}
