package ServidorAplicacao.Servicos.teacher;

import java.util.List;

import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteCommon;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadSiteAnnouncementTest
	extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param testName
	 */

	public ReadSiteAnnouncementTest(String testName) {
		super(testName);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testReadSiteAnnouncementDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testReadSiteAnnouncementExpectedDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "TeacherAdministrationSiteComponentService";
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
		Integer infoSiteCode = new Integer(1);
		InfoSiteCommon commonComponent = new InfoSiteCommon();
		InfoSiteAnnouncement bodyComponent = new InfoSiteAnnouncement();
		Object obj1 = null;
		Object obj2 = null;

		Object[] args =
			{
				infoExecutionCourseCode,
				commonComponent,
				bodyComponent,
				infoSiteCode,
				obj1,
				obj2 };
		return args;
	}

	public void testSuccessfullSiteWithAnnouncements() {

		try {
			SiteView result = null;
			String[] args1 = getAuthenticatedAndAuthorizedUser();
			IUserView userView = authenticateUser(args1);

			Object[] args2 = getAuthorizeArguments();

			result =
				(SiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args2);

			InfoSiteAnnouncement infoSiteAnnouncement =
				(InfoSiteAnnouncement) result.getComponent();
			List infoAnnouncements = infoSiteAnnouncement.getAnnouncements();
			assertEquals(infoAnnouncements.size(), 4);

			//check if database hasn't changed
			compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Reading the Announcements of a Site with announcements" + ex);
		} catch (Exception ex) {
			fail("Reading the Announcements of a Site with announcements" + ex);
		}
	}

	public void testSuccessfullSiteWithoutAnnouncements() {

		try {
			SiteView result = null;
			Integer infoExecutionCourseCode = new Integer(25);
			Integer infoSiteCode = new Integer(2);
			InfoSiteCommon commonComponent = new InfoSiteCommon();
			InfoSiteAnnouncement bodyComponent = new InfoSiteAnnouncement();
			Object obj1 = null;
			Object obj2 = null;

			String[] args1 = getAuthenticatedAndAuthorizedUser();
			Object[] args2 =
				{
					infoExecutionCourseCode,
					commonComponent,
					bodyComponent,
					infoSiteCode,
					obj1,
					obj2 };
			
			IUserView userView = authenticateUser(args1);

			result =
				(SiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args2);

			InfoSiteAnnouncement infoSiteAnnouncement =
				(InfoSiteAnnouncement) result.getComponent();
			List infoAnnouncements = infoSiteAnnouncement.getAnnouncements();
			assertEquals(infoAnnouncements.size(), 0);
			//check if database hasn't changed
			compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Reading the Announcements of a Site without announcements" + ex);
		} catch (Exception ex) {
			fail("Reading the Announcements of a Site without announcements" + ex);
		}
	}
}
