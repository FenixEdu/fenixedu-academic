/*
 * Created on 4/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.Servicos.strategy.enrolment.degree;

import java.util.HashMap;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.CollectionUtils;

import Dominio.CurricularCourse;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class StrategyFactoryTest extends TestCaseServicos {
	ISuportePersistente sp = null;
	IStudentCurricularPlanPersistente studentCurricularPlanDAO = null;
	/**
	 * @param testName
	 */
	public StrategyFactoryTest(String testName) {
		super(testName);

	}

	/**
	 * it must:
	 * 		initialize acumulated enrolments...
	 * 		initialize final span with not yet done courses.
	 * 		initialize curricular courses done
	 */
	public void testSucessfullCreationOfStrategy() {
		EnrolmentContext enrolmentContext = new EnrolmentContext();
		IStudentCurricularPlan studentCurricularPlan = null;
		try {
			sp.iniciarTransaccao();
			studentCurricularPlan =
				studentCurricularPlanDAO.readActiveStudentCurricularPlan(
					new Integer(600),
					new TipoCurso(TipoCurso.LICENCIATURA));
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace(System.out);
			fail("Reading active student curricular plan!");
		}
		assertNotNull(
			"Student curricular plan must be not null!",
			studentCurricularPlan);

		enrolmentContext.setSemester(new Integer(2));
		enrolmentContext.setStudent(studentCurricularPlan.getStudent());
		enrolmentContext.setDegree(
			studentCurricularPlan.getDegreeCurricularPlan().getDegree());
		IEnrolmentStrategy enrolmentStrategy = null;
		try {
			sp.iniciarTransaccao();
			
			enrolmentStrategy =
				EnrolmentStrategyFactory.getEnrolmentStrategyInstance(
					EnrolmentStrategyFactory.LERCI,
					enrolmentContext);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
		EnrolmentContext strategyEnrolmentContext =
			enrolmentStrategy.getEnrolmentContext();

		assertEquals(
			"Student not equal!",
			enrolmentContext.getStudent(),
			strategyEnrolmentContext.getStudent());
		assertEquals(
			"Semester not equal!",
			enrolmentContext.getSemester(),
			strategyEnrolmentContext.getSemester());
		assertEquals(
			"Degree not equal!",
			enrolmentContext.getDegree(),
			strategyEnrolmentContext.getDegree());

		HashMap acumulatedEnrolments =
			(HashMap) strategyEnrolmentContext.getAcumulatedEnrolments();

		assertNotNull("Acumulated enrolments is null!", acumulatedEnrolments);

		assertEquals(3, acumulatedEnrolments.size());

		CurricularCourse curricularCourse = new CurricularCourse();
		curricularCourse.setDegreeCurricularPlan(
			studentCurricularPlan.getDegreeCurricularPlan());
		curricularCourse.setCode("AMII");
		curricularCourse.setName("Analise Matematica II");
		assertEquals(
			"AMII",
			new Integer(2),
			acumulatedEnrolments.get(curricularCourse));

		curricularCourse = new CurricularCourse();
		curricularCourse.setDegreeCurricularPlan(
			studentCurricularPlan.getDegreeCurricularPlan());
		curricularCourse.setCode("TFCI");
		curricularCourse.setName("Trabalho Final de Curso I");
		assertEquals(
			"TFCI",
			new Integer(1),
			acumulatedEnrolments.get(curricularCourse));

		curricularCourse = new CurricularCourse();
		curricularCourse.setDegreeCurricularPlan(
			studentCurricularPlan.getDegreeCurricularPlan());
		curricularCourse.setCode("AMI");
		curricularCourse.setName("Analise Matematica I");
		assertEquals(
			"AMI",
			new Integer(1),
			acumulatedEnrolments.get(curricularCourse));

		List doneCourses = enrolmentContext.getCurricularCoursesDoneByStudent();
		assertNotNull("curricular courses done is null!", doneCourses);
		assertEquals("Curricular courses done!", 0, doneCourses.size());

		List finalSpan =
			enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled();
		assertNotNull("Final span is null!", finalSpan);
		assertEquals("Final span size!", 8, finalSpan.size());
		assertEquals("Final span is wrong!",
		true,
		CollectionUtils.isEqualCollection(studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourses(), finalSpan));
		
		assertNotNull(enrolmentContext.getStudentActiveCurricularPlan());
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(StrategyFactoryTest.class);
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

}
