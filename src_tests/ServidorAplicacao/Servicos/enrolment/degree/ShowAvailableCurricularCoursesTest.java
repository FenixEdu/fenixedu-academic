package ServidorAplicacao.Servicos.enrolment.degree;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ShowAvailableCurricularCoursesTest extends TestCaseReadServices {
	
	public ShowAvailableCurricularCoursesTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ShowAvailableCurricularCoursesTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ShowAvailableCurricularCourses";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		return null;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 4;
	}

	protected Object getObjectToCompare() {
		return null;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentServicesLERCIDataSet.xml";
	}
	
	public void testShowAvailableCurricularCoursesServiceRun() {

		List finalSpan = new ArrayList();

		Object args[] = {_userView, new Integer(1)};
		InfoEnrolmentContext result = null;
		try {
			result = (InfoEnrolmentContext) _gestor.executar(_userView, getNameOfServiceToBeTested(), args);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}

		finalSpan = result.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();

		assertEquals("Final span size:",  this.getNumberOfItemsToRetrieve(), finalSpan.size());

		ICurricularCourse curricularCourse = getCurricularCourse("SISTEMAS OPERATIVOS", "");
		ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(0);
		InfoCurricularCourseScope infoCurricularCourseScope = Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
		assertEquals(true, finalSpan.contains(infoCurricularCourseScope));

		curricularCourse = getCurricularCourse("BASES DE DADOS", "");
		curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(0);
		infoCurricularCourseScope = Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
		assertEquals(true, !finalSpan.contains(curricularCourseScope));

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