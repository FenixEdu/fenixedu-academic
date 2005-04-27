/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;

/**
 * @author jpvl
 */
public abstract class DatePeriodBaseCreditLine extends DatePeriodBaseCreditLine_Base {

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return (getEnd().after(executionPeriod.getBeginDate()) && getStart().before(executionPeriod
                .getEndDate()));
    }

}