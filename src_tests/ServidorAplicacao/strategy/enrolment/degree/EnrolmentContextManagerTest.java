/*
 * Created on 4/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class EnrolmentContextManagerTest extends TestCaseServicos {
	ISuportePersistente sp = null;
	IStudentCurricularPlanPersistente studentCurricularPlanDAO = null;
	/**
	 * @param testName
	 */
	public EnrolmentContextManagerTest(String testName) {
		super(testName);

	}

	/**
	 * it must:
	 * 		initialize acumulated enrolments...
	 * 		initialize final span with not yet done courses.
	 * 		initialize curricular courses done
	 */
	public void testSucessfullCreationOfStrategy() {
//		EnrolmentContext enrolmentContext = null;
//		IStudentCurricularPlan studentCurricularPlan = null;
//		try {
//			sp.iniciarTransaccao();
//			studentCurricularPlan =
//				studentCurricularPlanDAO.readActiveStudentCurricularPlan(
//					new Integer(600),
//					new TipoCurso(TipoCurso.LICENCIATURA));
//			sp.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia e1) {
//			e1.printStackTrace(System.out);
//			fail("Reading active student curricular plan!");
//		}
//		assertNotNull(
//			"Student curricular plan must be not null!",
//			studentCurricularPlan);
//
//		try {
//			sp.iniciarTransaccao();
//			enrolmentContext =
//				EnrolmentContextManager.initialEnrolmentContext(
//					studentCurricularPlan.getStudent());
//			sp.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia e2) {
//			e2.printStackTrace(System.out);
//			fail("Getting initial enrolment Context");
//		} catch (OutOfCurricularCourseEnrolmentPeriod e) {
//			e.printStackTrace();
//			fail("No valid enrolment period!");
//		}
//
//		IEnrolmentStrategyFactory enrolmentStrategyFactory =
//			EnrolmentStrategyFactory.getInstance();
//		IEnrolmentStrategy enrolmentStrategy = null;
//		try {
//			sp.iniciarTransaccao();
//
//			enrolmentStrategy =
//				enrolmentStrategyFactory.getEnrolmentStrategyInstance(
//					enrolmentContext);
//			sp.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia e) {
//			e.printStackTrace();
//		}
//		EnrolmentContext strategyEnrolmentContext =
//			enrolmentStrategy.getEnrolmentContext();
//
//		assertEquals(
//			"Student not equal!",
//			enrolmentContext.getStudent(),
//			strategyEnrolmentContext.getStudent());
//		assertEquals(
//			"Semester not equal!",
//			enrolmentContext.getSemester(),
//			strategyEnrolmentContext.getSemester());
//
//		CurricularCourse curricularCourse = new CurricularCourse();
//		curricularCourse.setDegreeCurricularPlan(
//			studentCurricularPlan.getDegreeCurricularPlan());
//		curricularCourse.setCode("AMII");
//		curricularCourse.setName("Analise Matematica II");
//		assertEquals(
//			"AMII",
//			new Integer(2),
//			strategyEnrolmentContext.getCurricularCourseAcumulatedEnrolments(
//				curricularCourse));
//
//		curricularCourse = new CurricularCourse();
//		curricularCourse.setDegreeCurricularPlan(
//			studentCurricularPlan.getDegreeCurricularPlan());
//		curricularCourse.setCode("TFCI");
//		curricularCourse.setName("Trabalho Final de Curso I");
//		assertEquals(
//			"TFCI",
//			new Integer(1),
//			strategyEnrolmentContext.getCurricularCourseAcumulatedEnrolments(
//				curricularCourse));
//
//		curricularCourse = new CurricularCourse();
//		curricularCourse.setDegreeCurricularPlan(
//			studentCurricularPlan.getDegreeCurricularPlan());
//		curricularCourse.setCode("AMI");
//		curricularCourse.setName("Analise Matematica I");
//		assertEquals(
//			"AMI",
//			new Integer(1),
//			strategyEnrolmentContext.getCurricularCourseAcumulatedEnrolments(
//				curricularCourse));
//
//		List doneCourses = enrolmentContext.getEnrolmentsAprovedByStudent();
//		assertNotNull("curricular courses done is null!", doneCourses);
//		assertEquals("Curricular courses done!", 0, doneCourses.size());
//
//		List finalSpan =
//			enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
//		assertNotNull("Final span is null!", finalSpan);
//		assertEquals("Final span size!", 8, finalSpan.size());
//		assertNotNull(enrolmentContext.getStudentActiveCurricularPlan());
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentContextManagerTest.class);
		return suite;
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() {
		super.setUp();
		try {
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Error getting persistent factory");
		}
		studentCurricularPlanDAO = sp.getIStudentCurricularPlanPersistente();
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServicos#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetEnrolmentContextManager.xml";
	}

}
