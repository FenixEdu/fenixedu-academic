package Dominio;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */

public class RestrictionCurricularCourseNotDone	extends RestrictionByCurricularCourse implements IRestrictionByCurricularCourse {

	public RestrictionCurricularCourseNotDone() {
		super();
	}

	public boolean evaluate(EnrolmentContext enrolmentContext) {
		return !enrolmentContext.isCurricularCourseDone(this.getPrecedentCurricularCourse());
	}
}