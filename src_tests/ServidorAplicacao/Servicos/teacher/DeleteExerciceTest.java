/*
 * Created on 25/Set/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Susana Fernandes
 */
public class DeleteExerciceTest extends TestCaseDeleteAndEditServices {

	public DeleteExerciceTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "DeleteExercice";
	}

	protected boolean needsAuthorization() {
		return true;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		Object[] args =
			{
				new Integer(26),
				new Integer(1),
				new String("file:///Users/susana/Documents/software/eclipse/workspace/fenix/build/app/")};
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
