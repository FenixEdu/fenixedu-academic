/*
 * Created on Oct 4, 2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop.utils;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExam;

/**
 * @author Luis Cruz
 *  
 */
public class RequestContextUtil {

    /**
     * @param request
     * @param examDateAndTime
     */
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

    /**
     * @param request
     * @param examDateAndTime
     */
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

    /**
     * @param request
     * @return
     */
    public static Calendar getExamDateAndTimeContext(HttpServletRequest request) {
        return (Calendar) request.getAttribute(SessionConstants.EXAM_DATEANDTIME);
    }

    /**
     * @param examDateAndTime
     * @return
     */
    public static InfoViewExam getInfoViewExams(IUserView userView, Calendar examDateAndTime)
            throws FenixServiceException, FenixFilterException {
        Calendar examDate = Calendar.getInstance();
        Calendar examTime = Calendar.getInstance();

        examDate.set(Calendar.DAY_OF_MONTH, examDateAndTime.get(Calendar.DAY_OF_MONTH));
        examDate.set(Calendar.MONTH, examDateAndTime.get(Calendar.MONTH));
        examDate.set(Calendar.YEAR, examDateAndTime.get(Calendar.YEAR));
        examDate.set(Calendar.HOUR_OF_DAY, 0);
        examDate.set(Calendar.MINUTE, 0);
        examDate.set(Calendar.SECOND, 0);

        examTime.set(Calendar.DAY_OF_MONTH, 1);
        examTime.set(Calendar.MONTH, 1);
        examTime.set(Calendar.YEAR, 1970);
        examTime.set(Calendar.HOUR_OF_DAY, examDateAndTime.get(Calendar.HOUR_OF_DAY));
        examTime.set(Calendar.MINUTE, 0);
        examTime.set(Calendar.SECOND, 0);

        Object args[] = { examDate, examTime };
        InfoViewExam infoViewExams = (InfoViewExam) ServiceUtils.executeService(userView,
                "ReadExamsByDayAndBeginning", args);

        return infoViewExams;
    }

}