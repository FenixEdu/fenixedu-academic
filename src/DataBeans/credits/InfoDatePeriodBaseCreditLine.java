/*
 * Created on 7/Mar/2004
 */
package DataBeans.credits;

import java.util.Date;

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

}