package ServidorApresentacao.teacher;

import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISite;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ivo Brandão
 */
public class AnnouncementManagementActionTest extends TestCasePresentationTeacherPortal {

	private SuportePersistenteOJB persistentSupport = null; 
	private IPersistentAnnouncement persistentAnnouncement = null;
	private IPersistentSite persistentSite = null;
	private IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	private IPersistentExecutionPeriod persistentExecutionPeriod = null;
	private IPersistentExecutionYear persistentExecutionYear = null;

	private ISite site = null;
	private IDisciplinaExecucao executionCourse = null;
	private IExecutionPeriod executionPeriod = null;
	private IExecutionYear executionYear = null;

	private List announcementsList;

	/**
	 * Constructor for AnnouncementManagementFormActionTest.
	 */
	public AnnouncementManagementActionTest(String test) {
		super(test);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(AnnouncementManagementActionTest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();
		
		//read announcements for this site
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error while setting up");
		}

		persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();
		persistentSite = persistentSupport.getIPersistentSite();
		persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
		persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
		persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
		
		//read existing executionYear
		try {
			persistentSupport.iniciarTransaccao();
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("Error while setting up: readExecutionYearByName");
		}
		assertNotNull(executionYear);

		//read existing executionPeriod
		try {
			persistentSupport.iniciarTransaccao();
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("Error while setting up: readByNameAndExecutionYear");
		}
		assertNotNull(executionPeriod);

		//read existing executionCourse
		try {
			persistentSupport.iniciarTransaccao();
			executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", executionPeriod);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("Error while setting up: readByExecutionCourseInitialsAndExecutionPeriod");
		}
		assertNotNull(executionCourse);

		//read existing site
		try {
			persistentSupport.iniciarTransaccao();
			site = persistentSite.readByExecutionCourse(executionCourse);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("Error while setting up: readByExecutionCourse");
		}
		assertNotNull(site);

		//read announcements list
		try {
			persistentSupport.iniciarTransaccao();
			announcementsList = persistentAnnouncement.readAnnouncementsBySite(site);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("Error while setting up: readAnnouncementsBySite");
		}
		 
	}

	public void testEdit() {
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);

		setServletConfigFile("/WEB-INF/tests/web-teacher.xml");

		//set request path
		setRequestPathInfo("/teacher", "/announcementManagementAction");

		addRequestParameter("option", "Edit");
		addRequestParameter("index", "0");

		//announcements required
		getSession().setAttribute("Announcements", this.announcementsList);

		//action perform
		actionPerform();
		
		//verify that there are no errors
		verifyNoActionErrors();

		//verify forward		
		verifyForwardPath("/editAnnouncement.do");
	}

	public void testInsert() {
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);

		setServletConfigFile("/WEB-INF/tests/web-teacher.xml");

		//set request path
		setRequestPathInfo("/teacher", "/announcementManagementAction");

		addRequestParameter("option", "Insert");
		addRequestParameter("index", "0");

		//announcements required
		getSession().setAttribute("Announcements", this.announcementsList);

		//action perform
		actionPerform();
		
		//verify that there are no errors
		verifyNoActionErrors();

		//verify forward		
		verifyForwardPath("/insertAnnouncement.do");
	}

	public void testDelete() {
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);

		setServletConfigFile("/WEB-INF/tests/web-teacher.xml");

		//set request path
		setRequestPathInfo("/teacher", "/announcementManagementAction");

		addRequestParameter("option", "Delete");
		addRequestParameter("index", "0");

		//announcements required
		getSession().setAttribute("Announcements", this.announcementsList);

		//action perform
		actionPerform();
		
		//verify that there are no errors
		verifyNoActionErrors();

		//verify forward		
		verifyForwardPath("/deleteAnnouncement.do");
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/tests/web-teacher.xml";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return null;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return null;
	}
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		return null;
	}
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return null;
	}
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		return null;
	}
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}
}
