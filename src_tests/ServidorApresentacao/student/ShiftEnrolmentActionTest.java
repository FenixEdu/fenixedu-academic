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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoPerson;
import DataBeans.InfoRole;
import DataBeans.InfoStudent;
import Dominio.IStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.student.ShiftEnrolmentAction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author jpvl
 *
 * 
 */
public class ShiftEnrolmentActionTest extends MockStrutsTestCase {
	private final String serviceName = new String("StudentShiftEnrolment");


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

		setServletConfigFile("/WEB-INF/web.xml");
		setRequestPathInfo("student", "/shiftEnrolment");

		dbaccess dbAccess = new dbaccess();
		
		dbAccess.openConnection();
		dbAccess.loadDataBase("etc/testDataSetForStudent.xml");
		dbAccess.closeConnection();
		PersistenceBrokerFactory.defaultPersistenceBroker().clearCache();

	}
	/**
	 * Method initializeDAO.
	 */
	private void initializeDAO() throws Exception {
//		setDAO();
//		cleanData();
//		_sp.iniciarTransaccao();
//
//		/* Degree struture */
//		_degree =
//			new Curso(
//				"LEIC",
//				"Curso de Engenharia Informatica e de Computadores",
//				new TipoCurso(TipoCurso.LICENCIATURA));
//
////		_executionDegree = new CursoExecucao("2002/2003", _degree);
//		_executionDegreeDAO.lockWrite(_executionDegree);
//
////		_class = new Turma("class1", new Integer(1),new Integer(1), _degree);
//		_classDAO.lockWrite(_class);
//
//		_executionCourse = new DisciplinaExecucao();
//		_executionCourse.setNome("ec1");
//		_executionCourse.setSigla("ec1");
//		_executionCourseDAO.escreverDisciplinaExecucao(_executionCourse);
//		_shift1 =
//			new Turno(
//				"shift1",
//				new TipoAula(TipoAula.PRATICA),
//				new Integer(1),
//				_executionCourse);
//		_shiftDAO.lockWrite(_shift1);
//
//		_shift2 =
//			new Turno(
//				"shift2",
//				new TipoAula(TipoAula.PRATICA),
//				new Integer(1),
//				_executionCourse);
//		_shiftDAO.lockWrite(_shift2);
//
//		_shift3 =
//			new Turno(
//				"shift3",
//				new TipoAula(TipoAula.TEORICA),
//				new Integer(1),
//				_executionCourse);
//		_shiftDAO.lockWrite(_shift3);
//
//		/* Person and student 1 */
//		_person1 = new Pessoa();
//		_person1.setNumeroDocumentoIdentificacao("123123123");
//		_person1.setTipoDocumentoIdentificacao(
//			new TipoDocumentoIdentificacao(
//				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
//		_person1.setNome("person1");
//		_person1.setUsername("person1");
//		_person1.setPassword(PasswordEncryptor.encryptPassword("pass1"));
//
//		_personDAO.escreverPessoa(_person1);
//
//		_student1 = new Student(new Integer(1), new StudentState(1), _person1, new TipoCurso(TipoCurso.LICENCIATURA));
//		_studentDAO.lockWrite(_student1);
//
//		/* Person and student 2 */
//		_person2 = new Pessoa();
//		_person2.setNumeroDocumentoIdentificacao("223223223");
//		_person2.setTipoDocumentoIdentificacao(
//			new TipoDocumentoIdentificacao(
//				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
//		_person2.setNome("person2");
//		_person2.setUsername("person2");
//		_person2.setPassword(PasswordEncryptor.encryptPassword("pass2"));
//		_personDAO.escreverPessoa(_person2);
//
//		_student2 = new Student(new Integer(2), new StudentState(2), _person2, new TipoCurso(TipoCurso.LICENCIATURA));
//		_studentDAO.lockWrite(_student2);
//
//		/* Assist info */
//		_assistCourse = new Frequenta(_student1, _executionCourse);
//		_assistCourseDAO.lockWrite(_assistCourse);
//		
//		_sp.confirmarTransaccao();

		
	}


	public void testSucessfullEnrolment() {
		verifyTestError(getStudent(new Integer(1)), "shift3", null);
	}

	/**
	 * @param integer
	 * @return
	 */
	private IStudent getStudent(Integer number) {
		IStudent student = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO = sp.getIPersistentStudent();
			sp.iniciarTransaccao();
			student = studentDAO.readByNumero(number, new TipoCurso(TipoCurso.LICENCIATURA));
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading student number "+number);
		}
		assertNotNull(student);
		return student;
	}

	public void testNonExistingShiftEnrolment() {
		verifyTestError(
			getStudent(new Integer(1)),
			"non_existing",
			ShiftEnrolmentAction.SHIFT_NON_EXISTING_ERROR);

	}

	public void testChangeEnrolment() {
		verifyTestError(getStudent(new Integer(1)),"shift2",null);
	}

	public void testFullShiftEnrolment() {
		verifyTestError(getStudent(new Integer(1)), "shift3", null);
		verifyTestError(getStudent(new Integer(2)), "shift3", ShiftEnrolmentAction.SHIFT_FULL_ERROR);
	
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
	
		roles.add(infoRole);
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
}
