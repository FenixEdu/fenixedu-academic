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
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.CreateWeeklyWorkLoad;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentParticipateApp;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import pt.ist.fenixWebFramework.renderers.components.state.LifeCycleConstants;
import pt.ist.fenixWebFramework.renderers.components.state.ViewState;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentParticipateApp.class, path = "weekly-workload", titleKey = "link.weekly.work.load")
@Mapping(module = "student", path = "/weeklyWorkLoad", input = "/weeklyWorkLoad.do?method=prepare&page=0",
        formBean = "weeklyWorkLoadForm")
@Forwards(@Forward(name = "showWeeklyWorkLoad", path = "/student/weeklyWorkLoad.jsp"))
public class WeeklyWorkLoadDA extends FenixDispatchAction {

    public enum IntervalType {
        LESSON_INTERVAL, EXAM_INTERVAL;
    }

    private static final Comparator<Attends> ATTENDS_COMPARATOR = new Comparator<Attends>() {
        @Override
        public int compare(final Attends attends1, final Attends attends2) {
            final ExecutionCourse executionCourse1 = attends1.getExecutionCourse();
            final ExecutionCourse executionCourse2 = attends2.getExecutionCourse();
            return executionCourse1.getNome().compareTo(executionCourse2.getNome());
        }
    };

    public class WeeklyWorkLoadView {
        final Interval executionPeriodInterval;

        final int numberOfWeeks;

        final Map<Attends, WeeklyWorkLoad[]> weeklyWorkLoadMap = new TreeMap<Attends, WeeklyWorkLoad[]>(ATTENDS_COMPARATOR);

        final Interval[] intervals;

        final IntervalType[] intervalTypes;

        public WeeklyWorkLoadView(final Interval executionPeriodInterval) {
            this.executionPeriodInterval = executionPeriodInterval;
            final Period period = executionPeriodInterval.toPeriod();
            int extraWeek = period.getDays() > 0 ? 1 : 0;
            numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks() + extraWeek + 1;
            intervals = new Interval[numberOfWeeks];
            intervalTypes = new IntervalType[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                final DateTime start = executionPeriodInterval.getStart().plusWeeks(i);
                final DateTime end = start.plusWeeks(1);
                intervals[i] = new Interval(start, end);
            }
        }

        public void add(final Attends attends) {
            final WeeklyWorkLoad[] weeklyWorkLoadArray = new WeeklyWorkLoad[numberOfWeeks];
            for (final WeeklyWorkLoad weeklyWorkLoad : attends.getWeeklyWorkLoadsSet()) {
                weeklyWorkLoadArray[weeklyWorkLoad.getWeekOffset()] = weeklyWorkLoad;
            }
            weeklyWorkLoadMap.put(attends, weeklyWorkLoadArray);
        }

        public Interval[] getIntervals() {
            return intervals;
        }

        public Interval getExecutionPeriodInterval() {
            return executionPeriodInterval;
        }

        public Map<Attends, WeeklyWorkLoad[]> getWeeklyWorkLoadMap() {
            return weeklyWorkLoadMap;
        }
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {

        final Collection<ExecutionSemester> executionSemesters = rootDomainObject.getExecutionPeriodsSet();
        final Set<ExecutionSemester> sortedExecutionPeriods = new TreeSet<ExecutionSemester>(executionSemesters);
        request.setAttribute("executionPeriods", sortedExecutionPeriods);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final String executionPeriodID = getExecutionPeriodID(dynaActionForm);
        final ExecutionSemester selectedExecutionPeriod = findExecutionPeriod(executionSemesters, executionPeriodID);
        request.setAttribute("selectedExecutionPeriod", selectedExecutionPeriod);

        dynaActionForm.set("executionPeriodID", selectedExecutionPeriod.getExternalId().toString());

        // if (selectedExecutionPeriod.isCurrent()) {
        // request.setAttribute("weeks", getWeeks(selectedExecutionPeriod));
        // }

        final Attends firstAttends = findFirstAttends(request, selectedExecutionPeriod);
        request.setAttribute("firstAttends", firstAttends);
        if (firstAttends != null) {
            final Interval executionPeriodInterval = firstAttends.getWeeklyWorkLoadInterval();
            final WeeklyWorkLoadView weeklyWorkLoadView = new WeeklyWorkLoadView(executionPeriodInterval);
            request.setAttribute("weeklyWorkLoadView", weeklyWorkLoadView);

            final Collection<Attends> attends = new ArrayList<Attends>();
            request.setAttribute("attends", attends);

            for (final Registration registration : getUserView(request).getPerson().getStudents()) {
                for (final Attends attend : registration.getOrderedAttends()) {
                    if (attend.getEnrolment() != null) {
                        final ExecutionCourse executionCourse = attend.getExecutionCourse();
                        if (executionCourse.getExecutionPeriod() == selectedExecutionPeriod) {
                            weeklyWorkLoadView.add(attend);
                            attends.add(attend);
                        }
                    }
                }
            }

            request.setAttribute("weeklyWorkLoadBean", new WeeklyWorkLoadBean());
        }

        dynaActionForm.set("contact", null);
        dynaActionForm.set("autonomousStudy", null);
        dynaActionForm.set("other", null);

        return mapping.findForward("showWeeklyWorkLoad");
    }

