/*
 * Created on 22/Jul/2003
 *
 */
package ServidorApresentacao.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationManagerPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class ReadDegreesActionTest extends TestCasePresentationManagerPortal{

	
	/**
	 * @param testName
	 */
	public ReadDegreesActionTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/readDegrees";
	}

	/**
	 * This method must return a array of strings identifying the ActionErrors for
	 * use with testUnsuccessfulExecutionOfAction.
	 */
	protected String[] getActionErrors() {
//		String[] result = { "java.lang.Exception" };
//		return result;
		return null;
	}

	/**
	* This method must return a string identifying the forward path when the action executes unsuccessfuly.
	*/
	protected String getUnsuccessfulForwardPath() {
		return null;
	}

	/**
	 * This method must return a string identifying the forward path when the action executes successfuly.
	 */
	protected String getSuccessfulForwardPath() {
		return null;
	}

	/**
	 * This method must return a string identifying the forward when the action executes successfuly.
	 */
	protected String getSuccessfulForward() {
		return "readDegrees";
	}

	/**
	 * This method must return a string identifying the forward when the action executes unsuccessfuly.
	 */
	protected String getUnsuccessfulForward() {
		return null;
	}
	
	protected int getScope() {
		return ScopeConstants.REQUEST;
	}

	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		return null;
	}

	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		
		return null;
	}

	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		return null;
	}
	
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
	return null;
	}

	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		Map result = new HashMap();

		List requestAttributtes = new ArrayList(1);
		requestAttributtes.add(SessionConstants.INFO_DEGREES_LIST);
		result.put(new Integer(ScopeConstants.REQUEST), requestAttributtes);
	
		return result;
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

}
