package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InterceptingRoomsServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InterceptingRoomsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestContextUtil;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.Util;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamAction extends
        FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        IUserView userView = SessionUtils.getUserView(request);

        String input = request.getParameter("input");

        DynaValidatorForm chooseDayAndShiftForm = (DynaValidatorForm) form;

        Season season = new Season(new Integer((String) chooseDayAndShiftForm.get("season")));
        Calendar examDate = Calendar.getInstance();
        Calendar examTime = Calendar.getInstance();

        Integer day = new Integer((String) chooseDayAndShiftForm.get("day"));
        Integer month = new Integer((String) chooseDayAndShiftForm.get("month"));
        Integer year = new Integer((String) chooseDayAndShiftForm.get("year"));
        Integer beginning = null;
        try {
            beginning = new Integer((String) chooseDayAndShiftForm.get("beginning"));
            examTime.set(Calendar.HOUR_OF_DAY, beginning.intValue());
            examTime.set(Calendar.MINUTE, 0);
            examTime.set(Calendar.SECOND, 0);
        } catch (NumberFormatException ex) {
            // No problem, it isn't requiered.
        }

        examDate.set(Calendar.YEAR, year.intValue());
        examDate.set(Calendar.MONTH, month.intValue());
        examDate.set(Calendar.DAY_OF_MONTH, day.intValue());

        //InfoViewExamByDayAndShift infoViewOldExam =
        // (InfoViewExamByDayAndShift)
        // session.getAttribute(SessionConstants.INFO_EXAMS_KEY);
        ContextUtils.setExecutionCourseContext(request);
        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

        Season oldExamsSeason = new Season(new Integer(request.getParameter("oldExamSeason")));

        Object args[] = { infoExecutionCourse.getSigla(), oldExamsSeason,
                infoExecutionCourse.getInfoExecutionPeriod() };
        InfoViewExamByDayAndShift infoViewOldExam = (InfoViewExamByDayAndShift) ServiceUtils
                .executeService(userView,
                        "ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod", args);

        // Put everything back in request in case an exception occores so the
        // edit exams page can be viewed again.
        request.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoViewOldExam);
        List horas = Util.getExamShifts();
        request.setAttribute(SessionConstants.LABLELIST_HOURS, horas);
        List daysOfMonth = Util.getDaysOfMonth();
        request.setAttribute(SessionConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);
        List monthsOfYear = Util.getMonthsOfYear();
        request.setAttribute(SessionConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);
        List examSeasons = Util.getExamSeasons();
        request.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);
        request.setAttribute(SessionConstants.NEXT_PAGE, input);
        RequestContextUtil.setExamDateAndTimeContext(request);
        ////////////////////////////////////////////////////////////////////////

        // Create an exam with season, examDateAndTime and executionCourse
        Object argsCreateExam[] = { examDate, examTime, season, infoViewOldExam };
        try {
            ServiceUtils.executeService(userView, "EditExam", argsCreateExam);
        } catch (ExistingServiceException ex) {
            throw new ExistingActionException("O exame de " + season, ex);
        } catch (InterceptingRoomsServiceException ex) {
            throw new InterceptingRoomsActionException("O exame de " + season, ex);
        }

        return mapping.findForward(input);
    }

}