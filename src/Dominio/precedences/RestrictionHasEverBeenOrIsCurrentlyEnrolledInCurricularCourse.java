package Dominio.precedences;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;

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

		List studentEnrolledEnrollments = precedenceContext.getStudentEnrolledEnrollments();
		for (int i = 0; i < studentEnrolledEnrollments.size(); i++)
		{
			IEnrolment enrolment = (IEnrolment) studentEnrolledEnrollments.get(i);
			if (enrolment.getCurricularCourse().equals(curricularCourse))
			{
				return true;
			}
		}

		return super.evaluate(precedenceContext);
	}
}