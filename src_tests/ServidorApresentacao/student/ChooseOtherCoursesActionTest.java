package ServidorApresentacao.student;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoPerson;
import DataBeans.InfoRole;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import Dominio.IStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;
import Util.RoleType;
import Util.TipoCurso;

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
	protected IPersistentDegreeCurricularPlan _persistentDegreeCurricularPlan =
		null;
	protected IStudentCurricularPlanPersistente _persistentStudentCurricularPlan =
		null;
	protected IDisciplinaDepartamentoPersistente _persistentDepartmentCourse =
		null;
	protected IPersistentDepartment _persistentDepartment = null;

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
		setServletConfigFile("/WEB-INF/web.xml");

		dbaccess dbAccess = new dbaccess();

		dbAccess.openConnection();

		dbAccess.loadDataBase("etc/testDataSetForStudent.xml");

		dbAccess.closeConnection();
		PersistenceBroker pb =
			PersistenceBrokerFactory.defaultPersistenceBroker();

		pb.clearCache();

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

		// coloca credenciais na sessão
		Collection roles = new ArrayList();

		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.STUDENT);
		roles.add(infoRole);

		infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.PERSON);
		roles.add(infoRole);

		IUserView userView = new UserView("user", roles);

		InfoStudent infoStudent = getInfoStudent();

		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		// colocar outras informações na sessão
		getSession().setAttribute("infoStudent", infoStudent);

		// invoca acção
		actionPerform();

		// verifica reencaminhamento
		verifyForward("sucess");

		//verifica ausencia de erros
		verifyNoActionErrors();

		//verifica UserView guardado na sessão
		InfoShiftEnrolment iSE =
			(InfoShiftEnrolment) getSession().getAttribute(
				"infoShiftEnrolment");
		assertNotNull("Student enrolment information not present", iSE);
		assertNull(
			"Student enrolment information not present: courses without shifts",
			iSE.getInfoEnrolmentWithOutShift());
		assertNotNull(
			"Student enrolment information not present: courses with shifts",
			iSE.getInfoEnrolmentWithShift());
	}

	public InfoStudent getInfoStudent() {
		InfoStudent infoStudent = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO = sp.getIPersistentStudent();
			sp.iniciarTransaccao();
			IStudent student =
				studentDAO.readByNumero(
					new Integer(45498),
					new TipoCurso(TipoCurso.LICENCIATURA));
			sp.confirmarTransaccao();
			infoStudent = Cloner.copyIStudent2InfoStudent(student);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Reading student 45498!");
		}
		return infoStudent;
	}
}