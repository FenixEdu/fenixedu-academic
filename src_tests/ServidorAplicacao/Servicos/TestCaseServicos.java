package ServidorAplicacao.Servicos;

import java.util.Calendar;
import java.util.Set;

import junit.framework.TestCase;
import Dominio.IAula;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IPessoa;
import Dominio.ISala;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ITurnoAula;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDepartamentoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISalaPersistente;
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
import Util.TipoSala;


public class TestCaseServicos extends TestCase {
	protected ISuportePersistente _suportePersistente = null;
	protected IPessoaPersistente _pessoaPersistente = null;
	protected IAulaPersistente _aulaPersistente = null;
	protected ISalaPersistente _salaPersistente = null;
	protected ITurmaPersistente _turmaPersistente = null;
	protected ITurnoPersistente _turnoPersistente = null;
	protected ITurnoAlunoPersistente _turnoAlunoPersistente = null;
	protected ITurnoAulaPersistente _turnoAulaPersistente = null;
	protected ITurmaTurnoPersistente _turmaTurnoPersistente = null;
	protected IPersistentCurricularCourse _disciplinaCurricularPersistente = null;
	protected IDisciplinaExecucaoPersistente _disciplinaExecucaoPersistente = null;
	protected ICursoExecucaoPersistente _cursoExecucaoPersistente = null;
	protected ICursoPersistente _cursoPersistente = null;
	protected IPersistentStudent _alunoPersistente = null;
	protected IFrequentaPersistente _frequentaPersistente;
	protected IPlanoCurricularCursoPersistente _persistentDegreeCurricularPlan =	null;
	protected IStudentCurricularPlanPersistente _persistentStudentCurricularPlan = null;
	protected IDisciplinaDepartamentoPersistente _persistentDepartmentCourse = null;
	protected IDepartamentoPersistente _persistentDepartment = null;
	protected IPersistentExecutionPeriod persistentExecutionPeriod=null;
	protected IPersistentExecutionYear persistentExecutionYear=null;
	protected GestorServicos _gestor = null;
	protected IUserView _userView = null;
	protected IUserView _userView2 = null;
	protected IPessoa _pessoa1 = null;
	protected IPessoa _pessoa2 = null;
	protected Set _privilegios;
	protected TipoSala _tipoSala = null;
	protected ISala _sala1 = null;
	protected ISala _sala2 = null;
	protected ITurma _turma1 = null;
	protected ITurma _turma2 = null;
	protected ITurma _turma3 = null;
	protected ITurno _turno1 = null;
	protected ITurno _turno2 = null;
	protected ITurno _turno4 = null;
	protected ITurno _turno3 = null;
	protected IAula _aula1 = null;
	protected IAula _aula2 = null;	
	protected IAula _aula3 = null;
	protected IAula _aula4 = null;
	protected DiaSemana _diaSemana1 = null;
	protected DiaSemana _diaSemana2 = null;
	protected DiaSemana _diaSemana3 = null;	
	protected Calendar _inicio = null;
	protected Calendar _fim = null;
	protected Calendar _fim2 = null;	
	protected TipoAula _tipoAula = null;
	protected ITurnoAula _turnoAula1 = null;
	protected ITurnoAluno _turnoAluno1 = null;
	protected ITurmaTurno _turmaTurno1 = null;
	protected ICurricularCourse _disciplinaCurricular1 = null;
	protected ICurricularCourse _disciplinaCurricular2 = null;
	protected IDisciplinaExecucao _disciplinaExecucao1 = null;
	protected IDisciplinaExecucao _disciplinaExecucao2 = null;
	protected ICurso _curso1 = null;
	protected ICurso _curso2 = null;
	protected ICursoExecucao _cursoExecucao1 = null;
	protected ICursoExecucao _cursoExecucao2 = null;
	protected IStudent _aluno1 = null;

	private dbaccess dbAcessPoint = null;


	public TestCaseServicos(String testName) {
		super(testName);
	}
	
