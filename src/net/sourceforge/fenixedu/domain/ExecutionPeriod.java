package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.util.PeriodState;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota
 * @author jpvl
 * 
 */
public class ExecutionPeriod extends ExecutionPeriod_Base implements Comparable {

    public ExecutionPeriod() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public int compareTo(Object object) {
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) object;
        final ExecutionYear executionYear = executionPeriod.getExecutionYear();

        if (getExecutionYear() == executionYear) {
            return getSemester().compareTo(executionPeriod.getSemester());
        } else {
            return getExecutionYear().compareTo(executionYear);
        }
    }

    public String getQualifiedName() {
        return new StringBuilder().append(this.getName()).append(" ").append(
                this.getExecutionYear().getYear()).toString();
    }

    public boolean containsDay(Date day) {
        return !(this.getBeginDate().after(day) || this.getEndDate().before(day));
    }

    public DateMidnight getThisMonday() {
        final DateTime beginningOfSemester = new DateTime(this.getBeginDate());
        final DateTime endOfSemester = new DateTime(this.getEndDate());

        if (beginningOfSemester.isAfterNow() || endOfSemester.isBeforeNow()) {
            return null;
        }

        final DateMidnight now = new DateMidnight();
        return now.withField(DateTimeFieldType.dayOfWeek(), 1);
    }

    public Interval getCurrentWeek() {
        final DateMidnight thisMonday = getThisMonday();
        return thisMonday == null ? null : new Interval(thisMonday, thisMonday.plusWeeks(1));
    }

    public Interval getPreviousWeek() {
        final DateMidnight thisMonday = getThisMonday();
        return thisMonday == null ? null : new Interval(thisMonday.minusWeeks(1), thisMonday);
    }

    public boolean isAfter(ExecutionPeriod executionPeriod) {
        return this.compareTo(executionPeriod) > 0;
    }

    public boolean isAfterOrEquals(ExecutionPeriod executionPeriod) {
        return this.compareTo(executionPeriod) >= 0;
    }

    public boolean isBefore(ExecutionPeriod executionPeriod) {
        return this.compareTo(executionPeriod) < 0;
    }

    public boolean isBeforeOrEquals(ExecutionPeriod executionPeriod) {
        return this.compareTo(executionPeriod) <= 0;
    }

    public ExecutionCourse getExecutionCourseByInitials(String courseInitials) {
        for (ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
            if (executionCourse.getSigla().equalsIgnoreCase(courseInitials)) {
                return executionCourse;
            }
        }
        return null;
    }

    public List<ExecutionCourse> getExecutionCoursesWithNoCurricularCourses() {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
            if (!executionCourse.hasAnyAssociatedCurricularCourses()) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public List<ExecutionCourse> getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
            DegreeCurricularPlan degreeCurricularPlan, Integer semester, CurricularYear curricularYear,
            String name) {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
            if (name == null || name.length() == 0
                    || executionCourse.getNome().matches(name.replaceAll("%", ".*"))) {
                if (executionCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(semester,
                        curricularYear, degreeCurricularPlan)) {
                    result.add(executionCourse);
                }
            }
        }
        return result;
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static ExecutionPeriod readActualExecutionPeriod() {
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getState().equals(PeriodState.CURRENT)) {
                return executionPeriod;
            }
        }
        return null;
    }

    public static List<ExecutionPeriod> readNotClosedExecutionPeriods() {
        final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getState().equals(PeriodState.CLOSED)) {
                result.add(executionPeriod);
            }
        }
        return result;
    }

    public static List<ExecutionPeriod> readPublicExecutionPeriods() {
        final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getState().equals(PeriodState.NOT_OPEN)) {
                result.add(executionPeriod);
            }
        }
        return result;
    }

    public static List<ExecutionPeriod> readNotClosedPublicExecutionPeriods() {
        final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getState().equals(PeriodState.NOT_OPEN)
                    && executionPeriod.getState().equals(PeriodState.CLOSED)) {
                result.add(executionPeriod);
            }
        }
        return result;
    }

    public static List<ExecutionPeriod> readExecutionPeriodsInTimePeriod(final Date beginDate,
            final Date endDate) {
        final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getBeginDate().before(endDate)
                    && executionPeriod.getEndDate().after(beginDate)) {
                result.add(executionPeriod);
            }
        }
        return result;
    }

    public static ExecutionPeriod readByNameAndExecutionYear(String name, String year) {
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getName().equals(name)
                    && executionPeriod.getExecutionYear().getYear().equals(year)) {
                return executionPeriod;
            }
        }
        return null;
    }

    public static ExecutionPeriod readBySemesterAndExecutionYear(Integer semester, String year) {
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getSemester().equals(semester)
                    && executionPeriod.getExecutionYear().getYear().equals(year)) {
                return executionPeriod;
            }
        }
        return null;
    }
}
