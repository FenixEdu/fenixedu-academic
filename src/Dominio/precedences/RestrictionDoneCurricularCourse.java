package Dominio.precedences;

import Util.enrollment.CurricularCourseEnrollmentType;


/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionDoneCurricularCourse extends RestrictionByCurricularCourse implements IRestrictionByCurricularCourse
{
	public RestrictionDoneCurricularCourse()
	{
		super();
	}

	public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {

	    if (precedenceContext.getStudentCurricularPlan().isCurricularCourseApproved(
                this.getPrecedentCurricularCourse())) {
            return CurricularCourseEnrollmentType.DEFINITIVE;
        }
        
	    CurricularCourseEnrollmentType type = precedenceContext.getStudentCurricularPlan()
                .getCurricularCourseEnrollmentType(this.getPrecedentCurricularCourse(),
                        precedenceContext.getExecutionPeriod());
        
	    if (type.equals(CurricularCourseEnrollmentType.TEMPORARY)) {
            return CurricularCourseEnrollmentType.TEMPORARY;
        }
        
	    return CurricularCourseEnrollmentType.NOT_ALLOWED;
    }
}