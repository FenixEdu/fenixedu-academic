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
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import Util.TipoCurso;

public class ReadCourseByStudentTest extends TestCaseReadServices {

	public ReadCourseByStudentTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ReadCourseByStudentTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }



  protected String getNameOfServiceToBeTested() {
	  return "ReadCourseByStudent";
  }

  protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
	  Object[] result = { new Integer(800), new TipoCurso(TipoCurso.MESTRADO) };
	  return result;
  }

  protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
	  Object[] result = { new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA) };
	  return result;
  }

  protected int getNumberOfItemsToRetrieve() {
	  return 1;
  }

  protected Object getObjectToCompare() {
  	InfoDegree infoDegree = new InfoDegree();
  	infoDegree.setSigla("LEIC");
	return infoDegree;
  }

}
