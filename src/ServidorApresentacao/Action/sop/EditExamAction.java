package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.InterceptingRoomsServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.InterceptingRoomsActionException;
import ServidorApresentacao
    .Action
    .sop
    .base
    .FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.RequestContextUtil;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;
import ServidorApresentacao.Action.utils.ContextUtils;
import Util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamAction
    extends FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction
{

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

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
        try
        {
            beginning = new Integer((String) chooseDayAndShiftForm.get("beginning"));
            examTime.set(Calendar.HOUR_OF_DAY, beginning.intValue());
            examTime.set(Calendar.MINUTE, 0);
            examTime.set(Calendar.SECOND, 0);
        } catch (NumberFormatException ex)
        {
            // No problem, it isn't requiered.
        }

        examDate.set(Calendar.YEAR, year.intValue());
        examDate.set(Calendar.MONTH, month.intValue());
        examDate.set(Calendar.DAY_OF_MONTH, day.intValue());

        //InfoViewExamByDayAndShift infoViewOldExam =  (InfoViewExamByDayAndShift) session.getAttribute(SessionConstants.INFO_EXAMS_KEY);
        ContextUtils.setExecutionCourseContext(request);
        InfoExecutionCourse infoExecutionCourse =
            (InfoExecutionCourse) request.getAttribute(SessionConstants.EXECUTION_COURSE);

        System.out.println("infoExecutionCourse= " + infoExecutionCourse);

        Season oldExamsSeason = new Season(new Integer(request.getParameter("oldExamSeason")));

        Object args[] =
            {
                infoExecutionCourse.getSigla(),
                oldExamsSeason,
                infoExecutionCourse.getInfoExecutionPeriod()};
        InfoViewExamByDayAndShift infoViewOldExam =
            (InfoViewExamByDayAndShift) ServiceUtils.executeService(
                userView,
                "ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod",
                args);
        System.out.println("infoViewOldExam" + infoViewOldExam);

        // Put everything back in request in case an exception occores so the
        // edit exams page can be viewed again.
        request.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoViewOldExam);
        ArrayList horas = Util.getExamShifts();
        request.setAttribute(SessionConstants.LABLELIST_HOURS, horas);
        ArrayList daysOfMonth = Util.getDaysOfMonth();
        request.setAttribute(SessionConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);
        ArrayList monthsOfYear = Util.getMonthsOfYear();
        request.setAttribute(SessionConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);
        ArrayList examSeasons = Util.getExamSeasons();
        request.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);
        request.setAttribute(SessionConstants.NEXT_PAGE, input);
        RequestContextUtil.setExamDateAndTimeContext(request);
        ////////////////////////////////////////////////////////////////////////

        // Create an exam with season, examDateAndTime and executionCourse
        Object argsCreateExam[] = { examDate, examTime, season, infoViewOldExam };
        try
        {
            ServiceUtils.executeService(userView, "EditExam", argsCreateExam);
        } catch (ExistingServiceException ex)
        {
            throw new ExistingActionException("O exame de " + season, ex);
        } catch (InterceptingRoomsServiceException ex)
        {
            throw new InterceptingRoomsActionException("O exame de " + season, ex);
        }

        System.out.println("input=" + input);
        return mapping.findForward(input);
    }

}
