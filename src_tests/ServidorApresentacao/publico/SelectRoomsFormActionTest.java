package ServidorApresentacao.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import Dominio.ISala;
import Dominio.Sala;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoSala;

/**
 * @author tfc130
 *
 */
public class SelectRoomsFormActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected ISalaPersistente _salaPersistente = null;
  protected ISala _sala1 = null;
  protected ISala _sala2 = null;

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(SelectRoomsFormActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configuração Struts a utilizar
    setServletConfigFile("/WEB-INF/tests/web-publico.xml");
    
    ligarSuportePersistente();
    cleanData();
    _suportePersistente.iniciarTransaccao();
    _sala1 = new Sala("Fa1",
                      "Pavilhão Novas Licenciaturas",
                      new Integer(0),
                      new TipoSala(TipoSala.ANFITEATRO),
                      new Integer(100),
                      new Integer(50));
	_sala2 = new Sala("C9",
					  "Pavilhão Central",
					  new Integer(1),
					  new TipoSala(TipoSala.PLANA),
					  new Integer(40),
					  new Integer(20));
    _salaPersistente.lockWrite(_sala1);
	_salaPersistente.lockWrite(_sala2);

    _suportePersistente.confirmarTransaccao();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public SelectRoomsFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulSelectRooms() {      
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

  public void testUnexistingSelectRooms() {      
	// define mapping de origem
	setRequestPathInfo("publico", "/chooseRoomsForm");

	// Fill in Form
	addRequestParameter("name","Fa2");
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
	addRequestParameter("name","Fa1");
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
	verifyForwardPath("/consultRooms.jsp");

	//verifica ausencia de erros
	verifyActionErrors(new String[] {"errors.integer"});
  }

  protected void ligarSuportePersistente() {
	try {
	  _suportePersistente = SuportePersistenteOJB.getInstance();
	} catch (ExcepcaoPersistencia excepcao) {
	  fail("Exception when opening database");
	}
	_salaPersistente = _suportePersistente.getISalaPersistente();
  }
    
  protected void cleanData() {
	try {
	  _suportePersistente.iniciarTransaccao();
	  _salaPersistente.deleteAll();
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia excepcao) {
	  fail("Exception when cleaning data");
	}
  }


}