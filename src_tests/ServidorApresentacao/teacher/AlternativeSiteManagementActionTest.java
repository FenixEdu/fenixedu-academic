package ServidorApresentacao.teacher;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoSection;
import DataBeans.InfoSite;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brandão
 */
public class AlternativeSiteManagementActionTest extends TestCasePresentationTeacherPortal {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public AlternativeSiteManagementActionTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(AlternativeSiteManagementActionTest.class);
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
		return "/alternativeSite";
	}

	public void testAuthorizedManagement() {
		super.setAuthorizedUser();
		if (getSession().getAttribute(SessionConstants.U_VIEW)==null) System.out.println("userView==null");		

		//set request path
		setRequestPathInfo("teacher", "/alternativeSite");

		//sets needed objects to session/request
		addRequestParameter("method", "management");

		InfoSection infoSection = new InfoSection();
		InfoSite infoSite = new InfoSite();

		//infoSection
		infoSection = new InfoSection("Seccao1deTFCI", new Integer(0), null);

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
		infoSite = new InfoSite(infoExecutionCourse);
		infoSection.setInfoSite(infoSite);
		infoSite.setInitialInfoSection(infoSection);

		getSession().setAttribute(SessionConstants.INFO_SITE, infoSite);
		
		//action perform
		actionPerform();

		//verify that there are no errors
		verifyNoActionErrors();

		//verifies forward
		verifyForward("editAlternativeSite");
	}

	public void testAuthorizedEdit() {
		super.setAuthorizedUser();
		if (getSession().getAttribute(SessionConstants.U_VIEW)==null) System.out.println("userView==null");		

		//set request path
		setRequestPathInfo("teacher", "/alternativeSite");

		//sets needed objects to session/request
		addRequestParameter("method", "edit");
		addRequestParameter("siteAddress", "newSiteAddress");
		addRequestParameter("mail", "newMail");
		addRequestParameter("initialStatement", "newInitialStatement");
		addRequestParameter("introduction", "newIntroduction");

		InfoSection infoSection = new InfoSection();
		InfoSite infoSite = new InfoSite();

		//infoSection
		infoSection = new InfoSection("Seccao1deTFCI", new Integer(0), null);

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
		infoSite = new InfoSite(infoExecutionCourse);
		infoSection.setInfoSite(infoSite);
		infoSite.setInitialInfoSection(infoSection);

		getSession().setAttribute(SessionConstants.INFO_SITE, infoSite);
		
		//action perform
		actionPerform();

		//verify that there are no errors
		verifyNoActionErrors();

		//verifies forward
		verifyForward("viewSite");
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