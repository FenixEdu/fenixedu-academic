package ServidorAplicacao.strategy.enrolment.rules;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentFilterTFCRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		int size1 = enrolmentContext.getEnrolmentsAprovedByStudent().size();
		int size2 = enrolmentContext.getCurricularCoursesFromStudentCurricularPlan().size();
		
		if ((size2 - size1) > 14) {
			for(int i = 0; i < enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().size(); i++) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
				if (curricularCourseScope.getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.TFC_COURSE))) {
					enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().remove(curricularCourseScope);
				}
			}
		}

		return enrolmentContext;
	}
}