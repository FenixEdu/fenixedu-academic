/*
 * Created on 25/Jul/2003
 */
package ServidorApresentacao.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationManagerPortal;

/**
 * @author lmac1
 */

public class InsertDegreeDispatchActionTest extends TestCasePresentationManagerPortal{

	/**
	 * @param testName
	 */
	public InsertDegreeDispatchActionTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/insertDegree";
	}

	/**
	 * This method must return a array of strings identifying the ActionErrors for
	 * use with testUnsuccessfulExecutionOfAction.
	 */
	protected String[] getActionErrors() {
		String[] result = {"message.existingDegreeCode", "message.existingDegreeName"};		
		return result;
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
		return "readDegrees";
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
		Map result = new HashMap();
		result.put("method", "insert");
		result.put("code", "LEC");
		result.put("name", "Licenciatura em Engenharia Civil");
		result.put("degreeType", "1");
		return result;
	}
	
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		Map result = new HashMap();
		result.put("method", "insert");
		result.put("code", "MEM");
		result.put("name", "Engenharia Mecanica");
		result.put("degreeType", "2");
		return result;
	}

	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		Map result = new HashMap();
		List requestAttributtes = new ArrayList(1);
		requestAttributtes.add("infoDegreesList");
		result.put(new Integer(ScopeConstants.REQUEST), requestAttributtes);
		return result;
	}

	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		Map result = new HashMap();
		List requestAttributtes = new ArrayList(1);
		requestAttributtes.add("infoDegreesList");
		result.put(new Integer(ScopeConstants.REQUEST), requestAttributtes);
		return result;
	}

	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

}