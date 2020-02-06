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
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYearCE;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota ciapl Dominio
 * 
 */
public class ExecutionYear extends ExecutionYear_Base {

//    static {
//        getRelationExecutionPeriodExecutionYear().addListener(new ExecutionPeriodExecutionYearListener());
//    }

    static final public Comparator<ExecutionYear> COMPARATOR_BY_YEAR = new Comparator<ExecutionYear>() {
        @Override
        public int compare(ExecutionYear o1, ExecutionYear o2) {
            return o1.getYear().compareTo(o2.getYear());
        }
    };

    static final public Comparator<ExecutionYear> REVERSE_COMPARATOR_BY_YEAR = new Comparator<ExecutionYear>() {
        @Override
        public int compare(ExecutionYear o1, ExecutionYear o2) {
            return -COMPARATOR_BY_YEAR.compare(o1, o2);
        }
    };

    private ExecutionYear() {
        super();
        setRootDomainObjectForExecutionYear(Bennu.getInstance());
    }

    public ExecutionYear(AcademicInterval academicInterval, String year) {
        this();
        setAcademicInterval(academicInterval);
        setAcademicCalendarEntry(academicInterval.getAcademicCalendarEntry());
        setBeginDateYearMonthDay(academicInterval.getBeginYearMonthDayWithoutChronology());
        setEndDateYearMonthDay(academicInterval.getEndYearMonthDayWithoutChronology());
        setYear(year);
    }

    public String getYear() {
        return getName();
    }

    public void setYear(String year) {
        if (year == null || StringUtils.isEmpty(year.trim())) {
            throw new DomainException("error.ExecutionYear.empty.year");
        }
        if (Bennu.getInstance().getExecutionYearsSet().stream().filter(ey -> ey != this)
                .anyMatch(ey -> year.equals(ey.getName()))) {
            throw new DomainException("error.ExecutionYear.existing.year");
        }
        super.setName(year);
    }

    @Override
    public String getQualifiedName() {
        return getName();
    }

    /**
     * @deprecated use {@link #getNext()}
     */
    @Deprecated
    public ExecutionYear getNextExecutionYear() {
        return Optional.ofNullable(super.getNext()).map(ei -> (ExecutionYear) ei).orElse(null);
//        AcademicYearCE year = getAcademicInterval().plusYear(1);
//        return getExecutionYear(year);
    }

    /**
     * @deprecated use {@link #getPrevious()}
     */
    @Deprecated
    public ExecutionYear getPreviousExecutionYear() {
        return Optional.ofNullable(super.getPrevious()).map(ei -> (ExecutionYear) ei).orElse(null);
//        AcademicYearCE year = getAcademicInterval().minusYear(1);
//        return getExecutionYear(year);
    }

    @Deprecated
    public int compareTo(ExecutionYear object) {
        return super.compareTo(object);
    }

    @Deprecated
    public boolean isAfter(final ExecutionYear executionYear) {
        return super.compareTo(executionYear) > 0;
    }

    @Deprecated
    public boolean isAfterOrEquals(final ExecutionYear executionYear) {
        return super.compareTo(executionYear) >= 0;
    }

    @Deprecated
    public boolean isBefore(final ExecutionYear executionYear) {
        return super.compareTo(executionYear) < 0;
    }

    @Deprecated
    public boolean isBeforeOrEquals(final ExecutionYear executionYear) {
        return super.compareTo(executionYear) <= 0;
    }

    @Deprecated
    public ExecutionInterval getExecutionSemesterFor(final Integer order) {
        return getChildInterval(order, AcademicPeriod.SEMESTER);
    }

    public ExecutionInterval getChildInterval(final Integer order, final AcademicPeriod type) {
        return getExecutionPeriodsSet().stream()
                .filter(ei -> ei.getChildOrder().equals(order) && ei.getAcademicPeriod().equals(type)).findAny().orElse(null);
    }

    public ExecutionInterval getFirstExecutionPeriod() {
        return this.getExecutionPeriodsSet().stream().min(Comparator.naturalOrder()).orElse(null);
    }

    public ExecutionInterval getLastExecutionPeriod() {
        return this.getExecutionPeriodsSet().stream().max(Comparator.naturalOrder()).orElse(null);
    }

