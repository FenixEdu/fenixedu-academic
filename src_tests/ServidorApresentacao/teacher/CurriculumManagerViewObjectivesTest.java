/*
 * Created on 18/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.teacher;

import java.util.Map;

import ServidorApresentacao.TestCasePresentationTeacherPortal;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CurriculumManagerViewObjectivesTest
	extends TestCasePresentationTeacherPortal {

	/**
	 * @param testName
	 */
	public CurriculumManagerViewObjectivesTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/tests/web-teacher.xml";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "/teacher";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/objectivesManagerDA";
	}

	/**
	 * This method must return a array of strings identifying the ActionErrors for
	 * use with testUnsuccessfulExecutionOfAction.
	 */
	protected String[] getActionErrors() {
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
		return "viewObjectives";
	}

	/**
	 * This method must return a string identifying the forward when the action executes successfuly.
	 */
	protected String getSuccessfulForward() {
		return null;
	}

	/**
	 * This method must return a string identifying the forward when the action executes unsuccessfuly.
	 */
	protected String getUnsuccessfulForward() {
		return null;
	}

	/**
	 * This method must return a Map with all the items that should be in session to execute
	 * the action successfuly.
	 * The Map must be an Map and it's keys must be the SessionUtils string constants
	 * correspondent to each object to put in session.
	 * This method must return null if not to be used.
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		return null;
	}

	/**
	 * This method must return a Map with all the items that should be in session to execute
	 * the action unsuccessfuly.
	 * The Map must be an Map and it's keys must be the SessionUtils string constants
	 * correspondent to each object to put in session.
	 * This method must return null if not to be used.
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/**
	 * This method must return a Map with all the items that should be in request (form) to execute
	 * the action successfuly.
	 * The Map must be an Map and it's keys must be the form field names
	 * correspondent to each property to get out of the request.
	 * This method must return null if not to be used.
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		return null;
	}

	/**
	 * This method must return a Map with all the items that should be in request (form) to execute
	 * the action unsuccessfuly.
	 * The Map must be an Map and it's keys must be the form field names
	 * correspondent to each property to get out of the request.
	 * This method must return null if not to be used.
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/**
	 * This method must return a List with the attributes that are supose to be present in a specified scope
	 * when the action executes successfuly.
	 * The scope is specified by the method getScope().
	 * This method must return null if not to be used.
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/**
	 * This method must return a List with the attributes that are not supose to be present in a specified scope
	 * when the action executes successfuly.
	 * The scope is specified by the method getScope().
	 * This method must return null if not to be used.
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/**
	 * This method must return a List with the attributes that are supose to be present in a specified scope
	 * when the action executes unsuccessfuly.
	 * The scope is specified by the method getScope().
	 * This method must return null if not to be used.
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/**
	 * This method must return a List with the attributes that are not supose to be present in a specified scope
	 * when the action executes unsuccessfuly.
	 * The scope is specified by the method getScope().
	 * This method must return null if not to be used.
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

}
