/*
 * Created on 25/Jul/2003
 */
package ServidorApresentacao.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationManagerPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

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
	public static void main(java.lang.String[] args) {
			junit.textui.TestRunner.run(suite());
		}

	public static Test suite() {
			TestSuite suite = new TestSuite(InsertDegreeDispatchActionTest.class);
			return suite;
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
		String[] result = {"message.existingDegreeCode","message.existingDegreeName"};		
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
		Map result = new HashMap();
		
		result.put("method","insert");
		result.put("code","LEC");
		result.put("name","Licenciatura em Engenharia Civil");
		result.put("degreeType","Licenciatura");
		return result;
	}
	
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		Map result = new HashMap();
		
		result.put("method","insert");
		result.put("code","MEM");
		result.put("name","Engenharia Mecânica");
		result.put("degreeType","Licenciatura");
		return result;
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
		Map result = new HashMap();
		
		List requestAttributtes = new ArrayList(1);
		requestAttributtes.add(SessionConstants.INFO_DEGREES_LIST);
		result.put(new Integer(ScopeConstants.REQUEST), requestAttributtes);
		
		return result;
	}

	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

}


