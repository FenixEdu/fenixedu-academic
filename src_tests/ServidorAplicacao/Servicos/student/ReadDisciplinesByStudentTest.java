/*
 * ReadCourseByStudentTest.java JUnit based test
 *
 * Created on February 26th, 2003, 16:51
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author Nuno Nunes & Joana Mota
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoPerson;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import Util.TipoCurso;

public class ReadDisciplinesByStudentTest extends TestCaseReadServices {

	private InfoPerson infoPerson = null;
	private InfoStudent infoStudent = null;
	private InfoShiftEnrolment infoShiftEnrolment = null;
	
    public ReadDisciplinesByStudentTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ReadDisciplinesByStudentTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }


	// TODO: Not used at this time


  protected String getNameOfServiceToBeTested() {
	  return "ReadDisciplinesByStudent";
  }

  protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
	  Object[] result = { new Integer(800), new TipoCurso(TipoCurso.DOUTORAMENTO) };
	  return result;
  }

  protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
	  Object[] result = { new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA) };
	  return result;
  }

  protected int getNumberOfItemsToRetrieve() {
	  return 0;
  }

  protected Object getObjectToCompare() {
	return null;
  }

}
