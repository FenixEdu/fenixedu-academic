package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.CurricularCourse;
import Dominio.EnrolmentEvaluation;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class EnrolmentEvaluationOJBTest extends TestCaseOJB {

	private SuportePersistenteOJB persistentSupport = null;

	private IPersistentExecutionPeriod executionPeriodDAO = null;
	private IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
	private IPersistentCurricularCourseScope persistentCurricularCourseScope = null;

	private IPersistentCurricularCourse persistentCurricularCourse = null;
	private IPersistentCurricularYear persistentCurricularYear = null;
	private IPersistentCurricularSemester persistentCurricularSemester = null;

	private IPersistentEnrolment persistentEnrolment = null;
	private IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = null;

	public EnrolmentEvaluationOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println("Beginning of test from class EnrolmentEvaluationOJB.\n");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of test from class EnrolmentEvaluationOJB.\n");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentEvaluationOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error in SetUp.");
		}
		executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();
		persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
		persistentCurricularCourseScope = persistentSupport.getIPersistentCurricularCourseScope();

		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
		persistentCurricularYear = persistentSupport.getIPersistentCurricularYear();
		persistentCurricularSemester = persistentSupport.getIPersistentCurricularSemester();

		persistentEnrolment = persistentSupport.getIPersistentEnrolment();
		persistentEnrolmentEvaluation = persistentSupport.getIPersistentEnrolmentEvaluation();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testWriteEnrolmentEvaluation() {

//		ICurricularCourse curricularCourse = null;
//		IStudentCurricularPlan studentCurricularPlan = null;
//		IExecutionPeriod executionPeriod = null;

		System.out.println("\n- Test 1.1 : Write Existing EnrolmentEvaluation\n");



		IEnrolmentEvaluation enrolmentEvaluation = getExistingEnrolmentEvaluation();
		try {
			persistentSupport.iniciarTransaccao();
			persistentEnrolment.lockWrite(enrolmentEvaluation);
			persistentSupport.confirmarTransaccao();
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing EnrolmentEvaluation");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing EnrolmentEvaluation");
		}



//		enrolmentEvaluation = getNonExistingEnrolmentEvaluation();
//
//		System.out.println("\n- Test 1.2 : Write Non Existing Enrolment\n");
//		try {
//			persistentSupport.iniciarTransaccao();
//			persistentEnrolment.lockWrite(enrolmentEvaluation);
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex2) {
//			fail("Write Non Existing EnrolmentEvaluation");
//		}
//
//		IEnrolment enrolment2 = null;
//
//		try {
//			persistentSupport.iniciarTransaccao();
//			enrolment2 = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(enrolment.getStudentCurricularPlan(), enrolment.getCurricularCourseScope());
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("Reading Non Existing Enrolment Just Writen Before");
//		}
//
//		assertNotNull(enrolment2);
//
//		assertEquals("Enrolments not equal!", enrolmentEvaluation, enrolment2);

	}

	// -------------------------------------------------------------------------------------------------------------------------
