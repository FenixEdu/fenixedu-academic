
package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationCoordinatorPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
public class CandidateOperationDispatchActionChooseCandidateMethodTest
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
		TestSuite suite = new TestSuite(CandidateOperationDispatchActionChooseCandidateMethodTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public CandidateOperationDispatchActionChooseCandidateMethodTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
		List candidates = new ArrayList();
		candidates.add(infoMasterDegreeCandidate);
		candidates.add(infoMasterDegreeCandidate);
		
		HashMap items = new HashMap();
		items.put(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST, candidates);
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
		requestParameters.put("method","chooseCandidate");
		requestParameters.put("candidatePosition","1");
		return requestParameters;		
	}

	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		HashMap attributes = new HashMap();
		
		List sessionAttributes = new ArrayList();
		sessionAttributes.add(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST);
		sessionAttributes.add(SessionConstants.MASTER_DEGREE_CANDIDATE);
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
		return "ActionReady";
	}


}
