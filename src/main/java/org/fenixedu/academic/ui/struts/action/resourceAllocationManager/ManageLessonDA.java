/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.FrequencyType;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoRoom;
import org.fenixedu.academic.dto.InfoRoomOccupationEditor;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.dto.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceMultipleException;
import org.fenixedu.academic.service.services.resourceAllocationManager.CreateLesson;
import org.fenixedu.academic.service.services.resourceAllocationManager.DeleteLessonInstance;
import org.fenixedu.academic.service.services.resourceAllocationManager.DeleteLessons;
import org.fenixedu.academic.service.services.resourceAllocationManager.EditLesson;
import org.fenixedu.academic.ui.struts.action.exceptions.ExistingActionException;
import org.fenixedu.academic.ui.struts.action.exceptions.InterceptingActionException;
import org.fenixedu.academic.ui.struts.action.exceptions.InvalidTimeIntervalActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.ui.struts.action.utils.ContextUtils;
import org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */

@Mapping(path = "/manageLesson", module = "resourceAllocationManager", input = "/manageLesson.do?method=findInput&page=0",
        formBean = "manageLessonForm", functionality = ExecutionPeriodDA.class)
@Forwards({ @Forward(name = "ShowLessonForm", path = "/resourceAllocationManager/manageLesson_bd.jsp"),
        @Forward(name = "EditShift", path = "/resourceAllocationManager/manageShift.do?method=prepareEditShift&page=0"),
        @Forward(name = "LessonDeleted", path = "/resourceAllocationManager/manageShift.do?method=prepareEditShift&page=0"),
        @Forward(name = "ViewAllLessonDates", path = "/resourceAllocationManager/showAllLessonDates.jsp"),
        @Forward(name = "ChangeRoom", path = "/resourceAllocationManager/changeRoom_bd.jsp") })
@Exceptions({
        @ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException",
                handler = FenixErrorExceptionHandler.class, type = ExistingActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.InterceptingActionException",
                handler = FenixErrorExceptionHandler.class, type = InterceptingActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.InvalidTimeIntervalActionException",
                handler = FenixErrorExceptionHandler.class, type = InvalidTimeIntervalActionException.class) })
