package ServidorApresentacao;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import java.util.HashSet;

import ServidorPersistente.OJB.*;
import ServidorPersistente.*;
import Dominio.*;

import servletunit.struts.MockStrutsTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author jorge
 *
 */
public class CriarSitioFormActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected ISitioPersistente _sitioPersistente = null;
  protected IPessoaPersistente _pessoaPersistente = null;
  protected ISitio _sitio1 = null;
  protected IPessoa _pessoa1 = null;

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(CriarSitioFormActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configuração Struts a utilizar
    setServletConfigFile("/WEB-INF/tests/web-docente.xml");
    
    ligarSuportePersistente();
    cleanData();
    _suportePersistente.iniciarTransaccao();
    _sitio1 = new Sitio("EP", 4, 1, "LEIC", "DEI");
    _sitioPersistente.lockWrite(_sitio1);
    _suportePersistente.confirmarTransaccao();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public CriarSitioFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulCriarSitio() {      
    // define mapping de origem
    setRequestPathInfo("", "/criarSitioForm");
    
    // Preenche campos do formulário
    addRequestParameter("nome","AED");
    addRequestParameter("anoCurricular","1");
    addRequestParameter("semestre","2");
    addRequestParameter("curso","LEIC");
    addRequestParameter("departamento","DEI");

    // coloca credenciais na sessão
    HashSet privilegios = new HashSet();
    privilegios.add("CriarSitio");
    IUserView userView = new UserView("jorge", privilegios);
    getSession().setAttribute("UserView", userView);
    
    // invoca acção
    actionPerform();
    
    // verifica reencaminhamento
    verifyForward("Sucesso");
    
    //verifica ausencia de erros
    verifyNoActionErrors();
  }

  public void testUnsuccessfulCriarSitio() {
    setRequestPathInfo("", "/criarSitioForm");
    addRequestParameter("nome","EP");
    addRequestParameter("anoCurricular","4");
    addRequestParameter("semestre","1");
    addRequestParameter("curso","LEIC");
    addRequestParameter("departamento","DEI");
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
    _sitioPersistente = _suportePersistente.getISitioPersistente();
  }
    
  protected void cleanData() {
    try {
      _suportePersistente.iniciarTransaccao();
      _sitioPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when cleaning data");
    }
  }
}