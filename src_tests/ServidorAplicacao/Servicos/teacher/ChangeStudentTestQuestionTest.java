/*
 * Created on Oct 29, 2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseReadServices;
import Util.TestQuestionChangesType;
import Util.TestQuestionStudentsChangesType;

/**
 * @author Susana Fernandes
 *
 */
public class ChangeStudentTestQuestionTest extends TestCaseReadServices {

	public ChangeStudentTestQuestionTest(String testName) {
			super(testName);
		}

		protected void setUp() {
			super.setUp();
		}

		protected void tearDown() {
			super.tearDown();
		}

		protected String getNameOfServiceToBeTested() {
			return "ChangeStudentTestQuestion";
		}

		protected boolean needsAuthorization() {
			return true;
		}
//			Integer executionCourseId,
//			Integer distributedTestId,
//			Integer oldQuestionId,
//			Integer newMetadataId,
//			Integer studentId,
//			TestQuestionChangesType changesType,
//			Boolean delete,
//			TestQuestionStudentsChangesType studentsType,
//			String path

		protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

			Object[] args = { new Integer(26), new Integer(3), new Integer(10), null, new Integer(9), new TestQuestionChangesType(1), new Boolean(true), new TestQuestionStudentsChangesType(1), new String("file:///Users/susana/Documents/software/eclipse/workspace/fenix/build/app/")};
	//		Object[] args = { new Integer(26), new Integer(1), new Integer(1), null, new Integer(9), new TestQuestionChangesType(1), new Boolean(true), new TestQuestionStudentsChangesType(1), new String("file:///Users/susana/Documents/software/eclipse/workspace/fenix/build/app/")};
			return args;
		}

		protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
			return null;
		}

		protected int getNumberOfItemsToRetrieve() {
			return 0;
		}

		protected Object getObjectToCompare() {
			return new Boolean(true);
			//return new Boolean(false);			
		}
}