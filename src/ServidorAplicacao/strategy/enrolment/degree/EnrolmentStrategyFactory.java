package ServidorAplicacao.strategy.enrolment.degree;

import ServidorAplicacao.strategy.enrolment.degree.strategys.EnrolmentStrategyLERCI;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentStrategyFactory {

	private static IEnrolmentStrategy strategyInstance = null;

	private static SuportePersistenteOJB persistentSupport = null;
	private static IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
	private static IPersistentCurricularCourse persistentCurricularCourse = null;
	private static IPersistentCurricularCourseScope persistentCurricularCourseScope = null;

	private static IPersistentEnrolment persistentEnrolment = null;

	public static synchronized IEnrolmentStrategy getEnrolmentStrategyInstance(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {
		if (enrolmentContext.getStudent() == null)
			throw new IllegalArgumentException("Must initialize student in context!");

		if (enrolmentContext.getSemester() == null)
			throw new IllegalArgumentException("Must initialize semester in context!");
			
		if (enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled() == null)
			throw new IllegalArgumentException("Must initialize FinalCurricularCoursesScopesSpanToBeEnrolled in context!");

		if (enrolmentContext.getStudentActiveCurricularPlan() == null)
			throw new IllegalArgumentException("Must initialize StudentActiveCurricularPlan in context!");

		if (strategyInstance == null) {
			String degree = enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan().getDegree().getSigla();
			
			if (degree.equals("LERCI")) {
				strategyInstance = new EnrolmentStrategyLERCI();
				strategyInstance.setEnrolmentContext(enrolmentContext);
			}
		}
		return strategyInstance;
	}

	public static synchronized void resetInstance() {
		if (strategyInstance != null) {
			strategyInstance = null;
		}
	}
}