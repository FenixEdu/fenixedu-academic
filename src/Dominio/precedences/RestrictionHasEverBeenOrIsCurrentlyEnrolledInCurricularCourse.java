package Dominio.precedences;

import Dominio.ICurricularCourse;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse extends
	RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse implements IRestrictionByCurricularCourse
{
	public RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse()
	{
		super();
	}

	public boolean evaluate(PrecedenceContext precedenceContext)
	{
		ICurricularCourse curricularCourse = super.getPrecedentCurricularCourse();
		return (precedenceContext.getStudentCurricularPlan().isCurricularCourseEnrolled(curricularCourse) || super
			.evaluate(precedenceContext));
	}
}