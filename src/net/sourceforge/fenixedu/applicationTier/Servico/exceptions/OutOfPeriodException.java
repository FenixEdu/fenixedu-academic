/*
 * Created on 8/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

import java.util.Date;

/**
 * @author jpvl
 */
public class OutOfPeriodException extends FenixServiceException {
    private String messageKey;

    private Date startDate;

    private Date endDate;

    public OutOfPeriodException(String messageKey, Date startDate, Date endDate) {
        this.messageKey = messageKey;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @return
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

}