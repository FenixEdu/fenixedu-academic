/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.teacher.workingTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import DataBeans.teacher.workTime.InfoTeacherInstitutionWorkTime;
import DataBeans.teacher.workTime.TeacherInstitutionWorkingTimeDTO;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.framework.CRUDActionByOID;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.mapping.framework.CRUDMapping;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class CRUDTeacherInstitutionWorkingTimeAction extends CRUDActionByOID
{
    /**
	 * @param string
	 * @return
	 */
    private DiaSemana getWeekDay(String weekday)
    {
        int weekDayInt = 0;
        try
        {
            weekDayInt = Integer.parseInt(weekday);
        }
        catch (NumberFormatException e)
        {
            if (weekday.equalsIgnoreCase("S"))
            {
                weekDayInt = DiaSemana.SABADO;
            }
            else if (weekday.equalsIgnoreCase("D"))
            {
                weekDayInt = DiaSemana.DOMINGO;
            }
        }
        return new DiaSemana(weekDayInt);
    }

    /**
	 * @param semana
	 * @return
	 */
    private String getWeekDayString(DiaSemana weekday)
    {
        switch (weekday.getDiaSemana().intValue())
        {
            case DiaSemana.DOMINGO :
                return "D";
            case DiaSemana.SEGUNDA_FEIRA :
                return "2";
            case DiaSemana.TERCA_FEIRA :
                return "3";
            case DiaSemana.QUARTA_FEIRA :
                return "4";
            case DiaSemana.QUINTA_FEIRA :
                return "5";
            case DiaSemana.SEXTA_FEIRA :
                return "6";
            case DiaSemana.SABADO :
                return "S";
            default :
                return "";
        }

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
	 *          DataBeans.InfoObject, org.apache.struts.action.ActionForm,
	 *          javax.servlet.http.HttpServletRequest)
	 */
    protected void populateFormFromInfoObject(
        ActionMapping mapping,
        InfoObject infoObject,
        ActionForm form,
        HttpServletRequest request)
        throws FenixActionException
    {
        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime =
            (InfoTeacherInstitutionWorkTime) infoObject;
        DynaActionForm teacherInstitutionWorkTimeForm = (DynaActionForm) form;

        teacherInstitutionWorkTimeForm.set("idInternal", infoTeacherInstitutionWorkTime.getIdInternal());
        teacherInstitutionWorkTimeForm.set(
            "teacherId",
            infoTeacherInstitutionWorkTime.getInfoTeacher().getIdInternal());
        teacherInstitutionWorkTimeForm.set(
            "executionPeriodId",
            infoTeacherInstitutionWorkTime.getInfoExecutionPeriod().getIdInternal());

        teacherInstitutionWorkTimeForm.set(
            "weekDay",
            getWeekDayString(infoTeacherInstitutionWorkTime.getWeekDay()));

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date startTime = infoTeacherInstitutionWorkTime.getStartTime();
		Date endTime = infoTeacherInstitutionWorkTime.getEndTime();

		if (startTime != null || endTime != null)
		{
			Calendar time = Calendar.getInstance();
			if (startTime != null)
			{
				time.setTime(startTime);
				teacherInstitutionWorkTimeForm.set("startTimeHour", String.valueOf(time.get(Calendar.HOUR_OF_DAY)));
				teacherInstitutionWorkTimeForm.set("startTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));
			}
			if (endTime != null)
			{
				time.setTime(endTime);
				teacherInstitutionWorkTimeForm.set("endTimeHour", String.valueOf(time.get(Calendar.HOUR_OF_DAY)));
				teacherInstitutionWorkTimeForm.set("endTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));
			}
		}
        teacherInstitutionWorkTimeForm.set(
            "startTime",
            sdf.format(infoTeacherInstitutionWorkTime.getStartTime()));
        teacherInstitutionWorkTimeForm.set(
            "endTime",
            sdf.format(infoTeacherInstitutionWorkTime.getEndTime()));

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateInfoObjectFromForm(org.apache.struts.action.ActionForm,
	 *          ServidorApresentacao.mapping.framework.CRUDMapping)
	 */
    protected InfoObject populateInfoObjectFromForm(ActionForm form, CRUDMapping mapping)
        throws FenixActionException
    {
        DynaActionForm teacherInstitutionWorkTimeForm = (DynaActionForm) form;
        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime =
            new InfoTeacherInstitutionWorkTime();

        infoTeacherInstitutionWorkTime.setIdInternal(
            (Integer) teacherInstitutionWorkTimeForm.get("idInternal"));
        InfoTeacher infoTeacher =
            new InfoTeacher((Integer) teacherInstitutionWorkTimeForm.get("teacherId"));
        infoTeacherInstitutionWorkTime.setInfoTeacher(infoTeacher);

        InfoExecutionPeriod infoExecutionPeriod =
            new InfoExecutionPeriod((Integer) teacherInstitutionWorkTimeForm.get("executionPeriodId"));
        infoTeacherInstitutionWorkTime.setInfoExecutionPeriod(infoExecutionPeriod);

        DiaSemana weekDay = getWeekDay((String) teacherInstitutionWorkTimeForm.get("weekDay"));
        infoTeacherInstitutionWorkTime.setWeekDay(weekDay);

		Calendar calendar = Calendar.getInstance();

		setHoursAndMinutes(
			calendar,
			Integer.valueOf((String) teacherInstitutionWorkTimeForm.get("startTimeHour")),
			Integer.valueOf((String) teacherInstitutionWorkTimeForm.get("startTimeMinutes")));
		infoTeacherInstitutionWorkTime.setStartTime(new Date(calendar.getTimeInMillis()));

		setHoursAndMinutes(
			calendar,
			Integer.valueOf((String) teacherInstitutionWorkTimeForm.get("endTimeHour")),
			Integer.valueOf((String) teacherInstitutionWorkTimeForm.get("endTimeMinutes")));
		infoTeacherInstitutionWorkTime.setEndTime(new Date(calendar.getTimeInMillis()));

        return infoTeacherInstitutionWorkTime;
    }
	/**
	 * @param calendar
	 * @param integer
	 * @param integer2
	 */
	private void setHoursAndMinutes(Calendar calendar, Integer hour, Integer minutes)
	{
		calendar.set(Calendar.HOUR_OF_DAY, hour != null ? hour.intValue() : 0);
		calendar.set(Calendar.MINUTE, minutes != null ? minutes.intValue() : 0);
	}

    
    public ActionForward list(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);

        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        if (infoTeacher == null)
        {
            DynaActionForm teacherInstitutionWorkTimeForm = (DynaActionForm) form;
            Integer teacherId = (Integer) teacherInstitutionWorkTimeForm.get("teacherId");
            infoTeacher = new InfoTeacher(teacherId);
        }
        Object args[] = { infoTeacher };

        TeacherInstitutionWorkingTimeDTO teacherInstitutionWorkingTimeDTO =
            (TeacherInstitutionWorkingTimeDTO) ServiceUtils.executeService(
                userView,
                "ReadTeacherInstitutionWorkingTime",
                args);

        request.setAttribute("teacherInstitutionWorkingTime", teacherInstitutionWorkingTimeDTO);
      
        return mapping.findForward("list-teacher-institution-working-time");
    }

}
