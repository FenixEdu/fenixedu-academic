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
		return super.evaluate(precedenceContext)
			|| (precedenceContext.getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue() > 0);
	}
}