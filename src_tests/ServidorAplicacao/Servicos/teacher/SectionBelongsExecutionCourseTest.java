/*
 * Created on 7/Out/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author  Luis Egidio, lmre@mega.ist.utl.pt
 * 			Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public abstract class SectionBelongsExecutionCourseTest
	extends ServiceNeedsAuthenticationTestCase {

	protected SectionBelongsExecutionCourseTest(String name) {
		super(name);
	}

	public void testSectionBelongsExecutionCourse() {

		Object serviceArguments[] = getTestSectionSuccessfullArguments();

		try {
			ServiceManagerServiceFactory.executeService(
				userView,
				getNameOfServiceToBeTested(),
				serviceArguments);
			System.out.println(
				"testSectionBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (NotAuthorizedException ex) {
			fail(
				getNameOfServiceToBeTested()
					+ "fail testSectionlongsExecutionCourse");

		} catch (Exception ex) {
			fail("Unable to run service: " + getNameOfServiceToBeTested());

		}

	}

	public void testSectionNotBelongsExecutionCourse() {

		Object serviceArguments[] = getTestSectionUnsuccessfullArguments();

		try {
			
				ServiceManagerServiceFactory.executeService(
					userView,
					getNameOfServiceToBeTested(),
					serviceArguments);
			fail(
				getNameOfServiceToBeTested()
					+ "fail testSectionNotBelongsExecutionCourse");

		} catch (NotAuthorizedException ex) {

			System.out.println(
				"testSectionNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (Exception ex) {
			fail("Unable to run service: " + getNameOfServiceToBeTested());

		}

	}

	protected abstract Object[] getAuthorizeArguments();
	protected abstract String[] getAuthenticatedAndAuthorizedUser();
	protected abstract String getDataSetFilePath();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract String[] getNotAuthenticatedUser();
	protected abstract String[] getAuthenticatedAndUnauthorizedUser();
	protected abstract Object[] getTestSectionSuccessfullArguments();
	protected abstract Object[] getTestSectionUnsuccessfullArguments();
	protected abstract String getApplication();

}
