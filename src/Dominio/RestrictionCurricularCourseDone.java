package Dominio;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */

public class RestrictionCurricularCourseDone extends RestrictionByCurricularCourse implements IRestrictionByCurricularCourse {

	public RestrictionCurricularCourseDone() {
		super();
	}

	public boolean evaluate(EnrolmentContext enrolmentContext) {
		return enrolmentContext.isCurricularCourseDone(this.getPrecedentCurricularCourse());
	}
}