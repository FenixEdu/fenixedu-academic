package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Barbosa
 * @author Pica
 * Created on 8/Out/2003
 */

public abstract class AnnouncementBelongsToExecutionCourseTest
	extends ServiceNeedsAuthenticationTestCase {

	protected AnnouncementBelongsToExecutionCourseTest(String name) {
		super(name);
	}

	public void testAnnouncementNotBelongsToExecutionCourse() {
		Object[] serviceArguments = getAnnouncementUnsuccessfullArguments();
		IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
		
		try {
			ServiceManagerServiceFactory.executeService(
				userView,
				getNameOfServiceToBeTested(),
				serviceArguments);
			fail(
				"Fail AnnouncementBelongsToExecutionCourseTestUnsuccessful"
					+ getNameOfServiceToBeTested());
		} catch (NotAuthorizedException ex) {
			/*
			 * O anúncio existe mas não pertence à disciplina.
			 * Os pré-filtros lançam uma excepcao NotAuthorizedException,
			 * o serviço não chega a ser invocado
			 */
			//Comparacao do dataset
			compareDataSetUsingExceptedDataSetTablesAndColumns(getExpectedUnsuccessfullDataSetFilePath());
			System.out.println(
				"AnnouncementBelongsToExecutionCourseTestUnsuccessful was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (Exception ex) {
			fail(
				"Fail AnnouncementBelongsToExecutionCourseTestUnsuccessful"
					+ getNameOfServiceToBeTested());
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
	 */
	protected abstract String[] getAuthenticatedAndAuthorizedUser();
	protected abstract String[] getAuthenticatedAndUnauthorizedUser();
	protected abstract String[] getNotAuthenticatedUser();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract Object[] getAuthorizeArguments();
	protected abstract String getDataSetFilePath();
	protected abstract String getApplication();
	/*
	 * Anúncio que não pertence à disciplina escolhida
	 */
	protected abstract Object[] getAnnouncementUnsuccessfullArguments();
	/*
	 * DataSet igual ao dataset carregado na base de dados (sem alterações).
	 */
	protected abstract String getExpectedUnsuccessfullDataSetFilePath();
}