//
//	public void testDeleteAllEnrolments() {
//
//		System.out.println("\n- Test 2 : Delete All Enrolments");
//		try {
//			persistentSupport.iniciarTransaccao();
//			persistentEnrolment.deleteAll();
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("Delete All Enrolments");
//		}
//
//		List result = null;
//
//		try {
//			persistentSupport.iniciarTransaccao();
//			result = persistentEnrolment.readAll();
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("Reading Result Of Deleting All Enrolments");
//		}
//
//		assertNotNull(result);
//		assertTrue(result.isEmpty());
//	}
//
//	// -------------------------------------------------------------------------------------------------------------------------
//
//	public void testReadEnrolment() {
//
////		ICurricularCourse curricularCourse = null;
////		IStudentCurricularPlan studentCurricularPlan = null;
//
//		System.out.println("\n- Test 3.1 : Read Existing Enrolment\n");
//
////		try {
////			persistentSupport.iniciarTransaccao();
////			curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Trabalho Final de Curso I", "TFCI");
////			studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(45498), new TipoCurso(TipoCurso.LICENCIATURA));
////			persistentSupport.confirmarTransaccao();
////		} catch (ExcepcaoPersistencia ex) {
////			fail("Reading CurricularCourse & StudentCurricularPlan");
////		}
////
////		assertNotNull(curricularCourse);
////		assertNotNull(studentCurricularPlan);
//
//		// Enrolment ja existente
//		IEnrolment enrolment = null;
//		IEnrolment enrolment2 = getExistingEnrolment();
//		try {
//			persistentSupport.iniciarTransaccao();
//			enrolment = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(enrolment2.getStudentCurricularPlan(), enrolment2.getCurricularCourseScope());
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex2) {
//			fail("Read Existing Enrolment");
//		}
//		assertNotNull(enrolment);
//		assertTrue(enrolment.getCurricularCourseScope().equals(enrolment2.getCurricularCourseScope()));
//		assertTrue(enrolment.getStudentCurricularPlan().equals(enrolment2.getStudentCurricularPlan()));
//
//		// Enrolment inexistente
////		try {
////			persistentSupport.iniciarTransaccao();
////			curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Trabalho Final de Curso II", "TFCII");
////			studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
////			persistentSupport.confirmarTransaccao();
////		} catch (ExcepcaoPersistencia ex) {
////			fail("Reading CurricularCourse & StudentCurricularPlan");
////		}
////
////		assertNotNull(curricularCourse);
////		assertNotNull(studentCurricularPlan);
//
//		enrolment = null;
//		enrolment2 = getNonExistingEnrolment();
//		System.out.println("\n- Test 3.2 : Read Non Existing Enrolment");
//		try {
//			persistentSupport.iniciarTransaccao();
//			enrolment = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(enrolment2.getStudentCurricularPlan(), enrolment2.getCurricularCourseScope());
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex2) {
//			fail("Read Non Existing Enrolment");
//		}
//		assertNull(enrolment);
//	}
//
//	// -------------------------------------------------------------------------------------------------------------------------
//
//	public void testDeleteEnrolment() {
//
////		ICurricularCourse curricularCourse = null;
////		IStudentCurricularPlan studentCurricularPlan = null;
////
////		try {
////			persistentSupport.iniciarTransaccao();
////			curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Trabalho Final de Curso I", "TFCI");
////			studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(45498), new TipoCurso(TipoCurso.LICENCIATURA));
////			persistentSupport.confirmarTransaccao();
////		} catch (ExcepcaoPersistencia ex) {
////			fail("Reading CurricularCourse & StudentCurricularPlan");
////		}
////
////		assertNotNull(curricularCourse);
////		assertNotNull(studentCurricularPlan);
//
//		// Enrolment ja existente
//		System.out.println("\n- Test 4.1 : Delete Existing Enrolment\n");
//		IEnrolment enrolment = null;
//		IEnrolment enrolment2 = getExistingEnrolment();
//		try {
//			persistentSupport.iniciarTransaccao();
//			enrolment = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(enrolment2.getStudentCurricularPlan(), enrolment2.getCurricularCourseScope());
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("Reading Existing Enrolment To Delete");
//		}
//		assertNotNull(enrolment);
//
//		try {
//			persistentSupport.iniciarTransaccao();
//			persistentEnrolment.delete(enrolment);
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex3) {
//			fail("Delete Existing Enrolment");
//		}
//
//		IEnrolment enr2 = null;
//		try {
//			persistentSupport.iniciarTransaccao();
//			enr2 = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(enrolment2.getStudentCurricularPlan(), enrolment2.getCurricularCourseScope());
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("Reading Just Deleted Enrolment");
//		}
//		assertNull(enr2);
//
//		// Enrolment inexistente
//		System.out.println("\n- Test 4.2 : Delete Non Existing Enrolment\n");
//		try {
//			persistentSupport.iniciarTransaccao();
//			persistentEnrolment.delete(new Enrolment());
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex2) {
//			fail("Delete Existing Enrolment");
//		}
//	}
//
//	// -------------------------------------------------------------------------------------------------------------------------
//
//	public void testReadAllEnrolments() {
//
//		List list = null;
//
//		System.out.println("\n- Test 5 : Read All Existing Enrolment\n");
//		try {
//			persistentSupport.iniciarTransaccao();
//			list = persistentEnrolment.readAll();
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex2) {
//			fail("Read All Enrolments");
//		}
//		assertNotNull(list);
//		assertEquals(6, list.size());
//	}
//
//	// -------------------------------------------------------------------------------------------------------------------------
//
//	public void testReadEnrolmentByStudentCurricularPlanAndEnrolmentState() {
//
//		List list = null;
//
//		IStudentCurricularPlan studentCurricularPlan = null;
//
//		System.out.println("\n- Test 6 : Read Existing Enrolment By StudentCurricularPlan And EnrolmentState\n");
//
//		try {
//			persistentSupport.iniciarTransaccao();
//			studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(45498), new TipoCurso(TipoCurso.LICENCIATURA));
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("Reading StudentCurricularPlan");
//		}
//
//		assertNotNull(studentCurricularPlan);
//
//		// Enrolment ja existente
//		try {
//			persistentSupport.iniciarTransaccao();
//			list = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlan, EnrolmentState.APROVED_OBJ);
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex2) {
//			fail("Read Existing Enrolment");
//		}
//		assertNotNull(list);
//		assertEquals(1, list.size());
//	}
//
//	public void testReadAllByStudentCurricularPlan() {
//		IStudentCurricularPlan studentCurricularPlan = null;
//		try {
//			persistentSupport.iniciarTransaccao();
//			studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("Reading CurricularCourse & StudentCurricularPlan");
//		}
//		assertNotNull("Can't find 4598 student curricular plan!", studentCurricularPlan);
//		List enrolments = null;
//		try {
//			persistentSupport.iniciarTransaccao();
//			enrolments = persistentEnrolment.readAllByStudentCurricularPlan(studentCurricularPlan);
//			persistentEnrolment.readAllByStudentCurricularPlan(studentCurricularPlan);
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia e) {
//			fail("Reading all enrolments by student!");
//		}
//		assertNotNull("Enrolments must be not null!", enrolments);
//		assertEquals(1, enrolments.size());
//	}

	private IEnrolmentEvaluation getExistingEnrolmentEvaluation() {
		try {
			persistentSupport.iniciarTransaccao();

			IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(46865), new TipoCurso(TipoCurso.LICENCIATURA));
			IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
			IBranch branch = studentCurricularPlan.getBranch();
			ICurricularCourse curricularCourseCriteria = new CurricularCourse();
			curricularCourseCriteria.setName("Trabalho Final de Curso II");
			curricularCourseCriteria.setCode("TFCII");
//			curricularCourseCriteria.setDegreeCurricularPlan(degreeCurricularPlan);
			ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseCriteria);
			ICurricularYear curricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(1));
			ICurricularSemester curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(new Integer(1), curricularYear);
			ICurricularCourseScope curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(curricularCourse, curricularSemester, branch, null);
			IEnrolment enrolment = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(studentCurricularPlan,curricularCourseScope);
			assertNotNull(studentCurricularPlan);
			assertNotNull(degreeCurricularPlan);
			assertNotNull(branch);
			assertNotNull(curricularCourse);
			assertNotNull(curricularYear);
			assertNotNull(curricularSemester);
			assertNotNull(curricularCourseScope);
			assertNotNull(enrolment);				
