
package ServidorApresentacao.Action.masterDegree.candidate;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoCandidateSituation;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorApresentacao.TestCasePresentationCandidatePortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.SituationName;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

public class ChangeApplicationInfoDispatchActionPrepareMethodTest
	extends TestCasePresentationCandidatePortal {
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
		TestSuite suite = new TestSuite(ChangeApplicationInfoDispatchActionPrepareMethodTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public ChangeApplicationInfoDispatchActionPrepareMethodTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
		InfoCandidateSituation infoCandidateSituation = new InfoCandidateSituation();
		infoCandidateSituation.setSituation(SituationName.PENDENTE_OBJ);
		infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
		
		HashMap items = new HashMap();
		items.put(SessionConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidate);
		return items;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
		InfoCandidateSituation infoCandidateSituation = new InfoCandidateSituation();
		infoCandidateSituation.setSituation(SituationName.ADMITIDO_OBJ);
		infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
		
		HashMap items = new HashMap();
		items.put(SessionConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidate);
		return items;
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly() 
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		HashMap requestParameters = new HashMap();
		requestParameters.put("method","prepare");
		return requestParameters;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/changeApplicationInfoDispatchAction";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "prepareReady";
	}


}
