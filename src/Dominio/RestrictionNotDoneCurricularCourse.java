package Dominio;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;

/**
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public class RestrictionNotDoneCurricularCourse extends RestrictionByCurricularCourse implements IRestrictionByCurricularCourse
{
	public RestrictionNotDoneCurricularCourse()
	{
		super();
	}

	public boolean evaluate(StudentEnrolmentContext studentEnrolmentContext)
	{
		return !studentEnrolmentContext.isCurricularCourseDone(this.getPrecedentCurricularCourse());
	}
}