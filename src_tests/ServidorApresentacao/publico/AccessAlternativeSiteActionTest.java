/*
 * AccessAlternativeSiteActionTest.java
 * Apr 1, 2003
 */
package ServidorApresentacao.publico;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoSection;
import DataBeans.gesdis.InfoSite;
import ServidorApresentacao.TestCaseActionExecution;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brandão
 */
public class AccessAlternativeSiteActionTest extends TestCaseActionExecution {

	private InfoSite infoSite;

	/**
	 * @param testName
	 */
	public AccessAlternativeSiteActionTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		Map items = new HashMap();

		items.put(SessionConstants.INFO_SITE, infoSite);

		return items;
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
		//should be alternativeSite and mail
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
		Map items = new HashMap();
		items.put(SessionConstants.ALTERNATIVE_SITE, this.infoSite.getAlternativeSite());
		items.put(SessionConstants.MAIL, this.infoSite.getMail());
		
		return null;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
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
		return "/publico";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/accessAlternativeSite";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getAuthorizedRolesCollection()
	 */
	public Collection getAuthorizedRolesCollection() {
		return null;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "AlternativeSite";
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		super.setUp();
		InfoSection infoSection = new InfoSection();
		this.infoSite = new InfoSite();

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
		this.infoSite = new InfoSite(infoExecutionCourse);
		infoSection.setInfoSite(infoSite);
		this.infoSite.setInitialInfoSection(infoSection);
	}

}
