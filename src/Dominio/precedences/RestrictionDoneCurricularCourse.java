package Dominio.precedences;

import ServidorPersistente.ExcepcaoPersistencia;
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

//	public boolean evaluate(PrecedenceContext precedenceContext)
//	{
//		return precedenceContext.getStudentCurricularPlan().isCurricularCourseApproved(this.getPrecedentCurricularCourse());
//	}

	public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext)
	{
	    try {
			if (precedenceContext.getStudentCurricularPlan().isCurricularCourseApproved(
                    this.getPrecedentCurricularCourse())) {
                return CurricularCourseEnrollmentType.DEFINITIVE;
            } else {
                CurricularCourseEnrollmentType type = precedenceContext.getStudentCurricularPlan()
                        .getCurricularCourseEnrollmentType(this.getPrecedentCurricularCourse(),
                                precedenceContext.getExecutionPeriod());
                if (type.equals(CurricularCourseEnrollmentType.TEMPORARY)) {
                    return CurricularCourseEnrollmentType.TEMPORARY;
                } else {
                    return CurricularCourseEnrollmentType.NOT_ALLOWED;
                }
            }
	    } catch(ExcepcaoPersistencia e) {
	        throw new RuntimeException(e);
	    }
	}
}