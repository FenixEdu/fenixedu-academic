/*
 * Created on 7/Out/2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Luis
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class ItemBelongsExecutionCourseTest
	extends ServiceNeedsAuthenticationTestCase {

	protected ItemBelongsExecutionCourseTest(String name) {
		super(name);
	}

	public void testItemBelongsExecutionCourse() {

		Object serviceArguments[] = getTestItemSuccessfullArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					serviceArguments);
			System.out.println(
				"testItemBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (NotAuthorizedException ex) {
			fail(getNameOfServiceToBeTested() + "fail testItemBelongsExecutionCourse");

		} catch (Exception ex) {
			fail(
				"Não foi possivel correr o serviço"
					+ getNameOfServiceToBeTested());

		}

	}

	public void testItemNotBelongsExecutionCourse() {

		Object serviceArguments[] = getTestItemUnsuccessfullArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					serviceArguments);
			fail(getNameOfServiceToBeTested() + "fail testItemNotBelongsExecutionCourse");

		} catch (NotAuthorizedException ex) {

			System.out.println(
				"testItemNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (Exception ex) {
			fail(
				"Não foi possivel correr o serviço"
					+ getNameOfServiceToBeTested());

		}

	}

	protected abstract Object[] getAuthorizeArguments();
	protected abstract String[] getAuthorizedUser();
	protected abstract String getDataSetFilePath();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract String[] getNonTeacherUser();
	protected abstract String[] getUnauthorizedUser();
	protected abstract Object[] getTestItemSuccessfullArguments();
	protected abstract Object[] getTestItemUnsuccessfullArguments();
	protected abstract String getApplication();

}
