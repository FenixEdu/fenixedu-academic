package ServidorApresentacao.Action.sop;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.base.FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class CreateExamActionDA extends
        FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        SessionUtils.getExecutionCourses(request);

        String nextPage = request.getParameter("nextPage");
        request.setAttribute(SessionConstants.NEXT_PAGE, nextPage);

        List horas = Util.getExamShifts();
        request.setAttribute(SessionConstants.LABLELIST_HOURS, horas);

        List daysOfMonth = Util.getDaysOfMonth();
        request.setAttribute(SessionConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);

        List monthsOfYear = Util.getMonthsOfYear();
        request.setAttribute(SessionConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);

        List examSeasons = Util.getExamSeasons();
        request.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);

        DynaValidatorForm chooseDayAndShiftForm = (DynaValidatorForm) form;
        chooseDayAndShiftForm.set("year", null);
        chooseDayAndShiftForm.set("month", null);
        chooseDayAndShiftForm.set("day", null);
        chooseDayAndShiftForm.set("beginning", null);
        chooseDayAndShiftForm.set("end", null);
        chooseDayAndShiftForm.set("season", null);

        return mapping.findForward("showCreateForm");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaValidatorForm chooseDayAndShiftForm = (DynaValidatorForm) form;

        Season season = new Season(new Integer((String) chooseDayAndShiftForm.get("season")));
        Calendar examDate = Calendar.getInstance();
        Calendar examTime = Calendar.getInstance();
        InfoExecutionCourse executionCourse = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

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

        // Create an exam with season, examDateAndTime and executionCourse
        Object argsCreateExam[] = { examDate, examTime, season, executionCourse };
        try {
            ServiceUtils.executeService(userView, "CreateExam", argsCreateExam);
        } catch (ExistingServiceException ex) {
            throw new ExistingActionException("O exame", ex);
        }

        String nextPage = request.getParameter("nextPage");
        //request.setAttribute(SessionConstants.NEXT_PAGE, nextPage);
        //String nextPage = (String)
        // request.getAttribute(SessionConstants.NEXT_PAGE);

        if (nextPage != null) {
            return mapping.findForward(nextPage);
        }
        return mapping.findForward("Sucess");

    }

}