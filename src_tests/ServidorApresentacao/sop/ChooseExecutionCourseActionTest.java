package ServidorApresentacao.sop;
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorApresentacao.TestCaseActionExecution;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * ChooseExecutionCourseActionTest.java
 * 
 * @author Ivo Brandão
 */
public class ChooseExecutionCourseActionTest extends TestCaseActionExecution {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ChooseExecutionCourseActionTest.class);
		return suite;
	}
	
	public void setUp() {
		super.setUp();
	}

	public ChooseExecutionCourseActionTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {

		HashMap items = new HashMap();
		items.put("courseInitials", "TFCI");
		return items;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		HashMap items = new HashMap();
		items.put("courseInitials", "");
		return items;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {

		HashMap items = new HashMap();

		// create execution period
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);

		items.put(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

		// curricular year
		Integer curricularYear = new Integer(2);
		items.put(SessionConstants.CURRICULAR_YEAR_KEY, curricularYear);

		// execution degree
		InfoDegree infoDegree = new InfoDegree("LEIC", "Licenciatura de Engenharia Informatica e de Computadores");
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1", infoDegree);
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
		items.put(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);

		// list of EXECUTION_COURSE_LIST_KEY
		try {
			Object[] args = { infoExecutionDegree, infoExecutionPeriod, curricularYear };
			ArrayList infoCourseList = (ArrayList) ServiceUtils.executeService(userView, "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", args);
			items.put(SessionConstants.EXECUTION_COURSE_LIST_KEY, infoCourseList);
		} catch (Exception exception){
			exception.printStackTrace(System.out);
			fail("testSuccessfulChooseExecutionCourse - executing service");
		}

		return items;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {

		HashMap items = new HashMap();

		// create execution period
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);

		items.put(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

		// curricular year
		Integer curricularYear = new Integer(2);
		items.put(SessionConstants.CURRICULAR_YEAR_KEY, curricularYear);

		// execution degree
		InfoDegree infoDegree = new InfoDegree("LEIC", "Licenciatura de Engenharia Informatica e de Computadores");
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1", infoDegree);
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
		items.put(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);

		// list of EXECUTION_COURSE_LIST_KEY
		try {
			Object[] args = { infoExecutionDegree, infoExecutionPeriod, curricularYear };
			ArrayList infoCourseList = (ArrayList) ServiceUtils.executeService(userView, "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", args);
			items.put(SessionConstants.EXECUTION_COURSE_LIST_KEY, infoCourseList);
		} catch (Exception exception){
			exception.printStackTrace(System.out);
			fail("testUnSuccessfulChooseExecutionCourse - executing service");
		}

		return items;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/escolherDisciplinaExecucaoForm";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "/sop";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/tests/web-sop.xml";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "forwardChoose";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getUnsuccessfulForward()
	 */
	protected String getUnsuccessfulForward() {
		return "showForm";
	}

}
