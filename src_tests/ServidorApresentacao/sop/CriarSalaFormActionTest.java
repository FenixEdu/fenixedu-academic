package ServidorApresentacao.sop;

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

	public void setUp() {
		super.setUp();
		// define ficheiro de configuração Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");

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

}