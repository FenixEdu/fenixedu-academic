/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.credits;

import java.util.Date;

import net.sourceforge.fenixedu.domain.credits.IServiceExemptionCreditLine;

/**
 * @author jpvl
 */
public abstract class InfoDatePeriodBaseCreditLine extends InfoCreditLine {
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

    public void populateDomainObject(IServiceExemptionCreditLine serviceExemptionCreditLine) {
        serviceExemptionCreditLine.setEnd(getEnd());
        serviceExemptionCreditLine.setStart(getStart());
    }

}