package Dominio.precedences;

import Dominio.ICurricularCourse;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse extends RestrictionDoneCurricularCourse implements
	IRestrictionByCurricularCourse
{
	public RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse()
	{
		super();
	}

	public boolean evaluate(PrecedenceContext precedenceContext)
	{
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
		precedenceContext.getStudentCurricularPlan().calculateStudentAcumulatedEnrollments();
		return super.evaluate(precedenceContext)
			|| (precedenceContext.getStudentCurricularPlan().getCurricularCourseAcumulatedEnrollments(curricularCourse).intValue() > 0);
	}
}