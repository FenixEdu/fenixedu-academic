package ServidorApresentacao.teacher;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSite;
import DataBeans.util.Cloner;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ivo Brandão
 */
public class AnnouncementManagementActionTest extends TestCasePresentationTeacherPortal {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public AnnouncementManagementActionTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(AnnouncementManagementActionTest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();
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
		return "/teacher";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/announcementManagementAction";
	}

	public void testAuthorizedEditAnnouncement() {

		//set request path
		setRequestPathInfo("/teacher", "/editAnnouncement");

		//sets needed objects to session/request
		addRequestParameter("method", "Guardar");

		//infoSite in session
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse.setNome("Trabalho Final de Curso I");
		infoExecutionCourse.setSigla("TFCI");
		infoExecutionCourse.setPrograma("programa1");
		infoExecutionCourse.setTheoreticalHours(new Double(1.5));
		infoExecutionCourse.setPraticalHours(new Double(2));
		infoExecutionCourse.setTheoPratHours(new Double(1.5));
		infoExecutionCourse.setLabHours(new Double(2));
		InfoExecutionPeriod iep =
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003"));
		infoExecutionCourse.setInfoExecutionPeriod(iep);
		InfoSite infoSite = new InfoSite(infoExecutionCourse);
		getSession().setAttribute("Site", infoSite);

		//infoAnnouncement in session
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2003);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 22);
		Date creationDate = calendar.getTime();
		calendar.set(Calendar.DATE, 24);
		Date lastModifiedDate = calendar.getTime();
		
		InfoAnnouncement infoAnnouncement = null;

		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(SuportePersistenteOJB
				.getInstance()
				.getIPersistentAnnouncement()
				.readAnnouncementByTitleAndCreationDateAndSite(
					"announcement2deTFCI",
					creationDate,
					Cloner.copyInfoSite2ISite(infoSite)));
			SuportePersistenteOJB.getInstance().confirmarTransaccao();					
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testAuthorizedEditAnnouncement - before alteration:" + excepcaoPersistencia.getMessage());
		}

		getSession().setAttribute("Announcement", infoAnnouncement);
				
//		fills the form
		addRequestParameter("title", "newTitle");
		addRequestParameter("information", "newInformation");

//		action perform
		actionPerform();		

//		verify forward
		verifyForward("accessAnnouncementManagement");

//		post conditions
		infoAnnouncement = null;

		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(SuportePersistenteOJB
				.getInstance()
				.getIPersistentAnnouncement()
				.readAnnouncementByTitleAndCreationDateAndSite(
					"newTitle",
					creationDate,
					Cloner.copyInfoSite2ISite(infoSite)));
			SuportePersistenteOJB.getInstance().confirmarTransaccao();					
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testAuthorizedEditAnnouncement - after alteration:" + excepcaoPersistencia.getMessage());
		}
		assertNotNull(infoAnnouncement);

	}

	public void testAuthorizedPrepareEditAnnouncement() {
		//set request path
		setRequestPathInfo("/teacher", "/announcementManagementAction");

//		sets needed objects to session/request
		addRequestParameter("method", "Editar");

//		action perform
		actionPerform();

//		verify that there are no errors
		verifyNoActionErrors();

//		verifies forward
		verifyForward("editAnnouncement");

//		verifyForwardPath("/editAnnouncement");
	}

	public void testAuthorizedCreateAnnouncement() {
		//set request path
		setRequestPathInfo("/teacher", "/announcementManagementAction");

		//sets needed objects to session/request
		addRequestParameter("method", "Inserir");

//		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
//		infoExecutionCourse.setNome("Trabalho Final de Curso I");
//		infoExecutionCourse.setSigla("TFCI");
//		infoExecutionCourse.setPrograma("programa1");
//		infoExecutionCourse.setTheoreticalHours(new Double(1.5));
//		infoExecutionCourse.setPraticalHours(new Double(2));
//		infoExecutionCourse.setTheoPratHours(new Double(1.5));
//		infoExecutionCourse.setLabHours(new Double(2));
//		InfoExecutionPeriod iep = new InfoExecutionPeriod("2º Semestre", new InfoExecutionYear("2002/2003"));
//		infoExecutionCourse.setInfoExecutionPeriod(iep);
//		
//		InfoSite infoSite = new InfoSite(infoExecutionCourse);
//		getSession().setAttribute("Site", infoSite);
//
//		setAuthorizedUser();
//
//		fills the form
//		addRequestParameter("title", "newTitle");
//		addRequestParameter("information", "newInformation");
//
//		action perform
//		actionPerform();
//
//		verify that there are no errors
//		verifyNoActionErrors();
//
//		verifies forward
//		verifyForward("accessAnnouncementManagement");
	}

	public void testAuthorizedDeleteAnnouncement() {

		//set request path		
		setRequestPathInfo("/teacher", "/announcementManagementAction2");

		//sets needed objects to session/request
		addRequestParameter("method", "Apagar");
//		addRequestParameter("infoBibliographicReferenceIndex", "0");
//
//		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
//		infoExecutionCourse.setNome("Trabalho Final de Curso I");
//		infoExecutionCourse.setSigla("TFCI");
//		infoExecutionCourse.setPrograma("programa1");
//		infoExecutionCourse.setTheoreticalHours(new Double(1.5));
//		infoExecutionCourse.setPraticalHours(new Double(2));
//		infoExecutionCourse.setTheoPratHours(new Double(1.5));
//		infoExecutionCourse.setLabHours(new Double(2));
//
//		InfoExecutionPeriod iep =
//			new InfoExecutionPeriod(
//				"2º Semestre",
//				new InfoExecutionYear("2002/2003"));
//		infoExecutionCourse.setInfoExecutionPeriod(iep);
//
//		HttpSession session = getSession();
//
//		session.setAttribute("InfoExecutionCourse", infoExecutionCourse);
//
//		UserView userView = (UserView) session.getAttribute("UserView");
//		Object args[] = { infoExecutionCourse, null };
//		GestorServicos gestor = GestorServicos.manager();
//		ArrayList references = null;
//		try {
//			references =
//				(ArrayList) gestor.executar(
//					userView,
//					"ReadBibliographicReference",
//					args);
//		} catch (Exception e) {
//		}
//
//		session.setAttribute("BibliographicReferences", references);
//
//		setAuthorizedUser();
//
//		//action perform
//		actionPerform();
//
//		ArrayList biblioRefs =
//			(ArrayList) session.getAttribute("BibliographicReferences");
//		assertEquals(biblioRefs.size(), 1);
//		InfoBibliographicReference infoBibRef =
//			(InfoBibliographicReference) biblioRefs.get(0);
//		assertEquals(infoBibRef.getTitle(), "as");
//		verifyForward("bibliographyManagement");
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