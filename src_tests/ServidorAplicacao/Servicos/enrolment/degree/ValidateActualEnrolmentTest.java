package ServidorAplicacao.Servicos.enrolment.degree;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoCurricularCourseScope;
import Dominio.ICurricularCourse;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import framework.factory.ServiceManagerServiceFactory;

public class ValidateActualEnrolmentTest extends TestCaseReadServices {

	public ValidateActualEnrolmentTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ValidateActualEnrolmentTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ValidateActualEnrolment";
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

	public void testValidateActualEnrolmentServiceRun() {

		Object args[] = { _userView };
		InfoEnrolmentContext result = null;
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, "ShowAvailableCurricularCourses", args);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}

		// tudo ok
		result.getActualEnrolment().clear();
		for (int i = 0; i < 6; i++) {
			InfoCurricularCourseScope infoCurricularCourseScope =	(InfoCurricularCourseScope) result.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			result.getActualEnrolment().add(infoCurricularCourseScope);
		}

		Object args2[] = { result };
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args2);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}
		assertEquals(result.getEnrolmentValidationResult().isSucess(), true);
		result.getEnrolmentValidationResult().setSucess(true);

		// menos que 3
		result.getActualEnrolment().clear();
		for (int i = 0; i < 2; i++) {
			InfoCurricularCourseScope infoCurricularCourseScope =	(InfoCurricularCourseScope) result.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			result.getActualEnrolment().add(infoCurricularCourseScope);
		}

		Object args3[] = { result };
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args3);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}

		assertEquals(result.getEnrolmentValidationResult().isSucess(), false);
		result.getEnrolmentValidationResult().setSucess(true);

		// mais que 7
		result.getActualEnrolment().clear();
		for (int i = 0; i < 6; i++) {
			InfoCurricularCourseScope infoCurricularCourseScope =	(InfoCurricularCourseScope) result.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			result.getActualEnrolment().add(infoCurricularCourseScope);
		}
		for (int i = 0; i < 5; i++) {
			InfoCurricularCourseScope infoCurricularCourseScope =	(InfoCurricularCourseScope) result.getInfoCurricularCoursesScopesAutomaticalyEnroled().get(i);
			result.getActualEnrolment().add(infoCurricularCourseScope);
		}

		Object args4[] = { result };
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args4);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}

		assertEquals(result.getEnrolmentValidationResult().isSucess(), false);
		result.getEnrolmentValidationResult().setSucess(true);

		// mais que 10 acumuladas
		result.getActualEnrolment().clear();
		for (int i = 0; i < 5; i++) {
			InfoCurricularCourseScope infoCurricularCourseScope =	(InfoCurricularCourseScope) result.getInfoCurricularCoursesScopesAutomaticalyEnroled().get(i);
			result.getActualEnrolment().add(infoCurricularCourseScope);
		}
		for (int i = 0; i < 2; i++) {
			InfoCurricularCourseScope infoCurricularCourseScope =	(InfoCurricularCourseScope) result.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			result.getActualEnrolment().add(infoCurricularCourseScope);
		}

		Object args5[] = { result };
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args5);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}
		assertEquals(result.getEnrolmentValidationResult().isSucess(), false);
		result.getEnrolmentValidationResult().setSucess(true);
		
		
		// anos curriculares anteriores ao maior ano nao escolhidos
		result.getActualEnrolment().clear();
		InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) result.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(5);
		result.getActualEnrolment().add(infoCurricularCourseScope);

		Object args6[] = { result };
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args6);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}
		assertEquals(result.getEnrolmentValidationResult().isSucess(), false);
		result.getEnrolmentValidationResult().setSucess(true);
	
	}

	public ICurricularCourse getCurricularCourse(String name, String code) {
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
			fail("Reading curricular course! (name:" + name + ";code:" + code);
		}
		assertNotNull("Reading curricular course (name:" + name + ";code:" + code + "!", curricularCourse);

		return curricularCourse;
	}

}