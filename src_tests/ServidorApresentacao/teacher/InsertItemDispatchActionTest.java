/*
 * Created on 14/Abr/2003
 */

package ServidorApresentacao.teacher;
/**
 * @author lmac1
 *
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import DataBeans.InfoItem;
import DataBeans.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class InsertItemDispatchActionTest extends TestCasePresentationTeacherPortal {


	private InfoItem infoItem = null;
		
	private String itemOrder = null;
	private String urgent = null;
		
	/**
	 * @param testName
	 */
	public InsertItemDispatchActionTest(String testName) {
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
		return "/insertItem";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "viewSection";
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
	
		Map result = new Hashtable();
	try{
		
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		sp.iniciarTransaccao();
	
		IDisciplinaExecucao executionCourse = sp.getIDisciplinaExecucaoPersistente().readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		ISite site = sp.getIPersistentSite().readByExecutionCourse(executionCourse);
		ISection section = sp.getIPersistentSection().readBySiteAndSectionAndName(site, null, "seccao1deTFCI");
		List itemsList = sp.getIPersistentItem().readAllItemsBySection(section);
		
							
		sp.confirmarTransaccao(); 
	    
		Iterator iterator = itemsList.iterator();
	    
	    List infoItemsList = new ArrayList(itemsList.size());
		 while(iterator.hasNext())
		 {
		 IItem item = (IItem) iterator.next();
		 InfoItem infoItem = Cloner.copyIItem2InfoItem(item);
		 infoItemsList.add(infoItem); 
		 }
		 Collections.sort(infoItemsList);
	    
		InfoSection infoSection = Cloner.copyISection2InfoSection(section);
		
		result.put(SessionConstants.INFO_SECTION, infoSection);
		result.put(SessionConstants.INFO_SECTION_ITEMS_LIST, infoItemsList);
			
				
	  }catch (ExcepcaoPersistencia exception) {
	  exception.printStackTrace(System.out);
	  fail("Using services at getItemsToPutInSessionForActionToBeTestedSuccessfuly()!");
	}

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
		
		urgent = new String ("false");
		itemOrder = new String("1");
		
		result.put("method","insert");
		result.put("information","information");
		result.put("itemName","itemName");
		result.put("itemOrder",itemOrder);
		result.put("urgent",urgent);
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