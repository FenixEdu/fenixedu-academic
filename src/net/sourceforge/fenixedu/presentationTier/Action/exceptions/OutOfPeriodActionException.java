/*
 * Created on 8/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

import java.util.Date;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;

/**
 * @author jpvl
 */
public abstract class OutOfPeriodActionException extends FenixActionException {
    private String messageKey;

    private Date startDate;

    private Date endDate;

    public OutOfPeriodActionException(String messageKey, Date startDate, Date endDate,
            ActionForward actionForward) {
        super(actionForward);
        this.startDate = startDate;
        this.endDate = endDate;
        this.messageKey = messageKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.exceptions.FenixActionException#getError()
     */
    public ActionError getError() {
        ActionError actionError = null;
        if (startDate == null || endDate == null) {
            actionError = new ActionError(this.getDefaultMessageKey());
        } else {
            actionError = new ActionError(this.messageKey, startDate, endDate);
        }
        return actionError;
    }

    /**
     *  
     */
    abstract protected String getDefaultMessageKey();

}