package ServidorPersistente.OJB;

import junit.framework.TestCase;
import Tools.dbaccess;

public class TestCaseOJB extends TestCase {

  private dbaccess dbAcessPoint = null;
    
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
  		dbAcessPoint.loadDataBase("etc/testDataSet.xml");
  		dbAcessPoint.closeConnection();
  	} catch (Exception ex) {
  		System.out.println("Setup failed: " + ex);
  	}
  	
  }

  protected void tearDown() {
  	try {
  		dbAcessPoint.openConnection();
  		dbAcessPoint.loadDataBase("etc/testBackup.xml");
  		//dbAcessPoint.loadDataBase("etc/testDataSet.xml");
  		dbAcessPoint.closeConnection();
  	} catch (Exception ex) {
  		System.out.println("Tear down failed: " +ex);
  	}
  }
            
    
}
