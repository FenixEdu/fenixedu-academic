/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.student
 * 
 * Created on 22/Dez/2003
 *
 */
package ServidorApresentacao.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.beanutils.BeanUtils;

import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoPerson;
import DataBeans.InfoRole;
import DataBeans.InfoStudent;
import Dominio.Curso;
import Dominio.DisciplinaExecucao;
import Dominio.Frequenta;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Pessoa;
import Dominio.Privilegio;
import Dominio.Student;
import Dominio.Turno;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.student.ShiftEnrolmentAction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoAula;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

/**
 * @author jpvl
 *
 * 
 */
public class ShiftEnrolmentActionTest extends MockStrutsTestCase {
	private final String serviceName = new String("StudentShiftEnrolment");

	private ISuportePersistente _sp;

	private IDisciplinaExecucaoPersistente _executionCourseDAO;
	private IPersistentCurricularCourse _degreeCourseDAO;
	private ICursoPersistente _degreeDAO;
	private ICursoExecucaoPersistente _executionDegreeDAO;
	private IPessoaPersistente _personDAO;
	private IPersistentStudent _studentDAO;
	private ITurnoPersistente _shiftDAO;
	private ITurnoAlunoPersistente _studentShiftDAO;
	private IFrequentaPersistente _assistCourseDAO;

	private IPessoa _person1;
	private IPessoa _person2;

	private IStudent _student1;
	private IStudent _student2;

	private IDisciplinaExecucao _executionCourse;
	private ICurricularCourse _course;
	private ICurso _degree;
	private ITurma _class;
	private ITurmaPersistente _classDAO;

	private ITurno _shift1; /* _student1 enroled in*/
	private ITurno _shift2; /* same type of _shift1 */ 
	private ITurno _shift3; /* other shift*/

	private ICursoExecucao _executionDegree;
	private Frequenta _assistCourse;

	/**
	 * Constructor for ShiftEnrolmentActionTest.
	 * @param arg0
	 */
	public ShiftEnrolmentActionTest(String name) {
		super(name);

	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ShiftEnrolmentActionTest.class);
		return suite;
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Exception {
		super.setUp();

		initializeDAO();

		setServletConfigFile("/WEB-INF/tests/web-student.xml");
		setRequestPathInfo("", "/shiftEnrolment");

	}
	/**
	 * Method initializeDAO.
	 */
	private void initializeDAO() throws Exception {
		setDAO();
		cleanData();
		_sp.iniciarTransaccao();

		/* Degree struture */
		_degree =
			new Curso(
				"LEIC",
				"Curso de Engenharia Informatica e de Computadores",
				new TipoCurso(TipoCurso.LICENCIATURA));

//		_executionDegree = new CursoExecucao("2002/2003", _degree);
		_executionDegreeDAO.lockWrite(_executionDegree);

//		_class = new Turma("class1", new Integer(1),new Integer(1), _degree);
		_classDAO.lockWrite(_class);

		_executionCourse = new DisciplinaExecucao();

//		_executionCourse.setLicenciaturaExecucao(_executionDegree);
		_executionCourse.setNome("ec1");
		_executionCourse.setSigla("ec1");
		// This has to be done because the domain conections are not yet complete
		((DisciplinaExecucao) _executionCourse).setChaveResponsavel(new Integer(0));
		_executionCourseDAO.escreverDisciplinaExecucao(_executionCourse);
		_shift1 =
			new Turno(
				"shift1",
				new TipoAula(TipoAula.PRATICA),
				new Integer(1),
				_executionCourse);
		_shiftDAO.lockWrite(_shift1);

		_shift2 =
			new Turno(
				"shift2",
				new TipoAula(TipoAula.PRATICA),
				new Integer(1),
				_executionCourse);
		_shiftDAO.lockWrite(_shift2);

		_shift3 =
			new Turno(
				"shift3",
				new TipoAula(TipoAula.TEORICA),
				new Integer(1),
				_executionCourse);
		_shiftDAO.lockWrite(_shift3);

		/* Person and student 1 */
		_person1 = new Pessoa();
		_person1.setNumeroDocumentoIdentificacao("123123123");
		_person1.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		_person1.setNome("person1");
		_person1.setUsername("person1");
		_person1.setPassword("pass1");

		HashSet privileges = new HashSet(1);
		privileges.add(new Privilegio(_person1, this.serviceName));
		_person1.setPrivilegios(privileges);
		_personDAO.escreverPessoa(_person1);

		_student1 = new Student(new Integer(1), new Integer(1), _person1, new TipoCurso(TipoCurso.LICENCIATURA));
		_studentDAO.lockWrite(_student1);

		/* Person and student 2 */
		_person2 = new Pessoa();
		_person2.setNumeroDocumentoIdentificacao("223223223");
		_person2.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		_person2.setNome("person2");
		_person2.setUsername("person2");
		_person2.setPassword("pass2");
		privileges.clear();
		privileges.add(new Privilegio(_person2, this.serviceName));
		_person2.setPrivilegios(privileges);
		_personDAO.escreverPessoa(_person2);

		_student2 = new Student(new Integer(2), new Integer(2), _person2, new TipoCurso(TipoCurso.LICENCIATURA));
		_studentDAO.lockWrite(_student2);

		/* Assist info */
		_assistCourse = new Frequenta(_student1, _executionCourse);
		_assistCourseDAO.lockWrite(_assistCourse);
		
		_sp.confirmarTransaccao();
	}

