package ServidorApresentacao.student;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoPerson;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.Frequenta;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IPessoa;
import Dominio.IPlanoCurricularCurso;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.Pessoa;
import Dominio.PlanoCurricularCurso;
import Dominio.Privilegio;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.Turno;
import Dominio.TurnoAluno;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDepartamentoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.TipoAula;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;


/**
 * @author tfc130
 *
 */
public class ChooseOtherCoursesActionTest extends MockStrutsTestCase {

  protected ISuportePersistente _suportePersistente = null;
  protected IPessoaPersistente _persistentPerson = null;
  protected IPersistentStudent _persistentStudent = null;
  protected ICursoExecucaoPersistente _cursoExecucaoPersistente = null;
  protected ICursoPersistente _cursoPersistente = null;
  protected IFrequentaPersistente _frequentaPersistente;
  protected ITurnoPersistente _turnoPersistente = null;
  protected ITurnoAlunoPersistente _turnoAlunoPersistente = null;
  protected ITurmaTurnoPersistente _turmaTurnoPersistente = null;
  protected IPlanoCurricularCursoPersistente _persistentDegreeCurricularPlan =	null;
  protected IStudentCurricularPlanPersistente _persistentStudentCurricularPlan = null;
  protected IDisciplinaDepartamentoPersistente _persistentDepartmentCourse = null;
  protected IDepartamentoPersistente _persistentDepartment = null;
  
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
    TestSuite suite = new TestSuite(ChooseOtherCoursesActionTest.class);
        
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
	privileges.add(new Privilegio(person, new String("ReadShiftEnrolment")));
	privileges.add(new Privilegio(person, new String("ReadOtherCoursesWithShifts")));
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
	IDisciplinaExecucao discipline5 =
		new DisciplinaExecucao(
			"Percepção",
			"PERC",
			"Program5",
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
	ITurno shift8 =
		new Turno(
			"turno_perc_teorico1",
			new TipoAula(TipoAula.TEORICA),
			new Integer(25),
			discipline5);
	ITurno shift9 =
		new Turno(
			"turno_perc_pratica1",
			new TipoAula(TipoAula.PRATICA),
			new Integer(25),
			discipline5);

	ITurnoAluno shiftStudent1 = new TurnoAluno(shift6, student);
	ITurnoAluno shiftStudent2 = new TurnoAluno(shift7, student);

	ITurma class1 = new Turma("turma1", new Integer(2), new Integer(1), _curso1);

	ITurmaTurno classShift1 = new TurmaTurno();
	classShift1.setTurma(class1);
	classShift1.setTurno(shift1);
	ITurmaTurno classShift2 = new TurmaTurno();
	classShift2.setTurma(class1);
	classShift2.setTurno(shift2);
	ITurmaTurno classShift3 = new TurmaTurno();
	classShift3.setTurma(class1);
	classShift3.setTurno(shift3);
	ITurmaTurno classShift4 = new TurmaTurno();
	classShift4.setTurma(class1);
	classShift4.setTurno(shift4);
	ITurmaTurno classShift5 = new TurmaTurno();
	classShift5.setTurma(class1);
	classShift5.setTurno(shift5);
	ITurmaTurno classShift6 = new TurmaTurno();
	classShift6.setTurma(class1);
	classShift6.setTurno(shift6);
	ITurmaTurno classShift7 = new TurmaTurno();
	classShift7.setTurma(class1);
	classShift7.setTurno(shift7);
	ITurmaTurno classShift8 = new TurmaTurno();
	classShift8.setTurma(class1);
	classShift8.setTurno(shift8);
	ITurmaTurno classShift9 = new TurmaTurno();
	classShift9.setTurma(class1);
	classShift9.setTurno(shift9);


	Calendar someDate = Calendar.getInstance();
	someDate.set(2002, Calendar.NOVEMBER, 17);

	IPlanoCurricularCurso pCC = new PlanoCurricularCurso();
	pCC.setNome("nome");
	pCC.setSigla("sigla");
	pCC.setCurso(_cursoExecucao1.getCurso());

	IStudentCurricularPlan sCP = new StudentCurricularPlan();
	sCP.setStudent(student);
	sCP.setStartDate(someDate.getTime());
	sCP.setCurrentState(new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
	sCP.setCourseCurricularPlan(pCC);

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
		_turnoPersistente.lockWrite(shift8);
		_turnoPersistente.lockWrite(shift9);
		_turmaTurnoPersistente.lockWrite(classShift1);
		_turmaTurnoPersistente.lockWrite(classShift2);
		_turmaTurnoPersistente.lockWrite(classShift3);
		_turmaTurnoPersistente.lockWrite(classShift4);
		_turmaTurnoPersistente.lockWrite(classShift5);
		_turmaTurnoPersistente.lockWrite(classShift6);
		_turmaTurnoPersistente.lockWrite(classShift7);
		_turmaTurnoPersistente.lockWrite(classShift8);
		_turmaTurnoPersistente.lockWrite(classShift9);		
		_turnoAlunoPersistente.lockWrite(shiftStudent1);
		_turnoAlunoPersistente.lockWrite(shiftStudent2);
		_persistentStudentCurricularPlan.lockWrite(sCP);
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
  
  public ChooseOtherCoursesActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulChooseOtherCourses() {      
    // define mapping de origem
    setRequestPathInfo("/student", "/chooseOtherCourse");

    // Preenche campos do formulário
    addRequestParameter("username","12345");
    addRequestParameter("password","pass");

    // coloca credenciais na sessão
    HashSet privileges = new HashSet();
	privileges.add("ReadOtherCoursesWithShifts");
    IUserView userView = new UserView("athirduser", privileges);
    getSession().setAttribute("UserView", userView);
    
    // colocar outras informações na sessão
	getSession().setAttribute("infoStudent", _infoStudent);
    
    // invoca acção
    actionPerform();

    // verifica reencaminhamento
    verifyForward("sucess");

    //verifica ausencia de erros
    verifyNoActionErrors();

    //verifica UserView guardado na sessão
    InfoShiftEnrolment iSE = (InfoShiftEnrolment) getSession().getAttribute("infoShiftEnrolment");
	assertNotNull("Student enrolment information not present", iSE);
	assertNull("Student enrolment information not present: courses without shifts", iSE.getInfoEnrolmentWithOutShift());
	assertNotNull("Student enrolment information not present: courses with shifts", iSE.getInfoEnrolmentWithShift());
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
	_turmaTurnoPersistente =_suportePersistente.getITurmaTurnoPersistente();
	_persistentDepartmentCourse = _suportePersistente.getIDisciplinaDepartamentoPersistente();
	_persistentDegreeCurricularPlan = _suportePersistente.getIPlanoCurricularCursoPersistente();
	_persistentDepartment = _suportePersistente.getIDepartamentoPersistente();
	_persistentStudentCurricularPlan = _suportePersistente.getIStudentCurricularPlanPersistente();
  }
    
  protected void cleanData() {
    try {
      _suportePersistente.iniciarTransaccao();
	  _persistentStudentCurricularPlan.deleteAll();
	  _persistentDegreeCurricularPlan.apagarTodosOsPlanosCurriculares();
	  _turmaTurnoPersistente.deleteAll();      
	  _turnoAlunoPersistente.deleteAll();	  
	  _frequentaPersistente.deleteAll();
	  _turnoPersistente.deleteAll();
	  _cursoExecucaoPersistente.deleteAll();
	  _cursoPersistente.deleteAll();
	  _persistentDepartmentCourse.apagarTodasAsDisciplinasDepartamento();
	  _persistentDepartment.apagarTodosOsDepartamentos();	  
	  _persistentStudent.deleteAll();
      _persistentPerson.apagarTodasAsPessoas();
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when cleaning data");
    }
  } 
}