/*
 * Created on 5/Fev/2004
 */
package ServidorApresentacao.Action.sop.exams;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoViewExam;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Ana e Ricardo
 */
public class ExamSearchByDate extends FenixContextDispatchAction
{

    public ActionForward prepare(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        return mapping.findForward("choose");
    }

    public ActionForward search(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        DynaActionForm examSearchByDateForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        // exam date
        Calendar examDate = Calendar.getInstance();
        Integer day = new Integer((String) examSearchByDateForm.get("day"));
        Integer month = new Integer((String) examSearchByDateForm.get("month"));
        Integer year = new Integer((String) examSearchByDateForm.get("year"));
        examDate.set(Calendar.YEAR, year.intValue());
        examDate.set(Calendar.MONTH, month.intValue() - 1);
        examDate.set(Calendar.DAY_OF_MONTH, day.intValue());
        if (examDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            ActionError actionError = new ActionError("error.sunday");
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.sunday", actionError);
            saveErrors(request, actionErrors);
            return prepare(mapping, form, request, response);
        }

        // exam start time
        Calendar examStartTime = Calendar.getInstance();
        try
        {
            Integer startHour = new Integer((String) examSearchByDateForm.get("beginningHour"));
            Integer startMinute = new Integer((String) examSearchByDateForm.get("beginningMinute"));
            examStartTime.set(Calendar.HOUR_OF_DAY, startHour.intValue());
            examStartTime.set(Calendar.MINUTE, startMinute.intValue());
            examStartTime.set(Calendar.SECOND, 0);
        }
        catch (NumberFormatException ex)
        {
            examStartTime = null;
        }

        // exam end time
        Calendar examEndTime = Calendar.getInstance();
        try
        {
            Integer endHour = new Integer((String) examSearchByDateForm.get("endHour"));
            Integer endMinute = new Integer((String) examSearchByDateForm.get("endMinute"));
            examEndTime.set(Calendar.HOUR_OF_DAY, endHour.intValue());
            examEndTime.set(Calendar.MINUTE, endMinute.intValue());
            examEndTime.set(Calendar.SECOND, 0);
        }
        catch (Exception e)
        {
            examEndTime = null;
        }

        if (examStartTime != null && examEndTime != null && examStartTime.after(examEndTime))
        {
            ActionError actionError = new ActionError("error.dateSwitched");
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.dateSwitched", actionError);
            saveErrors(request, actionErrors);
            return prepare(mapping, form, request, response);
        }

        String examDateString =
            " Exames de dia "
                + new Integer(examDate.get(Calendar.DAY_OF_MONTH))
                + "/"
                + new Integer(examDate.get(Calendar.MONTH) + 1).toString()
                + "/"
                + new Integer(examDate.get(Calendar.YEAR));

        //String examStartTimeString = "";
        if (examStartTime != null)
        {
            examDateString += " a começar às "
                + new Integer(examStartTime.get(Calendar.HOUR_OF_DAY))
                + ":"
                + new Integer(examStartTime.get(Calendar.MINUTE));
        }
        //String examEndTimeString = "";
        if (examEndTime != null && examStartTime != null)
        {
            examDateString += " e";
        }
        if (examEndTime != null)
        {
            examDateString += " a terminar às "
                + new Integer(examEndTime.get(Calendar.HOUR_OF_DAY))
                + ":"
                + new Integer(examEndTime.get(Calendar.MINUTE));
        }
	  	
	  	Object[] args = { examDate, examStartTime, examEndTime };
        InfoViewExam infoViewExam =
            (InfoViewExam) ServiceUtils.executeService(userView, "ReadExamsByDate", args);

        if (infoViewExam.getInfoViewExamsByDayAndShift().size() != 0)
        {
            request.setAttribute(
                SessionConstants.LIST_EXAMSANDINFO,
                infoViewExam.getInfoViewExamsByDayAndShift());
        }
        request.setAttribute(SessionConstants.EXAM_DATEANDTIME_STR, examDateString);

        return mapping.findForward("show");
    }

}
