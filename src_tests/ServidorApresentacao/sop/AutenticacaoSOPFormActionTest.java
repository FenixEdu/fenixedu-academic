package ServidorApresentacao.sop;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;


/**
 * @author tfc130
 *
 */
public class AutenticacaoSOPFormActionTest extends TestCasePresentation {
  

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AutenticacaoSOPFormActionTest.class);
        
    return suite;
  }

  public void setUp() {
    super.setUp();
    // define ficheiro de configuração a utilizar
    setServletConfigFile("/WEB-INF/tests/web-sop.xml");
    
    
  }
  
  
  
  public AutenticacaoSOPFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulAutenticacao() {      
    // define mapping de origem
    setRequestPathInfo("", "/autenticacaoSOPForm");

    // Preenche campos do formulário
    addRequestParameter("utilizador","user");
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
    UserView newUserView = (UserView) getSession().getAttribute(SessionConstants.U_VIEW);
    assertEquals("Verify UserView", newUserView.getUtilizador(), "user");
    assertTrue("Verify authorization", newUserView.getPrivilegios().contains("CriarSala"));
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

  
    
  
}