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

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionYear;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class ChooseContextDispatchActionTest extends MockStrutsTestCase {
	private final String moduleName = "publico";

	protected ISuportePersistente sp = null;
	protected ICursoPersistente _degreeDAO = null;
	protected IPersistentExecutionPeriod executionPeriodDAO = null;
	protected IPersistentExecutionYear executionYearDAO = null;
	protected ICursoExecucaoPersistente _executionDegreeDAO = null;
	protected ICurso _degree = null;
	protected ICursoExecucao _executionDegree = null;
	protected ICursoExecucao executionDegree2 = null;
	private IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = null;

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
	InfoExecutionYear infoExecutionYear = null;
	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/web.xml");
		startPersistentLayer();
		cleanData();
		try {
			sp.iniciarTransaccao();
			IExecutionYear executionYear = null;
			executionYear =
				executionYearDAO.readExecutionYearByName("2003/2004");
			if (executionYear == null) {
				executionYear = new ExecutionYear("2003/2004");
				executionYearDAO.lockWrite(executionYear);
			}
			
			sp.confirmarTransaccao();
			infoExecutionYear = Cloner.copyIExecutionYear2InfoExecutionYear(executionYear);
			_infoDegreeList = new ArrayList();
			for (int i = 1; i < 10; i++) {
				
				String degreeName = "Lic " + i;
				String degreeInitials = "L" + i;
		
				_infoDegreeList.add(new InfoDegree(degreeInitials, degreeName));
				sp.iniciarTransaccao();
				_degree = _degreeDAO.readBySigla(degreeInitials);
				if (_degree == null) {
					_degree =
						new Curso(
							degreeInitials,
							degreeName,
							new TipoCurso(TipoCurso.LICENCIATURA));
					_degreeDAO.lockWrite(_degree);
				}
				sp.confirmarTransaccao();
				sp.iniciarTransaccao();
				IDegreeCurricularPlan degreeCurricularPlan =
					degreeCurricularPlanDAO.readByNameAndDegree(
						degreeInitials,_degree);
						
				if (degreeCurricularPlan == null) {
					degreeCurricularPlan =
						new DegreeCurricularPlan(degreeInitials, _degree);
					degreeCurricularPlanDAO.lockWrite(degreeCurricularPlan);
				}
				sp.confirmarTransaccao();
				sp.iniciarTransaccao();
				_executionDegree =
					_executionDegreeDAO.readByDegreeNameAndExecutionYear(degreeName,executionYear);
				if (_executionDegree == null) {
					_executionDegree =
						new CursoExecucao(executionYear, degreeCurricularPlan);
					_executionDegreeDAO.lockWrite(_executionDegree);
				}
				sp.confirmarTransaccao();
			}

		} catch (Throwable e) {
			System.out.println(_executionDegree);
			sp.cancelarTransaccao();
			fail("Fail in set Up");
		}
	}

	public void testClassSearch() {
		String chooseContext = "chooseContext";
		String nextPage = "classSearch";
		String curricularYear = "1";
		String index = "0";

		doPrepare(chooseContext, nextPage);
		doNextPage(nextPage, curricularYear, index);
	}

	public void testExecutionCourseSearch() {
		String chooseContext = "chooseContext";
		String nextPage = "executionCourseSearch";
		String curricularYear = "1";
		String index = "0";

		doPrepare(chooseContext, nextPage);
		doNextPage(nextPage, curricularYear, index);
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
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when opening database");
		}
		_degreeDAO = sp.getICursoPersistente();
		_executionDegreeDAO = sp.getICursoExecucaoPersistente();
		executionPeriodDAO = sp.getIPersistentExecutionPeriod();
		executionYearDAO = sp.getIPersistentExecutionYear();
		degreeCurricularPlanDAO = sp.getIPersistentDegreeCurricularPlan();
	}

	protected void cleanData() {
		try {
			sp.iniciarTransaccao();

			_executionDegreeDAO.deleteAll();
			executionPeriodDAO.deleteAll();
			executionYearDAO.deleteAll();
			_degreeDAO.deleteAll();
			degreeCurricularPlanDAO.deleteAll();
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when cleaning data");
		}
	}
	private void doNextPage(
		String nextPage,
		String curricularYear,
		String index)
		throws AssertionFailedError {
		setRequestPathInfo(moduleName, "/chooseContextDA");

		addRequestParameter("method", "nextPage");
		addRequestParameter("curricularYear", curricularYear);
		addRequestParameter("index", index);

		actionPerform();
		verifyNoActionErrors();

		verifyForward(nextPage);
		List attributesList = new ArrayList();

		attributesList.add(SessionConstants.CURRICULAR_YEAR_LIST_KEY);
		attributesList.add(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);
		attributesList.add(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);
		attributesList.add(SessionConstants.INFO_EXECUTION_DEGREE_KEY);

		// FIXME: put this in Constants ... 
		attributesList.add("anoCurricular");
		attributesList.add("semestre");

		verifySessionAttributes(getSession(), attributesList, null);
		//		TODO:Test if execution degree is the correct.
	}

	private void doPrepare(String chooseContext, String nextPage)
		throws AssertionFailedError {

		setRequestPathInfo(moduleName, "/chooseContextDA");

		addRequestParameter("method", "prepare");
		addRequestParameter(SessionConstants.INPUT_PAGE, chooseContext);
		addRequestParameter(SessionConstants.NEXT_PAGE, nextPage);

		actionPerform();

		verifyNoActionErrors();

		verifyForward("chooseContext");

		assertNotNull(
			"Request degrees!",
			getRequest().getAttribute(SessionConstants.DEGREES));

		List attributesList = new ArrayList();

		
		attributesList.add(SessionConstants.CURRICULAR_YEAR_LIST_KEY);
		attributesList.add(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);

		verifySessionAttributes(getSession(), attributesList, null);

		assertTrue(
			"Curricular Year list must be not empty!",
			!((List) getSession()
				.getAttribute(SessionConstants.CURRICULAR_YEAR_LIST_KEY))
				.isEmpty());
		List infoExecutionDegreeList =
			(List) getSession().getAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);
		assertTrue(
			"Info Degree list must be not empty!",
			!(infoExecutionDegreeList.isEmpty()));

		// TODO:Test if the list is the correct			
	}

}
