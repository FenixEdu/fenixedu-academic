package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import Dominio.Aula;
import Dominio.Curso;
import Dominio.DisciplinaExecucao;
import Dominio.Enrolment;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.Frequenta;
import Dominio.IAula;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDepartamento;
import Dominio.IDisciplinaDepartamento;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IItem;
import Dominio.IPessoa;
import Dominio.ISala;
import Dominio.ISeccao;
import Dominio.ISitio;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ITurnoAula;
import Dominio.Item;
import Dominio.Pessoa;
import Dominio.Sala;
import Dominio.Seccao;
import Dominio.Sitio;
import Dominio.Student;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.Turno;
import Dominio.TurnoAluno;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDepartamentoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IItemPersistente;
import ServidorPersistente.IPersistentCandidateSituation;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISeccaoPersistente;
import ServidorPersistente.ISitioPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import Tools.dbaccess;
import Util.DiaSemana;
import Util.TipoAula;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;
import Util.TipoSala;

public class TestCaseOJB extends TestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected ISitioPersistente _sitioPersistente = null;
  protected ISeccaoPersistente _seccaoPersistente = null;
  protected IItemPersistente _itemPersistente = null;
  protected IPessoaPersistente _pessoaPersistente = null;

  protected ICursoExecucaoPersistente cursoExecucaoPersistente = null;
  protected ICursoPersistente cursoPersistente = null;

  protected IDisciplinaExecucaoPersistente _disciplinaExecucaoPersistente = null;

  protected IAulaPersistente _aulaPersistente = null;
  protected ISalaPersistente _salaPersistente = null;
  protected ITurmaPersistente _turmaPersistente = null;
  protected ITurnoPersistente _turnoPersistente = null;
  protected IFrequentaPersistente _frequentaPersistente = null;
  protected IPersistentEnrolment _inscricaoPersistente = null;  
  protected ITurmaTurnoPersistente _turmaTurnoPersistente = null;
  protected ITurnoAlunoPersistente _turnoAlunoPersistente = null;  
  protected ITurnoAulaPersistente _turnoAulaPersistente = null;

  protected IPersistentStudent persistentStudent = null;

  protected IDepartamentoPersistente departamentoPersistente = null;
  protected IDisciplinaDepartamentoPersistente disciplinaDepartamentoPersistente = null;

  protected IPlanoCurricularCursoPersistente planoCurricularCursoPersistente = null;
  protected IStudentCurricularPlanPersistente studentCurricularPlanPersistente = null; 
  protected IPersistentCountry persistentCountry = null;
  protected IPersistentCurricularCourse persistantCurricularCourse = null;

  protected IPersistentMasterDegreeCandidate persistentMasterDegreeCandidate = null;
  protected IPersistentCandidateSituation persistentCandidateSituation = null;
  protected IPersistentExecutionYear persistentExecutionYear = null;
  protected IPersistentExecutionPeriod persistentExecutionPeriod = null;

  private dbaccess _dbAcessPoint = null;


  protected ISitio _sitio1 = null;
  protected ISitio _sitio2 = null;
  protected ISeccao _seccaoSitio1Topo1 = null;
  protected ISeccao _seccaoSitio1Topo2 = null;
  protected ISeccao _seccaoSitio2Topo1 = null;
  protected ISeccao _seccaoSitio1Topo1Sub1 = null;
  protected ISeccao _seccaoSitio1Topo1Sub2 = null;
  protected ISeccao _seccaoSitio1Topo1Sub1Sub1 = null;
  protected IItem _item1Sitio1Topo1 = null;
  protected IItem _item2Sitio1Topo1 = null;
  protected IItem _item1Sitio1Topo1Sub1Sub1 = null;
  protected IItem _item2Sitio1Topo1Sub1Sub1 = null;
  protected IItem _item3Sitio1Topo1Sub1Sub1 = null;
  protected IItem _item4Sitio1Topo1Sub1Sub1 = null;
  protected IPessoa _pessoa1 = null;
  protected IPessoa _pessoa2 = null;
  protected IPessoa _pessoa3 = null;
  protected IPessoa _pessoa4 = null;

  protected ICurricularCourse _curricularCourse1 = null;
  protected ICurricularCourse _curricularCourse2 = null;
  protected ICurricularCourse _curricularCourse3 = null;
  
  protected IDisciplinaDepartamento disciplinaDepartamento1 = null;
  protected IDisciplinaDepartamento disciplinaDepartamento2 = null;
  
  protected IDisciplinaExecucao _disciplinaExecucao1 = null;
  protected IDisciplinaExecucao _disciplinaExecucao2 = null;
  protected IDisciplinaExecucao _disciplinaExecucao3 = null;

  protected IAula _aula1 = null;
  protected IAula _aula2 = null;
  protected IAula _aula3 = null;
  protected IAula _aula4 = null;
  protected IAula _aula5 = null;
  protected ISala _sala1 = null;
  protected ISala _sala2 = null;
  protected ISala _sala3 = null;
  protected ITurma _turma1 = null;
  protected ITurma _turma2 = null;
  protected ITurno _turno1 = null;
  protected ITurno _turno2 = null;

  protected DiaSemana _diaSemana1 = null;
  protected DiaSemana _diaSemana2 = null;
  protected Calendar _inicio = null;
  protected Calendar _fim = null;
  protected TipoSala _tipoSala = null;
  protected TipoAula _tipoAula = null;

  protected IFrequenta _frequenta1 = null;
  protected IEnrolment _inscricao1 = null;

  protected ITurmaTurno _turmaTurno1 = null;
  protected ITurmaTurno _turmaTurno2 = null;

  protected IDepartamento dept = null;

  protected ITurno _turno453 = null;
  protected ITurno _turno454 = null;
  protected ITurno _turno455 = null;
  protected ITurno _turno456 = null;
  protected ITurno _turno457 = null;
  protected ITurno _turno458 = null;
  protected ITurma _turma413 = null;
  protected ITurma _turma414 = null;
  protected ITurmaTurno _turmaTurno413_453 = null;
  protected ITurmaTurno _turmaTurno413_455 = null;
  protected ITurmaTurno _turmaTurno413_456 = null;
  protected ITurmaTurno _turmaTurno413_457 = null;
  protected ITurmaTurno _turmaTurno414_454 = null;
  protected ITurmaTurno _turmaTurno414_455 = null;
  protected ITurmaTurno _turmaTurno414_458 = null;

  protected ITurnoAluno _turnoAluno1 = null;
  protected ITurnoAluno _turnoAluno2 = null;
  protected ITurno _turno3 = null;
  protected ITurno _turno4 = null;
  protected ITurno _turno5 = null;
  protected ITurno _turnoInexistente = null;

  protected ITurnoAula _turnoAula1 = null;
  protected ITurnoAula _turnoAula2 = null;
  protected ITurnoAula _turnoAula3 = null;
  protected ITurnoAula _turnoAula4 = null;
  protected ITurnoAula _turnoAula5 = null;
  protected ITurnoAula _turnoAula6 = null;
  protected ITurnoAula _turnoAula7 = null;

  protected ICurso curso1 = null;
  protected ICurso curso2 = null;

  protected ICursoExecucao cursoExecucao1 = null;
  protected ICursoExecucao cursoExecucao2 = null;

  protected IStudent alunojaexistente = null;
  protected IStudent alunoapagado = null;
  protected IStudent _aluno1 = null;
  protected IStudent _aluno2 = null;

  protected TipoCurso licenciatura = new TipoCurso(TipoCurso.LICENCIATURA);
  protected IExecutionPeriod executionPeriod = null;
  protected IExecutionYear executionYear = null;
    
  public TestCaseOJB(String testName) {
    super(testName);
  }
  /*
    public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
    TestSuite suite = new TestSuite(TestCaseOJB.class);
        
    return suite;
    }*/

  protected void setUp() {
  	// The following code backs up the contents of the database
  	// and loads the database with the data set required to run
  	// the test cases.
  	try {
  		_dbAcessPoint = new dbaccess();
  		_dbAcessPoint.openConnection();
  		_dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
  		_dbAcessPoint.loadDataBase("etc/testDataSet.xml");
  		_dbAcessPoint.closeConnection();
  	} catch (Exception ex) {
  		System.out.println("Setup failed: " + ex);
  	}
  	
  	_sitio1 = new Sitio("EP", 4, 1, "LEIC", "DEI");
  	_sitio2 = new Sitio("PO", 2, 1, "LEIC", "DEI");
  	_seccaoSitio1Topo1 = new Seccao("Topo1",0,_sitio1,null);
  	_seccaoSitio1Topo2 = new Seccao("Topo2",1,_sitio1,null);
  	_seccaoSitio2Topo1 = new Seccao("Topo1",0,_sitio2,null);
  	_seccaoSitio1Topo1Sub1 = new Seccao("Sub1",0,_sitio1,_seccaoSitio1Topo1);
  	_seccaoSitio1Topo1Sub2 = new Seccao("Sub2",1,_sitio1,_seccaoSitio1Topo1);
  	_seccaoSitio1Topo1Sub1Sub1 = new Seccao("Sub1",0,_sitio1,_seccaoSitio1Topo1Sub1);
  	_item1Sitio1Topo1 = new Item("1",_seccaoSitio1Topo1,0,"sou a primeira",true);
  	_item2Sitio1Topo1 = new Item("2",_seccaoSitio1Topo1,1,"sou a segunda",false);
  	_item1Sitio1Topo1Sub1Sub1 = new Item("1",_seccaoSitio1Topo1Sub1Sub1,0,"sou outra vez a primeira", false);
  	_item2Sitio1Topo1Sub1Sub1 = new Item("2",_seccaoSitio1Topo1Sub1Sub1,1,"sou outra vez a primeira", false);
  	_item3Sitio1Topo1Sub1Sub1 = new Item("3",_seccaoSitio1Topo1Sub1Sub1,2,"sou outra vez a primeira", false);
  	_item4Sitio1Topo1Sub1Sub1 = new Item("4",_seccaoSitio1Topo1Sub1Sub1,3,"sou outra vez a primeira", false);
  	_pessoa1 = new Pessoa();
  	_pessoa1.setNumeroDocumentoIdentificacao("123456789");
  	_pessoa1.setCodigoFiscal("9876543210");
  	_pessoa1.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
  				 TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
  	_pessoa1.setUsername("jorge");
  	_pessoa1.setPassword("a");
  	_pessoa1.setPrivilegios(null);

  	curso1 = new Curso("LEIC", "Licenciatura de Engenharia Informatica e de Computadores", new TipoCurso(TipoCurso.LICENCIATURA));
  	curso2 = new Curso("LEEC", "Licenciatura de Engenharia Electrotecnica e de Computadores", new TipoCurso(TipoCurso.LICENCIATURA));

	

//  	cursoExecucao1 = new CursoExecucao("2002/03", curso1);
//  	cursoExecucao2 = new CursoExecucao("2003/04", curso2);
//
  	executionYear = new ExecutionYear("2000/2001");
	executionPeriod = new ExecutionPeriod("1 semestre", executionYear);

  	_disciplinaExecucao1 = new DisciplinaExecucao("Trabalho Final de Curso I", "TFCI", "programa1", new Double(0),new Double(0),new Double(0),new Double(0), executionPeriod );
  	_disciplinaExecucao2 = new DisciplinaExecucao("Trabalho Final de Curso II", "TFCII", "programa10", new Double(0),new Double(0),new Double(0),new Double(0), executionPeriod);
  	_disciplinaExecucao3 = new DisciplinaExecucao("Introducao a Programacao", "IP", "programa10", new Double(0),new Double(0),new Double(0),new Double(0), executionPeriod);

	
	List list = new LinkedList();
	list.add(_curricularCourse1);
	_disciplinaExecucao1.setAssociatedCurricularCourses(list);

	list = new LinkedList();
	list.add(_disciplinaExecucao1);
	_disciplinaExecucao1.setAssociatedCurricularCourses(list);

	list = new LinkedList();
	list.add(_curricularCourse2);
	_disciplinaExecucao1.setAssociatedCurricularCourses(list);

	list = new LinkedList();
	list.add(_disciplinaExecucao2);
	_disciplinaExecucao1.setAssociatedCurricularCourses(list);


	list = new LinkedList();
	list.add(_curricularCourse3);
	_disciplinaExecucao1.setAssociatedCurricularCourses(list);

	list = new LinkedList();
	list.add(_disciplinaExecucao3);
	_disciplinaExecucao1.setAssociatedCurricularCourses(list);

  	_tipoSala = new TipoSala(TipoSala.ANFITEATRO);

  	_sala1 = new Sala("Ga1", "Pavilhao Central", new Integer(0), _tipoSala, new Integer(100), new Integer(50));
  	_sala2 = new Sala("Ga2", "Pavilhao Central", new Integer(0), _tipoSala, new Integer(100), new Integer(50));
  	_sala3 = new Sala("Ga3", "Pavilhao Central", new Integer(0), _tipoSala, new Integer(100), new Integer(50));

  	_diaSemana1 = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
  	_diaSemana2 = new DiaSemana(DiaSemana.SEXTA_FEIRA);
  	_inicio = Calendar.getInstance();
  	_inicio.set(Calendar.HOUR_OF_DAY, 8);
  	_inicio.set(Calendar.MINUTE, 00);
  	_inicio.set(Calendar.SECOND, 00);
  	_fim = Calendar.getInstance();
  	_fim.set(Calendar.HOUR_OF_DAY, 9);
  	_fim.set(Calendar.MINUTE, 30);
  	_fim.set(Calendar.SECOND, 00);
  	_tipoAula = new TipoAula(TipoAula.TEORICA);

  	_aula1 = new Aula(_diaSemana1, _inicio, _fim, _tipoAula, _sala1, _disciplinaExecucao1);
  	_aula2 = new Aula(_diaSemana2, _inicio, _fim, _tipoAula, _sala1, _disciplinaExecucao1);
  	_aula3 = new Aula(new DiaSemana(DiaSemana.QUARTA_FEIRA), _inicio, _fim, _tipoAula, _sala1, _disciplinaExecucao1);
  	_aula4 = new Aula(new DiaSemana(DiaSemana.QUINTA_FEIRA), _inicio, _fim, _tipoAula, _sala1, _disciplinaExecucao1);
  	_aula5 = new Aula(new DiaSemana(DiaSemana.SABADO), _inicio, _fim, _tipoAula, _sala1, _disciplinaExecucao1);

  	_turma1 = new Turma("10501", new Integer(1),new Integer(1), curso1);
  	_turma2 = new Turma("14501", new Integer(1),new Integer(1), curso2);

  	_turno1 = new Turno("turno1", new TipoAula(TipoAula.TEORICA), new Integer(100), _disciplinaExecucao1);
  	_turno2 = new Turno("turno2", new TipoAula(TipoAula.TEORICA), new Integer(100), _disciplinaExecucao1);

  	_turmaTurno1 = new TurmaTurno(_turma1, _turno1);
  	_turmaTurno2 = new TurmaTurno(_turma2, _turno2);

  	_turno453 = new Turno("turno453", new TipoAula(TipoAula.TEORICA), new Integer(100), _disciplinaExecucao1);
  	_turno454 = new Turno("turno454", new TipoAula(TipoAula.TEORICA), new Integer(100), _disciplinaExecucao1);
  	_turno455 = new Turno("turno455", new TipoAula(TipoAula.PRATICA), new Integer(100), _disciplinaExecucao1);
  	_turno456 = new Turno("turno456", new TipoAula(TipoAula.PRATICA), new Integer(100), _disciplinaExecucao1);
  	_turno457 = new Turno("turno457", new TipoAula(TipoAula.PRATICA), new Integer(100), _disciplinaExecucao1);
  	_turno458 = new Turno("turno458", new TipoAula(TipoAula.PRATICA), new Integer(100), _disciplinaExecucao1);
  	_turma413 = new Turma("turma413", new Integer(1),new Integer(1), curso1);
  	_turma414 = new Turma("turma414", new Integer(1),new Integer(1), curso1);

  	_turmaTurno413_453 = new TurmaTurno(_turma413, _turno453);
  	_turmaTurno414_454 = new TurmaTurno(_turma414, _turno454);
  	_turmaTurno413_455 = new TurmaTurno(_turma413, _turno455);
  	_turmaTurno413_456 = new TurmaTurno(_turma413, _turno456);
  	_turmaTurno413_457 = new TurmaTurno(_turma413, _turno457);
  	_turmaTurno414_455 = new TurmaTurno(_turma414, _turno455);
  	_turmaTurno414_458 = new TurmaTurno(_turma414, _turno458);

  	_turnoInexistente = new Turno("turnoInexistente", new TipoAula(TipoAula.PRATICA), new Integer(100), _disciplinaExecucao1);
  	_turno3 = new Turno("turno3", new TipoAula(TipoAula.PRATICA), new Integer(100), _disciplinaExecucao1);
  	_turno4 = new Turno("turno4", new TipoAula(TipoAula.PRATICA), new Integer(100), _disciplinaExecucao1);
  	_turno5 = new Turno("turno5", new TipoAula(TipoAula.PRATICA), new Integer(100), _disciplinaExecucao1);

  	alunojaexistente = new Student(new Integer(600),new Integer(567),_pessoa1, licenciatura);
  	//persistentStudent.lockWrite(alunojaexistente);
  	alunoapagado = new Student(new Integer(700),new Integer(100),_pessoa2, licenciatura);
  	//persistentStudent.lockWrite(alunoapagado);

  	_aluno1 = new Student(new Integer(800),new Integer(567),_pessoa3, licenciatura);
  	//persistentStudent.lockWrite(_aluno1);
  	_aluno2 = new Student(new Integer(900),new Integer(567),_pessoa4, licenciatura);
  	//persistentStudent.lockWrite(_aluno2);
  	_turnoAluno1 = new TurnoAluno(_turno3, _aluno1);
  	//_turnoAlunoPersistente.lockWrite(_turnoAluno1);
  	_turnoAluno2 = new TurnoAluno(_turno4, _aluno2);
  	//_turnoAlunoPersistente.lockWrite(_turnoAluno2);

  	_turnoAula1 = new TurnoAula(_turno3, _aula1);
  	_turnoAula2 = new TurnoAula(_turno4, _aula1);
  	_turnoAula3 = new TurnoAula(_turno455, _aula3);
  	_turnoAula4 = new TurnoAula(_turno455, _aula4);
  	_turnoAula5 = new TurnoAula(_turno456, _aula3);
  	_turnoAula6 = new TurnoAula(_turno454, _aula4);
  	_turnoAula7 = new TurnoAula(_turno454, _aula5);

  	_frequenta1 = new Frequenta();
  	_frequenta1.setAluno(_aluno1);
  	_frequenta1.setDisciplinaExecucao(_disciplinaExecucao1);

  	_inscricao1 = new Enrolment();
  	//_inscricao1.setPlanoCurricularAluno(_planoCurricular1);
  	_inscricao1.setCurricularCourse(_curricularCourse1);

    ligarSuportePersistente();
  }

  protected void tearDown() {
  	try {
  		_dbAcessPoint.openConnection();
  		_dbAcessPoint.loadDataBase("etc/testBackup.xml");
  		//_dbAcessPoint.loadDataBase("etc/testDataSet.xml");
  		_dbAcessPoint.closeConnection();
  	} catch (Exception ex) {
  		System.out.println("Tear down failed: " +ex);
  	}
  }
            
  protected void ligarSuportePersistente() {
    try {
      _suportePersistente = SuportePersistenteOJB.getInstance();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when opening database");
    }
    _sitioPersistente = _suportePersistente.getISitioPersistente();
    _seccaoPersistente = _suportePersistente.getISeccaoPersistente();
    _itemPersistente = _suportePersistente.getIItemPersistente();
    _pessoaPersistente = _suportePersistente.getIPessoaPersistente();

    _aulaPersistente = _suportePersistente.getIAulaPersistente();
    _salaPersistente = _suportePersistente.getISalaPersistente();
    _turmaPersistente = _suportePersistente.getITurmaPersistente();
    _turnoPersistente = _suportePersistente.getITurnoPersistente();
    
    _frequentaPersistente = _suportePersistente.getIFrequentaPersistente();
    _inscricaoPersistente = _suportePersistente.getIInscricaoPersistente();    
    _turmaTurnoPersistente = _suportePersistente.getITurmaTurnoPersistente();
    _turnoAlunoPersistente = _suportePersistente.getITurnoAlunoPersistente();    
    _turnoAulaPersistente = _suportePersistente.getITurnoAulaPersistente();        

    cursoExecucaoPersistente = _suportePersistente.getICursoExecucaoPersistente();
    cursoPersistente = _suportePersistente.getICursoPersistente();

    _disciplinaExecucaoPersistente = _suportePersistente.getIDisciplinaExecucaoPersistente();
    
    persistentStudent = _suportePersistente.getIPersistentStudent();
    
    departamentoPersistente = _suportePersistente.getIDepartamentoPersistente();
    disciplinaDepartamentoPersistente = _suportePersistente.getIDisciplinaDepartamentoPersistente();
    planoCurricularCursoPersistente = _suportePersistente.getIPlanoCurricularCursoPersistente();
    studentCurricularPlanPersistente = _suportePersistente.getIStudentCurricularPlanPersistente();
    persistentCountry = _suportePersistente.getIPersistentCountry();
    persistantCurricularCourse = _suportePersistente.getIPersistentCurricularCourse();
    persistentMasterDegreeCandidate = _suportePersistente.getIPersistentMasterDegreeCandidate();
    persistentCandidateSituation = _suportePersistente.getIPersistentCandidateSituation();
	persistentExecutionYear = _suportePersistente.getIPersistentExecutionYear();
	persistentExecutionPeriod = _suportePersistente.getIPersistentExecutionPeriod();
 
  }
    
}
