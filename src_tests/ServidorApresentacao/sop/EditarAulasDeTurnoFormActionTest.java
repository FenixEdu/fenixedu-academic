package ServidorApresentacao.sop;
  
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorApresentacao.TestCasePresentationSopPortal;

/**
 * EditarAulasDeTurnoFormActionTest.java
 * 
 * @author Ivo Brandão
 */
public class EditarAulasDeTurnoFormActionTest extends TestCasePresentationSopPortal {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EditarAulasDeTurnoFormActionTest.class);

		return suite;
	}
	
	
	protected String getServletConfigFile() {
			return "/WEB-INF/web.xml";
		}
	public EditarAulasDeTurnoFormActionTest(String testName) {
		super(testName);
	}

	public void testAdicionarAulas() {
		/* :FIXME: change this when changing action */
		String operation = "Adicionar Aulas";
		
		//execute the test
		executeTest(operation);
	}

	public void testRemoverAula() {
		/* :FIXME: change this when changing action */
		String operation = "Remover Aula";

		//execute the test
		executeTest(operation);
	}

	private void executeTest(String operation){
		//set request path
		setRequestPathInfo("/sop", "/editarAulasDeTurnoForm");
		addRequestParameter("operation", operation);
		setAuthorizedUser();

		// Fill out form
		addRequestParameter("indexAula", "0");

		//action perform
		actionPerform();

		verifyNoActionErrors();

		//verify action forward
		verifyForward(operation);
		
		//verify that there are errors
		verifyNoActionErrors();		
	}

	

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		//
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		// 
		return null;
	}
	
}
