package ServidorAplicacao.Servicos.enrolment.degree;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import framework.factory.ServiceManagerServiceFactory;

public class SelectOptionalCurricularCourseTest extends TestCaseReadServices {
	
	public SelectOptionalCurricularCourseTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SelectOptionalCurricularCourseTest.class);

		return suite;
	}

	protected void setUp() {
		this.argsAutenticacao1[0] = "3";
		this.argsAutenticacao1[1] = "pass";
		this.argsAutenticacao1[2] = getApplication();
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "SelectOptionalCurricularCourse";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		return null;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		return null;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}
	
	public void testSelectOptionalCurricularCourseServiceRun() {

		Object args[] = {_userView};
		InfoEnrolmentContext result = null;
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, "ShowAvailableCurricularCourses", args);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}

		Object args2[] = {result};
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, "ShowAvailableDegreesForOption", args2);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}
		
		result.setChosenOptionalInfoDegree((InfoDegree) result.getInfoDegreesForOptionalCurricularCourses().get(0));
		
		Object args3[] = {result};
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, "ShowAvailableCurricularCoursesForOption", args3);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}

		result.setInfoChosenOptionalCurricularCourseScope(((InfoCurricularCourseScope) result.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(0)));

		Object args4[] = { result, (InfoCurricularCourse) result.getOptionalInfoCurricularCoursesToChooseFromDegree().get(0) };

		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args4);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}
		
		InfoEnrolmentInOptionalCurricularCourse optionalEnrolment = (InfoEnrolmentInOptionalCurricularCourse) result.getInfoOptionalCurricularCoursesEnrolments().get(0);
		
		ICurricularCourse curricularCourse = getCurricularCourse("INTERFACES PESSOA-MÁQUINA", "");
		InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
		assertEquals("Course Chosen For Option:", optionalEnrolment.getInfoCurricularCourseForOption(), infoCurricularCourse);

		curricularCourse = getCurricularCourse("OPÇÃO I", "");
		infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
//		assertEquals("Optional Scope Selected: ", optionalEnrolment.getInfoCurricularCourseScope().getInfoCurricularCourse(),infoCurricularCourse);
	}
	
	public ICurricularCourse getCurricularCourse (String name, String code){
		ISuportePersistente sp = null;
		ICurricularCourse curricularCourse = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Getting persistent layer!");
		}

		IPersistentCurricularCourse curricularCourseDAO = sp.getIPersistentCurricularCourse();
		try {
			sp.iniciarTransaccao();
			curricularCourse = curricularCourseDAO.readCurricularCourseByNameAndCode(name, code);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Reading curricular course! (name:"+name+";code:"+code);
		}
		assertNotNull("Reading curricular course (name:"+name+";code:"+code+"!",curricularCourse);

		return curricularCourse;
	}

}