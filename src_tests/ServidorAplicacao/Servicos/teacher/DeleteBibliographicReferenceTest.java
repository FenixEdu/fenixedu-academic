package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
import junit.framework.TestSuite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class DeleteBibliographicReferenceTest extends ServiceNeedsAuthenticationTestCase {

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
		return "etc/datasets/servicos/teacher/testDeleteBibliographicReferenceDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testExpectedDeleteBibliographicReferenceDataSet.xml";
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {

		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser() {

		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser() {

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

	public void testDeleteBibliographicReferenceByAuthenticatedAndAuthorizedUser() {

		try {

			String[] args = getAuthenticatedAndAuthorizedUser();
			IUserView userView = authenticateUser(args);

			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

			compareDataSet(getExpectedDataSetFilePath());

		} catch (FenixServiceException ex) {
			fail("testSuccessfullDeleteBibliographicReference" + ex);
		} catch (Exception ex) {
			fail("testSuccessfullDeleteBibliographicReference error on compareDataSet" + ex);
		}
	}

	public void testDeleteBibliographicReferenceByAuthenticatedAndNotAuthorizedUser() {

		try {

			String[] args = getAuthenticatedAndAuthorizedUser();
			IUserView userView = authenticateUser(args);

			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), getTestBibliographicReferenceUnsuccessfullArguments());

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