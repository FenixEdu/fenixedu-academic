
package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationCoordinatorPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.OJB.SuportePersistenteOJB;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
public class CandidateOperationDispatchActionGetCandidatesMethodTest
	extends TestCasePresentationCoordinatorPortal {
	/**
	 * Main method 
	 * @param args
	 */
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * 
	 * @return Test to be done
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(CandidateOperationDispatchActionGetCandidatesMethodTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public CandidateOperationDispatchActionGetCandidatesMethodTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		ICursoExecucao executionDegree = null;
		SuportePersistenteOJB sp = null;
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IExecutionYear executionYear = null;
			executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionDegree = sp.getICursoExecucaoPersistente().readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
						"MEEC", "plano2", executionYear);
			assertNotNull(executionDegree);
			
			sp.confirmarTransaccao();
			
		} catch (Exception e) {
			fail("Error reading from DataBase !");	
		}
		
		HashMap items = new HashMap();
		items.put(SessionConstants.MASTER_DEGREE, Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree));
		return items;

	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return null;
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly() 
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		HashMap requestParameters = new HashMap();
		requestParameters.put("method","getCandidates");
		return requestParameters;		
	}

	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		HashMap attributes = new HashMap();
		
		List sessionAttributes = new ArrayList();
		sessionAttributes.add(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST);
		attributes.put(new Integer(ScopeConstants.SESSION),sessionAttributes);
		return null;
	}

	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	protected String getRequestPathInfoNameAction() {
		return "/candidateOperation";
	}

	protected String getSuccessfulForward() {
		return "ViewList";
	}


}
