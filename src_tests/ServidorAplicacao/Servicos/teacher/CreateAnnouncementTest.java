package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author  Barbosa
 * @author  Pica
 * 
 */
public class CreateAnnouncementTest
	extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param testName
	 */
	public CreateAnnouncementTest(String testName) {
		super(testName);
	}
	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testCreateAnnouncementDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testCreateAnnouncementExpectedDataSet.xml";
	}
	
	protected String getExistingExpectedDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testCreateExistingAnnouncementExpectedDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "CreateAnnouncement";
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser() {
		String[] args = { "nmsn", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser() {
		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {
		Integer infoExecutionCourseCode = new Integer(24);
		String newAnnouncementTitle = "Anuncio Teste SUCESSO";
		String newAnnouncementInformation = "Sucesso Sucesso";
		Object[] args =
			{
				infoExecutionCourseCode,
				newAnnouncementTitle,
				newAnnouncementInformation };
		return args;
	}

	public void testCreateAnnouncementSuccessful() {
		try {
			boolean result = false;
			String[] args = getAuthenticatedAndAuthorizedUser();
			IUserView id = authenticateUser(args);
			Object[] args2 = getAuthorizeArguments();

			gestor.executar(id, getNameOfServiceToBeTested(), args2);

			compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
			System.out.println(
				"CreateAnnouncementSuccessfulTest was SUCCESSFULY runned by service: "
					+ this.getClass().getName());

		} catch (FenixServiceException e) {
			fail("Creating a new Announcement for a Site " + e);
		} catch (Exception e) {
			fail("Creating a new Announcement for a Site " + e);
		}
	}

	public void testCreateExistingAnnouncement() {
		Integer infoExecutionCourseCode = new Integer(24);
		String newAnnouncementTitle = "announcement1deTFCI";
		String newAnnouncementInformation = "information1";
		Object[] args =
			{
				infoExecutionCourseCode,
				newAnnouncementTitle,
				newAnnouncementInformation };
		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), args);
			System.out.println(
				"testCreateExistingAnnouncement was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testCreateExistingAnnouncement");

		} catch (ExistingServiceException e) {
			compareDataSetUsingExceptedDataSetTableColumns(getExistingExpectedDataSetFilePath());
			System.out.println(
				"testCreateExistingAnnouncement was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			System.out.println(
				"testCreateExistingAnnouncement was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testCreateExistingAnnouncement");
		}
	}
}