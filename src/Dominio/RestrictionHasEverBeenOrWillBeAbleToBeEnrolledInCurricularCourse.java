package Dominio;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;

/**
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public class RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse
	extends RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse
	implements IRestrictionByCurricularCourse
{
	public RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse()
	{
		super();
	}

	public boolean evaluate(StudentEnrolmentContext studentEnrolmentContext)
	{
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();

		if (studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled().contains(curricularCourse))
		{
			return true;
		}

		return super.evaluate(studentEnrolmentContext);
	}
}