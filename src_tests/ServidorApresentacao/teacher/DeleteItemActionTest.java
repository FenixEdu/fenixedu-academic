/*
 * Created on 10/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import DataBeans.gesdis.InfoItem;
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

	/**
	 * @author lmac1
	 *
	 */

	public class DeleteItemActionTest extends TestCasePresentationTeacherPortal {

		/**
		 * @param testName
		 */
		public DeleteItemActionTest(String testName) {
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
			return "/deleteItem";
		}

		/**
		 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
		 */
		protected String getSuccessfulForward() {
			return "AccessSectionManagement";
		}


		/**
		 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
		 */
		protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
	
			Map result = new Hashtable();
		try{
		
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
		System.out.println("inicio da funcao");
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
		
			Integer index = new Integer(0);
			String indexString = index.toString();
			
			result.put(SessionConstants.INDEX,indexString);
		
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