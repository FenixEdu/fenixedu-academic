/*
 * Created on 11/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseReadServices;

/**
 * @author Susana Fernandes
 */
public class InsertTestTest extends TestCaseReadServices {

	public InsertTestTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertTest";
	}

	protected boolean needsAuthorization() {
		return true;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args =
			{
				new Integer(26),
				new String("testTitle"),
				new String("testInformation")};
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		return new Integer(6);
	}

}