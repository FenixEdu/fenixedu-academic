/*
 * Created on 26/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao;

import java.util.Enumeration;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author dcs-rjao
 *
 * 26/Fev/2003
 */

public abstract class TestCaseActionExecution extends TestCasePresentation {

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

		doTest(	getItemsToPutInRequestForActionToBeTestedSuccessfuly(),
				getItemsToPutInSessionForActionToBeTestedSuccessfuly(),
				getSuccessfulForward(), getExistingAttributesListToVerifyInSuccessfulExecution(),
				getNonExistingAttributesListToVerifyInSuccessfulExecution() );
	}

	public void testUnsuccessfulExecutionOfAction() {

		doTest(	(Map) getItemsToPutInRequestForActionToBeTestedUnsuccessfuly(),
				(Map) getItemsToPutInSessionForActionToBeTestedUnsuccessfuly(),
				getUnsuccessfulForward(), (Map) getExistingAttributesListToVerifyInUnsuccessfulExecution(),
				(Map) getNonExistingAttributesListToVerifyInUnsuccessfulExecution()	);
	}
   
	/**
	 * @param itemsToPutInRequest
	 * @param itemsToPutInSession
	 * @param forward
	 * @param existingAttributesList
	 * @param nonExistingAttributesList
	 */
	protected void doTest(Map itemsToPutInRequest, Map itemsToPutInSession, String forward,
	Map existingAttributesList, Map nonExistingAttributesList) {

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

		if( (pathOfAction != null) && (nameOfAction != null) &&
//			(itemsToPutInSession != null) && (itemsToPutInRequest != null) &&
			(forward != null) ) {
//			perform
			actionPerform();
//			checks for errors
			verifyNoActionErrors();
//			checks forward
			verifyForward(forward);

//			CurricularYearAndSemesterAndInfoExecutionDegree ctx = SessionUtils.getContext(getRequest());
//			assertNotNull("Context is null!", ctx);

			Set keys = null;
			Iterator keysIterator = null;
			if(existingAttributesList != null) {
				keys = existingAttributesList.keySet();
				keysIterator = keys.iterator();
			} else if(nonExistingAttributesList != null) {
				keys = nonExistingAttributesList.keySet();
				keysIterator = keys.iterator();
			}
			if(keys != null) {
				while(keysIterator.hasNext()) {
					Integer key = (Integer) keysIterator.next();
					verifyScopeAttributes(key.intValue(), (List) existingAttributesList.get(key), (List) nonExistingAttributesList.get(key));
				}
			}
		}
   }

	/**
	 * @param scope
	 * @param existingAttributesList
	 * @param nonExistingAttributesList
	 */
	private void verifyScopeAttributes(int scope, List existingAttributesList, List nonExistingAttributesList) {

		Enumeration attNames = null;
		switch(scope) {
			case ScopeConstants.SESSION:
				attNames = getSession().getAttributeNames();
				break;
			case ScopeConstants.REQUEST:
				attNames = getRequest().getAttributeNames();
				break;
			case ScopeConstants.APP_CONTEXT:
				attNames = getActionServlet().getServletContext().getAttributeNames();
				break;
			default:
				throw new IllegalArgumentException("Unknown scope! Use " + ScopeConstants.class.getName());
		}
		verifyAttributes(attNames, existingAttributesList, true);
		verifyAttributes(attNames, nonExistingAttributesList, false);
	}

	/**
	 * @param attNames
	 * @param list
	 * @param exists
	 */
	private void verifyAttributes(Enumeration attNames, List list, boolean contains) {

		if( (list != null) && (attNames != null) ) {
			while(attNames.hasMoreElements()) {
				String attName = (String) attNames.nextElement();
				String message = null;
				if(list.contains(attName) == contains) {
					message = "Scope contains attribute ";
				} else {
					message = "Scope doesn't contain attribute ";
					fail(message + attName + ".");
				}
			}
		}
   }

// -------------------------------------------------------------------------------------------------------

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
	 * The Map must be an Map and it's keys must be the form fiel names
	 * correspondent to each property to get out of the request.
	 * This method must return null if not to be used.
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		return null;
	}

	/**
	 * This method must return a Map with all the items that should be in request (form) to execute
	 * the action unsuccessfuly.
	 * The Map must be an Map and it's keys must be the form fiel names
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

	/**
	 * This method must return one of these 3 constants: ScopeConstants.SESSION;
	 * ScopeConstants.REQUEST; ScopeConstants.APP_CONTEXT.
	 */
	protected int getScope() {
		return ScopeConstants.SESSION;
	}

	/**
	 * This method must return a string identifying the servlet configuration file.
	 */
	protected abstract String getServletConfigFile();
	
	/**
	 * This method must return a string identifying the action path of the request path.
	 */
	protected abstract String getRequestPathInfoPathAction();
	
	/**
	 * This method must return a string identifying the action name of the request path.
	 */
	protected abstract String getRequestPathInfoNameAction();

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

}
