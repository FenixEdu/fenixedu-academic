package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoAnnouncement;
import DataBeans.InfoSiteCommon;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAnnouncementTest
	extends AnnouncementBelongsToExecutionCourseTest {

	/**
	 * @param testName
	 */

	public ReadAnnouncementTest(String testName) {
		super(testName);
	}

	protected String getApplication() {
		return "EXTRANET";
	}

	protected String getDataSetFilePath() {
		return "etc/testReadAnnouncementDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "TeacherAdministrationSiteComponentService";
	}

	protected String[] getAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {
		String[] args = { "nmsn", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {
		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {
		Integer infoExecutionCourseCode = new Integer(24);
		Integer infoSiteCode = new Integer(1);
		InfoSiteCommon commonComponent = new InfoSiteCommon();
		InfoAnnouncement bodyComponent = new InfoAnnouncement();
		Integer announcementCode = new Integer(1);
		Object obj2 = null;

		Object[] args =
			{
				infoExecutionCourseCode,
				commonComponent,
				bodyComponent,
				infoSiteCode,
				announcementCode,
				obj2 };
		return args;
	}

	protected Object[] getAnnouncementUnsuccessfullArguments() {
		Integer infoExecutionCourseCode = new Integer(24);
		Integer infoSiteCode = new Integer(1);
		InfoSiteCommon commonComponent = new InfoSiteCommon();
		InfoAnnouncement bodyComponent = new InfoAnnouncement();
		Integer announcementCode = new Integer(5);
		Object obj2 = null;

		Object[] args =
			{
				infoExecutionCourseCode,
				commonComponent,
				bodyComponent,
				infoSiteCode,
				announcementCode,
				obj2 };
		return args;
	}

	protected String getExpectedUnsuccessfullDataSetFilePath() {
		return "";
	}

	public void testReadAnnouncementSuccessfull() {
		try {
			boolean result = false;
			String[] args = getAuthorizedUser();
			IUserView id = authenticateUser(args);

			gestor.executar(
				id,
				getNameOfServiceToBeTested(),
				getAuthorizeArguments());

			System.out.println(
				"ReadAnnouncementTest was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			fail("Reading an Announcement for a Site " + e);
		} catch (Exception e) {
			fail("Reading an Announcement for a Site " + e);
		}
	}
	
/*
	public void testReadAnnouncementUnsuccessfull() {
		try {
			boolean result = false;
			String[] args = getAuthorizedUser();
			IUserView id = authenticateUser(args);

			gestor.executar(
				id,
				getNameOfServiceToBeTested(),
			getAnnouncementUnsuccessfullArguments());

			fail("Reading an Announcement for a Site ");

		} catch (FenixServiceException e) {
			System.out.println(
				"ReadAnnouncementTest was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (Exception e) {
			fail("Reading an Announcement for a Site " + e);
		}
	}*/
}
