package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionNotEnrolledInCurricularCourse extends RestrictionNotEnrolledInCurricularCourse_Base {
    public RestrictionNotEnrolledInCurricularCourse() {
        super();
    }
	
	public RestrictionNotEnrolledInCurricularCourse(Integer number, Precedence precedence, CurricularCourse precedentCurricularCourse) {
		super();
		
		setPrecedence(precedence);
		setPrecedentCurricularCourse(precedentCurricularCourse);
	}
	

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
        CurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
        CurricularCourseEnrollmentType result1 = null;
        CurricularCourseEnrollmentType result2 = null;
        
        if(precedenceContext.getStudentCurricularPlan().isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, precedenceContext.getExecutionPeriod())) {
            result1 = CurricularCourseEnrollmentType.NOT_ALLOWED;
        } else if(precedenceContext.getStudentCurricularPlan().isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, precedenceContext.getExecutionPeriod().getPreviousExecutionPeriod())){
            result1 = CurricularCourseEnrollmentType.TEMPORARY;
        } else {
            result1 = CurricularCourseEnrollmentType.DEFINITIVE;
        }

        result2 = super.evaluate(precedenceContext);

        return result1.or(result2);
    }
}