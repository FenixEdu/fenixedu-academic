/*
 * Created on 12/Mai/2003 by jpvl
 *
 */
package Dominio;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */
public class RestrictionDoneOrAlreadyEnrolledInCurricularCourse extends RestrictionCurricularCourseDone {

	/* (non-Javadoc)
	 * @see Dominio.IRestriction#evaluate(ServidorAplicacao.strategy.enrolment.EnrolmentContext)
	 */
	public boolean evaluate(EnrolmentContext enrolmentContext) {
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
		return super.evaluate(enrolmentContext) || enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue() > 0;
	}

}
