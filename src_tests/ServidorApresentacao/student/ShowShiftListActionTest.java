package ServidorApresentacao.student;

import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoPerson;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import Dominio.IPessoa;
import Dominio.IStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *
 */
public class ShowShiftListActionTest extends MockStrutsTestCase {

	private final String serviceName =
		new String("ReadShiftsByTypeFromExecutionCourse");

	protected ISuportePersistente _suportePersistente = null;
	protected IPessoaPersistente _persistentPerson = null;
	protected IPersistentStudent _persistentStudent = null;
	protected ICursoExecucaoPersistente _cursoExecucaoPersistente = null;
	protected ICursoPersistente _cursoPersistente = null;
	protected IFrequentaPersistente _frequentaPersistente;
	protected ITurnoPersistente _turnoPersistente = null;
	protected ITurnoAlunoPersistente _turnoAlunoPersistente = null;

	protected IPessoa _person1 = null;
	protected IPessoa _person2 = null;
	protected IPessoa _person3 = null;
	protected IStudent _student1 = null;
	protected IStudent _student3 = null;
	protected InfoPerson _infoPerson = null;
	protected InfoStudent _infoStudent = null;
	protected InfoShiftEnrolment _iSE = null;

	/**
	 * Constructor for ShowShiftListActionTest.
	 * @param arg0
	 */
	public ShowShiftListActionTest(String arg0) {
		super(arg0);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ShowShiftListActionTest.class);
		return suite;
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuração a utilizar
		setServletConfigFile("/WEB-INF/tests/web-student.xml");

