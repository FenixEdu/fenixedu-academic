package Dominio.precedences;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionDoneCurricularCourse extends RestrictionByCurricularCourse implements IRestrictionByCurricularCourse
{
	public RestrictionDoneCurricularCourse()
	{
		super();
	}

	public boolean evaluate(PrecedenceContext precedenceContext)
	{
		return isCurricularCourseDone(this.getPrecedentCurricularCourse(), precedenceContext.getStudentApprovedEnrollments());
	}

	public static boolean isCurricularCourseDone(ICurricularCourse curricularCourse, List studentApprovedEnrollments)
	{
		for (int i = 0; i < studentApprovedEnrollments.size(); i++)
		{
			IEnrolment enrolment = (IEnrolment) studentApprovedEnrollments.get(i);

			if(enrolment instanceof IEnrolmentInOptionalCurricularCourse)
			{
				if (((IEnrolmentInOptionalCurricularCourse) enrolment).getCurricularCourseForOption().equals(curricularCourse))
				{
					return true;
				}
			} else
			{
				if (enrolment.getCurricularCourse().equals(curricularCourse))
				{
					return true;
				}
			}
		}
		return false;
	}
}