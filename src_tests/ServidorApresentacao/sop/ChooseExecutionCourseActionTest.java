package ServidorApresentacao.sop;
  
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * ChooseExecutionCourseActionTest.java
 * 
 * @author Ivo Brandão
 */
public class ChooseExecutionCourseActionTest extends TestCasePresentation {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ChooseExecutionCourseActionTest.class);

		return suite;
	}
	
	public void setUp() throws Exception {
		super.setUp();

		//defines struts config file to use
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");
	}

	public ChooseExecutionCourseActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulChooseExecutionCourse() {

		//required to put form escolherDisciplinaExecucaoForm in session
		setRequestPathInfo("/sop", "/escolherDisciplinaExecucaoForm");
		addRequestParameter("courseInitials", "TFCI");

		//create execution period
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);

		getSession().setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

		//curricular year
		Integer curricularYear = new Integer(2);
		getSession().setAttribute(SessionConstants.CURRICULAR_YEAR_KEY, curricularYear);

		//execution degree
		InfoDegree infoDegree = new InfoDegree("LEIC", "Licenciatura de Engenharia Informatica e de Computadores");
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1", infoDegree);
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
		getSession().setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);

		//privileges
		HashSet privileges = new HashSet();
		privileges.add("LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");		
		IUserView userView = new UserView("user", privileges);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);
				
		//list of EXECUTION_COURSE_LIST_KEY
		try {
		Object[] args = { infoExecutionDegree, infoExecutionPeriod, curricularYear };

		ArrayList infoCourseList =
			(ArrayList) ServiceUtils.executeService(
				userView,
				"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
				args);
		
		getSession().setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, infoCourseList);
		} catch (Exception exception){
			exception.printStackTrace(System.out);
			fail("testSuccessfulChooseExecutionCourse - executing service");
		}
		
		//perform
		actionPerform();

		//checks for errors
		verifyNoActionErrors();

		//checks forward
		verifyForward("forwardChoose"); 
	}

	public void testUnSuccessfulChooseExecutionCourse() {

		//required to put form escolherDisciplinaExecucaoForm in session
		setRequestPathInfo("/sop", "/escolherDisciplinaExecucaoForm");
		addRequestParameter("courseInitials", "");

		//create execution period
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);

		getSession().setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

		//curricular year
		Integer curricularYear = new Integer(2);
		getSession().setAttribute(SessionConstants.CURRICULAR_YEAR_KEY, curricularYear);

		//execution degree
		InfoDegree infoDegree = new InfoDegree("LEIC", "Licenciatura de Engenharia Informatica e de Computadores");
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1", infoDegree);
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
		getSession().setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);

		//privileges
		HashSet privileges = new HashSet();
		privileges.add("LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");		
		IUserView userView = new UserView("user", privileges);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);
				
		//list of EXECUTION_COURSE_LIST_KEY
		try {
		Object[] args = { infoExecutionDegree, infoExecutionPeriod, curricularYear };

		ArrayList infoCourseList =
			(ArrayList) ServiceUtils.executeService(
				userView,
				"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
				args);
		
		getSession().setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, infoCourseList);
		} catch (Exception exception){
			exception.printStackTrace(System.out);
			fail("testUnSuccessfulChooseExecutionCourse - executing service");
		}
		
		//perform
		actionPerform();

		//checks for errors
		verifyNoActionErrors();

		//checks forward
		verifyForward("showForm"); 
	}
}
