/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;


/**
 * @author lmac1
 */

public class ReadExecutionCoursesByCurricularCourseServiceTest extends TestCaseManagerReadServices {
	    
	/**
	 * @param testName
	 */
	 public ReadExecutionCoursesByCurricularCourseServiceTest(String testName) {
		super(testName);
	 }

	 protected String getNameOfServiceToBeTested() {
		return "ReadExecutionCoursesByCurricularCourse";
	 }
		
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(14) };
		return args;
	}
		
	protected int getNumberOfItemsToRetrieve() {
		return 1;
	}
}