package ServidorApresentacao.student;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import Dominio.Aula;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.Frequenta;
import Dominio.IAula;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IPessoa;
import Dominio.ISala;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ITurnoAula;
import Dominio.Pessoa;
import Dominio.Privilegio;
import Dominio.Sala;
import Dominio.Student;
import Dominio.Turno;
import Dominio.TurnoAluno;
import Dominio.TurnoAula;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DiaSemana;
import Util.TipoAula;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;
import Util.TipoSala;


/**
 * @author tfc130
 *
 */
public class ViewShiftScheduleActionTest extends MockStrutsTestCase {

  protected ISuportePersistente _suportePersistente = null;
  protected IPessoaPersistente _persistentPerson = null;
  protected IPersistentStudent _persistentStudent = null;
  protected ICursoExecucaoPersistente _cursoExecucaoPersistente = null;
  protected ICursoPersistente _cursoPersistente = null;
  protected IFrequentaPersistente _frequentaPersistente;
  protected ITurnoPersistente _turnoPersistente = null;
  protected ITurnoAlunoPersistente _turnoAlunoPersistente = null;
  protected ITurnoAulaPersistente _turnoAulaPersistente = null;
  protected ISalaPersistente _salaPersistente = null;
  
  protected IPessoa _person1 = null;
  protected IPessoa _person2 = null;
  protected IPessoa _person3 = null;
  protected IStudent _student1 = null;
  protected IStudent _student3 = null;
  protected InfoPerson _infoPerson = null;
  protected InfoStudent _infoStudent = null;
  

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ViewShiftScheduleActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configuração a utilizar
    setServletConfigFile("/WEB-INF/tests/web-student.xml");

    ligarSuportePersistente();
    cleanData();

	ICurso _curso1 = new Curso("LEIC", "Informatica", new TipoCurso(TipoCurso.LICENCIATURA));

	ICursoExecucao _cursoExecucao1 = new CursoExecucao("2002/03", _curso1);

