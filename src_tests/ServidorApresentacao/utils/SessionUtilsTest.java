/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.utils
 * 
 * Created on 6/Jan/2003
 *
 */
package ServidorApresentacao.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import servletunit.HttpServletRequestSimulator;
import servletunit.HttpSessionSimulator;
import servletunit.ServletContextSimulator;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoRole;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import Tools.dbaccess;
import Util.RoleType;

/**
 * @author jpvl
 *
 * 
 */
public class SessionUtilsTest extends TestCase {
	private ServletContextSimulator _ctx;

	private String _schoolYear = new String("2002/2003");

	private int _semester = 1;
	private int _curricularYear = 2;

	private String _degreeName = "Licenciatura";
	private String _degreeInitials = "LEIC";

	protected ISuportePersistente sp;

	protected ICursoExecucaoPersistente executionDegreeDAO;
	protected ICursoPersistente _degreeDAO;
	protected IDisciplinaExecucaoPersistente executionCourseDAO;
	protected IPersistentCurricularCourse curricularCourseDAO;
	protected IPersistentDegreeCurricularPlan _degreeCurriculumDAO;
	protected IPersistentExecutionYear executionYearDAO;
	protected IPersistentExecutionPeriod executionPeriodDAO;
	private IPersistentCurricularSemester curricularSemesterDAO = null;
	private IPersistentCurricularCourseScope curricularCourseScopeDAO = null;
	private IPersistentBranch branchDAO = null;

	protected ICursoExecucao _executionDegree;
	protected ICurso degree;

	protected IDisciplinaExecucao _executionCourse;
	protected ICurricularCourse _curricularCourse;
	protected IPessoaPersistente _personDAO = null;
	//private List executionCourseList;

	/**
	 * Constructor for SessionUtilsTest.
	 * @param arg0
	 */
	public SessionUtilsTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SessionUtilsTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() {
		try {
			super.setUp();
			_ctx = new ServletContextSimulator();
			PersistenceBroker pb =
				PersistenceBrokerFactory.defaultPersistenceBroker();
			pb.clearCache();
			dbaccess dbAccess = new dbaccess();
			dbAccess.openConnection();
			dbAccess.backUpDataBaseContents("etc/testBackup.xml");
			dbAccess.loadDataBase("etc/testDataSetForSessionUtils.xml");
			dbAccess.closeConnection();
			
		} catch (Exception e) {

			e.printStackTrace(System.out);
			fail("Fail in setUp!");
		}

	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testRemoveAttributtes() {
		String prefix = "prefix.";
		HttpSessionSimulator sessionSimulator = new HttpSessionSimulator(_ctx);
		for (int i = 0; i < 10; i++) {
			sessionSimulator.setAttribute("dummy" + i, "dummy");
			sessionSimulator.setAttribute(prefix + i, "value");
		}
		sessionSimulator.setAttribute("dummy", "dummy");
		SessionUtils.removeAttributtes(sessionSimulator, prefix);

		Enumeration attNames = sessionSimulator.getAttributeNames();

		while (attNames.hasMoreElements()) {
			String attName = (String) attNames.nextElement();
			if (attName.startsWith(prefix)) {
				fail(
					"Contains one attribute starting with "
						+ prefix
						+ ": att =("
						+ attName
						+ ")");
			}
		}
	}

	public void testGetContext() {
		HttpServletRequestSimulator request =
			new HttpServletRequestSimulator(_ctx);

		CurricularYearAndSemesterAndInfoExecutionDegree c =
			setToSessionCurricularYearAndSemesterAndInfoExecutionDegree(request);
		CurricularYearAndSemesterAndInfoExecutionDegree c2 =
			SessionUtils.getContext(request);

		assertNotNull("Obtnained object from session must be not null!", c2);
		assertEquals(c, c2);
	}

	public void testGetUserView() {
		HttpServletRequestSimulator request =
			new HttpServletRequestSimulator(_ctx);
		IUserView userView = setToSessionUserView(request);

		IUserView userView2 = SessionUtils.getUserView(request);

		assertNotNull(
			"Obtnained object from session must be not null!",
			userView2);
		assertEquals(userView, userView2);
	}

	public void testGetExecutionCoursesAlreadyInSession() {
		HttpServletRequestSimulator request =
			new HttpServletRequestSimulator(_ctx);

		createCurricularYearAndSemesterAndInfoExecutionDegree();

		setToSessionUserView(request);

		List executionCourseList = new ArrayList();

		request.getSession().setAttribute(
			SessionConstants.EXECUTION_COURSE_LIST_KEY,
			executionCourseList);

		List executionCourseList2 = null;
		try {
			executionCourseList2 = SessionUtils.getExecutionCourses(request);
		} catch (Exception e) {
			fail("Exception running getExecutionCourses "+e);
		}

		assertNotNull(
			"Execution course list must be not null!",
			executionCourseList2);
		assertEquals(true, executionCourseList2.isEmpty());
	}

	public void testGetExecutionCoursesNotYetInSession() {
		HttpServletRequestSimulator request =
			new HttpServletRequestSimulator(_ctx);

		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear(_schoolYear);

		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		setToSessionUserView(request);
		HttpSession session = request.getSession();
		session.setAttribute(
			SessionConstants.CURRICULAR_YEAR_KEY,
			new Integer(2));

		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		InfoDegree infoDegree = new InfoDegree();
		infoDegree.setSigla(_degreeInitials);
		infoDegree.setNome(_degreeName);
		infoDegreeCurricularPlan.setInfoDegree(infoDegree);
		infoDegreeCurricularPlan.setName("plano 1");
		
		infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
		
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			infoExecutionDegree);
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			infoExecutionPeriod);

