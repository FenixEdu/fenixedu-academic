package Dominio;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;

/**
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public class RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse
	extends RestrictionDoneCurricularCourse
	implements IRestrictionByCurricularCourse
{
	public RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse()
	{
		super();
	}

	public boolean evaluate(StudentEnrolmentContext studentEnrolmentContext)
	{
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
		return super.evaluate(studentEnrolmentContext) ||
			(studentEnrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue() > 0);
	}
}
