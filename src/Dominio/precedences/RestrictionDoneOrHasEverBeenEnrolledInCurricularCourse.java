package Dominio.precedences;

import Util.enrollment.CurricularCourseEnrollmentType;
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

//	public boolean evaluate(PrecedenceContext precedenceContext)
//	{
//		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
//		return super.evaluate(precedenceContext)
//			|| (precedenceContext.getStudentCurricularPlan().getCurricularCourseAcumulatedEnrollments(curricularCourse).intValue() > 0);
//	}

	public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext)
	{
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
		CurricularCourseEnrollmentType result1 = null;
		CurricularCourseEnrollmentType result2 = null;

        if (precedenceContext.getStudentCurricularPlan().getCurricularCourseAcumulatedEnrollments(
                curricularCourse).intValue() > curricularCourse.getMinimumValueForAcumulatedEnrollments().intValue()) {
            result1 = CurricularCourseEnrollmentType.DEFINITIVE;
        } else {
            result1 = CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        result2 = super.evaluate(precedenceContext);
        
        return result2.or(result1);
	}
}