/*
 * Created on 26/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author dcs-rjao
 *
 * 26/Fev/2003
 */

public abstract class TestCaseActionExecution extends TestCasePresentation {

	public static final int SESSION = 1;
	public static final int REQUEST = 2;
	public static final int APP_CONTEXT = 3;

	protected GestorServicos gestor = null;
	protected IUserView userView = null;

	public TestCaseActionExecution(String testName) {
		super(testName);
	}

	public void setUp() {

		super.setUp();

//		defines struts config file to use
		setServletConfigFile(getServletConfigFile());

		this.gestor = GestorServicos.manager();
		String argsAutenticacao[] = {"user", "pass"};
		try {
			this.userView = (IUserView) this.gestor.executar(null, "Autenticacao", argsAutenticacao);
			getSession().setAttribute(SessionConstants.U_VIEW, userView);
		} catch (Exception ex) {
			System.out.println("Autenticacao nao executada: " + ex);
			fail("Setting up!");
		}
	}

	protected void tearDown() {
		super.tearDown();
	}

// -------------------------------------------------------------------------------------------------------

	public void testSuccessfulExecutionOfAction() {

		testIt(	(HashMap) getItemsToPutInRequestForActionToBeTestedSuccessfuly(),
				(HashMap) getItemsToPutInSessionForActionToBeTestedSuccessfuly(),
				getSuccessfulForward()	);
	}

	public void testUnsuccessfulExecutionOfAction() {

		testIt(	(HashMap) getItemsToPutInRequestForActionToBeTestedUnsuccessfuly(),
				(HashMap) getItemsToPutInSessionForActionToBeTestedUnsuccessfuly(),
				getUnsuccessfulForward()	);
	}
   
	/**
	 * @param itemsToPutInRequest
	 * @param itemsToPutInSession
	 * @param forward
	 */
	protected void testIt(HashMap itemsToPutInRequest, HashMap itemsToPutInSession, String forward) {

		String pathOfAction = getRequestPathInfoPathAction();
		String nameOfAction = getRequestPathInfoNameAction();
			
		if( (pathOfAction != null) && (nameOfAction != null) ) {
			setRequestPathInfo(getRequestPathInfoPathAction(), getRequestPathInfoNameAction());
		}
	
		if(itemsToPutInRequest != null) {
	
			Set keys = itemsToPutInRequest.keySet();
			Iterator keysIterator = keys.iterator();
					
			while(keysIterator.hasNext()) {
				String key = (String) keysIterator.next();
				String item = (String) itemsToPutInRequest.get(key);
				addRequestParameter(key, item);
			}
		}
	
		if(itemsToPutInSession != null) {
		
			Set keys = itemsToPutInSession.keySet();
			Iterator keysIterator = keys.iterator();
						
			while(keysIterator.hasNext()) {
				String key = (String) keysIterator.next();
				Object item = itemsToPutInSession.get(key);
				getSession().setAttribute(key, item);
			}
		}

		if( (pathOfAction != null) && (nameOfAction != null) && (itemsToPutInSession != null) && (itemsToPutInRequest != null) && (forward != null) ) {
//			perform
			actionPerform();
//			checks for errors
			verifyNoActionErrors();
//			checks forward
			verifyForward(forward);

			CurricularYearAndSemesterAndInfoExecutionDegree ctx = SessionUtils.getContext(getRequest());
			assertNotNull("Context is null!", ctx);
		}
   }

	/**
	 * @param scope
	 * @param existingAttributesList
	 * @param nonExistingAttributesList
	 */
	protected void verifyScopeAttributes(int scope, List existingAttributesList, List nonExistingAttributesList) {

		Enumeration attNames = null;
		switch(scope) {
			case TestCaseActionExecution.SESSION:
				attNames = getSession().getAttributeNames();
				break;
			case TestCaseActionExecution.REQUEST:
				attNames = getRequest().getAttributeNames();
				break;
			case TestCaseActionExecution.APP_CONTEXT:
				attNames = getActionServlet().getServletContext().getAttributeNames();
				break;
		}
		verifyAttributes(attNames, existingAttributesList);
		verifyAttributes(attNames, nonExistingAttributesList);
	}

	/**
	 * @param attNames
	 * @param list
	 * @param exists
	 */
	protected void verifyAttributes(Enumeration attNames, List list) {

		if( (list != null) && (attNames != null) ) {
			while(attNames.hasMoreElements()) {
				String attName = (String) attNames.nextElement();
				String message = null;
				if(list.contains(attName)) {
					message = " Session contains attribute ";
				} else {
					message = "Session doesn't contain attribute ";
					fail(message + attName + ".");
				}
			}
		}
   }

// -------------------------------------------------------------------------------------------------------

	/**
	 * This method must return a Map with all the items that should be in session to execute
	 * the action successfuly.
	 * The Map must be an HashMap and it's keys must be the SessionUtils string constants
	 * correspondent to each object to put in session.
	 * This method must return null if not to be used.
	 */
	protected abstract Map getItemsToPutInSessionForActionToBeTestedSuccessfuly();

	/**
	 * This method must return a Map with all the items that should be in session to execute
	 * the action unsuccessfuly.
	 * The Map must be an HashMap and it's keys must be the SessionUtils string constants
	 * correspondent to each object to put in session.
	 * This method must return null if not to be used.
	 */
	protected abstract Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly();

	/**
	 * This method must return a Map with all the items that should be in request (form) to execute
	 * the action successfuly.
	 * The Map must be an HashMap and it's keys must be the form fiel names
	 * correspondent to each property to get out of the request.
	 * This method must return null if not to be used.
	 */
	protected abstract Map getItemsToPutInRequestForActionToBeTestedSuccessfuly();

	/**
	 * This method must return a Map with all the items that should be in request (form) to execute
	 * the action unsuccessfuly.
	 * The Map must be an HashMap and it's keys must be the form fiel names
	 * correspondent to each property to get out of the request.
	 * This method must return null if not to be used.
	 */
	protected abstract Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly();

	/**
	 * This method must a string identifying the servlet configuration file.
	 */
	protected abstract String getServletConfigFile();
	
	/**
	 * This method must a string identifying the action path of the request path.
	 */
	protected abstract String getRequestPathInfoPathAction();
	
	/**
	 * This method must a string identifying the action name of the request path.
	 */
	protected abstract String getRequestPathInfoNameAction();

	/**
	 * This method must a string identifying the forward when the action executes successfuly.
	 */
	protected abstract String getSuccessfulForward();

	/**
	 * This method must a string identifying the forward when the action executes unsuccessfuly.
	 */
	protected abstract String getUnsuccessfulForward();

}