public class ManageLessonDA extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextUtils.setLessonContext(request);
        ActionForward actionForward = super.execute(mapping, actionForm, request, response);
        return actionForward;
    }

    public static String INVALID_TIME_INTERVAL = "errors.lesson.invalid.time.interval";

    public static String INVALID_WEEKDAY = "errors.lesson.invalid.weekDay";

    public static String UNKNOWN_ERROR = "errors.unknown";

    public ActionForward findInput(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String action = request.getParameter("action");
        if (action != null && action.equals("edit")) {
            return prepareEdit(mapping, form, request, response);
        }

        return prepareCreate(mapping, form, request, response);
    }

    public ActionForward viewAllLessonDates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final InfoLesson infoLesson = (InfoLesson) request.getAttribute(PresentationConstants.LESSON);
        final Lesson lesson = infoLesson.getLesson();
        final Set<NextPossibleSummaryLessonsAndDatesBean> lessonDatesBean =
                new TreeSet<NextPossibleSummaryLessonsAndDatesBean>(
                        NextPossibleSummaryLessonsAndDatesBean.COMPARATOR_BY_DATE_AND_HOUR);

        for (final LessonInstance instance : infoLesson.getLesson().getLessonInstancesSet()) {
            final NextPossibleSummaryLessonsAndDatesBean bean =
                    new NextPossibleSummaryLessonsAndDatesBean(lesson, instance.getDay());
            bean.setRoom(instance.getRoom());
            bean.setTime(instance.getStartTime());
            lessonDatesBean.add(bean);
        }
        if (!lesson.wasFinished()) {
            for (final YearMonthDay yearMonthDay : lesson.getAllLessonDatesWithoutInstanceDates()) {
                final NextPossibleSummaryLessonsAndDatesBean bean =
                        new NextPossibleSummaryLessonsAndDatesBean(lesson, yearMonthDay);
                bean.setRoom(lesson.getSala());
                bean.setTime(lesson.getBeginHourMinuteSecond());
                lessonDatesBean.add(bean);
            }
        }

        request.setAttribute("lessonDates", lessonDatesBean);
        request.setAttribute("lesson", lesson);
        return mapping.findForward("ViewAllLessonDates");
    }

    public ActionForward deleteLessonInstance(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        NextPossibleSummaryLessonsAndDatesBean bean =
                NextPossibleSummaryLessonsAndDatesBean.getNewInstance(request.getParameter("lessonDate"));

        try {
            DeleteLessonInstance.run(bean.getLesson(), bean.getDate());
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(null, new ActionError("message.deleteLesson", bean.getDate()));
            saveErrors(request, actionErrors);
        } catch (UnsupportedOperationException unsupportedOperationException) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(unsupportedOperationException.getMessage(),
                    new ActionError("error.Lesson.not.instanced", bean.getDate()));
            saveErrors(request, actionErrors);
        } catch (DomainException domainException) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(domainException.getMessage(), new ActionError(domainException.getMessage()));
            saveErrors(request, actionErrors);
        }

        return viewAllLessonDates(mapping, form, request, response);
    }

    public ActionForward deleteLessonInstances(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final SortedSet<NextPossibleSummaryLessonsAndDatesBean> set = new TreeSet<NextPossibleSummaryLessonsAndDatesBean>();
        for (final String lessonDate : request.getParameterValues("lessonDatesToDelete")) {
            set.add(NextPossibleSummaryLessonsAndDatesBean.getNewInstance(lessonDate));
        }

        try {
            DeleteLessonInstance.run(set);
            ActionErrors actionErrors = new ActionErrors();
            for (final NextPossibleSummaryLessonsAndDatesBean n : set) {
                actionErrors.add(null, new ActionError("message.deleteLesson", n.getDate()));
            }
            saveErrors(request, actionErrors);
        } catch (UnsupportedOperationException unsupportedOperationException) {
            ActionErrors actionErrors = new ActionErrors();
            for (final NextPossibleSummaryLessonsAndDatesBean n : set) {
                actionErrors.add(unsupportedOperationException.getMessage(),
                        new ActionError("error.Lesson.not.instanced", n.getDate()));
            }
            saveErrors(request, actionErrors);
        } catch (DomainException domainException) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(domainException.getMessage(), new ActionError(domainException.getMessage()));
            saveErrors(request, actionErrors);
        }

        return viewAllLessonDates(mapping, form, request, response);
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm manageLessonForm = (DynaActionForm) form;

        InfoShift infoShift = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);
        Shift shift = FenixFramework.getDomainObject(infoShift.getExternalId());
        GenericPair<YearMonthDay, YearMonthDay> maxLessonsPeriod = shift.getExecutionCourse().getMaxLessonsPeriod();

        if (maxLessonsPeriod != null) {
            request.setAttribute("executionDegreeLessonsStartDate", maxLessonsPeriod.getLeft().toString("dd/MM/yyyy"));
            request.setAttribute("executionDegreeLessonsEndDate", maxLessonsPeriod.getRight().toString("dd/MM/yyyy"));
            manageLessonForm.set("newBeginDate", maxLessonsPeriod.getLeft().toString("dd/MM/yyyy"));
            manageLessonForm.set("newEndDate", maxLessonsPeriod.getRight().toString("dd/MM/yyyy"));
            manageLessonForm.set("createLessonInstances", Boolean.TRUE);

        } else {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.executionDegree.empty.lessonsPeriod", new ActionError(
                    "error.executionDegree.empty.lessonsPeriod"));
            saveErrors(request, actionErrors);
            return mapping.findForward("EditShift");
        }

        return mapping.findForward("ShowLessonForm");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm manageLessonForm = (DynaActionForm) form;

        InfoLesson infoLesson = (InfoLesson) request.getAttribute(PresentationConstants.LESSON);

        manageLessonForm.set("diaSemana", infoLesson.getDiaSemana().getDiaSemana().toString());
        manageLessonForm.set("horaInicio", "" + infoLesson.getInicio().get(Calendar.HOUR_OF_DAY));
        manageLessonForm.set("minutosInicio", "" + infoLesson.getInicio().get(Calendar.MINUTE));
        manageLessonForm.set("horaFim", "" + infoLesson.getFim().get(Calendar.HOUR_OF_DAY));
        manageLessonForm.set("minutosFim", "" + infoLesson.getFim().get(Calendar.MINUTE));

        final Space allocatableSpace = infoLesson.getAllocatableSpace();
        if (allocatableSpace != null) {
            manageLessonForm.set("nomeSala", "" + allocatableSpace.getName());
        }

        if (infoLesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
            manageLessonForm.set("quinzenal", Boolean.TRUE);
        }

        if (infoLesson.getLessonBegin() != null) {
            manageLessonForm.set("newBeginDate", infoLesson.getLessonBegin().toString("dd/MM/yyyy"));
        }
        if (infoLesson.getLessonEnd() != null) {
            manageLessonForm.set("newEndDate", infoLesson.getLessonEnd().toString("dd/MM/yyyy"));
        }

        manageLessonForm.set("createLessonInstances", Boolean.TRUE);

        Lesson lesson = FenixFramework.getDomainObject(infoLesson.getExternalId());
        GenericPair<YearMonthDay, YearMonthDay> maxLessonsPeriod = lesson.getShift().getExecutionCourse().getMaxLessonsPeriod();
        if (maxLessonsPeriod != null) {
            request.setAttribute("executionDegreeLessonsStartDate", maxLessonsPeriod.getLeft().toString("dd/MM/yyyy"));
            request.setAttribute("executionDegreeLessonsEndDate", maxLessonsPeriod.getRight().toString("dd/MM/yyyy"));
        }

        request.setAttribute("action", "edit");
        return mapping.findForward("ShowLessonForm");
    }

    public ActionForward prepareChangeRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        viewAllLessonDates(mapping, form, request, response);

        final InfoLesson infoLesson = (InfoLesson) request.getAttribute(PresentationConstants.LESSON);
        final Interval[] intervals = infoLesson.getLesson().getAllLessonIntervals().toArray(new Interval[0]);
        final List<Space> emptySpaces = SpaceUtils.allocatableSpace(null, true, intervals);
        Collections.sort(emptySpaces, SpaceUtils.COMPARATOR_BY_PRESENTATION_NAME);
        request.setAttribute("emptySpaces", emptySpaces);

        return mapping.findForward("ChangeRoom");
    }

    public ActionForward changeRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final InfoLesson infoLesson = (InfoLesson) request.getAttribute(PresentationConstants.LESSON);
        final Space space = getDomainObject(request, "spaceOID");

        try {
            EditLesson.run(infoLesson.getLesson(), space);
        } catch (final DomainException domainException) {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(domainException.getMessage(),
                    new ActionError(domainException.getMessage(), domainException.getArgs()));
            saveErrors(request, actionErrors);
        }

        return viewAllLessonDates(mapping, form, request, response);
    }

    public ActionForward createEditLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm manageLessonForm = (DynaActionForm) form;
        request.setAttribute("manageLessonForm", manageLessonForm);

        ContextUtils.setExecutionPeriodContext(request);

        DiaSemana weekDay = new DiaSemana(new Integer(formDay2EnumerateDay((String) manageLessonForm.get("diaSemana"))));
        YearMonthDay newBeginDate = getDateFromForm(manageLessonForm, "newBeginDate");
        YearMonthDay newEndDate = getDateFromForm(manageLessonForm, "newEndDate");

        Boolean quinzenal = (Boolean) manageLessonForm.get("quinzenal");
        if (quinzenal == null) {
            quinzenal = new Boolean(false);
        }

        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaInicio")));
        inicio.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
        inicio.set(Calendar.SECOND, 0);
        inicio.set(Calendar.MILLISECOND, 0);

        Calendar fim = Calendar.getInstance();
        fim.setTimeInMillis(inicio.getTimeInMillis());
        fim.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaFim")));
        fim.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosFim")));
        fim.set(Calendar.SECOND, 0);
        fim.set(Calendar.MILLISECOND, 0);

        InfoRoom infoSala = null;
        if (!StringUtils.isEmpty((String) manageLessonForm.get("nomeSala"))) {
            infoSala = new InfoRoom(SpaceUtils.findAllocatableSpaceForEducationByName((String) manageLessonForm.get("nomeSala")));
        }

        ActionErrors actionErrors = checkTimeIntervalAndWeekDay(inicio, fim, weekDay);
        Lesson lesson = null;
        if (actionErrors.isEmpty()) {

            InfoShift infoShift = (InfoShift) (request.getAttribute(PresentationConstants.SHIFT));

            InfoRoomOccupationEditor infoRoomOccupation = null;
            if (infoSala != null) {
                infoRoomOccupation = new InfoRoomOccupationEditor();
                infoRoomOccupation.setDayOfWeek(weekDay);
                infoRoomOccupation.setEndTime(fim);
                infoRoomOccupation.setStartTime(inicio);
                infoRoomOccupation.setInfoRoom(infoSala);
            }

            final FrequencyType frequency;
            if (quinzenal.booleanValue()) {
                frequency = FrequencyType.BIWEEKLY;
            } else {
                frequency = FrequencyType.WEEKLY;
            }

            if (infoRoomOccupation != null) {
                infoRoomOccupation.setFrequency(frequency);
            }

            String action = request.getParameter("action");
            if (action != null && action.equals("edit")) {

                InfoLesson infoLessonOld = (InfoLesson) request.getAttribute(PresentationConstants.LESSON);
                Boolean createLessonInstances = (Boolean) manageLessonForm.get("createLessonInstances");

                try {
                    lesson =
                            EditLesson.run(infoLessonOld, weekDay, inicio, fim, frequency, infoRoomOccupation, infoShift,
                                    newBeginDate, newEndDate, createLessonInstances);

                } catch (DomainException domainException) {
                    actionErrors.add(domainException.getMessage(),
                            new ActionError(domainException.getMessage(), domainException.getArgs()));
                    saveErrors(request, actionErrors);
                }

            } else {
                try {
                    lesson =
                            CreateLesson.run(weekDay, inicio, fim, frequency, infoRoomOccupation, infoShift, newBeginDate,
                                    newEndDate);

                } catch (DomainException domainException) {
                    actionErrors.add(domainException.getMessage(),
                            new ActionError(domainException.getMessage(), domainException.getArgs()));
                    saveErrors(request, actionErrors);
                }
            }
            if (lesson != null) {
                request.setAttribute(PresentationConstants.LESSON, InfoLesson.newInfoFromDomain(lesson));
                request.setAttribute("action", action != null ? action : "create");
                request.setAttribute("lesson", InfoLesson.newInfoFromDomain(lesson));
                return prepareChangeRoom(mapping, manageLessonForm, request, response);
            }
            return mapping.findForward("EditShift");

        }
        saveErrors(request, actionErrors);
        return mapping.getInputForward();

    }

    public ActionForward deleteLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<String> lessons = new ArrayList<String>();
        lessons.add(request.getParameter(PresentationConstants.LESSON_OID));

        try {
            DeleteLessons.run(lessons);

        } catch (FenixServiceMultipleException e) {
            final ActionErrors actionErrors = new ActionErrors();
            for (final DomainException domainException : e.getExceptionList()) {
                actionErrors.add(domainException.getMessage(),
                        new ActionError(domainException.getMessage(), domainException.getArgs()));
            }
            saveErrors(request, actionErrors);
            return mapping.findForward("LessonDeleted");
        }

        request.removeAttribute(PresentationConstants.LESSON_OID);
        return mapping.findForward("LessonDeleted");
    }

    private YearMonthDay getDateFromForm(DynaActionForm manageLessonForm, String property) {
        String newDateString = (String) manageLessonForm.get(property);
        if (!StringUtils.isEmpty(newDateString)) {
            try {
                int day = Integer.parseInt(newDateString.substring(0, 2));
                int month = Integer.parseInt(newDateString.substring(3, 5));
                int year = Integer.parseInt(newDateString.substring(6, 10));
                if (year == 0 || month == 0 || day == 0) {
                    return null;
                } else {
                    return new YearMonthDay(year, month, day);
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String formDay2EnumerateDay(String string) {
        String result = string;
        if (string.equalsIgnoreCase("2")) {
            result = "2";
        }
        if (string.equalsIgnoreCase("3")) {
            result = "3";
        }
        if (string.equalsIgnoreCase("4")) {
            result = "4";
        }
        if (string.equalsIgnoreCase("5")) {
            result = "5";
        }
        if (string.equalsIgnoreCase("6")) {
            result = "6";
        }
        if (string.equalsIgnoreCase("S")) {
            result = "7";
        }
        return result;
    }

    private ActionErrors checkTimeIntervalAndWeekDay(Calendar begining, Calendar end, DiaSemana weekday) {

        ActionErrors actionErrors = new ActionErrors();
        String beginMinAppend = "";
        String endMinAppend = "";

        if (begining.get(Calendar.MINUTE) == 0) {
            beginMinAppend = "0";
        }

        if (end.get(Calendar.MINUTE) == 0) {
            endMinAppend = "0";
        }

        if (begining.getTime().getTime() >= end.getTime().getTime()) {
            actionErrors.add(
                    INVALID_TIME_INTERVAL,
                    new ActionError(INVALID_TIME_INTERVAL, "" + begining.get(Calendar.HOUR_OF_DAY) + ":"
                            + begining.get(Calendar.MINUTE) + beginMinAppend + " - " + end.get(Calendar.HOUR_OF_DAY) + ":"
                            + end.get(Calendar.MINUTE) + endMinAppend));
        }

        if (weekday.getDiaSemana().intValue() < 1 || weekday.getDiaSemana().intValue() > 7) {
            actionErrors.add(INVALID_WEEKDAY, new ActionError(INVALID_WEEKDAY, ""));
        }

        return actionErrors;
    }
}