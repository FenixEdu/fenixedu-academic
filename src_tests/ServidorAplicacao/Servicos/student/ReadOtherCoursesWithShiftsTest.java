/*
 * ReadShiftEnrolmentTest.java JUnit based test
 *
 * Created on January 13th, 2002, 17:42
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoPerson;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.TipoCurso;

public class ReadOtherCoursesWithShiftsTest extends TestCaseServicos {

	private InfoPerson infoPerson = null;
	private InfoStudent infoStudent = null;
	private InfoShiftEnrolment infoShiftEnrolment = null;
	
    public ReadOtherCoursesWithShiftsTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ReadOtherCoursesWithShiftsTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

// FIXME : The service seens to be REALLY messed up

  protected String getNameOfServiceToBeTested() {
	  return "ReadOtherCoursesWithShifts";
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
