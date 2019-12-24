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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota
 * @author jpvl
 * 
 */
public class ExecutionSemester extends ExecutionSemester_Base {

    public static final Comparator<ExecutionSemester> COMPARATOR_BY_SEMESTER_AND_YEAR = new Comparator<ExecutionSemester>() {

        @Override
        public int compare(final ExecutionSemester o1, final ExecutionSemester o2) {
            final AcademicInterval ai1 = o1.getAcademicInterval();
            final AcademicInterval ai2 = o2.getAcademicInterval();
            final int c = ai1.getStartDateTimeWithoutChronology().compareTo(ai2.getStartDateTimeWithoutChronology());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    private ExecutionSemester() {
        super();
        setRootDomainObjectForExecutionPeriod(Bennu.getInstance());
    }

    public ExecutionSemester(ExecutionYear executionYear, AcademicInterval academicInterval, String name) {
        this();
        setExecutionYear(executionYear);
        setAcademicInterval(academicInterval);
        setBeginDateYearMonthDay(academicInterval.getBeginYearMonthDayWithoutChronology());
        setEndDateYearMonthDay(academicInterval.getEndYearMonthDayWithoutChronology());
        setName(name);
    }

//    public void editPeriod(YearMonthDay begin, YearMonthDay end) throws DomainException {
//        if (begin == null || end == null || end.isBefore(begin)) {
//            throw new DomainException("error.ExecutionPeriod.invalid.dates");
//        }
//        checkDatesIntersection(begin.toLocalDate(), end.toLocalDate());
//        setBeginDateYearMonthDay(begin);
//        setEndDateYearMonthDay(end);
//    }
//
//    private void checkDatesIntersection(final LocalDate begin, final LocalDate end) {
//
//        final List<ExecutionSemester> executionIntervals = Bennu.getInstance().getExecutionPeriodsSet().stream()
//                .sorted(COMPARATOR_BY_SEMESTER_AND_YEAR).collect(Collectors.toList());
//        for (ExecutionSemester interval : executionIntervals) {
//            final LocalDate beginDate = interval.getBeginLocalDate();
//            final LocalDate endDate = interval.getEndLocalDate();
//            if (begin.isAfter(endDate) || end.isBefore(beginDate) || interval.equals(this)) {
//                continue;
//            } else {
//                throw new DomainException("error.ExecutionPeriod.intersection.dates");
//            }
//        }
//    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        if (executionYear == null) {
            throw new DomainException("error.ExecutionPeriod.empty.executionYear");
        }
        super.setExecutionYear(executionYear);
    }

    public Integer getSemester() {
        return super.getChildOrder();
//        return getAcademicInterval().getAcademicSemesterOfAcademicYear();
    }

    /**
     * @deprecated use {@link #getNext()}
     */
    @Deprecated
    public ExecutionSemester getNextExecutionPeriod() {
        return Optional.ofNullable(super.getNext()).map(ei -> ei.convert(ExecutionSemester.class)).orElse(null);
//        AcademicSemesterCE semester = getAcademicInterval().plusSemester(1);
//        return semester != null ? ExecutionSemester.getExecutionPeriod(semester) : null;
    }

    /**
     * @deprecated use {@link #getPrevious()}
     */
    @Deprecated
    public ExecutionSemester getPreviousExecutionPeriod() {
        return Optional.ofNullable(super.getPrevious()).map(ei -> ei.convert(ExecutionSemester.class)).orElse(null);
//        AcademicSemesterCE semester = getAcademicInterval().minusSemester(1);
//        return semester != null ? ExecutionSemester.getExecutionPeriod(semester) : null;
    }

//    @Override
//    @Deprecated
//    public int compareTo(ExecutionSemester object) {
//        return super.compareTo(object);
//    }

//    @Override
//    public String getQualifiedName() {
//        final String localizedName = getAcademicInterval().getAcademicCalendarEntry().getTitle().getContent();
//        final String semesterName = StringUtils.isNotBlank(localizedName) ? localizedName : getName();
//
//        return new StringBuilder().append(semesterName).append(" ").append(this.getExecutionYear().getYear()).toString();
//    }

//    public boolean containsDay(final YearMonthDay date) {
//        return !getBeginDateYearMonthDay().isAfter(date) && !getEndDateYearMonthDay().isBefore(date);
//    }

//    @Deprecated
//    public boolean isAfter(ExecutionSemester executionSemester) {
//        return super.compareTo(executionSemester) > 0;
//    }
//
//    @Deprecated
//    public boolean isAfterOrEquals(ExecutionSemester executionSemester) {
//        return super.compareTo(executionSemester) >= 0;
//    }
//
//    @Deprecated
//    public boolean isBefore(ExecutionSemester executionSemester) {
//        return super.compareTo(executionSemester) < 0;
//    }
//
//    @Deprecated
//    public boolean isBeforeOrEquals(ExecutionSemester executionSemester) {
//        return super.compareTo(executionSemester) <= 0;
//    }

//    @Override
//    public boolean isCurrent() {
//        return getState().equals(PeriodState.CURRENT);
//    }

//    public boolean isClosed() {
//        return getState().equals(PeriodState.CLOSED);
//    }

//    public boolean isNotOpen() {
//        return getState().equals(PeriodState.NOT_OPEN);
//    }

//    public boolean isFirstOfYear() {
//        return getExecutionYear().getFirstExecutionPeriod() == this;
//    }

//    public String getYear() {
//        return getExecutionYear().getYear();
//    }

