package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;

/**
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public class RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse
	extends RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse
	implements IRestrictionByCurricularCourse
{
	public RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse()
	{
		super();
	}

	public boolean evaluate(StudentEnrolmentContext studentEnrolmentContext)
	{
		ICurricularCourse curricularCourse = super.getPrecedentCurricularCourse();

		List studentCurrentSemesterEnrollments = studentEnrolmentContext.getStudentCurrentSemesterEnrollments();
		for (int i = 0; i < studentCurrentSemesterEnrollments.size(); i++)
		{
			IEnrolment enrolment = (IEnrolment) studentCurrentSemesterEnrollments.get(i);
			if (enrolment.getCurricularCourse().equals(curricularCourse))
			{
				return true;
			}
		}

		return super.evaluate(studentEnrolmentContext);
	}
}