	protected void setUp() {
		
		try {
			dbAcessPoint = new dbaccess();
			dbAcessPoint.openConnection();
			dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
			dbAcessPoint.loadDataBase("etc/testDataSet.xml");
			dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			System.out.println("Setup failed: " + ex);
		}

//		
//		
//		// None of the variables defined here will be written to the database
//		// To remove later because of the DBUnit
//		
////		try {
////			_suportePersistente.iniciarTransaccao();
//			
//			_pessoa1 = new Pessoa();
//			_pessoa1.setNumeroDocumentoIdentificacao("0123456789");
//			_pessoa1.setCodigoFiscal("9876543210");
//			_pessoa1.setTipoDocumentoIdentificacao(
//				new TipoDocumentoIdentificacao(
//					TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
//			_pessoa1.setUsername("nome");
//			_pessoa1.setPassword("pass");
//			_pessoa1.setPrivilegios(null);
//			_privilegios = new HashSet();
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("AdicionarAula")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("AdicionarTurno")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ApagarAula")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ApagarSala")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ApagarTurma")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ApagarTurno")));
//			_privilegios.add(new Privilegio(_pessoa1, new String("CriarAula")));
//			_privilegios.add(new Privilegio(_pessoa1, new String("CriarSala")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("CriarTurma")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("CriarTurno")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("EditarAula")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("EditarSala")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("EditarTurma")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("EditarTurno")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("LerAlunosDeTurno")));
//			_privilegios.add(new Privilegio(_pessoa1, new String("LerAula")));
//			_privilegios.add(
//				new Privilegio(
//					_pessoa1,
//					new String("LerAulasDeDisciplinaExecucao")));
//			_privilegios.add(
//				new Privilegio(
//					_pessoa1,
//					new String("LerAulasDeDisciplinaExecucaoETipo")));
//			_privilegios.add(
//				new Privilegio(
//					_pessoa1,
//					new String("LerAulasDeSalaEmSemestre")));
//			_privilegios.add(new Privilegio(_pessoa1, new String("LerSala")));
//			_privilegios.add(new Privilegio(_pessoa1, new String("LerTurma")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("RemoverAula")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("RemoverTurno")));
//			_privilegios.add(new Privilegio(_pessoa1, new String("LerTurno")));
//			_privilegios.add(new Privilegio(_pessoa1, new String("LerSalas")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("LerLicenciatura")));
//			_privilegios.add(
//				new Privilegio(
//					_pessoa1,
//					new String("LerLicenciaturaExecucaoDeLicenciatura")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ReadExecutionDegreesByExecutionYear")));
//			_privilegios.add(new Privilegio(_pessoa1, new String("LerTurmas")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("LerTurnosDeTurma")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("LerAulasDeTurno")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("LerAulasDeTurma")));
//			_privilegios.add(
//				new Privilegio(
//					_pessoa1,
//					new String("LerTurnosDeDisciplinaExecucao")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ReadStudent")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ReadStudentByUsername")));
//			_privilegios.add(
//				new Privilegio(
//					_pessoa1,
//					new String("ReadShiftsByTypeFromExecutionCourse")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("StudentShiftEnrolment")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ReadShiftEnrolment")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ReadStudentLessons")));
//			_privilegios.add(
//				new Privilegio(_pessoa1, new String("ReadShiftLessons")));
//			_privilegios.add(
//				new Privilegio(
//					_pessoa1,
//					new String("LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular")));
//			_privilegios.add(new Privilegio(_pessoa1, new String("ReadOtherCoursesWithShifts")));
//
//			_pessoa1.setPrivilegios(_privilegios);
//			//_pessoaPersistente.lockWrite(_pessoa1);
//			//_pessoaPersistente.escreverPessoa(_pessoa1);
//			//_pessoa2 = new Pessoa("nome2", "pass2", null);
//			_pessoa2 = new Pessoa();
//			_pessoa2.setNumeroDocumentoIdentificacao("0321654987");
//			_pessoa2.setCodigoFiscal("7894561230");
//			_pessoa2.setTipoDocumentoIdentificacao(
//				new TipoDocumentoIdentificacao(
//					TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
//			_pessoa2.setUsername("nome2");
//			_pessoa2.setPassword("pass2");
//			_pessoa2.setPrivilegios(null);
//			
//			//_pessoaPersistente.escreverPessoa(_pessoa2);
//
//			IExecutionYear executionYear = new ExecutionYear("2002/03");
//			//persistentExecutionYear.lockWrite(executionYear);
//			
//			IExecutionPeriod executionPeriod = new ExecutionPeriod("2º Semestre",executionYear);
//			//persistentExecutionPeriod.lockWrite(executionPeriod);
//
//			_curso1 =
//				new Curso(
//					"LEIC",
//					"Informatica",
//					new TipoCurso(TipoCurso.LICENCIATURA));
//			//_cursoPersistente.lockWrite(_curso1);
//			_curso2 =
//				new Curso(
//					"LEGI",
//					"Gestao",
//					new TipoCurso(TipoCurso.LICENCIATURA));
//					
//			IPlanoCurricularCurso curricularPlan1 = new PlanoCurricularCurso("plano1",_curso1);
//			IPlanoCurricularCurso curricularPlan2 = new PlanoCurricularCurso("plano2",_curso2);
//					
//			_cursoExecucao1 = new CursoExecucao(executionYear, curricularPlan1);
////			_cursoExecucaoPersistente.lockWrite(_cursoExecucao1);
//			_cursoExecucao2 = new CursoExecucao(executionYear, curricularPlan2);
////			_suportePersistente.confirmarTransaccao();
//
//			IDisciplinaDepartamento departmentCourse = null;
//			IPlanoCurricularCurso degreeCurricularPlan = null;
//
////			_suportePersistente.iniciarTransaccao();
//			IDepartamento department = new Departamento("nome", "sigle");
//			departmentCourse =
//				new DisciplinaDepartamento("nome", "sigla", department);
////			_persistentDepartmentCourse.escreverDisciplinaDepartamento(
////				departmentCourse);
//			degreeCurricularPlan =
//				new PlanoCurricularCurso("nome",  _curso1);
////			_persistentDegreeCurricularPlan.escreverPlanoCurricular(
////				degreeCurricularPlan);
////			_suportePersistente.confirmarTransaccao();
//
////			_suportePersistente.iniciarTransaccao();
//			_disciplinaCurricular1 =
//				new CurricularCourse(
//					new Double(4.0),
//					new Double(10.0),
//					new Double(5.0),
//					new Double(3.0),
//					new Double(2.0),
//					new Integer(5),
//					new Integer(1),
//					"Trabalho Final Curso",
//					"TFC",
//					departmentCourse,
//					degreeCurricularPlan);
//			_disciplinaCurricular2 =
//				new CurricularCourse(
//					new Double(5.0),
//					new Double(11.0),
//					new Double(6.0),
//					new Double(4.0),
//					new Double(3.0),
//					new Integer(5),
//					new Integer(2),
//					"Trabalho Final Curso2",
//					"TFC2",
//					departmentCourse,
//					degreeCurricularPlan);
////			_disciplinaCurricularPersistente.writeCurricularCourse(
////				_disciplinaCurricular1);
////			_disciplinaCurricularPersistente.writeCurricularCourse(
////				_disciplinaCurricular2);
//
//			_disciplinaExecucao1 =
//			new DisciplinaExecucao(
//				"Trabalho Final Curso",
//				"TFC",
//				"programa1",
//				new Double(2.0),
//				new Double(1.0),
//				new Double(1.0),
//				new Double(1.0),executionPeriod);
//			List aCC1 = new ArrayList();
//			aCC1.add(_disciplinaCurricular1);
//			_disciplinaExecucao1.setAssociatedCurricularCourses(aCC1);
//			_disciplinaExecucao2 =
//			new DisciplinaExecucao(
//				"Trabalho Final Curso2",
//				"TFC2",
//				"programa10",
//				new Double(1.0),
//				new Double(1.0),
//				new Double(1.0),
//				new Double(1.0),executionPeriod);
//			List aCC2 = new ArrayList();
//			aCC2.add(_disciplinaCurricular1);
//			_disciplinaExecucao1.setAssociatedCurricularCourses(aCC2);
////			_disciplinaExecucaoPersistente.escreverDisciplinaExecucao(
////				_disciplinaExecucao1);
////			_disciplinaExecucaoPersistente.escreverDisciplinaExecucao(
////				_disciplinaExecucao2);
//
////			_suportePersistente.confirmarTransaccao();
////			_suportePersistente.iniciarTransaccao();
//			_tipoSala = new TipoSala(TipoSala.ANFITEATRO);
//			_sala1 =
//				new Sala(
//					new String("Ga1"),
//					new String("Pavilho Central"),
//					new Integer(1),
//					_tipoSala,
//					new Integer(100),
//					new Integer(50));
////			_salaPersistente.lockWrite(_sala1);
//			_turma1 =
//				new Turma("turma1",  new Integer(1), _cursoExecucao1,executionPeriod);
////			_turmaPersistente.lockWrite(_turma1);
//			_turma2 =
//				new Turma("turma2", new Integer(1), _cursoExecucao1,executionPeriod);
//			_turma3 =
//				new Turma("turma3",  new Integer(2), _cursoExecucao1,executionPeriod);
//
//			_turno1 =
//				new Turno(
//					"turno1",
//					new TipoAula(TipoAula.TEORICA),
//					new Integer(100),
//					_disciplinaExecucao1);
////			_turnoPersistente.lockWrite(_turno1);
//
//			_turno2 =
//				new Turno(
//					"turno2",
//					new TipoAula(TipoAula.TEORICA),
//					new Integer(100),
//					_disciplinaExecucao1);
//			_turno3 =
//							new Turno(
//								"turno3",
//								new TipoAula(TipoAula.TEORICA),
//								new Integer(100),
//								_disciplinaExecucao2);		
////			_turnoPersistente.lockWrite(_turno2);
//			_diaSemana1 = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
//			_inicio = Calendar.getInstance();
//			_inicio.set(Calendar.HOUR, 8);
//			_inicio.set(Calendar.MINUTE, 0);
//			_fim = Calendar.getInstance();
//			_fim.set(Calendar.HOUR, 9);
//			_fim.set(Calendar.MINUTE, 0);
//			_tipoAula = new TipoAula(TipoAula.TEORICA);
//			_aula1 =
//				new Aula(
//					_diaSemana1,
//					_inicio,
//					_fim,
//					_tipoAula,
//					_sala1,
//					_disciplinaExecucao1);
////			_aulaPersistente.lockWrite(_aula1);
//			_diaSemana2 = new DiaSemana(DiaSemana.TERCA_FEIRA);			
//			_aula2 =
//				new Aula(
//					_diaSemana2,
//					_inicio,
//					_fim,
//					_tipoAula,
//					_sala1,
//					_disciplinaExecucao2);
//			_fim2 = Calendar.getInstance();
//			_fim2.set(Calendar.HOUR, 10);
//			_fim2.set(Calendar.MINUTE, 30);
//			_diaSemana3 = new DiaSemana(DiaSemana.QUARTA_FEIRA);
//			_aula3 =
//				new Aula(
//					_diaSemana3,
//					_inicio,
//					_fim2,
//					_tipoAula,
//					_sala1,
//					_disciplinaExecucao2);
////			_aulaPersistente.lockWrite(_aula2);
////			_aulaPersistente.lockWrite(_aula3);			
//			_aluno1 =
//				new Student(
//					new Integer(600),
//					new Integer(567),
//					_pessoa1,
//					new TipoCurso(TipoCurso.LICENCIATURA));
//			_turnoAluno1 = new TurnoAluno(_turno1, _aluno1);
////			_turnoAlunoPersistente.lockWrite(_turnoAluno1);
//			_turnoAula1 = new TurnoAula(_turno1, _aula1);
////			_turnoAulaPersistente.lockWrite(_turnoAula1);
//			_turmaTurno1 = new TurmaTurno(_turma1, _turno1);
////			_turmaTurnoPersistente.lockWrite(_turmaTurno1);
//			_sala2 =
//				new Sala(
//					new String("Ga2"),
//					new String("Pavilho Central"),
//					new Integer(1),
//					_tipoSala,
//					new Integer(100),
//					new Integer(50));
////			_suportePersistente.confirmarTransaccao();
////		} catch (ExcepcaoPersistencia excepcao) {
////			fail("Exception when setUp");
////		}

		_gestor = GestorServicos.manager();
		String argsAutenticacao[] = { "user", "pass" };
		String argsAutenticacao2[] = { "4", "a" };
		try {
			_userView = (IUserView) _gestor.executar(null, "Autenticacao", argsAutenticacao);
		} catch (Exception ex) {
			System.out.println("Servico no executado: " + ex);
		}
		try {
			_userView2 = (IUserView) _gestor.executar(null, "Autenticacao", argsAutenticacao2);
		} catch (Exception ex) {
			System.out.println("Servico no executado: " + ex);
		}
	}

	protected void tearDown() {
//		try {
//			dbAcessPoint.openConnection();
//			dbAcessPoint.loadDataBase("etc/testBackup.xml");
//			dbAcessPoint.closeConnection();
//		} catch (Exception ex) {
//			System.out.println("Tear down failed: " +ex);
//		}
	}
}