package ServidorApresentacao.student;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.broker.PersistenceBrokerFactory;

import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoRole;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author tfc130
 *
 */
public class ViewEnrolmentActionTest extends MockStrutsTestCase {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ViewEnrolmentActionTest.class);

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
		PersistenceBrokerFactory.defaultPersistenceBroker().clearCache();
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public ViewEnrolmentActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulViewEnrolment() {
		// define mapping de origem
		setRequestPathInfo("/student", "/viewEnrolment");

		// Preenche campos do formulário
		addRequestParameter("username", "12345");
		addRequestParameter("password", "pass");

		// coloca credenciais na sessão

		Collection roles = new ArrayList();

		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.STUDENT);
		roles.add(infoRole);

		IUserView userView = new UserView("pessoa3", roles);

		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		// colocar outras informações na sessão
		getSession().setAttribute(
			"infoStudent",
			getInfoStudent(new Integer(3)));

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
		//assertNotNull("Student enrolment information not present: courses without shifts", iSE.getInfoEnrolmentWithOutShift());
		assertNotNull(
			"Student enrolment information not present: courses with shifts",
			iSE.getInfoEnrolmentWithShift());
	}

	// Not possible if user was able to log in as a student
	//public void testUnsuccessfulViewEnrolment() {
	//}

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