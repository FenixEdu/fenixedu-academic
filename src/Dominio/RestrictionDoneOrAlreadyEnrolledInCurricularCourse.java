package Dominio;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */

public class RestrictionDoneOrAlreadyEnrolledInCurricularCourse extends RestrictionCurricularCourseDone implements IRestrictionByCurricularCourse {

	public RestrictionDoneOrAlreadyEnrolledInCurricularCourse() {
		super();
	}

	public boolean evaluate(EnrolmentContext enrolmentContext) {
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
		return super.evaluate(enrolmentContext) || enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue() > 0;
	}
}
