package ServidorApresentacao.sop;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import servletunit.struts.MockStrutsTestCase;

import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IPessoa;
import Dominio.ITurma;
import Dominio.Pessoa;
import Dominio.Privilegio;
import Dominio.Turma;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;


/**
 * @author tfc130
 *
 */
public class EscolherContextoFormActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected ICursoPersistente _cursoPersistente = null;
  protected ICursoExecucaoPersistente _cursoExecucaoPersistente = null;
  protected ITurmaPersistente _turmaPersistente = null;
  protected IPessoaPersistente _pessoaPersistente = null;
  protected ICurso curso1 = null;
  protected ICurso curso2 = null;
  protected ICursoExecucao _cursoExecucao1 = null;
  protected ICursoExecucao _cursoExecucao2 = null;
  protected ITurma _turma1 = null;
  protected ITurma _turma2 = null;
  protected IPessoa _pessoa1 = null;

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(EscolherContextoFormActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configura��o Struts a utilizar
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
    privilegios.add(new Privilegio(_pessoa1, new String("LerLicenciatura")));
    privilegios.add(new Privilegio(_pessoa1, new String("ReadExecutionDegreesByExecutionYear")));
    privilegios.add(new Privilegio(_pessoa1, new String("LerLicenciaturaExecucaoDeLicenciatura")));
    privilegios.add(new Privilegio(_pessoa1, new String("LerTurmas")));
    _pessoa1.setPrivilegios(privilegios);
    _pessoaPersistente.escreverPessoa(_pessoa1);

    curso1 = new Curso("LEIC","Curso de Engenharia Informatica e de Computadores", new TipoCurso(TipoCurso.LICENCIATURA));
    _cursoPersistente.lockWrite(curso1);
    curso2 = new Curso("LEEC","Curso de Engenharia Electrotecnica e de Computadores", new TipoCurso(TipoCurso.LICENCIATURA));
    _cursoPersistente.lockWrite(curso1);

    _cursoExecucao1 = new CursoExecucao("2002/03", curso1);
    _cursoExecucaoPersistente.lockWrite(_cursoExecucao1);
    _cursoExecucao2 = new CursoExecucao("2002/03", curso2);
    _cursoExecucaoPersistente.lockWrite(_cursoExecucao1);

    _turma1 = new Turma("10501",new Integer(1),new Integer(1),curso1);
    _turmaPersistente.lockWrite(_turma1);
    _turma2 = new Turma("14501",new Integer(1),new Integer(1),curso2);
    _turmaPersistente.lockWrite(_turma2);

    _suportePersistente.confirmarTransaccao();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public EscolherContextoFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulEscolherContexto() {      
    // define mapping de origem
    setRequestPathInfo("", "/escolherContextoForm");
    
    // Preenche campos do formul�rio
    addRequestParameter("sigla","LEIC");
    addRequestParameter("anoCurricular","1");
    addRequestParameter("semestre","1");

    // coloca credenciais na sess�o
    HashSet privilegios = new HashSet();
    privilegios.add("LerLicenciatura");
    privilegios.add("ReadExecutionDegreesByExecutionYear");
    privilegios.add("LerLicenciaturaExecucaoDeLicenciatura");
    privilegios.add("LerTurmas");
    IUserView userView = new UserView("user", privilegios);
    getSession().setAttribute(SessionConstants.U_VIEW, userView);
    
    // invoca ac��o
    actionPerform();
    
	//verifica ausencia de erros
	verifyNoActionErrors();

    // verifica reencaminhamento
    verifyForward("Sucesso");
    
  }

  public void testUnsuccessfulEscolherContexto() {
    setRequestPathInfo("", "/escolherContextoForm");
    addRequestParameter("sigla","LEIC");
    addRequestParameter("anoCurricular","3");
    addRequestParameter("semestre","1");
    
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
    _cursoPersistente = _suportePersistente.getICursoPersistente();
    _cursoExecucaoPersistente = _suportePersistente.getICursoExecucaoPersistente();
    _pessoaPersistente = _suportePersistente.getIPessoaPersistente();
    _turmaPersistente = _suportePersistente.getITurmaPersistente();
  }
    
  protected void cleanData() {
    try {
      _suportePersistente.iniciarTransaccao();
      _cursoPersistente.deleteAll();
      _cursoExecucaoPersistente.deleteAll();
      _turmaPersistente.deleteAll();
      //_pessoaPersistente.deleteAll();
      _pessoaPersistente.apagarTodasAsPessoas();
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when cleaning data");
    }
  }
}