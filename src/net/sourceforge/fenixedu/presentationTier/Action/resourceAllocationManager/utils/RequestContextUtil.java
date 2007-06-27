/*
 * Created on Oct 4, 2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Luis Cruz
 *  
 */
public class RequestContextUtil {

    public static void setExamDateAndTimeContext(HttpServletRequest request) {
        Calendar examDateAndTime = Calendar.getInstance();
        Long dateAndTime = null;
        try {
            dateAndTime = new Long(request.getParameter(SessionConstants.EXAM_DATEANDTIME));
        } catch (NumberFormatException ex) {
            examDateAndTime = (Calendar) request.getAttribute(SessionConstants.EXAM_DATEANDTIME);
            if (examDateAndTime != null) {
                request.setAttribute(SessionConstants.EXAM_DATEANDTIME, examDateAndTime);
            } else {
                request.removeAttribute(SessionConstants.EXAM_DATEANDTIME);
                request.setAttribute(SessionConstants.EXAM_DATEANDTIME, null);
            }
        }

        if (dateAndTime != null) {
            examDateAndTime.setTimeInMillis(dateAndTime.longValue());
            request.setAttribute(SessionConstants.EXAM_DATEANDTIME, examDateAndTime);
        }
    }

    public static void setExamDateAndTimeContext(HttpServletRequest request, Calendar examDateAndTime) {
        /*
         * Note: this may seem a little strange, however if the calendar is not
         * initialized with the current date and time values, when it is placed
         * in request the web-container alters the hour to adjust it to local
         * winter/summer time.
         */
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, examDateAndTime.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, examDateAndTime.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, examDateAndTime.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, examDateAndTime.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, examDateAndTime.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, examDateAndTime.get(Calendar.SECOND));
        request.setAttribute(SessionConstants.EXAM_DATEANDTIME, calendar);
    }

    public static Calendar getExamDateAndTimeContext(HttpServletRequest request) {
        return (Calendar) request.getAttribute(SessionConstants.EXAM_DATEANDTIME);
    }
}