package ServidorApresentacao.sop;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import servletunit.struts.MockStrutsTestCase;

import Dominio.IPessoa;
import Dominio.Pessoa;
import Dominio.Privilegio;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;


/**
 * @author tfc130
 *
 */
public class AutenticacaoSOPFormActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected IPessoaPersistente _pessoaPersistente = null;
  protected IPessoa _pessoa1 = null;
  protected IPessoa _pessoa2 = null;

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AutenticacaoSOPFormActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configuração a utilizar
    setServletConfigFile("/WEB-INF/tests/web-sop.xml");
    
    ligarSuportePersistente();
    cleanData();
    _suportePersistente.iniciarTransaccao();

    HashSet privilegios = new HashSet();
    _pessoa1 = new Pessoa();
    _pessoa1.setNumeroDocumentoIdentificacao("0123456789");
    _pessoa1.setCodigoFiscal("9876543210");
    _pessoa1.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			   TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _pessoa1.setUsername("nome");
    _pessoa1.setPassword("pass");
    privilegios.add(new Privilegio(_pessoa1, new String("CriarSitio")));
    _pessoa1.setPrivilegios(privilegios);
    _pessoaPersistente.escreverPessoa(_pessoa1);
    

    
    _suportePersistente.confirmarTransaccao();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public AutenticacaoSOPFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulAutenticacao() {      
    // define mapping de origem
    setRequestPathInfo("", "/autenticacaoSOPForm");

    // Preenche campos do formulário
    addRequestParameter("utilizador","nome");
    addRequestParameter("password","pass");

    // coloca credenciais na sessão
    HashSet privilegios = new HashSet();
    IUserView userView = new UserView("athirduser", privilegios);
    getSession().setAttribute("UserView", userView);
    
    // invoca acção
    actionPerform();
    
    // verifica reencaminhamento
    verifyForward("SOP");
    
    //verifica ausencia de erros
    verifyNoActionErrors();
    
    //verifica UserView guardado na sessão
    UserView newUserView = (UserView) getSession().getAttribute("UserView");
    assertEquals("Verify UserView", newUserView.getUtilizador(), "nome");
    assertTrue("", newUserView.getPrivilegios().contains("CriarSitio"));
  }
  

  public void testUnsuccessfulAutorizacao() {
    // define mapping de origem
    setRequestPathInfo("", "/autenticacaoSOPForm");
    
    // Preenche campos do formulário
    addRequestParameter("utilizador","nome");
    addRequestParameter("password","xpto");

    // coloca credenciais na sessão
    HashSet privilegios = new HashSet();
    IUserView userView = new UserView("athirduser", privilegios);
    getSession().setAttribute("UserView", userView);
    
    // invoca acção
    actionPerform();
    
    // verifica endereço do reencaminhamento (ExcepcaoAutorizacao)
    verifyForwardPath("/autenticacaoSOP.jsp");
    
    //verifica existencia de erros
    verifyActionErrors(new String[] {"errors.invalidAuthentication"});

    
    //verifica UserView guardado na sessão
    UserView newUserView = (UserView) getSession().getAttribute("UserView");
    assertEquals("Verify UserView", newUserView.getUtilizador(), "athirduser");
  }

  protected void ligarSuportePersistente() {
    try {
      _suportePersistente = SuportePersistenteOJB.getInstance();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when opening database");
    }
    _pessoaPersistente = _suportePersistente.getIPessoaPersistente();
  }
    
  protected void cleanData() {
    try {
      _suportePersistente.iniciarTransaccao();
      //_pessoaPersistente.deleteAll();
      _pessoaPersistente.apagarTodasAsPessoas();
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when cleaning data");
    }
  } 
}