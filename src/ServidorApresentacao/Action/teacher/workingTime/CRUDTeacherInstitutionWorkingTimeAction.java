/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.teacher.workingTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try
        {
            infoTeacherInstitutionWorkTime.setStartTime(
                sdf.parse((String) teacherInstitutionWorkTimeForm.get("startTime")));
            infoTeacherInstitutionWorkTime.setEndTime(
                sdf.parse((String) teacherInstitutionWorkTimeForm.get("endTime")));
        }
        catch (ParseException e)
        {
            e.printStackTrace(System.out);
            throw new FenixActionException(e);
        }
        return infoTeacherInstitutionWorkTime;
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