    @Override
    public void delete() {
        super.setExecutionYear(null);
        setRootDomainObjectForExecutionPeriod(null);
        super.delete();
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

//    public static ExecutionSemester getExecutionPeriod(AcademicCalendarEntry entry) {
//        if (entry != null) {
//            for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
//                if (executionSemester.getAcademicInterval().getAcademicCalendarEntry().equals(entry)) {
//                    return executionSemester;
//                }
//            }
//        }
//        return null;
//    }

    private static transient ExecutionSemester currentExecutionPeriod = null;

    @Deprecated
    public static ExecutionSemester readActualExecutionSemester() {
        if (currentExecutionPeriod == null || currentExecutionPeriod.getRootDomainObject() != Bennu.getInstance()
                || !currentExecutionPeriod.isCurrent()) {
            for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
                if (executionSemester.isCurrent()) {
                    currentExecutionPeriod = executionSemester;
                    break;
                }
            }
        }
        return currentExecutionPeriod;
    }

    public static Collection<ExecutionSemester> findCurrents() {
        return Bennu.getInstance().getExecutionPeriodsSet().stream().filter(es -> es.isCurrent()).collect(Collectors.toSet());
    }

    /**
     * Returns current ExecutionSemester for provided calendar.
     * If provided calendar is null, use default academic calendar
     * 
     * @param calendar the calendar to search in
     * @return the current ExecutionSemester
     */
    public static ExecutionSemester findCurrent(final AcademicCalendarRootEntry calendar) {
        final AcademicCalendarRootEntry calendarToCheck =
                calendar != null ? calendar : Bennu.getInstance().getDefaultAcademicCalendar();
        return findCurrents().stream().filter(ey -> ey.getAcademicInterval().getAcademicCalendar() == calendarToCheck).findFirst()
                .orElse(null);
    }

    public static ExecutionSemester readFirstExecutionSemester() {
        final Set<ExecutionSemester> exeutionPeriods = Bennu.getInstance().getExecutionPeriodsSet();
        return exeutionPeriods.isEmpty() ? null : Collections.min(exeutionPeriods);
    }

    public static ExecutionSemester readLastExecutionSemester() {
        final Set<ExecutionSemester> exeutionPeriods = Bennu.getInstance().getExecutionPeriodsSet();
        final int size = exeutionPeriods.size();
        return size == 0 ? null : size == 1 ? exeutionPeriods.iterator().next() : Collections.max(exeutionPeriods);
    }

    public static List<ExecutionSemester> readNotClosedExecutionPeriods() {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (!executionSemester.getState().equals(PeriodState.CLOSED)) {
                result.add(executionSemester);
            }
        }
        return result;
    }

//    public static ExecutionSemester readByDateTime(final DateTime dateTime) {
//        final YearMonthDay yearMonthDay = dateTime.toYearMonthDay();
//        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
//            if (executionSemester.containsDay(yearMonthDay)) {
//                return executionSemester;
//            }
//        }
//        return null;
//    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends ExecutionInterval> E convert(final Class<E> input) {
        E result = null;

        if (ExecutionYear.class.equals(input)) {
            result = (E) getExecutionYear();
        } else if (ExecutionSemester.class.equals(input)) {
            result = (E) this;
        }

        return result;
    }

//    @Override
//    public Integer getChildOrder() {
//        return getSemester();
//    }
//
//    @Override
//    public ExecutionInterval getNext() {
//        return getNextExecutionPeriod();
//    }
//
//    @Override
//    public ExecutionInterval getPrevious() {
//        return getPreviousExecutionPeriod();
//    }

}