		ligarSuportePersistente();
		cleanData();

//		ICurso _curso1 =
//			new Curso(
//				"LEIC",
//				"Informatica",
//				new TipoCurso(TipoCurso.LICENCIATURA));
//
//		ICursoExecucao _cursoExecucao1 = null; //new CursoExecucao("2002/03", _curso1);
//
//		IPessoa person = new Pessoa();
//		Set privileges = new HashSet();
//		person.setNome("Marvin");
//		person.setUsername("45498");
//		person.setNumeroDocumentoIdentificacao("010101010101");
//		person.setCodigoFiscal("010101010101");
//		person.setTipoDocumentoIdentificacao(
//			new TipoDocumentoIdentificacao(
//				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
//		privileges.add(new Privilegio(person, serviceName));
//		privileges.add(
//			new Privilegio(person, new String("ReadShiftEnrolment")));
//		privileges.add(new Privilegio(person, new String("LerAlunosDeTurno")));
//
//		person.setPrivilegios(privileges);
//		IStudent student =
//			new Student(
//				new Integer(45498),
//				new Integer(567),
//				person,
//				new TipoCurso(TipoCurso.LICENCIATURA));
//
//		IExecutionYear executionYear = new ExecutionYear();
//		executionYear.setYear("2002/2003");
//		IExecutionPeriod executionPeriod = new ExecutionPeriod();
//		executionPeriod.setExecutionYear(executionYear);
//		executionPeriod.setName("2º Semestre");
//
//
//		IDisciplinaExecucao discipline1 =
//			new DisciplinaExecucao(
//				"Trabalho Final de Curso I",
//				"TFCI",
//				"Program1",
//
//				new Double(1),
//				new Double(1),
//				new Double(1),
//				new Double(1),
//				executionPeriod);
//		IDisciplinaExecucao discipline2 =
//			new DisciplinaExecucao(
//				"Trabalho Final de Curso II",
//				"TFCII",
//				"Program2",
//
//				new Double(1),
//				new Double(1),
//				new Double(1),
//				new Double(1),
//				executionPeriod);
//		IDisciplinaExecucao discipline3 =
//			new DisciplinaExecucao(
//				"Engenharia da Programacao",
//				"EP",
//				"Program3",
//				new Double(1),
//				new Double(1),
//				new Double(1),
//				new Double(1),
//				executionPeriod);
//		IDisciplinaExecucao discipline4 =
//			new DisciplinaExecucao(
//				"Aprendizagem",
//				"APR",
//				"Program4",
//				new Double(1),
//				new Double(1),
//				new Double(1),
//				new Double(1),
//				executionPeriod);
//		IFrequenta attend1 = new Frequenta(student, discipline1);
//		IFrequenta attend2 = new Frequenta(student, discipline2);
//		IFrequenta attend3 = new Frequenta(student, discipline3);
//		IFrequenta attend4 = new Frequenta(student, discipline4);
//
//		ITurno shift1 =
//			new Turno(
//				"turno_ep_teorico1",
//				new TipoAula(TipoAula.TEORICA),
//				new Integer(100),
//				discipline3);
//
//		ITurno shift2 =
//			new Turno(
//				"turno_ep_laboratorio1",
//				new TipoAula(TipoAula.LABORATORIAL),
//				new Integer(50),
//				discipline3);
//		ITurno shift3 =
//			new Turno(
//				"turno_ep_laboratorio2",
//				new TipoAula(TipoAula.LABORATORIAL),
//				new Integer(50),
//				discipline3);
//		ITurno shift4 =
//			new Turno(
//				"turno_ep_pratica1",
//				new TipoAula(TipoAula.PRATICA),
//				new Integer(50),
//				discipline3);
//		ITurno shift5 =
//			new Turno(
//				"turno_ep_pratica2",
//				new TipoAula(TipoAula.PRATICA),
//				new Integer(50),
//				discipline4);
//		ITurno shift6 =
//			new Turno(
//				"turno_apr_teorico1",
//				new TipoAula(TipoAula.TEORICA),
//				new Integer(25),
//				discipline4);
//		ITurno shift7 =
//			new Turno(
//				"turno_apr_pratica1",
//				new TipoAula(TipoAula.PRATICA),
//				new Integer(25),
//				discipline4);
//
//		TypeLessonAndInfoShift tLAndShift1 =
//			new TypeLessonAndInfoShift(
//				shift1.getTipo(),
//				new InfoShift(
//					shift1.getNome(),
//					shift1.getTipo(),
//					shift1.getLotacao(),
//					new InfoExecutionCourse(
//						shift1.getDisciplinaExecucao().getNome(),
//						shift1.getDisciplinaExecucao().getSigla(),
//						shift1.getDisciplinaExecucao().getPrograma(),
//						new InfoExecutionDegree(
//							shift1
//								.getDisciplinaExecucao()
//								.getLicenciaturaExecucao()
//								.getAnoLectivo(),
//							new InfoDegree(
//								shift1
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getSigla(),
//								shift1
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getNome())),
//							shift1.getDisciplinaExecucao().getTheoreticalHours(),
//							shift1.getDisciplinaExecucao().getPraticalHours(),
//							shift1.getDisciplinaExecucao().getTheoPratHours(),
//							shift1.getDisciplinaExecucao().getLabHours())));
//		TypeLessonAndInfoShift tLAndShift2 =
//			new TypeLessonAndInfoShift(
//				shift2.getTipo(),
//				new InfoShift(
//					shift2.getNome(),
//					shift2.getTipo(),
//					shift2.getLotacao(),
//					new InfoExecutionCourse(
//						shift2.getDisciplinaExecucao().getNome(),
//						shift2.getDisciplinaExecucao().getSigla(),
//						shift2.getDisciplinaExecucao().getPrograma(),
//						new InfoExecutionDegree(
//							shift2
//								.getDisciplinaExecucao()
//								.getLicenciaturaExecucao()
//								.getAnoLectivo(),
//							new InfoDegree(
//								shift2
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getSigla(),
//								shift2
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getNome())),
//								shift2.getDisciplinaExecucao().getTheoreticalHours(),
//								shift2.getDisciplinaExecucao().getPraticalHours(),
//								shift2.getDisciplinaExecucao().getTheoPratHours(),
//								shift2.getDisciplinaExecucao().getLabHours())));
//		TypeLessonAndInfoShift tLAndShift3 =
//			new TypeLessonAndInfoShift(
//				shift3.getTipo(),
//				new InfoShift(
//					shift3.getNome(),
//					shift3.getTipo(),
//					shift3.getLotacao(),
//					new InfoExecutionCourse(
//						shift3.getDisciplinaExecucao().getNome(),
//						shift3.getDisciplinaExecucao().getSigla(),
//						shift3.getDisciplinaExecucao().getPrograma(),
//						new InfoExecutionDegree(
//							shift3
//								.getDisciplinaExecucao()
//								.getLicenciaturaExecucao()
//								.getAnoLectivo(),
//							new InfoDegree(
//								shift3
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getSigla(),
//								shift3
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getNome())),
//								shift3.getDisciplinaExecucao().getTheoreticalHours(),
//								shift3.getDisciplinaExecucao().getPraticalHours(),
//								shift3.getDisciplinaExecucao().getTheoPratHours(),
//								shift3.getDisciplinaExecucao().getLabHours())));
//		TypeLessonAndInfoShift tLAndShift4 =
//			new TypeLessonAndInfoShift(
//				shift4.getTipo(),
//				new InfoShift(
//					shift4.getNome(),
//					shift4.getTipo(),
//					shift4.getLotacao(),
//					new InfoExecutionCourse(
//						shift4.getDisciplinaExecucao().getNome(),
//						shift4.getDisciplinaExecucao().getSigla(),
//						shift4.getDisciplinaExecucao().getPrograma(),
//						new InfoExecutionDegree(
//							shift4
//								.getDisciplinaExecucao()
//								.getLicenciaturaExecucao()
//								.getAnoLectivo(),
//							new InfoDegree(
//								shift4
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getSigla(),
//								shift4
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getNome())),
//								shift4.getDisciplinaExecucao().getTheoreticalHours(),
//								shift4.getDisciplinaExecucao().getPraticalHours(),
//								shift4.getDisciplinaExecucao().getTheoPratHours(),
//								shift4.getDisciplinaExecucao().getLabHours())));		TypeLessonAndInfoShift tLAndShift5 =
//			new TypeLessonAndInfoShift(
//				shift5.getTipo(),
//				new InfoShift(
//					shift5.getNome(),
//					shift5.getTipo(),
//					shift5.getLotacao(),
//					new InfoExecutionCourse(
//						shift5.getDisciplinaExecucao().getNome(),
//						shift5.getDisciplinaExecucao().getSigla(),
//						shift5.getDisciplinaExecucao().getPrograma(),
//						new InfoExecutionDegree(
//							shift5
//								.getDisciplinaExecucao()
//								.getLicenciaturaExecucao()
//								.getAnoLectivo(),
//							new InfoDegree(
//								shift5
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getSigla(),
//								shift5
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getNome())),
//								shift5.getDisciplinaExecucao().getTheoreticalHours(),
//								shift5.getDisciplinaExecucao().getPraticalHours(),
//								shift5.getDisciplinaExecucao().getTheoPratHours(),
//								shift5.getDisciplinaExecucao().getLabHours())));
//		TypeLessonAndInfoShift tLAndShift6 =
//			new TypeLessonAndInfoShift(
//				shift6.getTipo(),
//				new InfoShift(
//					shift6.getNome(),
//					shift6.getTipo(),
//					shift6.getLotacao(),
//					new InfoExecutionCourse(
//						shift6.getDisciplinaExecucao().getNome(),
//						shift6.getDisciplinaExecucao().getSigla(),
//						shift6.getDisciplinaExecucao().getPrograma(),
//						new InfoExecutionDegree(
//							shift6
//								.getDisciplinaExecucao()
//								.getLicenciaturaExecucao()
//								.getAnoLectivo(),
//							new InfoDegree(
//								shift6
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getSigla(),
//								shift6
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getNome())),
//								shift6.getDisciplinaExecucao().getTheoreticalHours(),
//								shift6.getDisciplinaExecucao().getPraticalHours(),
//								shift6.getDisciplinaExecucao().getTheoPratHours(),
//								shift6.getDisciplinaExecucao().getLabHours())));
//		
//		TypeLessonAndInfoShift tLAndShift7 =
//			new TypeLessonAndInfoShift(
//				shift7.getTipo(),
//				new InfoShift(
//					shift7.getNome(),
//					shift7.getTipo(),
//					shift7.getLotacao(),
//					new InfoExecutionCourse(
//						shift7.getDisciplinaExecucao().getNome(),
//						shift7.getDisciplinaExecucao().getSigla(),
//						shift7.getDisciplinaExecucao().getPrograma(),
//						new InfoExecutionDegree(
//							shift7
//								.getDisciplinaExecucao()
//								.getLicenciaturaExecucao()
//								.getAnoLectivo(),
//							new InfoDegree(
//								shift7
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getSigla(),
//								shift7
//									.getDisciplinaExecucao()
//									.getLicenciaturaExecucao()
//									.getCurso()
//									.getNome())),
//								shift7.getDisciplinaExecucao().getTheoreticalHours(),
//								shift7.getDisciplinaExecucao().getPraticalHours(),
//								shift7.getDisciplinaExecucao().getTheoPratHours(),
//								shift7.getDisciplinaExecucao().getLabHours())));
//
//		ArrayList shiftsList3 = new ArrayList();
//		shiftsList3.add(0, tLAndShift1);
//		shiftsList3.add(1, tLAndShift2);
//		shiftsList3.add(2, tLAndShift3);
//		shiftsList3.add(3, tLAndShift4);
//		ArrayList shiftsList4 = new ArrayList();
//		shiftsList4.add(0, tLAndShift5);
//		shiftsList4.add(1, tLAndShift6);
//		shiftsList4.add(2, tLAndShift7);
//
//		InfoExecutionCourse infoExecutionCourse1 =
//			new InfoExecutionCourse(
//				discipline1.getNome(),
//				discipline1.getSigla(),
//				discipline1.getPrograma(),
//				new InfoExecutionDegree(
//					discipline1.getLicenciaturaExecucao().getAnoLectivo(),
//					new InfoDegree(
//						discipline1
//							.getLicenciaturaExecucao()
//							.getCurso()
//							.getSigla(),
//						discipline1
//							.getLicenciaturaExecucao()
//							.getCurso()
//							.getNome())),
//				discipline1.getTheoreticalHours(),
//				discipline1.getPraticalHours(),
//				discipline1.getTheoPratHours(),
//				discipline1.getLabHours());
//
//
//		InfoExecutionCourse infoExecutionCourse2 =
//			new InfoExecutionCourse(
//				discipline2.getNome(),
//				discipline2.getSigla(),
//				discipline2.getPrograma(),
//				new InfoExecutionDegree(
//					discipline2.getLicenciaturaExecucao().getAnoLectivo(),
//					new InfoDegree(
//						discipline2
//							.getLicenciaturaExecucao()
//							.getCurso()
//							.getSigla(),
//						discipline2
//							.getLicenciaturaExecucao()
//							.getCurso()
//							.getNome())),
//					discipline2.getTheoreticalHours(),
//					discipline2.getPraticalHours(),
//					discipline2.getTheoPratHours(),
//					discipline2.getLabHours());
//
//
//		InfoCourseExecutionAndListOfTypeLessonAndInfoShift iCEAndList3 =
//			new InfoCourseExecutionAndListOfTypeLessonAndInfoShift(
//				new InfoExecutionCourse(
//					discipline3.getNome(),
//					discipline3.getSigla(),
//					discipline3.getPrograma(),
//					new InfoExecutionDegree(
//						discipline3.getLicenciaturaExecucao().getAnoLectivo(),
//						new InfoDegree(
//							discipline3
//								.getLicenciaturaExecucao()
//								.getCurso()
//								.getSigla(),
//							discipline3
//								.getLicenciaturaExecucao()
//								.getCurso()
//								.getNome())),
//						discipline3.getTheoreticalHours(),
//						discipline3.getPraticalHours(),
//						discipline3.getTheoPratHours(),
//						discipline3.getLabHours()),
//				shiftsList3);
//		InfoCourseExecutionAndListOfTypeLessonAndInfoShift iCEAndList4 =
//			new InfoCourseExecutionAndListOfTypeLessonAndInfoShift(
//				new InfoExecutionCourse(
//					discipline4.getNome(),
//					discipline4.getSigla(),
//					discipline4.getPrograma(),
//					new InfoExecutionDegree(
//						discipline4.getLicenciaturaExecucao().getAnoLectivo(),
//						new InfoDegree(
//							discipline4
//								.getLicenciaturaExecucao()
//								.getCurso()
//								.getSigla(),
//							discipline4
//								.getLicenciaturaExecucao()
//								.getCurso()
//								.getNome())),
//						discipline4.getTheoreticalHours(),
//						discipline4.getPraticalHours(),
//						discipline4.getTheoPratHours(),
//						discipline4.getLabHours()),
//				shiftsList4);
//
//		ArrayList withShifts = new ArrayList();
//
//		withShifts.add(0, iCEAndList3);
//		withShifts.add(1, iCEAndList4);
//		ArrayList withOutShifts = new ArrayList();
//		withOutShifts.add(0, infoExecutionCourse1);
//		withOutShifts.add(1, infoExecutionCourse2);
//
//		_iSE = new InfoShiftEnrolment(withShifts, withOutShifts);
//
//		ITurnoAluno shiftStudent1 = new TurnoAluno(shift6, student);
//		ITurnoAluno shiftStudent2 = new TurnoAluno(shift7, student);
//
//		try {
//			_suportePersistente.iniciarTransaccao();
//			_cursoPersistente.lockWrite(_curso1);
//			_cursoExecucaoPersistente.lockWrite(_cursoExecucao1);
//			_frequentaPersistente.lockWrite(attend1);
//			_frequentaPersistente.lockWrite(attend2);
//			_frequentaPersistente.lockWrite(attend3);
//			_frequentaPersistente.lockWrite(attend4);
//			_turnoPersistente.lockWrite(shift1);
//			_turnoPersistente.lockWrite(shift2);
//			_turnoPersistente.lockWrite(shift3);
//			_turnoPersistente.lockWrite(shift4);
//			_turnoPersistente.lockWrite(shift5);
//			_turnoPersistente.lockWrite(shift6);
//			_turnoPersistente.lockWrite(shift7);
//			_turnoAlunoPersistente.lockWrite(shiftStudent1);
//			_turnoAlunoPersistente.lockWrite(shiftStudent2);
//			
//			_suportePersistente.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia excepcao) {
//			fail("Exception when setUp");
//		}
//
//		_infoPerson = new InfoPerson();
//		_infoPerson.setNome(person.getNome());
//		_infoPerson.setUsername(person.getUsername());
//		_infoStudent =
//			new InfoStudent(
//				student.getNumber(),
//				student.getState(),
//				_infoPerson,
//				new TipoCurso(TipoCurso.LICENCIATURA));
	}

	/* (non-Javadoc)
		 * @see junit.framework.TestCase#tearDown()
		 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected void ligarSuportePersistente() {
		try {
			_suportePersistente = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when opening database");
		}
		_persistentPerson = _suportePersistente.getIPessoaPersistente();
		_persistentStudent = _suportePersistente.getIPersistentStudent();
		_cursoExecucaoPersistente =
			_suportePersistente.getICursoExecucaoPersistente();
		_cursoPersistente = _suportePersistente.getICursoPersistente();
		_frequentaPersistente = _suportePersistente.getIFrequentaPersistente();
		_turnoPersistente = _suportePersistente.getITurnoPersistente();
		_turnoAlunoPersistente =
			_suportePersistente.getITurnoAlunoPersistente();
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
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when cleaning data");
		}
	}

	/* Tests */

	public void testSucessfullShowShiftList() {
		// define origin mapping
		setRequestPathInfo("/student", "/viewShiftsList");

		// puts user to session 
		HashSet privileges = new HashSet();
		privileges.add("LerAlunosDeTurno");
		privileges.add("ReadShiftsByTypeFromExecutionCourse");
		
		
		IUserView userView = new UserView("45498", privileges);
		
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		// colocar outras informações na sessão
		getSession().setAttribute("infoStudent", _infoStudent);

		// puts iSE to session 
		getSession().setAttribute("infoShiftEnrolment", _iSE);
		//puts index to request
		addRequestParameter("index", "0-1");

		// performs action
		actionPerform();

		// verifies  forwad
		verifyForward("viewShiftsList");

		//verifies absence of errors
		verifyNoActionErrors();

		//verifies information stored in session
		ArrayList shiftsList =
			(ArrayList) getRequest().getAttribute("shiftsList");
		assertNotNull("ShiftsList not present", shiftsList);
		ArrayList vacancies =
			(ArrayList) getRequest().getAttribute("vacancies");
		assertNotNull("Vacancies not present", vacancies);

	}

	public void testunAuthorizedShowShiftList() {

	}
}
