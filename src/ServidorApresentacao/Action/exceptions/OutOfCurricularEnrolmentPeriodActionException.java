/*
 * Created on 8/Mai/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.exceptions;

import java.util.Date;

import org.apache.struts.action.ActionForward;

/**
 * @author jpvl
 */
public class OutOfCurricularEnrolmentPeriodActionException extends OutOfPeriodActionException {
    private final String DEFAULT_MESSAGE_KEY = "message.out.curricular.course.enrolment.period.default";

    /**
     * @param messageKey
     * @param startDate
     * @param endDate
     */
    public OutOfCurricularEnrolmentPeriodActionException(String messageKey, Date startDate,
            Date endDate, ActionForward actionForward) {
        super(messageKey, startDate, endDate, actionForward);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.exceptions.OutOfPeriodActionException#getDefaultMessageKey()
     */
    protected String getDefaultMessageKey() {
        return this.DEFAULT_MESSAGE_KEY;
    }

}