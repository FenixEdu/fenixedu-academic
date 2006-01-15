package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.util.PeriodToApplyRestriction;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionPeriodToApply extends RestrictionPeriodToApply_Base {

	public RestrictionPeriodToApply() {}
	
	
	public RestrictionPeriodToApply(Integer number, Precedence precedence, CurricularCourse precedentCurricularCourse) {
		
        setPrecedence(precedence);
        setPeriodToApplyRestriction(PeriodToApplyRestriction.getEnum(number.intValue()));
	}
	
	
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
        stringBuffer.append(this.getPeriodToApplyRestriction()).append("\n");
        return stringBuffer.toString();
    }

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
        ExecutionPeriod executionPeriod = precedenceContext.getExecutionPeriod();

        boolean isValid = false;

        if (this.getPeriodToApplyRestriction().equals(PeriodToApplyRestriction.BOTH_SEMESTERS)) {
            isValid = true;
        } else if (executionPeriod.getSemester().equals(
                new Integer(this.getPeriodToApplyRestriction().getValue()))) {
            isValid = true;
        }

        if (isValid) {
            return CurricularCourseEnrollmentType.DEFINITIVE;
        }

        return CurricularCourseEnrollmentType.NO_EVALUATE;
    }

}
