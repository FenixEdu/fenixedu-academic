package ServidorApresentacao.sop;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoSala;

/**
 * @author tfc130
 *
 */
public class CriarSalaFormActionTest extends TestCasePresentationSopPortal {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CriarSalaFormActionTest.class);

		return suite;
	}

	
	protected String getServletConfigFile() {
			return "/WEB-INF/tests/web-sop.xml";
		}
	public CriarSalaFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulCriarSala() {
		getSession().setAttribute(
			SessionConstants.SESSION_IS_VALID,
			SessionConstants.SESSION_IS_VALID);

		// define mapping de origem
		setRequestPathInfo("", "/criarSalaForm");

		// Preenche campos do formulário
		addRequestParameter("name", "Fa2");
		addRequestParameter("building", "Pavilhão Novas Licenciaturas");
		addRequestParameter("floor", "0");
		addRequestParameter(
			"type",
			(new Integer(TipoSala.ANFITEATRO)).toString());
		addRequestParameter("capacityNormal", "100");
		addRequestParameter("capacityExame", "50");

		// coloca credenciais na sessão
		setAuthorizedUser();
		// invoca acção
		actionPerform();

		// verifica reencaminhamento
		verifyForward("Sucesso");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}

	public void testUnsuccessfulCriarSala() {
		setRequestPathInfo("", "/criarSalaForm");
		
		setAuthorizedUser();
		addRequestParameter("name", "GA1");
		addRequestParameter("building", "Pavilhão Novas Licenciaturas");
		addRequestParameter("floor", "0");
		addRequestParameter(
			"type",
			(new Integer(TipoSala.ANFITEATRO)).toString());
		addRequestParameter("capacityNormal", "100");
		addRequestParameter("capacityExame", "50");
		actionPerform();
		String []errors = {"error.exception.existing"};
		verifyActionErrors(errors);
		
		verifyInputForward();
		

		//	verifyActionErrors(new String[] {"error.invalid.session"});
	}

	

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		
		return null;
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
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		
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