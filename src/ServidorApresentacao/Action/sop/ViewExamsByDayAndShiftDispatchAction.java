/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2003/03/21
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoViewExam;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.base.FenixDateAndTimeDispatchAction;
import ServidorApresentacao.Action.sop.utils.RequestContextUtil;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsByDayAndShiftDispatchAction extends FenixDateAndTimeDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        Calendar examDateAndTime = RequestContextUtil.getExamDateAndTimeContext(request);
        RequestContextUtil.setExamDateAndTimeContext(request, examDateAndTime);

        InfoViewExam infoViewExams = RequestContextUtil.getInfoViewExams(userView, examDateAndTime);

        List infoExams = infoViewExams.getInfoViewExamsByDayAndShift();

        if (infoExams != null && infoExams.size() == 0)
            infoExams = null;

        request.setAttribute(SessionConstants.AVAILABLE_ROOM_OCCUPATION, infoViewExams
                .getAvailableRoomOccupation());
        request.removeAttribute(SessionConstants.LIST_EXAMSANDINFO);
        request.setAttribute(SessionConstants.LIST_EXAMSANDINFO, infoExams);

        return mapping.findForward("View Exams");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);

        //List infoExams =
        //	(List) request.getAttribute(SessionConstants.LIST_EXAMSANDINFO);
        Calendar examDateAndTime = RequestContextUtil.getExamDateAndTimeContext(request);
        InfoViewExam infoViewExams;
        try {
            infoViewExams = RequestContextUtil.getInfoViewExams(userView, examDateAndTime);
        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }
        List infoExams = infoViewExams.getInfoViewExamsByDayAndShift();

        Integer indexExam = new Integer(request.getParameter("indexExam"));

        Object args[] = { (InfoViewExamByDayAndShift) infoExams.get(indexExam.intValue()) };
        try {
            ServiceUtils.executeService(userView, "DeleteExam", args);
        } catch (notAuthorizedServiceDeleteException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors
                    .add("notAuthorizedExamDelete", new ActionError("error.notAuthorizedExamDelete"));
            saveErrors(request, actionErrors);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("Deleted Exam");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaValidatorForm editExamForm = (DynaValidatorForm) form;

        //List infoExams =
        //	(List) request.getAttribute(SessionConstants.LIST_EXAMSANDINFO);
        Calendar examDateAndTime = RequestContextUtil.getExamDateAndTimeContext(request);
        InfoViewExam infoViewExams;
        try {
            infoViewExams = RequestContextUtil.getInfoViewExams(userView, examDateAndTime);
        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }
        List infoExams = infoViewExams.getInfoViewExamsByDayAndShift();

        Integer indexExam = new Integer(request.getParameter("indexExam"));

        InfoViewExamByDayAndShift infoViewExam = (InfoViewExamByDayAndShift) infoExams.get(indexExam
                .intValue());

        List horas = Util.getExamShifts();
        request.setAttribute(SessionConstants.LABLELIST_HOURS, horas);

        List daysOfMonth = Util.getDaysOfMonth();
        request.setAttribute(SessionConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);

        List monthsOfYear = Util.getMonthsOfYear();
        request.setAttribute(SessionConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);

        List examSeasons = Util.getExamSeasons();
        request.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);

        Calendar date = Calendar.getInstance();
        date = infoViewExam.getInfoExam().getDay();

        editExamForm.set("day", new Integer(date.get(Calendar.DAY_OF_MONTH)).toString());
        editExamForm.set("month", new Integer(date.get(Calendar.MONTH)).toString());
        editExamForm.set("year", new Integer(date.get(Calendar.YEAR)).toString());
        if (infoViewExam.getInfoExam().getBeginning() != null) {
            editExamForm.set("beginning", new Integer(infoViewExam.getInfoExam().getBeginning().get(
                    Calendar.HOUR_OF_DAY)).toString());
        }
        editExamForm.set("season", infoViewExam.getInfoExam().getSeason().getseason().toString());

        request.setAttribute("input", "viewExamsByDayAndShift");
        request.setAttribute(SessionConstants.NEXT_PAGE, "viewExamsByDayAndShift");

        request.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoViewExam);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal()
                .toString());

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) infoViewExam
                .getInfoExecutionCourses().get(0);
        request.setAttribute(SessionConstants.EXECUTION_COURSE_OID, infoExecutionCourse.getIdInternal()
                .toString());
        request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourse);

        return mapping.findForward("Edit Exam");
    }

    public ActionForward addExecutionCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        //List infoViewExams =
        //	(List) request.getAttribute(SessionConstants.LIST_EXAMSANDINFO);
        //        Calendar examDateAndTime =
        // RequestContextUtil.getExamDateAndTimeContext(request);
        //        InfoViewExam infoViewExams;
        //        try
        //        {
        //            infoViewExams = RequestContextUtil.getInfoViewExams(userView,
        // examDateAndTime);
        //        } catch (FenixServiceException e1)
        //        {
        //            throw new FenixActionException(e1);
        //        }
        Calendar examDateAndTime = RequestContextUtil.getExamDateAndTimeContext(request);
        RequestContextUtil.setExamDateAndTimeContext(request, examDateAndTime);

        InfoViewExam infoViewExams = RequestContextUtil.getInfoViewExams(userView, examDateAndTime);
        List infoExams = infoViewExams.getInfoViewExamsByDayAndShift();

        Integer indexExam = new Integer(request.getParameter("indexExam"));

        HttpSession session = request.getSession(false);
        session.setAttribute(SessionConstants.INFO_VIEW_EXAM, infoExams.get(indexExam.intValue()));

        request.setAttribute("input", "viewExamsByDayAndShift");

        return mapping.findForward("Add Execution Course");
    }

}