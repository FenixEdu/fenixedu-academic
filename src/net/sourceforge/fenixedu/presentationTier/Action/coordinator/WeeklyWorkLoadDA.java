package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

public class WeeklyWorkLoadDA extends FenixDispatchAction {

    public class CurricularYearWeeklyWorkLoadView {
        final Interval interval;
        final int numberOfWeeks;

        final Interval[] intervals;

        final Set<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);

        public CurricularYearWeeklyWorkLoadView(final DegreeCurricularPlan degreeCurricularPlan,
                final ExecutionPeriod executionPeriod, final Set<ExecutionCourse> executionCourses) {
            final ExecutionDegree executionDegree = findExecutionDegree(executionPeriod, degreeCurricularPlan);
            this.interval = new Interval(
                    new DateMidnight(getBegginingOfLessonPeriod(executionPeriod, executionDegree)),
                    new DateMidnight(getEndOfExamsPeriod(executionPeriod, executionDegree)));
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

        private ExecutionDegree findExecutionDegree(final ExecutionPeriod executionPeriod, final DegreeCurricularPlan degreeCurricularPlan) {
            return degreeCurricularPlan.getExecutionDegreeByYear(executionPeriod.getExecutionYear());
        }

        public Date getBegginingOfLessonPeriod(final ExecutionPeriod executionPeriod, final ExecutionDegree executionDegree) {
            if (executionPeriod.getSemester().intValue() == 1) {
                return executionDegree.getPeriodLessonsFirstSemester().getStart();
            } else if (executionPeriod.getSemester().intValue() == 2) {
                return executionDegree.getPeriodLessonsSecondSemester().getStart();
            } else {
                throw new DomainException("unsupported.execution.period.semester");
            }
        }

        public Date getEndOfExamsPeriod(final ExecutionPeriod executionPeriod, final ExecutionDegree executionDegree) {
            if (executionPeriod.getSemester().intValue() == 1) {
                return executionDegree.getPeriodExamsFirstSemester().getEnd();
            } else if (executionPeriod.getSemester().intValue() == 2) {
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

    public ActionForward prepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        final Collection<ExecutionPeriod> executionPeriods = rootDomainObject.getExecutionPeriodsSet();
        final Set<ExecutionPeriod> sortedExecutionPeriods = new TreeSet<ExecutionPeriod>(executionPeriods);
        request.setAttribute("executionPeriods", sortedExecutionPeriods);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final Integer executionPeriodID = getExecutionPeriodID(dynaActionForm);
        final ExecutionPeriod selectedExecutionPeriod = findExecutionPeriod(executionPeriods, executionPeriodID);
        dynaActionForm.set("executionPeriodID", selectedExecutionPeriod.getIdInternal().toString());

        final Collection<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
        for (final ExecutionDegree executionDegree : selectedExecutionPeriod.getExecutionYear().getExecutionDegreesSortedByDegreeName()) {
            if (executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso() == DegreeType.DEGREE) {
                executionDegrees.add(executionDegree);
            }
        }
        request.setAttribute("executionDegrees", executionDegrees);

        final Set<CurricularYear> curricularYears = new TreeSet<CurricularYear>();
        request.setAttribute("curricularYears", curricularYears);

        final Set<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        request.setAttribute("executionCourses", executionCourses);

        final ExecutionCourse selectedExecutionCourse = (ExecutionCourse)
            setDomainObjectInRequest(dynaActionForm, request, ExecutionCourse.class, "executionCourseID", "executionCourse");
        request.setAttribute("selectedExecutionCourse", selectedExecutionCourse);

        final Integer curricularYearID = getCurricularYearID(dynaActionForm);
        final CurricularYear selecctedCurricularYear = (CurricularYear)
                setDomainObjectInRequest(dynaActionForm, request, CurricularYear.class, "curricularYearID", "selecctedCurricularYear");

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan)
        setDomainObjectInRequest(dynaActionForm, request, DegreeCurricularPlan.class, "degreeCurricularPlanID", "executionCourse");
        if (degreeCurricularPlan != null) {
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlan.getIdInternal());
        	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
        		for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
        			final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
        			final CurricularYear curricularYear = curricularSemester.getCurricularYear();
        			curricularYears.add(curricularYear);

        			if (curricularYearID == null || curricularYear.getIdInternal().equals(curricularYearID)) {
        				for (final ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(selectedExecutionPeriod)) {
        					executionCourses.add(executionCourse);
        				}
        			}
        		}
        	}
        }

        if (selecctedCurricularYear != null) {
            request.setAttribute("curricularYearWeeklyWorkLoadView", new CurricularYearWeeklyWorkLoadView(degreeCurricularPlan, selectedExecutionPeriod, executionCourses));
        }

        return mapping.findForward("showWeeklyWorkLoad");
    }

    private DomainObject setDomainObjectInRequest(final DynaActionForm dynaActionForm, final HttpServletRequest request,
            final Class clazz, final String formAttributeName, final String requestAttributeName)
            throws FenixFilterException, FenixServiceException {
        final String domainObjectIDString = (String) dynaActionForm.get(formAttributeName);
        final Integer domainObjectID = domainObjectIDString == null || domainObjectIDString.length() == 0 ?
                null : Integer.valueOf(domainObjectIDString);
        final DomainObject domainObject = rootDomainObject.readDomainObjectByOID(clazz, domainObjectID);
        request.setAttribute(requestAttributeName, domainObject);
        return domainObject;
    }

    private Integer getCurricularYearID(final DynaActionForm dynaActionForm) {
        final String curricularYearIDString = dynaActionForm.getString("curricularYearID");
        return curricularYearIDString == null || curricularYearIDString.length() == 0 ? null : Integer.valueOf(curricularYearIDString);
	}

	private Integer getExecutionPeriodID(final DynaActionForm dynaActionForm) {
        final String exeutionPeriodIDString = dynaActionForm.getString("executionPeriodID");
        return exeutionPeriodIDString == null || exeutionPeriodIDString.length() == 0 ? null : Integer.valueOf(exeutionPeriodIDString);
    }

    private ExecutionPeriod findExecutionPeriod(final Collection<ExecutionPeriod> executionPeriods, final Integer executionPeriodID) {
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            if (executionPeriodID == null && executionPeriod.getState().equals(PeriodState.CURRENT)) {
                return executionPeriod;
            }
            if (executionPeriodID != null && executionPeriod.getIdInternal().equals(executionPeriodID)) {
                return executionPeriod;
            }
        }
        return null;
    }

}
