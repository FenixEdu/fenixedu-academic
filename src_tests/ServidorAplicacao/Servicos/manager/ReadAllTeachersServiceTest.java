/*
 * Created on 1/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

/**
 * @author lmac1
 */

public class ReadAllTeachersServiceTest extends TestCaseManagerReadServices {
	    
	/**
	 * @param testName
	 */
	 public ReadAllTeachersServiceTest(String testName) {
		super(testName);
	 }

	 protected String getNameOfServiceToBeTested() {
		return "ReadAllTeachers";
	 }

	protected int getNumberOfItemsToRetrieve() {
		return 7;
	}
}