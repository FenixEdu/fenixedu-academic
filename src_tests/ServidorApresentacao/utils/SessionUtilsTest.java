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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import servletunit.HttpServletRequestSimulator;
import servletunit.HttpSessionSimulator;
import servletunit.ServletContextSimulator;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.CurricularYear;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.Departamento;
import Dominio.DisciplinaDepartamento;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDepartamento;
import Dominio.IDisciplinaDepartamento;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDepartamentoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

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
	private List executionCourseList;

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

			startPersistentLayer();

			sp.iniciarTransaccao();
			ICursoPersistente degreeDAO = sp.getICursoPersistente();
			degree = degreeDAO.readBySigla(_degreeInitials);
			if (degree == null) {
				degree =
					new Curso(
						_degreeInitials,
						_degreeName,
						new TipoCurso(TipoCurso.LICENCIATURA));

				degreeDAO.lockWrite(degree);
			}
			sp.confirmarTransaccao();

			sp.iniciarTransaccao();
			IExecutionYear executionYear =
				executionYearDAO.readExecutionYearByName(_schoolYear);
			if (executionYear == null) {
				executionYear = new ExecutionYear(_schoolYear);
				executionYearDAO.lockWrite(executionYear);
			}
			sp.confirmarTransaccao();

			sp.iniciarTransaccao();
			IPersistentDegreeCurricularPlan degreeCurricularPlanDAO =
				sp.getIPersistentDegreeCurricularPlan();
			IDegreeCurricularPlan degreeCurriculum =
				degreeCurricularPlanDAO.readByNameAndDegree("plano1", degree);
			if (degreeCurriculum == null) {
				degreeCurriculum = new DegreeCurricularPlan("plano1", degree);
				degreeCurricularPlanDAO.lockWrite(degreeCurriculum);
			}
			sp.confirmarTransaccao();

			sp.iniciarTransaccao();
			ICursoExecucaoPersistente executionDegreeDAO =
				sp.getICursoExecucaoPersistente();
			_executionDegree =
				executionDegreeDAO.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurriculum,
					executionYear);
			if (_executionDegree == null) {
				_executionDegree =
					new CursoExecucao(executionYear, degreeCurriculum);
				executionDegreeDAO.lockWrite(_executionDegree);
			}
			sp.confirmarTransaccao();

			IDisciplinaExecucao executionCourse = null;
			ICurricularCourse curricularCourse = null;
			executionCourseList = new ArrayList();

			for (int i = 1; i < 10; i++) {
				//FIXME this test got something with the cache...
				pb.clearCache(); //don't remove this...
				
				IDepartamento d = new Departamento("nome" + i, "sigla" + i);
				IDisciplinaDepartamento dd =
					new DisciplinaDepartamento("Disciplina " + i, "D" + i, d);

				IDepartamentoPersistente departmentDAO =
					sp.getIDepartamentoPersistente();
				
				sp.iniciarTransaccao();
				IDepartamento d2 =
					(IDepartamento) departmentDAO.readDomainObjectByCriteria(d);
				if (d2 == null) {
					departmentDAO.escreverDepartamento(d);
				}
				
				sp.confirmarTransaccao();

				IDisciplinaDepartamentoPersistente departmentCourseDAO =
					sp.getIDisciplinaDepartamentoPersistente();
				sp.iniciarTransaccao();
				IDisciplinaDepartamento dd2 =
					departmentCourseDAO.lerDisciplinaDepartamentoPorNomeESigla(
						dd.getNome(),
						dd.getSigla());
				if (dd2 == null) {
					departmentCourseDAO.escreverDisciplinaDepartamento(dd);
				}
				sp.confirmarTransaccao();

				curricularCourse =
					new CurricularCourse(
						new Double(1.0),
						new Double(2.0),
						new Double(2.0),
						new Double(0),
						new Double(0),
						"Disciplina " + i,
						"D" + i,
						dd,
						degreeCurriculum);

				sp.iniciarTransaccao();
				ICurricularCourse curricularCourse2 =
					curricularCourseDAO.readCurricularCourseByNameAndCode(
						curricularCourse.getName(),
						curricularCourse.getCode());
				if (curricularCourse2 == null) {
					curricularCourseDAO.lockWrite(curricularCourse);
				}
				sp.confirmarTransaccao();


				IPersistentCurricularYear curricularYearDAO =
					sp.getIPersistentCurricularYear();

				
				sp.iniciarTransaccao();
				ICurricularYear curricularYear =
					curricularYearDAO.readCurricularYearByYear(new Integer(2));
				if (curricularYear == null) {
					curricularYear = new CurricularYear(new Integer(2));
					curricularYearDAO.lockWrite(curricularYear);
				}
				sp.confirmarTransaccao();
				
				
				sp.iniciarTransaccao();
				ICurricularSemester curricularSemester =
					curricularSemesterDAO
						.readCurricularSemesterBySemesterAndCurricularYear(
						new Integer(2),
						curricularYear);
				if (curricularSemester == null) {
					curricularSemester =
						new CurricularSemester(new Integer(2), curricularYear);
					curricularSemesterDAO.lockWrite(curricularSemester);
				}
				sp.confirmarTransaccao();

				List curricularSemesterList = new ArrayList();
				curricularSemesterList.add(curricularSemester);
				//				_curricularCourse.setAssociatedCurricularSemesters(curricularSemesterList);

				sp.iniciarTransaccao();
				IExecutionPeriod executionPeriod =
					executionPeriodDAO.readByNameAndExecutionYear(
						"2º Semestre",
						executionYear);
				if (executionPeriod == null) {
					executionPeriod =
						new ExecutionPeriod("2º Semestre", executionYear);
					executionPeriodDAO.lockWrite(executionPeriod);
				}
				sp.confirmarTransaccao();

				executionCourse =
					new DisciplinaExecucao(
						"Disciplina " + i,
						"D" + i,
						new Double(2.0),
						new Double(2.0),
						new Double(0),
						new Double(0),
						executionPeriod);

				List list = new ArrayList();

				list.add(curricularCourse);

				List listExecutionCourse = new ArrayList();
				listExecutionCourse.add(executionCourse);
				curricularCourse.setAssociatedExecutionCourses(
					listExecutionCourse);

				executionCourse.setAssociatedCurricularCourses(list);

				sp.iniciarTransaccao();
				IBranch branch = branchDAO.readBranchByNameAndCode("", "");
				if (branch == null) {
					branch = new Branch("", "");
					branchDAO.lockWrite(branch);
				}
				sp.confirmarTransaccao();

				sp.iniciarTransaccao();
				ICurricularCourseScope curricularCourseScope =
					curricularCourseScopeDAO
						.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
						curricularCourse,
						curricularSemester,
						branch);
				if (curricularCourseScope == null) {
					curricularCourseScope =
						new CurricularCourseScope(
							curricularCourse,
							curricularSemester,
							branch);
					curricularCourseScopeDAO.lockWrite(curricularCourseScope);
				}
				sp.confirmarTransaccao();


				sp.iniciarTransaccao();
				IDisciplinaExecucao executionCourse2 =
					executionCourseDAO
						.readByExecutionCourseInitialsAndExecutionPeriod(
						executionCourse.getSigla(),
						executionCourse.getExecutionPeriod());
				if (executionCourse2 == null) {
					executionCourseDAO.escreverDisciplinaExecucao(
						executionCourse);
				}
				sp.confirmarTransaccao();

				InfoExecutionCourse infoExecutionCourse =
					new InfoExecutionCourse();

				copyExecutionCourseToInfoExecutionCourse(
					infoExecutionCourse,
					executionCourse);
				executionCourseList.add(infoExecutionCourse);
			}
			sp.iniciarTransaccao();
			ICursoExecucao executionDegree2 =
				executionDegreeDAO.readByDegreeCurricularPlanAndExecutionYear(
					_executionDegree.getCurricularPlan(),
					_executionDegree.getExecutionYear());
			if (executionDegree2 == null) {
				executionDegreeDAO.lockWrite(_executionDegree);
			}
			sp.confirmarTransaccao();
		} catch (Exception e) {

			e.printStackTrace(System.out);
			fail("Fail in setUp!");
		}

	}
	/**
	 * Method copyExecutionCourseToInfoExecutionCourse.
	 * @param infoExecutionCourse
	 * @param _executionCourse
	 */
	private void copyExecutionCourseToInfoExecutionCourse(
		InfoExecutionCourse infoExecutionCourse,
		IDisciplinaExecucao executionCourse) {
		//		InfoDegree infoDegree = new InfoDegree();
		//		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		try {
			BeanUtils.copyProperties(infoExecutionCourse, executionCourse);

			BeanUtils.copyProperties(
				infoExecutionPeriod,
				executionCourse.getExecutionPeriod());

			BeanUtils.copyProperties(
				infoExecutionYear,
				executionCourse.getExecutionPeriod().getExecutionYear());
		} catch (Exception e) {
			fail("Executing copyExecutionCourseToInfoExecutionCourse");
		}
		infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
		infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
	}

	protected void startPersistentLayer() {
		try {
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			fail("Starting persistent layer!");
		}

		executionDegreeDAO = sp.getICursoExecucaoPersistente();
		_degreeDAO = sp.getICursoPersistente();
		executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
		curricularCourseDAO = sp.getIPersistentCurricularCourse();
		curricularCourseScopeDAO = sp.getIPersistentCurricularCourseScope();
		_degreeCurriculumDAO = sp.getIPersistentDegreeCurricularPlan();
		_personDAO = sp.getIPessoaPersistente();
		executionYearDAO = sp.getIPersistentExecutionYear();
		executionPeriodDAO = sp.getIPersistentExecutionPeriod();
		curricularSemesterDAO = sp.getIPersistentCurricularSemester();
		branchDAO = sp.getIPersistentBranch();
		cleanData();
	}

	protected void cleanData() {
		try {
			sp.iniciarTransaccao();

			executionDegreeDAO.deleteAll();
			_degreeDAO.deleteAll();
			executionCourseDAO.apagarTodasAsDisciplinasExecucao();
			curricularCourseDAO.deleteAll();
			_degreeCurriculumDAO.deleteAll();
			_personDAO.apagarTodasAsPessoas();
			sp.getIDepartamentoPersistente().apagarTodosOsDepartamentos();
			sp
				.getIDisciplinaDepartamentoPersistente()
				.apagarTodasAsDisciplinasDepartamento();
		} catch (ExcepcaoPersistencia e) {
			fail("Cleaning data!");
		} finally {
			try {
				sp.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ignored) {
			}
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
			fail("Exception running getExecutionCourses");
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

		session.setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			Cloner.copyIExecutionDegree2InfoExecutionDegree(_executionDegree));
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
			executionCourseList.size(),
			infoExecutionCourseList.size());
		assertTrue(
			"List not contains all elements!",
			infoExecutionCourseList.containsAll(executionCourseList));
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