    public boolean containsDate(final DateTime dateTime) {
        final DateMidnight begin = getBeginDateYearMonthDay().toDateMidnight();
        final DateMidnight end = getEndDateYearMonthDay().plusDays(1).toDateMidnight();
        return new Interval(begin, end).contains(dateTime);
    }

//    @Override
//    public boolean isCurrent() {
//        return this.getState().equals(PeriodState.CURRENT);
//    }

    public boolean isOpen() {
        return getState().equals(PeriodState.OPEN);
    }

    public boolean isClosed() {
        return getState().equals(PeriodState.CLOSED);
    }

    public boolean isFor(final String year) {
        return getYear().equals(year);
    }

    public void delete() {
        if (!getExecutionDegreesSet().isEmpty()) {
            throw new Error("cannot.delete.execution.year.because.execution.degrees.exist");
        }
        if (!getStudentGroupSet().isEmpty()) {
            throw new DomainException("error.executionYear.cannotDeleteExecutionYearUsedInAccessControl");
        }
        if (!getTeacherGroupSet().isEmpty()) {
            throw new DomainException("error.executionYear.cannotDeleteExecutionYearUsedInAccessControl");
        }
        if (!getStudentsConcludedInExecutionYearGroupSet().isEmpty()) {
            throw new DomainException("error.executionYear.cannotDeleteExecutionYearUsedInAccessControl");
        }
        for (; !getExecutionPeriodsSet().isEmpty(); getExecutionPeriodsSet().iterator().next().delete()) {
            ;
        }

        setRootDomainObject(null);
        setRootDomainObjectForExecutionYear(null);
        deleteDomainObject();
    }

//    private static class ExecutionPeriodExecutionYearListener extends RelationAdapter<ExecutionYear, ExecutionSemester> {
//        @Override
//        public void beforeAdd(ExecutionYear executionYear, ExecutionSemester executionSemester) {
//            if (executionYear != null && executionSemester != null && executionYear.getExecutionPeriodsSet().size() == 2) {
//                throw new DomainException("error.ExecutionYear.exceeded.number.of.executionPeriods", executionYear.getYear());
//            }
//        }
//    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static ExecutionYear getExecutionYear(AcademicYearCE entry) {
        return entry != null ? (ExecutionYear) entry.getExecutionInterval() : null;
    }

    @Deprecated
    static public ExecutionYear readCurrentExecutionYear() {
        return ExecutionYear.findCurrents().stream().findAny().orElse(null);
//        ExecutionSemester semester = ExecutionSemester.readActualExecutionSemester();
//        if (semester != null) {
//            return semester.getExecutionYear();
//        } else {
//            return null;
//        }
    }

    public static Collection<ExecutionYear> findCurrents() {
        return Stream
                .concat(Bennu.getInstance().getExecutionYearsSet().stream().filter(ey -> ey.isCurrent()),
                        ExecutionInterval.findCurrentsChilds().stream().map(es -> es.getExecutionYear()))
                .collect(Collectors.toSet());
    }

    /**
     * Returns current ExecutionYear for provided calendar.
     * If provided calendar is null, use default academic calendar
     * 
     * @param calendar the calendar to search in
     * @return the current ExecutionYear
     */
    public static ExecutionYear findCurrent(final AcademicCalendarRootEntry calendar) {
        final AcademicCalendarRootEntry calendarToCheck =
                calendar != null ? calendar : Bennu.getInstance().getDefaultAcademicCalendar();
        return findCurrents().stream().filter(ey -> ey.getAcademicInterval().getAcademicCalendar() == calendarToCheck).findFirst()
                .orElse(null);
    }

    static public List<ExecutionYear> readNotClosedExecutionYears() {
        final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (!executionYear.isClosed()) {
                result.add(executionYear);
            }
        }
        return result;
    }

    static public List<ExecutionYear> readExecutionYears(ExecutionYear startYear, ExecutionYear endYear) {
        final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
        result.add(startYear);

        ExecutionYear year = startYear.getNextExecutionYear();
        while (year != null && year.isBeforeOrEquals(endYear)) {
            result.add(year);
            year = year.getNextExecutionYear();
        }
        return result;
    }

    static public ExecutionYear readExecutionYearByName(final String year) {
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.isFor(year)) {
                return executionYear;
            }
        }
        return null;
    }

    public static class ExecutionYearSearchCache {
        private final Map<Integer, Set<ExecutionYear>> map = new HashMap<Integer, Set<ExecutionYear>>();

        private Set<ExecutionYear> updateIfNeeded(final Integer year) {
            Set<ExecutionYear> result = map.get(year);

            // for a given civil year, a maximum of two ExecutionYear can be indexed => must update cache if only one ExecutionYear is cached
            if (result == null || result.size() < 2) {
                for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
                    add(executionYear);
                }
                result = map.get(year);
            }

            return result;
        }

        public ExecutionYear findByDateTime(final DateTime dateTime) {
            final Integer year = Integer.valueOf(dateTime.getYear());
            final Set<ExecutionYear> executionYears = updateIfNeeded(year);
            if (executionYears != null) {
                for (final ExecutionYear executionYear : executionYears) {
                    if (executionYear.containsDate(dateTime)) {
                        return executionYear;
                    }
                }
            }
            return null;
        }

        public ExecutionYear findByPartial(final Partial partial) {
            final Integer year = Integer.valueOf(partial.get(DateTimeFieldType.year()));
            final Set<ExecutionYear> executionYears = updateIfNeeded(year);
            if (executionYears != null) {
                for (final ExecutionYear executionYear : executionYears) {
                    if (executionYear.getBeginDateYearMonthDay().getYear() == year) {
                        return executionYear;
                    }
                }
            }
            return null;
        }

        private void add(final ExecutionYear executionYear) {
            final Integer year1 = executionYear.getBeginDateYearMonthDay().getYear();
            final Integer year2 = executionYear.getEndDateYearMonthDay().getYear();
            add(executionYear, year1);
            add(executionYear, year2);
        }

        private void add(final ExecutionYear executionYear, final Integer year) {
            Set<ExecutionYear> executionYears = map.get(year);
            if (executionYears == null) {
                executionYears = new HashSet<ExecutionYear>();
                map.put(year, executionYears);
            }
            executionYears.add(executionYear);
        }
    }

    private static final ExecutionYearSearchCache executionYearSearchCache = new ExecutionYearSearchCache();

    static public ExecutionYear readByDateTime(final DateTime dateTime) {
        return executionYearSearchCache.findByDateTime(dateTime);
    }

    static public ExecutionYear readByDateTime(final LocalDate localDate) {
        return executionYearSearchCache.findByDateTime(localDate.toDateTimeAtCurrentTime());
    }

    static public ExecutionYear readByPartial(final Partial partial) {
        return executionYearSearchCache.findByPartial(partial);
    }

    public static ExecutionYear readBy(final YearMonthDay begin, YearMonthDay end) {
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.getBeginDateYearMonthDay().isEqual(begin) && executionYear.getEndDateYearMonthDay().isEqual(end)) {
                return executionYear;
            }
        }
        return null;
    }

    static public ExecutionYear getExecutionYearByDate(final YearMonthDay date) {
        return readByDateTime(date.toDateTimeAtMidnight());
    }

    static public ExecutionYear readFirstExecutionYear() {
        return Bennu.getInstance().getExecutionYearsSet().stream().min(Comparator.naturalOrder()).orElse(null);
    }

    static public ExecutionYear readLastExecutionYear() {
        return Bennu.getInstance().getExecutionYearsSet().stream().max(Comparator.naturalOrder()).orElse(null);
    }

    public static ExecutionYear readByAcademicInterval(AcademicInterval academicInterval) {
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.getAcademicInterval().equals(academicInterval)) {
                return executionYear;
            }
        }
        return null;
    }

    @Override
    public ExecutionYear getExecutionYear() {
        return this;
    }

    @Override
    public Integer getChildOrder() {
        return null;
    }

    public Set<ExecutionInterval> getChildIntervals() {
        return getExecutionPeriodsSet().stream().collect(Collectors.toSet());
    }

//    @Override
//    public ExecutionInterval getNext() {
//        return getNextExecutionYear();
//    }
//
//    @Override
//    public ExecutionInterval getPrevious() {
//        return getPreviousExecutionYear();
//    }

}