    private static class Week {
        private int weekOffset;
        private Interval interval;

        public int getWeekOffset() {
            return weekOffset;
        }

        public void setWeekOffset(int weekOffset) {
            this.weekOffset = weekOffset;
        }

        public Interval getInterval() {
            return interval;
        }

        public void setInterval(Interval interval) {
            this.interval = interval;
        }

    }

    private SortedSet<Week> getWeeks(final ExecutionSemester executionSemester) {
        return null;
    }

    private Attends findFirstAttends(final HttpServletRequest request, final ExecutionSemester selectedExecutionPeriod)
            throws FenixServiceException {
        for (final Registration registration : getUserView(request).getPerson().getStudents()) {
            for (final Attends attend : registration.getOrderedAttends()) {
                final ExecutionCourse executionCourse = attend.getExecutionCourse();
                if (executionCourse.getExecutionPeriod() == selectedExecutionPeriod && attend.getEnrolment() != null) {
                    return attend;
                }
            }
        }
        return null;
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        final WeeklyWorkLoadBean weeklyWorkLoadBean = getWeeklyWorkLoadBean(request);

        final String attendsID = weeklyWorkLoadBean.getAttendsID();
        final Integer contact = weeklyWorkLoadBean.getContact();
        final Integer autonomousStudy = weeklyWorkLoadBean.getAutonomousStudy();
        final Integer other = weeklyWorkLoadBean.getOther();

        create(request, attendsID, contact, autonomousStudy, other);

        return prepare(mapping, form, request, response);
    }

    public ActionForward createFromForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final String attendsID = dynaActionForm.getString("attendsID");
        final Integer contact = getInteger(dynaActionForm, "contact");
        final Integer autonomousStudy = getInteger(dynaActionForm, "autonomousStudy");
        final Integer other = getInteger(dynaActionForm, "other");

        create(request, attendsID, contact, autonomousStudy, other);

        dynaActionForm.set("contact", null);
        dynaActionForm.set("autonomousStudy", null);
        dynaActionForm.set("other", null);

        return prepare(mapping, form, request, response);
    }

    public void create(final HttpServletRequest request, final String attendsID, final Integer contact,
            final Integer autonomousStudy, final Integer other) throws FenixServiceException {

        CreateWeeklyWorkLoad.run(attendsID, contact, autonomousStudy, other);
    }

    private WeeklyWorkLoadBean getWeeklyWorkLoadBean(final HttpServletRequest request) {
        ViewState viewState = (ViewState) request.getAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
        return (WeeklyWorkLoadBean) viewState.getMetaObject().getObject();
    }

    private String getExecutionPeriodID(final DynaActionForm dynaActionForm) {
        final String exeutionPeriodIDString = dynaActionForm.getString("executionPeriodID");
        return exeutionPeriodIDString == null || exeutionPeriodIDString.length() == 0 ? null : exeutionPeriodIDString;
    }

    private ExecutionSemester findExecutionPeriod(final Collection<ExecutionSemester> executionSemesters,
            final String executionPeriodID) {
        for (final ExecutionSemester executionSemester : executionSemesters) {
            if (executionPeriodID == null && executionSemester.getState().equals(PeriodState.CURRENT)) {
                return executionSemester;
            }
            if (executionPeriodID != null && executionSemester.getExternalId().equals(executionPeriodID)) {
                return executionSemester;
            }
        }
        return null;
    }

}