package ServidorApresentacao.student;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import servletunit.struts.MockStrutsTestCase;

import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.Pessoa;
import Dominio.Privilegio;
import Dominio.Student;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;


/**
 * @author tfc130
 *
 */
public class AutenticacaoStudentFormActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected IPessoaPersistente _persistentPerson = null;
  protected IPersistentStudent _persistentStudent = null;
  protected IPessoa _person1 = null;
  protected IPessoa _person2 = null;
  protected IPessoa _person3 = null;
  protected IStudent _student1 = null;
  protected IStudent _student3 = null;

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AutenticacaoStudentFormActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configuração a utilizar
    setServletConfigFile("/WEB-INF/tests/web-student.xml");

    ligarSuportePersistente();
    cleanData();
    _suportePersistente.iniciarTransaccao();

    HashSet privileges = new HashSet();
    _person1 = new Pessoa();
    _person1.setNumeroDocumentoIdentificacao("0123456789");
    _person1.setCodigoFiscal("9876543210");
    _person1.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			   TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _person1.setUsername("12345");
    _person1.setPassword("pass");
	privileges.add(new Privilegio(_person1, new String("ReadStudentByUsername")));
    _person1.setPrivilegios(privileges);
    //_persistentPerson.escreverPessoa(_person1);
    
	_student1 =
		new Student(
			new Integer(12345),
			new Integer(567),
			_person1,
			new TipoCurso(TipoCurso.LICENCIATURA));
	_persistentStudent.lockWrite(_student1);

	privileges = new HashSet();
    _person2 = new Pessoa();
    _person2.setNumeroDocumentoIdentificacao("0321654987");
    _person2.setCodigoFiscal("7894561230");
    _person2.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			 TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _person2.setUsername("nome2");
    _person2.setPassword("pass2");
    _person2.setPrivilegios(privileges);
    _persistentPerson.escreverPessoa(_person2);

	privileges = new HashSet();
	_person3 = new Pessoa();
	_person3.setNumeroDocumentoIdentificacao("12121212112");
	_person3.setCodigoFiscal("12121212112");
	_person3.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
				   TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
	_person3.setUsername("54321");
	_person3.setPassword("pass");
	privileges.add(new Privilegio(_person3, new String("ReadStudentByUsername")));
	_person3.setPrivilegios(privileges);
	//_persistentPerson.escreverPessoa(_person3);

	_student3 =
		new Student(
			new Integer(12345),
			new Integer(567),
			_person3,
			new TipoCurso(TipoCurso.MESTRADO));
	_persistentStudent.lockWrite(_student3);

    _suportePersistente.confirmarTransaccao();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public AutenticacaoStudentFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulAutenticacao() {      
    // define mapping de origem
    setRequestPathInfo("", "/authenticationStudentForm");

    // Preenche campos do formulário
    addRequestParameter("username","12345");
    addRequestParameter("password","pass");

    // coloca credenciais na sessão
    HashSet privilegios = new HashSet();
    IUserView userView = new UserView("athirduser", privilegios);
    getSession().setAttribute("UserView", userView);
    
    // invoca acção
    actionPerform();

    // verifica reencaminhamento
    verifyForward("Student");

    //verifica ausencia de erros
    verifyNoActionErrors();

    //verifica UserView guardado na sessão
    UserView newUserView = (UserView) getSession().getAttribute("UserView");
    assertEquals("Verify UserView", newUserView.getUtilizador(), "12345");
  }
  

  public void testUnsuccessfulAutorizacao() {
    // define mapping de origem
	setRequestPathInfo("", "/authenticationStudentForm");
    
    // Preenche campos do formulário
	addRequestParameter("username","12345");
    addRequestParameter("password","pass2");

    // coloca credenciais na sessão
    HashSet privilegios = new HashSet();
    IUserView userView = new UserView("athirduser", privilegios);
    getSession().setAttribute("UserView", userView);
    
    // invoca acção
    actionPerform();
    
    // verifica endereço do reencaminhamento (ExcepcaoAutorizacao)
    verifyForwardPath("/authenticationStudent.do");
    
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
    _persistentPerson = _suportePersistente.getIPessoaPersistente();
	_persistentStudent = _suportePersistente.getIPersistentStudent();
  }
    
  protected void cleanData() {
    try {
      _suportePersistente.iniciarTransaccao();
      _persistentPerson.apagarTodasAsPessoas();
	  _persistentStudent.deleteAll();
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when cleaning data");
    }
  } 
}