package Dominio.precedences;

import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionByNumberOfDoneCurricularCourses extends RestrictionByNumberOfCurricularCourses implements
	IRestrictionByNumberOfCurricularCourses
{
	public RestrictionByNumberOfDoneCurricularCourses()
	{
		super();
	}

//	public boolean evaluate(PrecedenceContext precedenceContext)
//	{
//		return (precedenceContext.getStudentCurricularPlan().getNumberOfApprovedCurricularCourses() >= numberOfCurricularCourses
//			.intValue());
//	}

	public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext)
	{
		if (precedenceContext.getStudentCurricularPlan().getNumberOfApprovedCurricularCourses() >= numberOfCurricularCourses
                .intValue()) {
            return CurricularCourseEnrollmentType.DEFINITIVE;
        } 
            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        
	}
}