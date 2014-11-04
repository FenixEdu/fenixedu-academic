/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Nov 21, 2005
 */
package org.fenixedu.academic.ui.struts.action.credits;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.teacher.professorship.DeleteSupportLesson;
import org.fenixedu.academic.service.services.teacher.professorship.EditSupportLesson;
import org.fenixedu.academic.dto.teacher.professorship.SupportLessonDTO;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.SupportLesson;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.WeekDay;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageTeacherSupportLessonsDispatchAction extends FenixDispatchAction {

    protected void prepareToEdit(SupportLesson supportLesson, Professorship professorship, DynaActionForm supportLessonForm,
            HttpServletRequest request) throws NumberFormatException, FenixServiceException {

        if (supportLesson != null) {
            Date startTime = supportLesson.getStartTime();
            Date endTime = supportLesson.getEndTime();
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
            supportLessonForm.set("weekDay", getValidWeekDay(supportLesson.getWeekDay().getDiaSemana()));
            supportLessonForm.set("place", supportLesson.getPlace());
            supportLessonForm.set("supportLessonID", supportLesson.getExternalId());
            supportLessonForm.set("professorshipID", supportLesson.getProfessorship().getExternalId());

            request.setAttribute("supportLesson", supportLesson);
        }

        request.setAttribute("professorship", professorship);
    }

    protected void editSupportLesson(ActionForm form, HttpServletRequest request, RoleType roleType)
            throws NumberFormatException, FenixServiceException, InvalidPeriodException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        SupportLessonDTO supportLessonDTO = new SupportLessonDTO();

        supportLessonDTO.setExternalId(supportLessonForm.get("supportLessonID").equals("") ? null : (String) supportLessonForm
                .get("supportLessonID"));
        supportLessonDTO.setProfessorshipID((String) supportLessonForm.get("professorshipID"));
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
            EditSupportLesson.runEditSupportLesson(supportLessonDTO, roleType);
        } catch (DomainException e) {
            saveMessages(request, e);
        }
    }

    protected void deleteSupportLesson(HttpServletRequest request, ActionForm form, RoleType roleType)
            throws NumberFormatException, FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        String supportLessonID = (String) supportLessonForm.get("supportLessonID");
        try {
            DeleteSupportLesson.runDeleteSupportLesson(supportLessonID, roleType);
        } catch (DomainException e) {
            saveMessages(request, e);
        }
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
