/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.publico;

/**
 * @author lmac2
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataBeans.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISection;
import Dominio.ISite;
import ServidorApresentacao.TestCaseActionExecution;
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
 * @author tfc130
 *
 */
public class ViewSectionActionTest extends TestCaseActionExecution {

	/*o que tem que estar na sessao para a accao se executar*/
	private List listInfoSections;

	/*o que tem que estar no request para a accao se executar*/
	private String index;
	
	/*o que deve ficar em sessao depois da accao se executar*/
	private InfoSection infoSectionSuc;
	private List listInfoItemsSuc;

	/**
	 * @param testName
	 */
	public ViewSectionActionTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		
		Map items = new HashMap();

		items.put(SessionConstants.SECTIONS, listInfoSections);
		
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
		
		Map items = new HashMap();
		items.put("index", index);
		return items;
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
		
		Map items = new HashMap();
		ArrayList teste = new ArrayList(2);
		teste.add(SessionConstants.INFO_SECTION);
		teste.add(SessionConstants.INFO_SECTION_ITEMS_LIST);
		items.put( new Integer(1),teste);
		
		return items;
		
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
		return "/viewSection";
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
		return "Sucess";
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		super.setUp();
		this.listInfoSections = null;
		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		ISite site = null;
		List sections = null;
		
		try {
			
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ipey = sp.getIPersistentExecutionYear();
			executionYear = ipey.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod ipep =
				sp.getIPersistentExecutionPeriod();
			executionPeriod =
				ipep.readByNameAndExecutionYear("2º Semestre", executionYear);

			IDisciplinaExecucaoPersistente idep =
				sp.getIDisciplinaExecucaoPersistente();
			executionCourse =
				idep.readByExecutionCourseInitialsAndExecutionPeriod(
					"PO",
					executionPeriod);

			IPersistentSite ips = sp.getIPersistentSite();
			site = ips.readByExecutionCourse(executionCourse);
			
			IPersistentSection ipse = sp.getIPersistentSection();
			sections = ipse.readBySite(site);
		
				
			
			listInfoSections = Cloner.copyListISections2ListInfoSections(sections);
			
		
			index = new String("3");
		
			infoSectionSuc = (InfoSection)listInfoSections.get(3);
			System.out.println(infoSectionSuc.toString());
			ISection sectionSuc =Cloner.copyInfoSection2ISection(infoSectionSuc);
		
			IPersistentItem ipi = sp.getIPersistentItem();
			List listItemsSuc = ipi.readAllItemsBySection(sectionSuc);
			
			listInfoItemsSuc = Cloner.copyListIItems2ListInfoItems(listItemsSuc);
			System.out.println("size:"+listInfoItemsSuc.size());
			
			sp.confirmarTransaccao();

			
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
		}
		
		
		

	}

}
