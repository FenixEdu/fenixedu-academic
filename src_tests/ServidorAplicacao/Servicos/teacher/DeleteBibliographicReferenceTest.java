package ServidorAplicacao.Servicos.teacher;

import junit.framework.TestSuite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class DeleteBibliographicReferenceTest
	extends BibliographicReferenceBelongsExecutionCourse {

	public static void main(java.lang.String[] args) {
		TestSuite suite = new TestSuite(DeleteBibliographicReferenceTest.class);
		junit.textui.TestRunner.run(suite);
	}

	public DeleteBibliographicReferenceTest(java.lang.String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "DeleteBibliographicReference";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/testDeleteBibliographicReferenceDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/testExpectedDeleteBibliographicReferenceDataSet.xml";
	}

	protected String[] getAuthorizedUser() {

		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {

		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {

		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer bibliographicReferenceCode = new Integer(1);

		Object[] args = { executionCourseCode, bibliographicReferenceCode };

		return args;
	}

	protected Object[] getTestBibliographicReferenceSuccessfullArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer bibliographicReferenceCode = new Integer(1);

		Object[] args = { executionCourseCode, bibliographicReferenceCode };

		return args;
	}

	protected Object[] getTestBibliographicReferenceUnsuccessfullArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer bibliographicReferenceCode = new Integer(123);

		Object[] args = { executionCourseCode, bibliographicReferenceCode };

		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfullDeleteBibliographicReference() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

			compareDataSet(getExpectedDataSetFilePath());

		} catch (FenixServiceException ex) {
			fail("testSuccessfullDeleteBibliographicReference" + ex);
		} catch (Exception ex) {
			fail("testSuccessfullDeleteBibliographicReference error on compareDataSet" + ex);
		}
	}

	public void testUnsuccessfullDeleteBibliographicReference() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				getTestBibliographicReferenceUnsuccessfullArguments());

			fail("testUnsuccessfullDeleteBibliographicReference deletion of an unexisting bibliography");

		} catch (FenixServiceException ex) {
			System.out.println(
				"testUnsuccessfullDeleteBibliographicReference "
					+ "was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (Exception ex) {
			fail("testUnsuccessfullDeleteBibliographicReference error on compareDataSet" + ex);
		}
	}
}