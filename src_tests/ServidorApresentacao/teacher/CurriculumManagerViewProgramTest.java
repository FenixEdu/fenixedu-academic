/*
 * Created on 20/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.teacher;

import java.util.HashMap;
import java.util.Map;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoSite;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CurriculumManagerViewProgramTest
	extends TestCasePresentationTeacherPortal {

	/**
	 * @param testName
	 */
	public CurriculumManagerViewProgramTest(String testName) {
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
		return "/programManagerDA";
	}

	/**
	 * This method must return a array of strings identifying the ActionErrors for
	 * use with testUnsuccessfulExecutionOfAction.
	 */
	protected String[] getActionErrors() {
		String[] result = {"java.lang.Exception"};
		return result;
	}

	/**
	* This method must return a string identifying the forward path when the action executes unsuccessfuly.
	*/
	protected String getUnsuccessfulForwardPath() {
		return "/naoExecutado.do";
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
		return "viewProgram";
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
		Map result = new HashMap();
		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"Trabalho Final de Curso I",
				"TFCI",
				"programa?",
				new Double(1.5),
				new Double(2),
				new Double(1.5),
				new Double(2),
				infoExecutionPeriod);
		InfoSite infoSite = new InfoSite(infoExecutionCourse);
		
		result.put(SessionConstants.INFO_SITE, infoSite);

		return result;
	}

	/**
	 * This method must return a Map with all the items that should be in session to execute
	 * the action unsuccessfuly.
	 * The Map must be an Map and it's keys must be the SessionUtils string constants
	 * correspondent to each object to put in session.
	 * This method must return null if not to be used.
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		Map result = new HashMap();
		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"Programacao com Objectos",
				"PO",
				"programa?",
				new Double(1.5),
				new Double(1.5),
				new Double(1.5),
				new Double(1.5),
				infoExecutionPeriod);
		InfoSite infoSite = new InfoSite(infoExecutionCourse);
		
		result.put(SessionConstants.INFO_SITE, infoSite);
		return result;
	}

	/**
	 * This method must return a Map with all the items that should be in request (form) to execute
	 * the action successfuly.
	 * The Map must be an Map and it's keys must be the form field names
	 * correspondent to each property to get out of the request.
	 * This method must return null if not to be used.
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		Map result = new HashMap();
		result.put("method","acessProgram");
		return result;
	}

	/**
	 * This method must return a Map with all the items that should be in request (form) to execute
	 * the action unsuccessfuly.
	 * The Map must be an Map and it's keys must be the form field names
	 * correspondent to each property to get out of the request.
	 * This method must return null if not to be used.
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		Map result = new HashMap();
	result.put("method","acessProgram");
	return result;
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