	/**
	 * Method setDAO.
	 */
	private void setDAO() {
		try {
			_sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when opening database");
		}

		_executionCourseDAO = _sp.getIDisciplinaExecucaoPersistente();
		_degreeCourseDAO = _sp.getIPersistentCurricularCourse();
		_degreeDAO = _sp.getICursoPersistente();
		_executionDegreeDAO = _sp.getICursoExecucaoPersistente();
		_personDAO = _sp.getIPessoaPersistente();
		_studentDAO = _sp.getIPersistentStudent();
		_shiftDAO = _sp.getITurnoPersistente();
		_studentShiftDAO = _sp.getITurnoAlunoPersistente();
		_assistCourseDAO = _sp.getIFrequentaPersistente();
		_classDAO = _sp.getITurmaPersistente();

	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		cleanData();
	}

	public void testSucessfullEnrolment() {
		verifyTestError(_student1, _shift3.getNome(), null);
	}

// The following test will not be runned because verification that
// students must be allowed to frequent a course in order to enrole
// at the time of shift enrolment cannot be made. 
//	public void testNotEnroledInExecutionCourse() {
//		verifyTestError(
//			_student2,
//			_shift1.getNome(),
//			ShiftEnrolmentAction.STUDENT_NOT_ENROLED_IN_EXECUTION_COURSE_ERROR);
//	}

	public void testNonExistingShiftEnrolment() {
		verifyTestError(
			_student1,
			"non_existing",
			ShiftEnrolmentAction.SHIFT_NON_EXISTING_ERROR);

	}

	public void testChangeEnrolment() {
		verifyTestError(_student1,_shift2.getNome(),null);
	}

	public void testFullShiftEnrolment() {
		verifyTestError(_student1, _shift3.getNome(), null);
		verifyTestError(_student2, _shift3.getNome(), ShiftEnrolmentAction.SHIFT_FULL_ERROR);
	
	}

	private void verifyTestError(
		IStudent student,
		String shiftName,
		String errorMessage) {
		setUserToSession(student);
		addRequestParameter(
			ShiftEnrolmentAction.PARAMETER_SHIFT_NAME,
			shiftName);

		actionPerform();

		if (errorMessage == null) {
			verifyForward("sucess");
			verifyNoActionErrors();
		} else {
			verifyInputForward();
			verifyActionErrors(new String[] { errorMessage });
		}
	}

	/**
	 * Method setUserToSession.
	 * @param _student1
	 */
	private void setUserToSession(IStudent student) {
		Collection roles = new ArrayList();
		
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.STUDENT);

		IUserView userView =
			new UserView(student.getPerson().getUsername(), roles);

		InfoPerson infoPerson = new InfoPerson();
		try {
			BeanUtils.copyProperties(infoPerson, student.getPerson());
		} catch (Exception e) {}
		InfoStudent studentView =
			new InfoStudent(student.getNumber(), student.getState(), infoPerson, student.getDegreeType());

		getSession().setAttribute(SessionConstants.U_VIEW, userView);
		getSession().setAttribute(
			SessionConstants.INFO_STUDENT_KEY,
			studentView);

	}

	private void cleanData() {
		try {

			_sp.iniciarTransaccao();

			_executionCourseDAO.apagarTodasAsDisciplinasExecucao();
			_degreeCourseDAO.deleteAllCurricularCourse();
			_degreeDAO.deleteAll();
			_executionDegreeDAO.deleteAll();
			_personDAO.apagarTodasAsPessoas();
			_studentDAO.deleteAll();
			_shiftDAO.deleteAll();
			_studentShiftDAO.deleteAll();
			_assistCourseDAO.deleteAll();
			_classDAO.deleteAll();

			_sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("Cleaning data : " + e.getMessage());
		}

	}

}
