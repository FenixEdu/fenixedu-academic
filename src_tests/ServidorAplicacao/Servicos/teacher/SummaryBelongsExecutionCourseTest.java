/*
 * Created on 8/Out/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo 
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class SummaryBelongsExecutionCourseTest
	extends ServiceNeedsAuthenticationTestCase {

	protected SummaryBelongsExecutionCourseTest(String name) {
		super(name);
	}

	public void testSummaryBelongsExecutionCourse() {

		Object serviceArguments[] = getAuthorizeArguments();

		try {
			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				serviceArguments);
			System.out.println(
				"testSummaryBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		}
		catch (NotAuthorizedException ex) {
			fail(
				getNameOfServiceToBeTested()
					+ " testSummaryBelongsExecutionCourse "
					+ ex);
		}
		catch (Exception ex) {
			fail(
				getNameOfServiceToBeTested()
					+ " testSummaryBelongsExecutionCourse "
					+ ex);
		}
	}

	public void testSummaryNotBelongsExecutionCourse() {

		Object serviceArguments[] = getTestSummaryUnsuccessfullArguments();

		try {
			gestor.executar(
				userView3,
				getNameOfServiceToBeTested(),
				serviceArguments);
			fail(
				getNameOfServiceToBeTested()
					+ " testSummaryNotBelongsExecutionCourse");
		}
		catch (NotAuthorizedException ex) {
			System.out.println(
				"testItemNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		}
		catch (Exception ex) {
			fail(
				getNameOfServiceToBeTested()
					+ " testSummaryNotBelongsExecutionCourse" + ex);
		}
	}

	protected abstract Object[] getAuthorizeArguments();
	protected abstract String[] getAuthenticatedAndAuthorizedUser();
	protected abstract String getDataSetFilePath();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract String[] getNotAuthenticatedUser();
	protected abstract String[] getAuthenticatedAndUnauthorizedUser();
	protected abstract String getApplication();

	protected abstract Object[] getTestSummaryUnsuccessfullArguments();
}