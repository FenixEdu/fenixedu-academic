/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

/**
 * @author jpvl
 */
public abstract class DatePeriodBaseCreditLine extends DatePeriodBaseCreditLine_Base {

    public DatePeriodBaseCreditLine() {
        super();
    }

    @Override
    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return (getEnd().isAfter(executionSemester.getBeginDateYearMonthDay()) && getStart().isBefore(
                executionSemester.getEndDateYearMonthDay()));
    }

    @Deprecated
    public boolean hasEnd() {
        return getEnd() != null;
    }

    @Deprecated
    public boolean hasStart() {
        return getStart() != null;
    }

}
