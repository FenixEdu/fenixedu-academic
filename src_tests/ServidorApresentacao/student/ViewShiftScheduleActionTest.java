package ServidorApresentacao.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoPerson;
import DataBeans.InfoRole;
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
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

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
		setServletConfigFile("/WEB-INF/web.xml");

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
		addRequestParameter("shiftName", "turno_apr_teorico1");

		// coloca credenciais na sessão
		Collection roles = new ArrayList();

		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.STUDENT);
		roles.add(infoRole);
		IUserView userView = new UserView("pessoa3", roles);

		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		// invoca acção
		actionPerform();

		// verifica reencaminhamento
		verifyForward("sucess");

		//verifica ausencia de erros
		verifyNoActionErrors();

		//verifica UserView guardado na sessão
		List lessons =
			(List) getSession().getAttribute(SessionConstants.LESSON_LIST_ATT);
		assertNotNull("Shift lessons not present", lessons);
		assertEquals("Shift lessons not present", lessons.size(), 1);
	}

	/**
	* @param integer
	* @return
	*/
	private InfoStudent getInfoStudent(Integer number) {
		InfoStudent infoStudent = null;

		IStudent student = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO = sp.getIPersistentStudent();
			sp.iniciarTransaccao();
			student =
				studentDAO.readByNumero(
					number,
					new TipoCurso(TipoCurso.LICENCIATURA));
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Reading student number " + number.intValue());
		}
		assertNotNull(student);
		infoStudent = Cloner.copyIStudent2InfoStudent(student);
		return infoStudent;
	}

}