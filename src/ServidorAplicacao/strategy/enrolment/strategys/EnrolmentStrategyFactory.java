package ServidorAplicacao.strategy.enrolment.strategys;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.student.EnrolmentStrategyLARQ;
import ServidorAplicacao.strategy.enrolment.strategys.student.EnrolmentStrategyLEQ;
import ServidorAplicacao.strategy.enrolment.strategys.student.EnrolmentStrategyLERCI;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentStrategyFactory implements IEnrolmentStrategyFactory {

	private static EnrolmentStrategyFactory instance = null;

	private EnrolmentStrategyFactory() {
	}

	public static synchronized EnrolmentStrategyFactory getInstance() {
		if (instance == null) {
			instance = new EnrolmentStrategyFactory();
		}
		return instance;
	}

	public static synchronized void resetInstance() {
		if (instance != null) {
			instance = null;
		}
	}



	public IEnrolmentStrategy getEnrolmentStrategyInstance(EnrolmentContext enrolmentContext) {
		
		IEnrolmentStrategy strategyInstance = null;

		if (enrolmentContext.getStudent() == null)
			throw new IllegalArgumentException("Must initialize student in context!");

		if (enrolmentContext.getSemester() == null)
			throw new IllegalArgumentException("Must initialize semester in context!");
			
		if (enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled() == null)
			throw new IllegalArgumentException("Must initialize FinalCurricularCoursesScopesSpanToBeEnrolled in context!");

		if (enrolmentContext.getStudentActiveCurricularPlan() == null)
			throw new IllegalArgumentException("Must initialize StudentActiveCurricularPlan in context!");

		String degree = enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan().getDegree().getSigla();
		String degreeCurricularPlan = enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan().getName();
		// FIXME [DAVID]: O nome do plano curricular e estratégias tem de ser alterados
		if ( (degree.equals("LERCI")) && degreeCurricularPlan.equals("LERCI2003/2004")) {
			strategyInstance = new EnrolmentStrategyLERCI();
		} else if ( (degree.equals("LARQ")) && degreeCurricularPlan.equals("LARQ2003/2004")) {
			strategyInstance = new EnrolmentStrategyLARQ();
		} else if ( (degree.equals("LEQ")) && degreeCurricularPlan.equals("LEQ2003/2004")) {
			strategyInstance = new EnrolmentStrategyLEQ();
		}else{
			throw new IllegalArgumentException("Degree or DegreeCurricularPlan invalid!");
		}
		
		strategyInstance.setEnrolmentContext(enrolmentContext);
		return strategyInstance;
	}

}