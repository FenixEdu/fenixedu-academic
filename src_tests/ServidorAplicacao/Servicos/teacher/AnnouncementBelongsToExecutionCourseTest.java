/*
 * Created on 8/Out/2003
 *
 */

package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Barbosa
 * @author Pica
 *
 */

public abstract class AnnouncementBelongsToExecutionCourseTest
	extends ServiceNeedsAuthenticationTestCase {

	protected AnnouncementBelongsToExecutionCourseTest(String name) {
		super(name);
	}

	protected void AnnouncementBelongsToExecutionCourseTestSuccessful() {
		Object serviceArguments[] = getTestAnnouncementSuccessfullArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					serviceArguments);
			System.out.println(
				"AnnouncementBelongsToExecutionCourseTestSuccessful was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (NotAuthorizedException ex) {

			fail(
				getNameOfServiceToBeTested()
					+ "fail AnnouncementBelongsToExecutionCourseTestSuccessful");

		} catch (Exception ex) {
			fail(
				"Não foi possivel correr o serviço"
					+ getNameOfServiceToBeTested());
		}
	}

	protected void AnnouncementBelongsToExecutionCourseTestUnsuccessful() {
		Object serviceArguments[] = getTestAnnouncementUnsuccessfullArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					serviceArguments);
			fail(getNameOfServiceToBeTested()
				+ "fail AnnouncementBelongsToExecutionCourseTestUnsuccessful");

		} catch (NotAuthorizedException ex) {
			System.out.println(
				"AnnouncementBelongsToExecutionCourseTestUnsuccessful was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			
		} catch (Exception ex) {
			fail(
				"Não foi possivel correr o serviço"
					+ getNameOfServiceToBeTested());
		}
	}

	protected abstract String[] getAuthorizedUser();
	protected abstract String[] getUnauthorizedUser();
	protected abstract String[] getNonTeacherUser();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract Object[] getAuthorizeArguments();
	protected abstract String getDataSetFilePath();
	protected abstract Object[] getNonAuthorizeArguments();
	protected abstract Object[] getTestAnnouncementSuccessfullArguments();
	protected abstract Object[] getTestAnnouncementUnsuccessfullArguments();
	protected abstract String getApplication();
}