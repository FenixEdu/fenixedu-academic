/*
 * ReadCourseByStudentTest.java JUnit based test
 *
 * Created on February 26th, 2003, 15:33
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author Nuno Nunes & Joana Mota
 */
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ISiteComponent;
import DataBeans.InfoPerson;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentSiteExams;
import DataBeans.SiteView;
import ServidorAplicacao.Servicos.TestCaseReadServices;

public class ReadExamsByStudentTest extends TestCaseReadServices {

	private InfoPerson infoPerson = null;
	private InfoStudent infoStudent = null;
	private InfoShiftEnrolment infoShiftEnrolment = null;
	
    public ReadExamsByStudentTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ReadExamsByStudentTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }



  protected String getNameOfServiceToBeTested() {
	  return "ReadExamsByStudent";
  }

  protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
	return null;
	 
  }

  protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
	  Object[] result = { "3"};
	  return result;
  }

  protected int getNumberOfItemsToRetrieve() {
	  return 1;
  }


//TODO: add data to run this test properly
  protected Object getObjectToCompare() {
  	SiteView siteView = new SiteView();
 	ISiteComponent component = new InfoStudentSiteExams(new ArrayList(),new ArrayList());
  	siteView.setComponent(component);
  	
	return siteView;
  }

}