	IPessoa person = new Pessoa();
	Set privileges = new HashSet();	
	person.setNome("Marvin");
	person.setUsername("45498");
	person.setNumeroDocumentoIdentificacao("010101010101");
	person.setCodigoFiscal("010101010101");
	person.setTipoDocumentoIdentificacao(
		new TipoDocumentoIdentificacao(
			TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
	privileges.add(new Privilegio(person, new String("ReadShiftLessons")));
	person.setPrivilegios(privileges);
	IStudent student = new Student(new Integer(45498), new Integer(567), person, new TipoCurso(TipoCurso.LICENCIATURA));

	IDisciplinaExecucao discipline1 =
		new DisciplinaExecucao(
			"Trabalho Final de Curso I",
			"TFCI",
			"Program1",
			_cursoExecucao1,
			new Double(1),
			new Double(1),
			new Double(1),
			new Double(1));
	IDisciplinaExecucao discipline2 =
		new DisciplinaExecucao(
			"Trabalho Final de Curso II",
			"TFCII",
			"Program2",
			_cursoExecucao1,
			new Double(1),
			new Double(1),
			new Double(1),
			new Double(1));
	IDisciplinaExecucao discipline3 =
		new DisciplinaExecucao(
			"Engenharia da Programação",
			"EP",
			"Program3",
			_cursoExecucao1,
			new Double(1),
			new Double(1),
			new Double(1),
			new Double(1));
	IDisciplinaExecucao discipline4 =
		new DisciplinaExecucao(
			"Aprendizagem",
			"APR",
			"Program4",
			_cursoExecucao1,
			new Double(1),
			new Double(1),
			new Double(1),
			new Double(1));

	IFrequenta attend1 = new Frequenta(student, discipline1);
	IFrequenta attend2 = new Frequenta(student, discipline2);
	IFrequenta attend3 = new Frequenta(student, discipline3);
	IFrequenta attend4 = new Frequenta(student, discipline4);
	
	ITurno shift1 =
		new Turno(
			"turno_ep_teorico1",
			new TipoAula(TipoAula.TEORICA),
			new Integer(100),
			discipline3);
	ITurno shift2 =
		new Turno(
			"turno_ep_laboratorio1",
			new TipoAula(TipoAula.LABORATORIAL),
			new Integer(50),
			discipline3);
	ITurno shift3 =
		new Turno(
			"turno_ep_laboratorio2",
			new TipoAula(TipoAula.LABORATORIAL),
			new Integer(50),
			discipline3);
	ITurno shift4 =
		new Turno(
			"turno_ep_pratica1",
			new TipoAula(TipoAula.PRATICA),
			new Integer(50),
			discipline3);
	ITurno shift5 =
		new Turno(
			"turno_ep_pratica2",
			new TipoAula(TipoAula.PRATICA),
			new Integer(50),
			discipline4);
	ITurno shift6 =
		new Turno(
			"turno_apr_teorico1",
			new TipoAula(TipoAula.TEORICA),
			new Integer(25),
			discipline4);
	ITurno shift7 =
		new Turno(
			"turno_apr_pratica1",
			new TipoAula(TipoAula.PRATICA),
			new Integer(25),
			discipline4);

	ITurnoAluno shiftStudent1 = new TurnoAluno(shift6, student);
	ITurnoAluno shiftStudent2 = new TurnoAluno(shift7, student);

	Calendar inicio = Calendar.getInstance();
	Calendar fim = Calendar.getInstance();
	ISala sala1 =
		new Sala(
			new String("Ga1"),
			new String("Pavilho Central"),
			new Integer(1),
			new TipoSala(TipoSala.ANFITEATRO),
			new Integer(100),
			new Integer(50));

	IAula lesson1 =
		new Aula(
			new DiaSemana(DiaSemana.QUARTA_FEIRA),
			inicio,
			fim,
			new TipoAula(TipoAula.TEORICA),
			sala1,
			discipline4);
	IAula lesson2 =
		new Aula(
			new DiaSemana(DiaSemana.QUINTA_FEIRA),
			inicio,
			fim,
			new TipoAula(TipoAula.PRATICA),
			sala1,
			discipline4);

	ITurnoAula shiftLesson1 = new TurnoAula(shift6, lesson1);
	ITurnoAula shiftLesson2 = new TurnoAula(shift7, lesson2);

	try {
		_suportePersistente.iniciarTransaccao();
		_cursoPersistente.lockWrite(_curso1);
		_cursoExecucaoPersistente.lockWrite(_cursoExecucao1);				
		_frequentaPersistente.lockWrite(attend1);
		_frequentaPersistente.lockWrite(attend2);
		_frequentaPersistente.lockWrite(attend3);
		_frequentaPersistente.lockWrite(attend4);
		_turnoPersistente.lockWrite(shift1);
		_turnoPersistente.lockWrite(shift2);
		_turnoPersistente.lockWrite(shift3);
		_turnoPersistente.lockWrite(shift4);
		_turnoPersistente.lockWrite(shift5);
		_turnoAlunoPersistente.lockWrite(shiftStudent1);
		_turnoAlunoPersistente.lockWrite(shiftStudent2);
		_turnoAulaPersistente.lockWrite(shiftLesson1);
		_turnoAulaPersistente.lockWrite(shiftLesson2);
		_suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia excepcao) {
		fail("Exception when setUp");
	}

	_infoPerson = new InfoPerson();
	_infoPerson.setNome(person.getNome());
	_infoPerson.setUsername(person.getUsername());
	_infoStudent =
		new InfoStudent(
			student.getNumber(),
			student.getState(),
			_infoPerson,
			new TipoCurso(TipoCurso.LICENCIATURA));
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public ViewShiftScheduleActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulViewEnrolment() {      
    // define mapping de origem
    setRequestPathInfo("/student", "/viewShiftSchedule");

	// Preenche campos do formulário
	addRequestParameter("shiftName","turno_apr_teorico1");

    // coloca credenciais na sessão
    HashSet privileges = new HashSet();
	privileges.add("ReadShiftLessons");
    IUserView userView = new UserView("athirduser", privileges);
    getSession().setAttribute("UserView", userView);
    
    // invoca acção
    actionPerform();

    // verifica reencaminhamento
    verifyForward("sucess");

    //verifica ausencia de erros
    verifyNoActionErrors();

    //verifica UserView guardado na sessão
    List lessons = (List) getSession().getAttribute(SessionConstants.LESSON_LIST_ATT);
	assertNotNull("Shift lessons not present", lessons);
	assertEquals("Shift lessons not present", lessons.size(), 1);
  }
  

  // Not possible if user was able to log in as a student
  //public void testUnsuccessfulViewEnrolment() {
  //}

  protected void ligarSuportePersistente() {
    try {
      _suportePersistente = SuportePersistenteOJB.getInstance();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when opening database");
    }
    _persistentPerson = _suportePersistente.getIPessoaPersistente();
	_persistentStudent = _suportePersistente.getIPersistentStudent();
	_cursoExecucaoPersistente =_suportePersistente.getICursoExecucaoPersistente();
	_cursoPersistente =_suportePersistente.getICursoPersistente();
	_frequentaPersistente = _suportePersistente.getIFrequentaPersistente();
	_turnoPersistente = _suportePersistente.getITurnoPersistente();
	_turnoAlunoPersistente =_suportePersistente.getITurnoAlunoPersistente();
	_turnoAulaPersistente =_suportePersistente.getITurnoAulaPersistente();
	_salaPersistente = _suportePersistente.getISalaPersistente();
  }
    
  protected void cleanData() {
    try {
      _suportePersistente.iniciarTransaccao();
      _persistentPerson.apagarTodasAsPessoas();
	  _persistentStudent.deleteAll();
	  _cursoExecucaoPersistente.deleteAll();
	  _cursoPersistente.deleteAll();
	  _frequentaPersistente.deleteAll();
	  _turnoPersistente.deleteAll();
	  _turnoAlunoPersistente.deleteAll();
	  _turnoAulaPersistente.deleteAll();
	  _salaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when cleaning data");
    }
  } 
}