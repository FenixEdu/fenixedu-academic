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

public abstract class TestCaseDispacthActionExecution extends TestCasePresentation {

	protected GestorServicos gestor = null;
	protected IUserView userView = null;

	public TestCaseDispacthActionExecution(String testName) {
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

   /**
	* @param itemsToPutInRequest
	* @param itemsToPutInSession
	* @param forward
	* @param existingAttributesList
	* @param nonExistingAttributesList
	*/
	protected void testIt(HashMap itemsToPutInRequest, HashMap itemsToPutInSession, String forward,
	List existingAttributesList, List nonExistingAttributesList) {

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
			
			verifyScopeAttributes(getScope(), existingAttributesList, nonExistingAttributesList);
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
	private void verifyAttributes(Enumeration attNames, List list) {

		if( (list != null) && (attNames != null) ) {
			while(attNames.hasMoreElements()) {
				String attName = (String) attNames.nextElement();
				String message = null;
				if(list.contains(attName)) {
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
	 * This method must return one of these 3 constants: ScopeConstants.SESSION;
	 * ScopeConstants.REQUEST; ScopeConstants.APP_CONTEXT.
	 */
	protected abstract int getScope();

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

}
