/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.teacher.professorship;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoObject;
import DataBeans.InfoProfessorship;
import DataBeans.teacher.professorship.InfoSupportLesson;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.framework.CRUDActionByOID;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.mapping.framework.CRUDMapping;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class CRUDSupportLessonAction extends CRUDActionByOID
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
        InfoSupportLesson infoSupportLesson = (InfoSupportLesson) infoObject;
        DynaActionForm supportLessonForm = (DynaActionForm) form;

        supportLessonForm.set("idInternal", infoSupportLesson.getIdInternal());
        supportLessonForm.set(
            "infoProfessorshipId",
            infoSupportLesson.getInfoProfessorship().getIdInternal());
        supportLessonForm.set("weekDay", getWeekDayString(infoSupportLesson.getWeekDay()));

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        supportLessonForm.set("startTime", sdf.format(infoSupportLesson.getStartTime()));
        supportLessonForm.set("endTime", sdf.format(infoSupportLesson.getEndTime()));
        supportLessonForm.set("place", infoSupportLesson.getPlace());
    }

    /* (non-Javadoc)
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateInfoObjectFromForm(org.apache.struts.action.ActionForm, ServidorApresentacao.mapping.framework.CRUDMapping)
     */
    protected InfoObject populateInfoObjectFromForm(ActionForm form, CRUDMapping mapping)
        throws FenixActionException
    {
		DynaActionForm supportLessonForm = (DynaActionForm) form;
		InfoSupportLesson infoSupportLesson = new InfoSupportLesson();

		infoSupportLesson.setIdInternal((Integer) supportLessonForm.get("idInternal"));
		InfoProfessorship infoProfessorship =
			new InfoProfessorship((Integer) supportLessonForm.get("infoProfessorshipId"));

		infoSupportLesson.setInfoProfessorship(infoProfessorship);
        

		DiaSemana weekDay = getWeekDay((String) supportLessonForm.get("weekDay"));
		infoSupportLesson.setWeekDay(weekDay);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		try
		{
			infoSupportLesson.setStartTime(sdf.parse((String) supportLessonForm.get("startTime")));
			infoSupportLesson.setEndTime(sdf.parse((String) supportLessonForm.get("endTime")));
		}
		catch (ParseException e)
		{
			e.printStackTrace(System.out);
			throw new FenixActionException(e);
		}

		infoSupportLesson.setPlace((String) supportLessonForm.get("place"));

		return infoSupportLesson;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.framework.CRUDActionByOID#prepareFormConstants(org.apache.struts.action.ActionMapping,
	 *          org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
    protected void prepareFormConstants(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request)
        throws FenixServiceException
    {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm supportLessonForm = (DynaActionForm) form;

        Integer professorshipId = (Integer) supportLessonForm.get("infoProfessorshipId");

        InfoProfessorship infoProfessorship =
            (InfoProfessorship) ServiceUtils.executeService(
                userView,
                "ReadProfessorshipByOID",
                new Object[] { professorshipId });
        request.setAttribute("infoProfessorship", infoProfessorship);
    }

}
