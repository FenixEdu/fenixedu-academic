package ServidorApresentacao.sop;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import Util.TipoSala;

/**
 * @author tfc130
 *
 */
public class CriarSalaFormActionTest extends TestCasePresentation {
  

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
  

}