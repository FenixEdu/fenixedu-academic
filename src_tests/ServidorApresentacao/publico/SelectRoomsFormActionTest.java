package ServidorApresentacao.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorApresentacao.TestCasePresentation;

/**
 * @author tfc130
 *
 */
public class SelectRoomsFormActionTest extends TestCasePresentation {
  

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(SelectRoomsFormActionTest.class);
        
    return suite;
  }

  public void setUp() {
    super.setUp();
    // define ficheiro de configuração Struts a utilizar
    setServletConfigFile("/WEB-INF/web.xml");  
   
  }
  
  public SelectRoomsFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulSelectRooms() {      
    // define mapping de origem
    setRequestPathInfo("publico", "/chooseRoomsForm");

	// Fill in Form
	addRequestParameter("name","Ga1");
	//addRequestParameter("building","Pavilhão Novas Licenciaturas");
	//addRequestParameter("floor","0");
	//addRequestParameter("type",(new Integer(TipoSala.ANFITEATRO)).toString());
	//addRequestParameter("capacityNormal","100");
	//addRequestParameter("capacityExame","50");


	// colocar qq coisa na sessão.
	// O simulator tem que lá ter qq coisa para funcionar.
	String xpto = "This is strange, I cannot explain it!";
	getSession().setAttribute("UserView", xpto);

    // invoca acção
    actionPerform();

    // verifica reencaminhamento
    verifyForward("Sucess");

    //verifica ausencia de erros
    verifyNoActionErrors();
  }

  public void testUnexistingSelectRooms() {      
	// define mapping de origem
	setRequestPathInfo("publico", "/chooseRoomsForm");

	// Fill in Form
	addRequestParameter("name","Fa1");
	//addRequestParameter("building","Pavilhão Novas Licenciaturas");
	//addRequestParameter("floor","0");
	//addRequestParameter("type",(new Integer(TipoSala.ANFITEATRO)).toString());
	//addRequestParameter("capacityNormal","100");
	//addRequestParameter("capacityExame","50");


	// colocar qq coisa na sessão.
	// O simulator tem que lá ter qq coisa para funcionar.
	String xpto = "This is strange, I cannot explain it!";
	getSession().setAttribute("UserView", xpto);

	// invoca acção
	actionPerform();

	// verifica reencaminhamento
	verifyForward("Sucess");

	//verifica ausencia de erros
	verifyNoActionErrors();
  }

  public void testUnsuccessfulSelectRooms() {      
	// define mapping de origem
	setRequestPathInfo("publico", "/chooseRoomsForm");

	// Fill in Form
	addRequestParameter("name","Ga1");
	//addRequestParameter("building","Pavilhão Novas Licenciaturas");
	addRequestParameter("floor","xpto");
	//addRequestParameter("type",(new Integer(TipoSala.ANFITEATRO)).toString());
	//addRequestParameter("capacityNormal","100");
	//addRequestParameter("capacityExame","50");


	// colocar qq coisa na sessão.
	// O simulator tem que lá ter qq coisa para funcionar.
	String xpto = "This is strange, I cannot explain it!";
	getSession().setAttribute("UserView", xpto);

	// invoca acção
	actionPerform();

	// verifica reencaminhamento
	verifyInputForward();

	//verifica ausencia de erros
	verifyActionErrors(new String[] {"errors.integer"});
  }

  


}