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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionYear;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCaseActionExecution;
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
public class ChooseContextDispatchActionTest2 extends TestCaseActionExecution {

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
	public ChooseContextDispatchActionTest2(String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ChooseContextDispatchActionTest2.class);
		return suite;
	}

	public void setUp() {
		super.setUp();

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
			try {
				_sp.cancelarTransaccao();
				fail("Fail in set Up");
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public void testPrepareSearch() {

		List attributesList = new ArrayList();

		attributesList.add(SessionConstants.SEMESTER_LIST_KEY);
		attributesList.add(SessionConstants.CURRICULAR_YEAR_LIST_KEY);
		attributesList.add(SessionConstants.INFO_DEGREE_LIST_KEY);

		HashMap list = new HashMap();
		list.put(new Integer(ScopeConstants.SESSION), list);

		doTest(null, null, "formPage",null, list, null, null);

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

		HashMap attribSession = new HashMap();
		HashMap attribRequest = new HashMap();

		attribRequest.put("method", "nextPage");

		attribRequest.put(
			ChooseContextDispatchAction.CURRICULAR_YEAR_PARAMETER,
			"2");
		attribRequest.put(
			ChooseContextDispatchAction.SEMESTER_PARAMETER,
			"2");
		attribRequest.put(
			ChooseContextDispatchAction.INFO_DEGREE_INITIALS_PARAMETER,
			"L1");

		attribSession.put(
			SessionConstants.SEMESTER_LIST_KEY,
			new ArrayList());
		attribSession.put(
			SessionConstants.CURRICULAR_YEAR_LIST_KEY,
			new ArrayList());
		attribSession.put(
			SessionConstants.INFO_DEGREE_LIST_KEY,
			_infoDegreeList);

		List attributesList = new ArrayList();
		attributesList.add(SessionConstants.CONTEXT_KEY);
		attributesList.add(SessionConstants.SEMESTER_LIST_KEY);
		attributesList.add(SessionConstants.CURRICULAR_YEAR_LIST_KEY);
		attributesList.add(SessionConstants.INFO_DEGREE_LIST_KEY);

		HashMap list = new HashMap();
		list.put(new Integer(ScopeConstants.SESSION), list);

		doTest(attribSession, attribRequest, "nextPage",null, list, null, null);

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

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/chooseClassSearchContextDA";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return moduleName;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/tests/web-publico.xml";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getAuthorizedRolesCollection()
	 */
	public Collection getAuthorizedRolesCollection() {
	
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
	
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
	
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		
		return null;
	}

}
