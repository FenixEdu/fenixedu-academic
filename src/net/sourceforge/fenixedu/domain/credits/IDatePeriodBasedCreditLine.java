/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;

/**
 * @author jpvl
 */
public interface IDatePeriodBasedCreditLine extends ICreditLine {
    /**
     * @return Returns the end.
     */
    public abstract Date getEnd();

    /**
     * @param end
     *            The end to set.
     */
    public abstract void setEnd(Date end);

    /**
     * @return Returns the start.
     */
    public abstract Date getStart();

    /**
     * @param start
     *            The start to set.
     */
    public abstract void setStart(Date start);

    public boolean belongsTo(IExecutionPeriod executionPeriod);
}