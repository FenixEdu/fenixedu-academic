/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.commons
 * 
 * Created on 8/Jan/2003
 *
 */
package ServidorApresentacao.Action.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionYear;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class ChooseContextDispatchActionTest extends MockStrutsTestCase {
	private final String moduleName = "publico";

	protected ISuportePersistente _sp = null;
	protected ICursoPersistente _degreeDAO = null;
	protected ICursoExecucaoPersistente _executionDegreeDAO = null;
	protected ICurso _degree = null;
	protected ICursoExecucao _executionDegree = null;
	protected ICursoExecucao _cursoExecucao2 = null;

	private List _infoDegreeList = null;

	/**
	 * Constructor for ChooseContextDispatchActionTest.
	 * @param testName
	 */
	public ChooseContextDispatchActionTest(String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ChooseContextDispatchActionTest.class);
		return suite;
	}

	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-publico.xml");
		startPersistentLayer();
		cleanData();
		try {
			_sp.iniciarTransaccao();
			_infoDegreeList = new ArrayList();
			for (int i = 1; i < 10; i++) {
				String degreeName = "Lic " + i;
				String degreeInitials = "L" + i;
				_infoDegreeList.add(new InfoDegree(degreeInitials, degreeName));
				_degree =
					new Curso(
						degreeInitials,
						degreeName,
						new TipoCurso(TipoCurso.LICENCIATURA));
				_executionDegree =
					new CursoExecucao(
						new ExecutionYear("2003/2004"),
						new DegreeCurricularPlan("plano1", _degree));

				_degreeDAO.lockWrite(_degree);
				_executionDegreeDAO.lockWrite(_executionDegree);
			}
			_sp.confirmarTransaccao();
		} catch (Throwable e) {
			_sp.cancelarTransaccao();
			fail("Fail in set Up");
		}

	}

	public void testPrepareSearch() {

		setRequestPathInfo(moduleName, "/chooseClassSearchContextDA");
		addRequestParameter("method", "prepare");

		actionPerform();

		verifyNoActionErrors();

		verifyForward("formPage");

		List attributesList = new ArrayList();

		attributesList.add(SessionConstants.SEMESTER_LIST_KEY);
		attributesList.add(SessionConstants.CURRICULAR_YEAR_LIST_KEY);
		attributesList.add(SessionConstants.INFO_DEGREE_LIST_KEY);

		verifySessionAttributes(getSession(), attributesList, null);

		assertTrue(
			"Semester list must be not empty!",
			!((List) getSession()
				.getAttribute(SessionConstants.SEMESTER_LIST_KEY))
				.isEmpty());

		assertTrue(
			"Curricular Year list must be not empty!",
			!((List) getSession()
				.getAttribute(SessionConstants.CURRICULAR_YEAR_LIST_KEY))
				.isEmpty());

		assertTrue(
			"Info Degree list must be not empty!",
			!((List) getSession()
				.getAttribute(SessionConstants.INFO_DEGREE_LIST_KEY))
				.isEmpty());

	}

	public void testNextPage() {
		setRequestPathInfo(moduleName, "/chooseClassSearchContextDA");
		addRequestParameter("method", "nextPage");

		addRequestParameter(
			ChooseContextDispatchAction.CURRICULAR_YEAR_PARAMETER,
			"2");
		addRequestParameter(
			ChooseContextDispatchAction.SEMESTER_PARAMETER,
			"2");
		addRequestParameter(
			ChooseContextDispatchAction.INFO_DEGREE_INITIALS_PARAMETER,
			"L1");

		getSession().setAttribute(
			SessionConstants.SEMESTER_LIST_KEY,
			new ArrayList());
		getSession().setAttribute(
			SessionConstants.CURRICULAR_YEAR_LIST_KEY,
			new ArrayList());
		getSession().setAttribute(
			SessionConstants.INFO_DEGREE_LIST_KEY,
			_infoDegreeList);

		actionPerform();

		verifyNoActionErrors();

		verifyForward("nextPage");

		List attributesList = new ArrayList();
		attributesList.add(SessionConstants.CONTEXT_KEY);
		attributesList.add(SessionConstants.SEMESTER_LIST_KEY);
		attributesList.add(SessionConstants.CURRICULAR_YEAR_LIST_KEY);
		attributesList.add(SessionConstants.INFO_DEGREE_LIST_KEY);

		verifySessionAttributes(getSession(), attributesList, null);

		CurricularYearAndSemesterAndInfoExecutionDegree ctx =
			SessionUtils.getContext(getRequest());

		assertNotNull("Context is null.", ctx);

		InfoExecutionDegree infoExecutionDegree =
			ctx.getInfoLicenciaturaExecucao();

		assertNotNull("Info Execution Degree is null.", infoExecutionDegree);
		assertNotNull(
			"Info Degree is null.",
			infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree());

		assertEquals(
			"Degree Initials value ",
			"L1",
			infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());
		assertEquals("Semester value ", new Integer(2), ctx.getSemestre());
		assertEquals(
			"Curricular Year value ",
			new Integer(2),
			ctx.getAnoCurricular());
	}

	/**
	 * Method verifySessionAttributes.
	 * @param httpSession
	 * @param attributesList
	 */
	private void verifySessionAttributes(
		HttpSession session,
		List existingAttributesList,
		List nonExistingAttributesList) {
		verifySessionAttributes(session, existingAttributesList, true);
		verifySessionAttributes(session, nonExistingAttributesList, false);
	}
	/**
	 * Method verifySessionAttributes.
	 * @param session
	 * @param existingAttributesList
	 * @param exists
	 */
	private void verifySessionAttributes(
		HttpSession session,
		List list,
		boolean exists) {
		if (list != null) {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				String attName = (String) iterator.next();
				if (!((session.getAttribute(attName) != null) == exists)) {
					String message = " Session contains attribute ";
					if (exists)
						message = "Session doesn't contains attribute ";
					fail(message + attName + ".");
				}
			}
		}

	}

	protected void startPersistentLayer() {
		try {
			_sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when opening database");
		}
		_degreeDAO = _sp.getICursoPersistente();
		_executionDegreeDAO = _sp.getICursoExecucaoPersistente();
	}

	protected void cleanData() {
		try {
			_sp.iniciarTransaccao();
			_degreeDAO.deleteAll();
			_executionDegreeDAO.deleteAll();
			_sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when cleaning data");
		}
	}

}
