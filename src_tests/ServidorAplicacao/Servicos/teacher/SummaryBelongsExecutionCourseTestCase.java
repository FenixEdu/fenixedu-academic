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
public abstract class SummaryBelongsExecutionCourseTestCase
	extends ServiceNeedsAuthenticationTestCase {

	protected SummaryBelongsExecutionCourseTestCase(String name) {
		super(name);
	}

	public void testSummaryBelongsExecutionCourse() {

		Object serviceArguments[] = getTestSummarySuccessfullArguments();

		Object result = null;

		try {
			result =
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
					+ "fail testSummaryBelongsExecutionCourse");
		}
		catch (Exception ex) {
			fail(
				getNameOfServiceToBeTested()
					+ "fail testSummaryBelongsExecutionCourse");
		}
	}

	public void testSummaryNotBelongsExecutionCourse() {

		Object serviceArguments[] = getTestSummaryUnsuccessfullArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView3,
					getNameOfServiceToBeTested(),
					serviceArguments);
			fail(
				getNameOfServiceToBeTested()
					+ "fail testSummaryNotBelongsExecutionCourse");
		}
		catch (NotAuthorizedException ex) {

			System.out.println(
				"testItemNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		}
		catch (Exception ex) {
			fail(
				getNameOfServiceToBeTested()
					+ "fail testSummaryNotBelongsExecutionCourse");
		}
	}

	protected abstract Object[] getAuthorizeArguments();
	protected abstract String[] getAuthorizedUser();
	protected abstract String getDataSetFilePath();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract String[] getNonTeacherUser();
	protected abstract String[] getUnauthorizedUser();
	protected abstract String getApplication();

	protected abstract Object[] getTestSummarySuccessfullArguments();
	protected abstract Object[] getTestSummaryUnsuccessfullArguments();
}