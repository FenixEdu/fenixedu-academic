package ServidorApresentacao.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoRoom;
import Dominio.ISala;
import Dominio.Sala;
import ServidorAplicacao.GestorServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoSala;

/**
 * @author tfc130
 *
 */
public class ViewRoomFormActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected ISalaPersistente _salaPersistente = null;
  protected ISala _sala1 = null;
  protected ISala _sala2 = null;
  protected List _infoRooms = null;

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ViewRoomFormActionTest.class);
        
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
    
	GestorServicos _gestor = GestorServicos.manager();
	Object argsSelectRooms[] = { new InfoRoom() };
    _infoRooms = (List) _gestor.executar(null, "SelectRooms", argsSelectRooms);
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public ViewRoomFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulViewRoom() {      
    // define mapping de origem
    setRequestPathInfo("publico", "/viewRoom");

	// Fill in Form
	addRequestParameter("index","0");

	// Place list of rooms in session.
	getSession().setAttribute("publico.infoRooms", _infoRooms);

    // invoca acção
    actionPerform();

    // verifica reencaminhamento
    verifyForward("Sucess");

    //verifica ausencia de erros
    verifyNoActionErrors();
  }

  public void testUnsuccessfulSelectRooms() {      
	// define mapping de origem
	setRequestPathInfo("publico", "/viewRoom");

	// Fill in Form
	//addRequestParameter("index","0");

	// Place list of rooms in session.
	getSession().setAttribute("publico.infoRooms", _infoRooms);

	// invoca acção
	actionPerform();

	// verifica reencaminhamento
	verifyForwardPath("/viewRoom.jsp");

	//verifica ausencia de erros
	verifyNoActionErrors();
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