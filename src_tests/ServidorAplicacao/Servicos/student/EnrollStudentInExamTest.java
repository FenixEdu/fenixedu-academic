/*
 * ReadCourseByStudentTest.java JUnit based test
 *
 * Created on February 26th, 2003, 15:33
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author João Mota
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoPerson;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

public class EnrollStudentInExamTest extends TestCaseDeleteAndEditServices {

	private InfoPerson infoPerson = null;
	private InfoStudent infoStudent = null;
	private InfoShiftEnrolment infoShiftEnrolment = null;
	
    public EnrollStudentInExamTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(EnrollStudentInExamTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }



  protected String getNameOfServiceToBeTested() {
	  return "EnrollStudentInExam";
  }

  protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {	
	return null;
	 
  }

  protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
	  Object[] result = { "user",new Integer(2)};
	  return result;
  }

 
}
