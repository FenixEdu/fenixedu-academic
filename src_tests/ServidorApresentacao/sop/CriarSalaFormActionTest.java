package ServidorApresentacao.sop;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import java.util.HashSet;

import ServidorPersistente.OJB.*;
import ServidorPersistente.*;
import Dominio.*;

import servletunit.struts.MockStrutsTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import Util.TipoDocumentoIdentificacao;
import Util.TipoSala;

/**
 * @author tfc130
 *
 */
public class CriarSalaFormActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected ISalaPersistente _salaPersistente = null;
  protected IPessoaPersistente _pessoaPersistente = null;
  protected ISala _sala1 = null;
  protected IPessoa _pessoa1 = null;

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(CriarSalaFormActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configuração Struts a utilizar
    setServletConfigFile("/WEB-INF/tests/web-sop.xml");
    
    ligarSuportePersistente();
    cleanData();
    _suportePersistente.iniciarTransaccao();
    _sala1 = new Sala("Fa1",
                      "Pavilhão Novas Licenciaturas",
                      new Integer(0),
                      new TipoSala(TipoSala.ANFITEATRO),
                      new Integer(100),
                      new Integer(50));
    _salaPersistente.lockWrite(_sala1);
    
    HashSet privilegios = new HashSet();
    //privilegios.add("CriarSala");
    //_pessoa1 = new Pessoa("user", "pass", privilegios);
    //_pessoaPersistente.lockWrite(_pessoa1);
    _pessoa1 = new Pessoa();
    _pessoa1.setNumeroDocumentoIdentificacao("0123456789");
    _pessoa1.setCodigoFiscal("9876543210");
    _pessoa1.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			   TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _pessoa1.setUsername("user");
    _pessoa1.setPassword("pass");
    privilegios.add(new Privilegio(_pessoa1, new String("CriarSala")));
    _pessoa1.setPrivilegios(privilegios);
    _pessoaPersistente.escreverPessoa(_pessoa1);

    _suportePersistente.confirmarTransaccao();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public CriarSalaFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulCriarSala() {      
    // define mapping de origem
    setRequestPathInfo("", "/criarSalaForm");
    
    // Preenche campos do formulário
    addRequestParameter("name","Fa2");
    addRequestParameter("building","Pavilhão Novas Licenciaturas");
    addRequestParameter("floor","0");
    addRequestParameter("type",(new Integer(TipoSala.ANFITEATRO)).toString());
    addRequestParameter("capacityNormal","100");
    addRequestParameter("capacityExame","50");

    // coloca credenciais na sessão
    HashSet privilegios = new HashSet();
    privilegios.add("CriarSala");
    IUserView userView = new UserView("user", privilegios);
    getSession().setAttribute("UserView", userView);
    
    // invoca acção
    actionPerform();
    
    // verifica reencaminhamento
    verifyForward("Sucesso");
    
    //verifica ausencia de erros
    verifyNoActionErrors();
  }

  public void testUnsuccessfulCriarSala() {
    setRequestPathInfo("", "/criarSalaForm");
    addRequestParameter("name","Fa1");
    addRequestParameter("building","Pavilhão Novas Licenciaturas");
    addRequestParameter("floor","0");
    addRequestParameter("type",(new Integer(TipoSala.ANFITEATRO)).toString());
    addRequestParameter("capacityNormal","100");
    addRequestParameter("capacityExame","50");
    actionPerform();
    verifyForwardPath("/naoExecutado.do");
    
    verifyActionErrors(new String[] {"ServidorAplicacao.NotExecutedException"});
  }
  

  protected void ligarSuportePersistente() {
    try {
      _suportePersistente = SuportePersistenteOJB.getInstance();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when opening database");
    }
    _salaPersistente = _suportePersistente.getISalaPersistente();
    _pessoaPersistente = _suportePersistente.getIPessoaPersistente();
  }
    
  protected void cleanData() {
    try {
      _suportePersistente.iniciarTransaccao();
      _salaPersistente.deleteAll();
      //_pessoaPersistente.deleteAll();
      _pessoaPersistente.apagarTodasAsPessoas();
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when cleaning data");
    }
  }
}