//			IEnrolmentEvaluation enrolmentEvaluation = persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(enrolment,evaluationType,"14");
			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			enrolmentEvaluation.setGrade("14");
			enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
			enrolmentEvaluation.setEnrolment(enrolment);
			enrolmentEvaluation.setEnrolmentEvaluationState(new EnrolmentEvaluationState(EnrolmentEvaluationState.FINAL));
	
			return enrolmentEvaluation;
		} catch (ExcepcaoPersistencia ex) {
			fail("Loading Enrolments from DB.");
		}
		return null;
	}

	private IEnrolmentEvaluation getNonExistingEnrolmentEvaluation() {
		try {
			persistentSupport.iniciarTransaccao();

			IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(45498), new TipoCurso(TipoCurso.LICENCIATURA));
			IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
			IBranch branch = studentCurricularPlan.getBranch();
			ICurricularCourse curricularCourseCriteria = new CurricularCourse();
			curricularCourseCriteria.setName("Cadeira Que Não Tem Enrolment 1");
			curricularCourseCriteria.setCode("CNE1");
//			curricularCourseCriteria.setDegreeCurricularPlan(degreeCurricularPlan);
			ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseCriteria);
			ICurricularYear curricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(1));
			ICurricularSemester curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(new Integer(1), curricularYear);
			ICurricularCourseScope curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(curricularCourse, curricularSemester, branch, null);
			IEnrolment enrolment = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(studentCurricularPlan,curricularCourseScope);
			assertNotNull(studentCurricularPlan);
			assertNotNull(degreeCurricularPlan);
			assertNotNull(branch);
			assertNotNull(curricularCourse);
			assertNotNull(curricularYear);
			assertNotNull(curricularSemester);
			assertNotNull(curricularCourseScope);
			assertNotNull(enrolment);
		    EnrolmentEvaluationType evaluationType = EnrolmentEvaluationType.NORMAL_OBJ;
			IEnrolmentEvaluation enrolmentEvaluation = persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(enrolment,evaluationType,"14");
			assertNotNull(enrolmentEvaluation);

			persistentSupport.confirmarTransaccao();
			
			return enrolmentEvaluation;
		} catch (ExcepcaoPersistencia ex) {
			fail("Loading Enrolments from DB.");
		}
		return null;
	}
}