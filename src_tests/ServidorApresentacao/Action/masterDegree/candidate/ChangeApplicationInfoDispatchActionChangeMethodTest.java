
package ServidorApresentacao.Action.masterDegree.candidate;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeCandidate;
import ServidorApresentacao.TestCasePresentationCandidatePortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Specialization;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

public class ChangeApplicationInfoDispatchActionChangeMethodTest
	extends TestCasePresentationCandidatePortal{
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
		TestSuite suite = new TestSuite(ChangeApplicationInfoDispatchActionChangeMethodTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public ChangeApplicationInfoDispatchActionChangeMethodTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		HashMap items = new HashMap();
		items.put(SessionConstants.MASTER_DEGREE_CANDIDATE, this.getCandidate(true));
		return items;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		HashMap items = new HashMap();
		items.put(SessionConstants.MASTER_DEGREE_CANDIDATE, this.getCandidate(false));
		return items;
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly() 
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		HashMap requestParameters = new HashMap();
		requestParameters.put("method","change");
		requestParameters.put("average","10.01");
		requestParameters.put("majorDegree","LEIC");
		requestParameters.put("majorDegreeYear","2000");
		requestParameters.put("majorDegreeSchool","IST");
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
		return "Success";
	}


	private InfoMasterDegreeCandidate getCandidate(boolean changeable){
		SuportePersistenteOJB sp = null;
		IMasterDegreeCandidate masterDegreeCandidate = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			if (changeable)
				masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(
					new Integer(1), "2002/2003", "MEEC", new Specialization(Specialization.MESTRADO));
			else
				masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(
					new Integer(2), "2002/2003", "MEEC", new Specialization(Specialization.INTEGRADO));
 
			assertNotNull(masterDegreeCandidate);
			
			sp.confirmarTransaccao();
		} catch(Exception e) {
			fail("Error Reading From DataBase !");
		}
		
		return Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
	}
	

}
