/*
 * AutenticacaoSOPFormActionTest2.java
 * Mar 6, 2003
 */
package ServidorApresentacao.sop;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCaseActionExecution;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brandão
 */
public class AutenticacaoSOPFormActionTest extends TestCaseActionExecution {

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
		assertTrue("Verify authorization", newUserView.getPrivilegios().contains("CriarSala"));		
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#testUnsuccessfulExecutionOfAction()
	 * 
	 * dummy
	 */
	public void testUnsuccessfulExecutionOfAction() {
		//place unauthorized user in session
		getSession().removeAttribute(SessionConstants.U_VIEW);
		Set privileges = new HashSet();
		IUserView userView = new UserView("athirduser", privileges);
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

}
