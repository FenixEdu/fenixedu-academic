/*
 * AutenticacaoSOPFormActionTest2.java
 * Mar 6, 2003
 */
package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brandão
 */
public class AutenticacaoSOPFormActionTest extends TestCasePresentationSopPortal {

	/**
	 * @param testName
	 */
	public AutenticacaoSOPFormActionTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/tests/web-sop.xml";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/autenticacaoSOPForm";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		Map result = new Hashtable();

		result.put("utilizador","user");
		result.put("password","pass");
		
		return result;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "SOP";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#testSuccessfulExecutionOfAction()
	 */
	public void testSuccessfulExecutionOfAction() {

		doTest(getItemsToPutInRequestForActionToBeTestedSuccessfuly(), null, getSuccessfulForward(), null, null, null, null);

		//verifies UserView
		UserView newUserView = (UserView) getSession().getAttribute(SessionConstants.U_VIEW);
		assertEquals("Verify UserView", newUserView.getUtilizador(), "user");
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#testUnsuccessfulExecutionOfAction()
	 * 
	 * dummy
	 */
	public void testUnsuccessfulExecutionOfAction() {
		//place unauthorized user in session
		getSession().removeAttribute(SessionConstants.U_VIEW);
		Collection roles = new ArrayList();
		IUserView userView = new UserView("athirduser", roles);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);
		
		String[] actionErrors = { "errors.invalidAuthentication" };

		doTest(getItemsToPutInRequestForActionToBeTestedUnsuccessfuly(), null, null, getUnsuccessfulForwardPath(), null, null, actionErrors);
		
		//verifies UserView
		UserView newUserView = (UserView) getSession().getAttribute(SessionConstants.U_VIEW);
		assertEquals("Verify UserView", newUserView.getUtilizador(), "athirduser");
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		Map result = new Hashtable();

		result.put("utilizador","nome");
		result.put("password","xpto");
		
		return result;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getUnsuccessfulForwardPath()
	 */
	protected String getUnsuccessfulForwardPath() {
		return "/autenticacaoSOP.jsp";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
	
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		
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

}
