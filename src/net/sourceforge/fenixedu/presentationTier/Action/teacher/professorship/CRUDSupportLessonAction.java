package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.presentationTier.Action.framework.CRUDActionByOID;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.mapping.framework.CRUDMapping;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class CRUDSupportLessonAction extends CRUDActionByOID {

    private DiaSemana getWeekDay(String weekday) {
        int weekDayInt = 0;
        try {
            weekDayInt = Integer.parseInt(weekday);
        } catch (NumberFormatException e) {
            if (weekday.equalsIgnoreCase("S")) {
                weekDayInt = DiaSemana.SABADO;
            } else if (weekday.equalsIgnoreCase("D")) {
                weekDayInt = DiaSemana.DOMINGO;
            }
        }
        return new DiaSemana(weekDayInt);
    }

    private String getWeekDayString(DiaSemana weekday) {
        switch (weekday.getDiaSemana().intValue()) {
        case DiaSemana.DOMINGO:
            return "D";
        case DiaSemana.SEGUNDA_FEIRA:
            return "2";
        case DiaSemana.TERCA_FEIRA:
            return "3";
        case DiaSemana.QUARTA_FEIRA:
            return "4";
        case DiaSemana.QUINTA_FEIRA:
            return "5";
        case DiaSemana.SEXTA_FEIRA:
            return "6";
        case DiaSemana.SABADO:
            return "S";
        default:
            return "";
        }

    }

    protected void populateFormFromInfoObject(ActionMapping mapping, InfoObject infoObject,
            ActionForm form, HttpServletRequest request) {
        InfoSupportLesson infoSupportLesson = (InfoSupportLesson) infoObject;
        DynaActionForm supportLessonForm = (DynaActionForm) form;

        supportLessonForm.set("idInternal", infoSupportLesson.getIdInternal());
        supportLessonForm.set("infoProfessorshipId", infoSupportLesson.getInfoProfessorship()
                .getIdInternal());
        supportLessonForm.set("weekDay", getWeekDayString(infoSupportLesson.getWeekDay()));
        Date startTime = infoSupportLesson.getStartTime();
        Date endTime = infoSupportLesson.getEndTime();

        if (startTime != null || endTime != null) {
            Calendar time = Calendar.getInstance();
            if (startTime != null) {
                time.setTime(startTime);
                supportLessonForm.set("startTimeHour", String.valueOf(time.get(Calendar.HOUR_OF_DAY)));
                supportLessonForm.set("startTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));
            }
            if (endTime != null) {
                time.setTime(endTime);
                supportLessonForm.set("endTimeHour", String.valueOf(time.get(Calendar.HOUR_OF_DAY)));
                supportLessonForm.set("endTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));
            }
        }
        supportLessonForm.set("place", infoSupportLesson.getPlace());
    }

    protected InfoObject populateInfoObjectFromForm(ActionForm form, CRUDMapping mapping) {
        DynaActionForm supportLessonForm = (DynaActionForm) form;
        InfoSupportLesson infoSupportLesson = new InfoSupportLesson();

        infoSupportLesson.setIdInternal((Integer) supportLessonForm.get("idInternal"));
        final Professorship professorship = rootDomainObject
		.readProfessorshipByOID((Integer) supportLessonForm.get("infoProfessorshipId"));
        InfoProfessorship infoProfessorship = InfoProfessorship.newInfoFromDomain(professorship);

        infoSupportLesson.setInfoProfessorship(infoProfessorship);

        DiaSemana weekDay = getWeekDay((String) supportLessonForm.get("weekDay"));
        infoSupportLesson.setWeekDay(weekDay);

        Calendar calendar = Calendar.getInstance();

        setHoursAndMinutes(calendar, Integer.valueOf((String) supportLessonForm.get("startTimeHour")),
                Integer.valueOf((String) supportLessonForm.get("startTimeMinutes")));
        infoSupportLesson.setStartTime(new Date(calendar.getTimeInMillis()));

        setHoursAndMinutes(calendar, Integer.valueOf((String) supportLessonForm.get("endTimeHour")),
                Integer.valueOf((String) supportLessonForm.get("endTimeMinutes")));
        infoSupportLesson.setEndTime(new Date(calendar.getTimeInMillis()));

        infoSupportLesson.setPlace((String) supportLessonForm.get("place"));

        return infoSupportLesson;
    }

    protected void prepareFormConstants(ActionMapping mapping, ActionForm form,
            HttpServletRequest request) throws FenixServiceException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm supportLessonForm = (DynaActionForm) form;

        Integer professorshipId = (Integer) supportLessonForm.get("infoProfessorshipId");

        InfoProfessorship infoProfessorship = (InfoProfessorship) ServiceUtils.executeService(userView,
                "ReadProfessorshipByOID", new Object[] { professorshipId });
        request.setAttribute("infoProfessorship", infoProfessorship);
    }

    private void setHoursAndMinutes(Calendar calendar, Integer hour, Integer minutes) {
        calendar.set(Calendar.HOUR_OF_DAY, hour != null ? hour.intValue() : 0);
        calendar.set(Calendar.MINUTE, minutes != null ? minutes.intValue() : 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}