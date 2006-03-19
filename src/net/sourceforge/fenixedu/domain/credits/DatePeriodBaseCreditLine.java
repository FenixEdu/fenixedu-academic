/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;

/**
 * @author jpvl
 */
public abstract class DatePeriodBaseCreditLine extends DatePeriodBaseCreditLine_Base {

    public DatePeriodBaseCreditLine() {
		super();
	}

	public boolean belongsToExecutionPeriod(ExecutionPeriod executionPeriod) {
        return (getEnd().after(executionPeriod.getBeginDate()) && getStart().before(executionPeriod
                .getEndDate()));
    }

}