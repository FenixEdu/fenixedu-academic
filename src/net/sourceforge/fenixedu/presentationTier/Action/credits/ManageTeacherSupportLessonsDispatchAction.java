/**
 * Nov 21, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageTeacherSupportLessonsDispatchAction extends FenixDispatchAction {

    protected void getSupportLessons(HttpServletRequest request, Professorship professorship) {
        if (!professorship.getSupportLessons().isEmpty()) {
            request.setAttribute("supportLessonList", professorship
                    .getSupportLessonsOrderedByStartTimeAndWeekDay());
        }
        request.setAttribute("professorship", professorship);
    }

    protected void prepareToEdit(SupportLesson supportLesson, Professorship professorship,
            DynaActionForm supportLessonForm, HttpServletRequest request) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        if (supportLesson != null) {
            Date startTime = supportLesson.getStartTime();
            Date endTime = supportLesson.getEndTime();
            if (startTime != null || endTime != null) {
                Calendar time = Calendar.getInstance();
                if (startTime != null) {
                    time.setTime(startTime);
                    supportLessonForm.set("startTimeHour", String
                            .valueOf(time.get(Calendar.HOUR_OF_DAY)));
                    supportLessonForm.set("startTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));
                }
                if (endTime != null) {
                    time.setTime(endTime);
                    supportLessonForm.set("endTimeHour", String.valueOf(time.get(Calendar.HOUR_OF_DAY)));
                    supportLessonForm.set("endTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));
                }
            }
            supportLessonForm.set("weekDay", getValidWeekDay(supportLesson.getWeekDay().getDiaSemana()));
            supportLessonForm.set("place", supportLesson.getPlace());
            supportLessonForm.set("supportLessonID", supportLesson.getIdInternal());
            supportLessonForm.set("professorshipID", supportLesson.getProfessorship().getIdInternal());

            request.setAttribute("supportLesson", supportLesson);
        }

        request.setAttribute("professorship", professorship);
    }

    protected void editSupportLesson(ActionForm form, HttpServletRequest request, RoleType roleType)
            throws NumberFormatException, FenixFilterException, FenixServiceException,
            InvalidPeriodException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        SupportLessonDTO supportLessonDTO = new SupportLessonDTO();

        supportLessonDTO.setIdInternal((Integer) supportLessonForm.get("supportLessonID"));
        supportLessonDTO.setProfessorshipID((Integer) supportLessonForm.get("professorshipID"));
        supportLessonDTO.setWeekDay(getCorrectWeekDay((String) supportLessonForm.get("weekDay")));
        Calendar calendar = Calendar.getInstance();

        setHoursAndMinutes(calendar, Integer.valueOf((String) supportLessonForm.get("startTimeHour")),
                Integer.valueOf((String) supportLessonForm.get("startTimeMinutes")));
        supportLessonDTO.setStartTime(new Date(calendar.getTimeInMillis()));

        setHoursAndMinutes(calendar, Integer.valueOf((String) supportLessonForm.get("endTimeHour")),
                Integer.valueOf((String) supportLessonForm.get("endTimeMinutes")));
        supportLessonDTO.setEndTime(new Date(calendar.getTimeInMillis()));
        supportLessonDTO.setPlace((String) supportLessonForm.get("place"));

        Calendar begin = Calendar.getInstance();
        begin.setTime(supportLessonDTO.getStartTime());
        Calendar end = Calendar.getInstance();
        end.setTime(supportLessonDTO.getEndTime());

        if (end.before(begin)) {
            throw new InvalidPeriodException();
        }
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "EditSupportLesson",
                    new Object[] { supportLessonDTO, roleType });
        } catch (DomainException e) {
            saveMessages(request, e);
        }
    }

    protected void deleteSupportLesson(HttpServletRequest request, ActionForm form, RoleType roleType)
            throws NumberFormatException, FenixFilterException, FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        Integer supportLessonID = (Integer) supportLessonForm.get("supportLessonID");
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteSupportLesson",
                    new Object[] { supportLessonID, roleType });
        } catch (DomainException e) {
            saveMessages(request, e);
        }
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException,
            FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");
        Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

        getSupportLessons(request, professorship);
        return mapping.findForward("list-support-lessons");
    }

    private String getValidWeekDay(Integer diaSemana) {
        switch (diaSemana) {     
        case 2:
            return WeekDay.MONDAY.getName();
        case 3:
            return WeekDay.TUESDAY.getName();
        case 4:
            return WeekDay.WEDNESDAY.getName();
        case 5:
            return WeekDay.THURSDAY.getName();
        case 6:
            return WeekDay.FRIDAY.getName();
        case 7:
            return WeekDay.SATURDAY.getName();
        default:
            break;
        }
        return null;
    }

    private DiaSemana getCorrectWeekDay(String weekDayString) {
        WeekDay weekDay = WeekDay.valueOf(weekDayString);
        switch (weekDay) {
        case MONDAY:
            return new DiaSemana(2);
        case TUESDAY:
            return new DiaSemana(3);
        case WEDNESDAY:
            return new DiaSemana(4);
        case THURSDAY:
            return new DiaSemana(5);
        case FRIDAY:
            return new DiaSemana(6);
        case SATURDAY:
            return new DiaSemana(7);
        default:
            break;
        }
        return null;
    }

    private void setHoursAndMinutes(Calendar calendar, Integer hour, Integer minutes) {
        calendar.set(Calendar.HOUR_OF_DAY, hour != null ? hour.intValue() : 0);
        calendar.set(Calendar.MINUTE, minutes != null ? minutes.intValue() : 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public class InvalidPeriodException extends FenixActionException {
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }
}
