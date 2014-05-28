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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;

@Mapping(module = "coordinator", path = "/weeklyWorkLoad", formBean = "weeklyWorkLoadForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "showWeeklyWorkLoad", path = "/coordinator/weeklyWorkLoad.jsp"))
public class WeeklyWorkLoadDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public class CurricularYearWeeklyWorkLoadView {
        Interval interval;
        int numberOfWeeks;

        Interval[] intervals;

        final Set<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(
                ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);

        public CurricularYearWeeklyWorkLoadView(final DegreeCurricularPlan degreeCurricularPlan,
                final ExecutionSemester executionSemester, final Set<ExecutionCourse> executionCourses) {
            final ExecutionDegree executionDegree = findExecutionDegree(executionSemester, degreeCurricularPlan);

            if (executionDegree != null) {
                this.interval =
                        new Interval(new DateMidnight(getBegginingOfLessonPeriod(executionSemester, executionDegree)),
                                new DateMidnight(getEndOfExamsPeriod(executionSemester, executionDegree)));
                final Period period = interval.toPeriod();
                int extraWeek = period.getDays() > 0 ? 1 : 0;
                numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks() + extraWeek + 1;
                intervals = new Interval[numberOfWeeks];
                for (int i = 0; i < numberOfWeeks; i++) {
                    final DateTime start = interval.getStart().plusWeeks(i);
                    final DateTime end = start.plusWeeks(1);
                    intervals[i] = new Interval(start, end);
                }
                this.executionCourses.addAll(executionCourses);
            }
        }

        private ExecutionDegree findExecutionDegree(final ExecutionSemester executionSemester,
                final DegreeCurricularPlan degreeCurricularPlan) {
            return degreeCurricularPlan.getExecutionDegreeByYear(executionSemester.getExecutionYear());
        }

        public Date getBegginingOfLessonPeriod(final ExecutionSemester executionSemester, final ExecutionDegree executionDegree) {
            if (executionSemester.getSemester().intValue() == 1) {
                return executionDegree.getPeriodLessonsFirstSemester().getStart();
            } else if (executionSemester.getSemester().intValue() == 2) {
                return executionDegree.getPeriodLessonsSecondSemester().getStart();
            } else {
                throw new DomainException("unsupported.execution.period.semester");
            }
        }

        public Date getEndOfExamsPeriod(final ExecutionSemester executionSemester, final ExecutionDegree executionDegree) {
            if (executionSemester.getSemester().intValue() == 1) {
                return executionDegree.getPeriodExamsFirstSemester().getEnd();
            } else if (executionSemester.getSemester().intValue() == 2) {
                return executionDegree.getPeriodExamsSecondSemester().getEnd();
            } else {
                throw new DomainException("unsupported.execution.period.semester");
            }
        }

        public Interval[] getIntervals() {
            return intervals;
        }

        public Interval getInterval() {
            return interval;
        }

        public Set<ExecutionCourse> getExecutionCourses() {
            return executionCourses;
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
        dynaActionForm.set("executionPeriodID", selectedExecutionPeriod.getExternalId().toString());

        final Collection<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
        for (final ExecutionDegree executionDegree : selectedExecutionPeriod.getExecutionYear()
                .getExecutionDegreesSortedByDegreeName()) {
            if (executionDegree.getDegreeCurricularPlan().getDegree().getDegreeType() == DegreeType.DEGREE) {
                executionDegrees.add(executionDegree);
            }
        }
        request.setAttribute("executionDegrees", executionDegrees);

        final Set<CurricularYear> curricularYears = new TreeSet<CurricularYear>(rootDomainObject.getCurricularYearsSet());
        request.setAttribute("curricularYears", curricularYears);

        final Set<ExecutionCourse> executionCourses =
                new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        request.setAttribute("executionCourses", executionCourses);

        final ExecutionCourse selectedExecutionCourse =
                setDomainObjectInRequest(dynaActionForm, request, "executionCourseID", "executionCourse");
        request.setAttribute("selectedExecutionCourse", selectedExecutionCourse);

        final String curricularYearID = getCurricularYearID(dynaActionForm);
        final CurricularYear selecctedCurricularYear =
                setDomainObjectInRequest(dynaActionForm, request, "curricularYearID", "selecctedCurricularYear");

        final DegreeCurricularPlan degreeCurricularPlan =
                setDomainObjectInRequest(dynaActionForm, request, "degreeCurricularPlanID", "executionCourse");
        if (degreeCurricularPlan != null) {
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlan.getExternalId());
            for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
                for (final DegreeModuleScope degreeCourseScope : curricularCourse.getDegreeModuleScopes()) {
                    final CurricularYear curricularYear = CurricularYear.readByYear(degreeCourseScope.getCurricularYear());
                    curricularYears.add(curricularYear);

                    if (curricularYearID == null || curricularYear.getExternalId().equals(curricularYearID)) {
                        for (final ExecutionCourse executionCourse : curricularCourse
                                .getExecutionCoursesByExecutionPeriod(selectedExecutionPeriod)) {
                            executionCourses.add(executionCourse);
                        }
                    }
                }
            }
        }

        if (selecctedCurricularYear != null) {
            request.setAttribute("curricularYearWeeklyWorkLoadView", new CurricularYearWeeklyWorkLoadView(degreeCurricularPlan,
                    selectedExecutionPeriod, executionCourses));
        }

        return mapping.findForward("showWeeklyWorkLoad");
    }

    private <T extends DomainObject> T setDomainObjectInRequest(final DynaActionForm dynaActionForm,
            final HttpServletRequest request, final String formAttributeName, final String requestAttributeName) {
        final T domainObject = getDomainObject(dynaActionForm, formAttributeName);
        request.setAttribute(requestAttributeName, domainObject);
        return domainObject;
    }

    private String getCurricularYearID(final DynaActionForm dynaActionForm) {
        final String curricularYearIDString = dynaActionForm.getString("curricularYearID");
        return curricularYearIDString == null || curricularYearIDString.length() == 0 ? null : curricularYearIDString;
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