		List infoExecutionCourseList = null;
		try {
			infoExecutionCourseList = SessionUtils.getExecutionCourses(request);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			fail("testGetExecutionCoursesNotYetInSession: Executing getExecutionCourses");
		}

		assertNotNull(
			"infoExecutionCourseList is null.",
			infoExecutionCourseList);
		assertEquals(
			"List is not of the same size!",
			9,
			infoExecutionCourseList.size());
		assertNotNull(
			"Session must contain attribute in SessionConstants.EXECUTION_COURSE_LIST_KEY",
			request.getSession().getAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY));
	}

	private IUserView createUserView() {
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.STUDENT);
		InfoRole sopRole = new InfoRole();
		sopRole.setRoleType(RoleType.TIME_TABLE_MANAGER);
		Collection roles = new ArrayList();
		roles.add(infoRole);
		roles.add(sopRole);

		IUserView userView = new UserView("45498", roles);
		return userView;
	}

	private IUserView setToSessionUserView(HttpServletRequest request) {
		IUserView userView = createUserView();
		HttpSession session = request.getSession(true);

		session.setAttribute(SessionConstants.U_VIEW, userView);
		return userView;
	}

	private CurricularYearAndSemesterAndInfoExecutionDegree setToSessionCurricularYearAndSemesterAndInfoExecutionDegree(HttpServletRequest request) {
		CurricularYearAndSemesterAndInfoExecutionDegree c =
			createCurricularYearAndSemesterAndInfoExecutionDegree();

		HttpSession session = request.getSession(true);

		session.setAttribute(SessionConstants.CONTEXT_KEY, c);
		return c;

	}
	private CurricularYearAndSemesterAndInfoExecutionDegree createCurricularYearAndSemesterAndInfoExecutionDegree() {
		CurricularYearAndSemesterAndInfoExecutionDegree c =
			new CurricularYearAndSemesterAndInfoExecutionDegree();
		c.setSemestre(new Integer(_semester));
		c.setAnoCurricular(new Integer(_curricularYear));

		InfoDegree infoDegree = new InfoDegree(_degreeInitials, _degreeName);
		InfoExecutionDegree infoExecutionDegree =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", infoDegree),
				new InfoExecutionYear(_schoolYear));
		c.setInfoLicenciaturaExecucao(infoExecutionDegree);

		return c;
	}

}
