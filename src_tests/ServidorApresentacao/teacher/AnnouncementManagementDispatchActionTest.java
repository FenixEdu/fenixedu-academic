package ServidorApresentacao.teacher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoSection;
import DataBeans.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brandão
 */
public class AnnouncementManagementDispatchActionTest extends TestCasePresentationTeacherPortal {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public AnnouncementManagementDispatchActionTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(AnnouncementManagementDispatchActionTest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/web.xml";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "/teacher";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/announcementManagementAction";
	}

	public void testAuthorizedPrepareCreateAnnouncement() {
		super.setAuthorizedUser();
		if (getSession().getAttribute(SessionConstants.U_VIEW)==null) System.out.println("userView==null");		

		//set request path
		setRequestPathInfo("teacher", "/announcementManagementAction");

		//sets needed objects to session/request
		addRequestParameter("method", "prepareCreateAnnouncement");
		
		//action perform
		actionPerform();

		//verify that there are no errors
		verifyNoActionErrors();

		//verifies forward
		verifyForward("insertAnnouncement");
	}

	public void testAuthorizedCreateAnnouncement() {
		super.setAuthorizedUser();

		//set request path
		setRequestPathInfo("teacher", "/announcementManagementAction");

		//sets needed objects to session/request
		addRequestParameter("method", "createAnnouncement");
		addRequestParameter("title", "title");
		addRequestParameter("information", "information");

		//infoSection
		InfoSection infoSection = new InfoSection("Seccao1deTFCI", new Integer(0), null);

		//infoSite
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"Trabalho Final de Curso I",
				"TFCI",
				"programa1",
				new Double(1.5),
				new Double(2),
				new Double(1.5),
				new Double(2),
				infoExecutionPeriod);
		InfoSite infoSite = new InfoSite(infoExecutionCourse);
		infoSection.setInfoSite(infoSite);
		infoSite.setInitialInfoSection(infoSection);
		
		getSession().setAttribute(SessionConstants.INFO_SITE, infoSite);
		
		//action perform
		actionPerform();

		//verify that there are no errors
		verifyNoActionErrors();

		//verifies forward
		verifyForward("accessAnnouncementManagement");
	}

	public void testAuthorizedPrepareEditAnnouncement() {
		super.setAuthorizedUser();

		//set request path
		setRequestPathInfo("teacher", "/announcementManagementAction");

		//sets needed objects to session/request
		addRequestParameter("method", "prepareEditAnnouncement");

		//infoSection
		InfoSection infoSection = new InfoSection("Seccao1deTFCI", new Integer(0), null);

		//infoSite
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"Trabalho Final de Curso I",
				"TFCI",
				"programa1",
				new Double(1.5),
				new Double(2),
				new Double(1.5),
				new Double(2),
				infoExecutionPeriod);
		InfoSite infoSite = new InfoSite(infoExecutionCourse);
		infoSection.setInfoSite(infoSite);
		infoSite.setInitialInfoSection(infoSection);

		try {
			Object args[] = new Object[1];
			args[0] = infoSite;
			GestorServicos manager = GestorServicos.manager();
			List announcements =
				(List) manager.executar(
					super.getAuthorizedUser(),
					"ReadAnnouncements",
					args);
			getSession().setAttribute(
				SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST,
				announcements);
		} catch (Exception exception) {
			exception.printStackTrace();
			fail("testAuthorizedPrepareEditAnnouncement");
		}

		addRequestParameter("index", "0");
		
		//action perform
		actionPerform();

		//verify that there are no errors
		verifyNoActionErrors();

		//verifies forward
		verifyForward("editAnnouncement");
	}

	public void testAuthorizedEditAnnouncement() {
		super.setAuthorizedUser();

		//set request path
		setRequestPathInfo("teacher", "/announcementManagementAction");

		//sets needed objects to session/request
		addRequestParameter("method", "editAnnouncement");
		addRequestParameter("title", "newTitle");
		addRequestParameter("information", "newInformation");

		//infoSection
		InfoSection infoSection = new InfoSection("Seccao1deTFCI", new Integer(0), null);

		//infoSite
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"Trabalho Final de Curso I",
				"TFCI",
				"programa1",
				new Double(1.5),
				new Double(2),
				new Double(1.5),
				new Double(2),
				infoExecutionPeriod);
		InfoSite infoSite = new InfoSite(infoExecutionCourse);
		infoSection.setInfoSite(infoSite);
		infoSite.setInitialInfoSection(infoSection);

		InfoAnnouncement infoAnnouncement = null;

		getSession().setAttribute(SessionConstants.INFO_SITE, infoSite);
		try {
			Object args[] = new Object[1];
			args[0] = infoSite;
			GestorServicos manager = GestorServicos.manager();
			List announcements = (List) manager.executar(super.getAuthorizedUser(), "ReadAnnouncements", args);
			infoAnnouncement = (InfoAnnouncement) announcements.get(0);
		} catch (Exception exception) {
			exception.printStackTrace();
			fail("testAuthorizedEditAnnouncement");
		}
		getSession().setAttribute("Announcement", infoAnnouncement);
				
		//action perform
		actionPerform();

		//verify that there are no errors
		verifyNoActionErrors();

		//verifies forward
		verifyForward("accessAnnouncementManagement");
	}

	public void testAuthorizedDeleteAnnouncement() {
		super.setAuthorizedUser();

		//set request path
		setRequestPathInfo("teacher", "/announcementManagementAction");

		//sets needed objects to session/request
		addRequestParameter("method", "deleteAnnouncement");

		//infoSection
		InfoSection infoSection = new InfoSection("Seccao1deTFCI", new Integer(0), null);

		//infoSite
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"Trabalho Final de Curso I",
				"TFCI",
				"programa1",
				new Double(1.5),
				new Double(2),
				new Double(1.5),
				new Double(2),
				infoExecutionPeriod);
		InfoSite infoSite = new InfoSite(infoExecutionCourse);
		infoSection.setInfoSite(infoSite);
		infoSite.setInitialInfoSection(infoSection);

		InfoAnnouncement infoAnnouncement = null;

		getSession().setAttribute(SessionConstants.INFO_SITE, infoSite);
		try {
			Object args[] = new Object[1];
			args[0] = infoSite;
			GestorServicos manager = GestorServicos.manager();
			List announcements = (List) manager.executar(super.getAuthorizedUser(), "ReadAnnouncements", args);
			getSession().setAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST, announcements);
			addRequestParameter("index", "0");
		} catch (Exception exception) {
			exception.printStackTrace();
			fail("testAuthorizedDeleteAnnouncement");
		}
				
		//action perform
		actionPerform();

		//verify that there are no errors
		verifyNoActionErrors();

		//verifies forward
		verifyForward("accessAnnouncementManagement");
	}
	
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		Map items = new HashMap();
		items.put("method", "prepareCreateAnnouncement");
		return items;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return new HashMap();
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		Map items = new HashMap();
		items.put("method", "prepareCreateAnnouncement");
		return items;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		Map items = new HashMap();
		items.put("method", "prepareCreateAnnouncement");
		return items;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		return new HashMap();
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return new HashMap();
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return new HashMap();
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return new HashMap();
	}
	
}