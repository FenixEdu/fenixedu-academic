/*
 * Created on 9/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.teacher;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import DataBeans.gesdis.InfoItem;
import DataBeans.gesdis.InfoSection;
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
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

// NOT TESTED 

public class EditItemActionTest extends TestCasePresentationTeacherPortal {

	private InfoSection infoSection = null;
	private List infoItemsList = null;
	private String index = null;
	private InfoItem newInfoItemTestSuc =null;
		
	/**
	 * @param testName
	 */
	public EditItemActionTest(String testName) {
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
		return "/editItem";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "ViewSection";
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
	
		Map result = new Hashtable();
		
		result.put(SessionConstants.INFO_SECTION,infoSection);
		result.put(SessionConstants.INFO_SECTION_ITEMS_LIST, infoItemsList);
		
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
		
		result.put("index", index);
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
		
		teste.add(SessionConstants.INFO_ITEM);
		teste.add(SessionConstants.INFO_SECTION_ITEMS_LIST);
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
		System.out.println("Entra no SETUP");
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
			
		IPersistentSection persistentSection = sp.getIPersistentSection();

		ISection section = persistentSection.readBySiteAndSectionAndName(site,null,"Seccao1deTFCI");
		
		
		IPersistentItem persistentItem = sp.getIPersistentItem();

		List itemsList = persistentItem.readAllItemsBySection(section);
		
		
		infoSection = Cloner.copyISection2InfoSection(section);
		infoItemsList=Cloner.copyListIItems2ListInfoItems(itemsList);
		index = new String("0");
		newInfoItemTestSuc = (InfoItem)infoItemsList.get(0);
		
		newInfoItemTestSuc.setInformation("newInformation");
		newInfoItemTestSuc.setItemOrder(new Integer(1)); 
		newInfoItemTestSuc.setName(("newName"));
		newInfoItemTestSuc.setUrgent(new Boolean(false));
		
		}
		catch (ExcepcaoPersistencia e) {
					System.out.println("failed setting up the test data");
					e.printStackTrace();
			}
}
}