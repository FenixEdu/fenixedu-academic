/*
 * Created on 7/Mar/2004
 */
package Dominio.credits;

import java.util.Date;

import Dominio.IExecutionPeriod;

/**
 * @author jpvl
 */
public abstract class DatePeriodBaseCreditLine extends CreditLine implements IDatePeriodBasedCreditLine {
    private Date start;

    private Date end;

    /**
     * @return Returns the end.
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end
     *            The end to set.
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * @return Returns the start.
     */
    public Date getStart() {
        return start;
    }

    /**
     * @param start
     *            The start to set.
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.credits.event.ICreditsEventOriginator#belongsToExecutionPeriod(Dominio.IExecutionPeriod)
     */
    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return belongsTo(executionPeriod);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.credits.IDatePeriodBasedCreditLine#belongsTo(Dominio.IExecutionPeriod)
     */
    public boolean belongsTo(IExecutionPeriod executionPeriod) {
        return (this.end.after(executionPeriod.getBeginDate()) && this.start.before(executionPeriod
                .getEndDate()));
    }
}