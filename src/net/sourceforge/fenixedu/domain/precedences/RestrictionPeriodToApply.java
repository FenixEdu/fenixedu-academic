package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.util.PeriodToApplyRestriction;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionPeriodToApply extends Restriction implements IRestrictionPeriodToApply {
    protected PeriodToApplyRestriction periodToApplyRestriction;

    public RestrictionPeriodToApply() {
    }

    public PeriodToApplyRestriction getPeriodToApplyRestriction() {
        return periodToApplyRestriction;
    }

    public void setPeriodToApplyRestriction(PeriodToApplyRestriction periodToApplyRestriction) {
        this.periodToApplyRestriction = periodToApplyRestriction;
    }

    public boolean equals(Object obj) {
        boolean result = super.equals(obj);
        if ((result) && (obj instanceof IRestrictionPeriodToApply)) {
            IRestrictionPeriodToApply restrictionPeriodToApply = (IRestrictionPeriodToApply) obj;
            result = restrictionPeriodToApply.getPeriodToApplyRestriction().equals(
                    this.getPeriodToApplyRestriction())
                    && this.getClass().getName().equals(restrictionPeriodToApply.getClass().getName());
        } else if (result) {
            result = false;
        }
        return result;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
        stringBuffer.append(this.getPeriodToApplyRestriction()).append("\n");
        return stringBuffer.toString();
    }

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
        IExecutionPeriod executionPeriod = precedenceContext.getExecutionPeriod();

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

        return CurricularCourseEnrollmentType.NOT_ALLOWED;
    }
}