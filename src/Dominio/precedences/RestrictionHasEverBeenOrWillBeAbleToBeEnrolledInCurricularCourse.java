package Dominio.precedences;

import Dominio.ICurricularCourse;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse extends
	RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse implements IRestrictionByCurricularCourse
{
	public RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse()
	{
		super();
	}

	public boolean evaluate(PrecedenceContext precedenceContext)
	{
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();

		if (precedenceContext.getCurricularCoursesWhereStudentCanBeEnrolled().contains(curricularCourse))
		{
			return true;
		}

		return super.evaluate(precedenceContext);
	}
}