/*
 * Created on 29/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DeleteStudentGroupTest extends TestCaseDeleteAndEditServices {

public DeleteStudentGroupTest(String testName) {
	super(testName);		
}
	
protected void setUp() {
	super.setUp();
}

protected void tearDown() {
	super.tearDown();
}
	
protected String getNameOfServiceToBeTested() {
	return "DeleteStudentGroup";
}
	
protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
	Object[] argsDeleteItem = {new Integer(5)};
		
	return argsDeleteItem;		
}
	
protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
	Object[] argsDeleteItem = {new Integer(6)};
		
	return argsDeleteItem;
}
}