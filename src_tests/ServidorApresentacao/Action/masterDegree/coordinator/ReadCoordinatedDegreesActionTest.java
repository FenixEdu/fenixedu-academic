
package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoRole;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationCoordinatorPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.RoleType;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
public class ReadCoordinatedDegreesActionTest
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
		TestSuite suite = new TestSuite(ReadCoordinatedDegreesActionTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public ReadCoordinatedDegreesActionTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		
		List degrees = new ArrayList();
		degrees.add(infoExecutionDegree);
		degrees.add(infoExecutionDegree);

		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.COORDINATOR);
		roles.add(infoRole);
		
		UserView userView = new UserView("nmsn", roles);
		
		HashMap items = new HashMap();
		items.put(SessionConstants.MASTER_DEGREE_LIST, degrees);
		items.put(SessionConstants.U_VIEW, userView);
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
		return null;	
	}

	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		HashMap attributes = new HashMap();
		
		List sessionAttributes = new ArrayList();
		sessionAttributes.add(SessionConstants.MASTER_DEGREE_LIST);
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
		return "/index";
	}

	protected String getSuccessfulForward() {
		return "ChooseDegree";
	}


}
