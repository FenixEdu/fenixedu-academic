package ServidorApresentacao;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import Dominio.IPessoa;
import Dominio.Pessoa;
import Dominio.Privilegio;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;


/**
 * @author jorge
 *
 */
public class AutenticacaoFormActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected IPessoaPersistente _pessoaPersistente = null;
  protected IPessoa _pessoa1 = null;
  protected IPessoa _pessoa2 = null;

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AutenticacaoFormActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configuração a utilizar
    setServletConfigFile("/WEB-INF/web.xml");
    
    ligarSuportePersistente();
    cleanData();
    _suportePersistente.iniciarTransaccao();

    HashSet privilegios = new HashSet();
    //privilegios.add("CriarSitio");
    //_pessoa1 = new Pessoa("user", "pass", privilegios);
    //_pessoaPersistente.lockWrite(_pessoa1);
    _pessoa1 = new Pessoa();
    _pessoa1.setNumeroDocumentoIdentificacao("0123456789");
    _pessoa1.setCodigoFiscal("9876543210");
    _pessoa1.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			   TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _pessoa1.setUsername("user");
    _pessoa1.setPassword("pass");
    privilegios.add(new Privilegio(_pessoa1, new String("CriarSitio")));
    _pessoa1.setPrivilegios(privilegios);
    _pessoaPersistente.escreverPessoa(_pessoa1);
    
    privilegios = new HashSet();
    //privilegios.add("AlterarSitio");
    //_pessoa2 = new Pessoa("anotheruser", "anotherpass", privilegios);
    //_pessoaPersistente.lockWrite(_pessoa2);
    _pessoa2 = new Pessoa();
    _pessoa2.setNumeroDocumentoIdentificacao("0321654987");
    _pessoa2.setCodigoFiscal("7894561230");
    _pessoa2.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			 TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _pessoa2.setUsername("nome2");
    _pessoa2.setPassword("pass2");
    privilegios.add(new Privilegio(_pessoa1, new String("AlterarSitio")));
    _pessoa2.setPrivilegios(privilegios);
    _pessoaPersistente.escreverPessoa(_pessoa2);    
    
    _suportePersistente.confirmarTransaccao();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public AutenticacaoFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulAutenticacao() {      
    // define mapping de origem
    setRequestPathInfo("", "/autenticacaoForm");
    
    // Preenche campos do formulário
    addRequestParameter("utilizador","user");
    addRequestParameter("password","pass");

    // coloca credenciais na sessão
//    HashSet privilegios = new HashSet();
//    IUserView userView = new UserView("athirduser", privilegios);
//    getSession().setAttribute("UserView", userView);
    
    // invoca acção
    actionPerform();
    
    // verifica reencaminhamento
    verifyForward("Docente");
    
    //verifica ausencia de erros
    verifyNoActionErrors();
    
    //verifica UserView guardado na sessão
    UserView newUserView = (UserView) getSession().getAttribute("UserView");
    assertEquals("Verify UserView", newUserView.getUtilizador(), "user");

  }
  

  public void testUnsuccessfulAutorizacao() {
    // define mapping de origem
    setRequestPathInfo("", "/autenticacaoForm");
    
    // Preenche campos do formulário
    addRequestParameter("utilizador","user");
    addRequestParameter("password","xpto");

    // coloca credenciais na sessão
//    HashSet privilegios = new HashSet();
//    IUserView userView = new UserView("athirduser", privilegios);
//    getSession().setAttribute("UserView", userView);
    
    // invoca acção
    actionPerform();
    
    // verifica endereço do reencaminhamento (ExcepcaoAutorizacao)
    verifyForwardPath("/autenticacao.do");
    
    //verifica existencia de erros
    verifyActionErrors(new String[] {"ServidorAplicacao.Servico.ExcepcaoAutenticacao"});

    
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