package ServidorApresentacao.sop;
  
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
	
	public void setUp() {
		super.setUp();
		
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");
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
		//action perform
		actionPerform();

		//verify action forward
		verifyForward(operation);
		
		//verify that there are errors
		verifyNoActionErrors();		
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
