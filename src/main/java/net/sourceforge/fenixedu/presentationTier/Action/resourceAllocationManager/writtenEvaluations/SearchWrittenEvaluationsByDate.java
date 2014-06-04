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
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.writtenEvaluations;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMEvaluationsApp;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = RAMEvaluationsApp.class, path = "search-by-date", titleKey = "link.written.evaluations.search.by.date")
@Mapping(module = "resourceAllocationManager", path = "/searchWrittenEvaluationsByDate", formBean = "examSearchByDateForm")
@Forwards(@Forward(name = "show", path = "/resourceAllocationManager/writtenEvaluations/showWrittenEvaluationsByDate.jsp"))
public class SearchWrittenEvaluationsByDate extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("show");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final LocalDate day = getDate(dynaActionForm);
        LocalTime begin = getTimeDateFromForm(dynaActionForm, "beginningHour", "beginningMinute");
        if (begin == null) {
            begin = new LocalTime(0, 0, 0);
        }
        LocalTime end = getTimeDateFromForm(dynaActionForm, "endHour", "endMinute");
        if (end == null) {
            end = new LocalTime(23, 59, 59);
        }

        return search(mapping, request, day, begin, end, dynaActionForm);
    }

    public ActionForward returnToSearchPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final String date = request.getParameter("date");
        final String selectedBegin = request.getParameter("selectedBegin");
        final String selectedEnd = request.getParameter("selectedEnd");

        final String year = date.substring(0, 4);
        final String month = date.substring(5, 7);
        final String day = date.substring(8, 10);
        dynaActionForm.set("year", year);
        dynaActionForm.set("month", month);
        dynaActionForm.set("day", day);

        final LocalTime begin;
        if (selectedBegin != null && selectedBegin.length() > 0) {
            final String hour = selectedBegin.substring(0, 2);
            final String minute = selectedBegin.substring(3, 5);
            dynaActionForm.set("beginningHour", hour);
            dynaActionForm.set("beginningMinute", minute);
            begin = getTimeDateFromForm(hour, minute);
        } else {
            begin = new LocalTime(0, 0, 0);
        }
        final LocalTime end;
        if (selectedEnd != null && selectedEnd.length() > 0) {
            final String hour = selectedEnd.substring(0, 2);
            final String minute = selectedEnd.substring(3, 5);
            dynaActionForm.set("endHour", selectedEnd.substring(0, 2));
            dynaActionForm.set("endMinute", selectedEnd.substring(3, 5));
            end = getTimeDateFromForm(hour, minute);
        } else {
            end = new LocalTime(23, 59, 59);
        }

        final LocalDate localDate = new LocalDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        return search(mapping, request, localDate, begin, end, dynaActionForm);
    }

    public static boolean isEvalBetweenDates(final WrittenEvaluation eval, final LocalTime begin, final LocalTime end) {
        final HourMinuteSecond bhms = eval.getBeginningDateHourMinuteSecond();
        final HourMinuteSecond ehms = eval.getEndDateHourMinuteSecond();
        return bhms.toLocalTime().isBefore(end) && ehms.toLocalTime().isAfter(begin);
    }

    public ActionForward search(ActionMapping mapping, HttpServletRequest request, final LocalDate day, final LocalTime begin,
            final LocalTime end, DynaActionForm dynaActionForm) throws Exception {
        Integer totalOfStudents = 0;
        final Set<WrittenEvaluation> writtenEvaluations = new HashSet<WrittenEvaluation>();
        for (final ExecutionCourse executionCourse : getExecutionCoursesActiveIn(day)) {
            for (final Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
                if (evaluation instanceof WrittenEvaluation) {
                    final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
                    final LocalDate evaluationDate = writtenEvaluation.getDayDateYearMonthDay().toLocalDate();
                    if (evaluationDate != null && evaluationDate.equals(day) && isEvalBetweenDates(writtenEvaluation, begin, end)) {
                        if (!writtenEvaluations.contains(writtenEvaluation)) {
                            totalOfStudents += writtenEvaluation.getCountStudentsEnroledAttendingExecutionCourses();
                        }
                        writtenEvaluations.add(writtenEvaluation);
                    }
                }
            }
        }
        request.setAttribute("availableRoomIndicationMsg", BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                "info.total.students.vs.available.seats", totalOfStudents.toString(), SpaceUtils.countAllAvailableSeatsForExams()
                        .toString()));
        request.setAttribute("writtenEvaluations", writtenEvaluations);
        return mapping.findForward("show");
    }

    private Collection<ExecutionCourse> getExecutionCoursesActiveIn(LocalDate day) {
        DateTime date = day.toDateTimeAtStartOfDay();
        Set<ExecutionCourse> courses = new HashSet<>();
        for (ExecutionInterval interval : Bennu.getInstance().getExecutionIntervalsSet()) {
            if (interval instanceof ExecutionSemester && interval.getAcademicInterval().contains(date)) {
                ExecutionSemester semester = (ExecutionSemester) interval;
                courses.addAll(semester.getAssociatedExecutionCoursesSet());
            }
        }
        return courses;
    }

    private LocalDate getDate(final DynaActionForm dynaActionForm) throws ParseException {
        final String yearString = dynaActionForm.getString("year");
        final String monthString = dynaActionForm.getString("month");
        final String dayString = dynaActionForm.getString("day");
        return new LocalDate(Integer.parseInt(yearString), Integer.parseInt(monthString), Integer.parseInt(dayString));
    }

    private LocalTime getTimeDateFromForm(final DynaActionForm dynaActionForm, final String hourField, final String minuteField)
            throws ParseException {
        final String hourString = dynaActionForm.getString(hourField);
        final String minuteString = dynaActionForm.getString(minuteField);
        return hourString == null || hourString.isEmpty() || minuteString == null || minuteString.isEmpty() ? null : getTimeDateFromForm(
                hourString, minuteString);
    }

    private LocalTime getTimeDateFromForm(final String hourString, final String minuteString) throws ParseException {
        return new LocalTime(Integer.parseInt(hourString), Integer.parseInt(minuteString), 0);
    }

}