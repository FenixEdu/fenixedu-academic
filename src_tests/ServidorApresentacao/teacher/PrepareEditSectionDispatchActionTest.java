/*
 * Created on 14/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.teacher;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import DataBeans.InfoSection;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISection;
import Dominio.ISite;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;



/**
 * @author lmac2
 */
public class PrepareEditSectionDispatchActionTest extends TestCasePresentationTeacherPortal {

	/*items to put in session for action to be tested successfuly*/
	private InfoSection infoSection = null;
	private InfoSite infoSite = null;
	private List infoSectionsList = null;
	
	/*existing attributes list to verify in successful execution*/
    private List allSections= null;
	private List childrenSections = null;
	
	
	/**
	 * @param testName
	 */
	public PrepareEditSectionDispatchActionTest(String testName) {
		super(testName);
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
		return "/editSection";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "editSection";
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
	
		Map result = new Hashtable();
		
		result.put(SessionConstants.SECTIONS, infoSectionsList);
		result.put(SessionConstants.INFO_SECTION, infoSection);
		result.put(SessionConstants.INFO_SITE, infoSite);
		
		return result;
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
		
		Map result = new Hashtable();
		
		result.put("method","prepareEdit");
		return result;
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
		Map result = new Hashtable();
		
		ArrayList teste = new ArrayList(2);
		
		teste.add(SessionConstants.SECTIONS);
		teste.add(SessionConstants.CHILDREN_SECTIONS);
		result.put(new Integer(1),teste);
		
		return result;
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
	
	public void setUp() {
		super.setUp();
		
		try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		sp.iniciarTransaccao();

		IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
		IExecutionYear executionYear = ieyp.readExecutionYearByName("2002/2003");

		IPersistentExecutionPeriod iepp =
		sp.getIPersistentExecutionPeriod();

		IExecutionPeriod executionPeriod =
						iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

		IDisciplinaExecucaoPersistente idep =
					 sp.getIDisciplinaExecucaoPersistente();
		IDisciplinaExecucao executionCourse =
						idep.readByExecutionCourseInitialsAndExecutionPeriod(
							"TFCI",
							executionPeriod);
		IPersistentSite persistentSite =
			sp.getIPersistentSite();

		ISite site =persistentSite.readByExecutionCourse(executionCourse);
		infoSite = Cloner.copyISite2InfoSite(site);
			
		IPersistentSection persistentSection = sp.getIPersistentSection();

		ISection section = persistentSection.readBySiteAndSectionAndName(site,null,"Seccao1deTFCI");
		
		List sectionsList = persistentSection.readBySite(site);
		
		List childrenSectionsList = persistentSection.readBySiteAndSection(site,section);
		
		infoSectionsList = Cloner.copyListISections2ListInfoSections(sectionsList);
		infoSection = Cloner.copyISection2InfoSection(section);
		childrenSections = Cloner.copyListISections2ListInfoSections(childrenSectionsList);
		
		
		//newInfoItemTestSuc = (InfoItem) infoItemsList.get(0);
		
		}
		catch (ExcepcaoPersistencia e) {
					System.out.println("failed setting up the test data");
					e.printStackTrace();
			}
}
}
