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

import java.util.Comparator;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

abstract public class ExecutionInterval extends ExecutionInterval_Base {

    public static final Comparator<ExecutionInterval> COMPARATOR_BY_BEGIN_DATE = new Comparator<ExecutionInterval>() {

        @Override
        public int compare(final ExecutionInterval o1, final ExecutionInterval o2) {
            return AcademicInterval.COMPARATOR_BY_BEGIN_DATE.compare(o1.getAcademicInterval(), o2.getAcademicInterval());
        }
    };

    protected ExecutionInterval() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setState(PeriodState.NOT_OPEN);
    }

    @Override
    public void setAcademicInterval(AcademicInterval academicInterval) {
        if (academicInterval == null) {
            throw new DomainException("error.executionInterval.empty.executionInterval");
        }
        super.setAcademicInterval(academicInterval);
    }

    @Override
    public void setState(PeriodState state) {
        if (state == null) {
            throw new DomainException("error.executionInterval.empty.state");
        }
        super.setState(state);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
        return getBeginDateYearMonthDay() != null && getEndDateYearMonthDay() != null
                && getBeginDateYearMonthDay().isBefore(getEndDateYearMonthDay());
    }

    abstract public String getQualifiedName();

    abstract public boolean isCurrent();

    public static ExecutionInterval getExecutionInterval(String qualifiedName) {
        for (ExecutionInterval interval : Bennu.getInstance().getExecutionIntervalsSet()) {
            if (interval.getQualifiedName().equals(qualifiedName)) {
                return interval;
            }
        }
        return null;
    }

    public static ExecutionInterval getExecutionInterval(AcademicInterval academicInterval) {
        for (ExecutionInterval interval : Bennu.getInstance().getExecutionIntervalsSet()) {
            if (interval.getAcademicInterval().equals(academicInterval)) {
                return interval;
            }
        }
        return null;
    }

    public boolean isAfter(final ExecutionInterval input) {
        return this.compareExecutionInterval(input) > 0;
    }

    public boolean isAfterOrEquals(final ExecutionInterval input) {
        return this.compareExecutionInterval(input) >= 0;
    }

    public int compareExecutionInterval(final ExecutionInterval input) {
        int result = 0;
        if (input == null) {
            result = -1;
        }
        if (result == 0) {
            assertExecutionIntervalType(this.getClass(), input);
            result = COMPARATOR_BY_BEGIN_DATE.compare(this, input);
        }
        return result;
    }

    /**
     * Asserts that the objects being manipulated belong to the same type.
     * E.g: Avoids comparison of ExecutionYears with ExecutionSemesters
     */
    public static <T extends ExecutionInterval> T assertExecutionIntervalType(final Class<T> clazz,
            final ExecutionInterval input) {
        T result = null;
        if (input != null) {
            if (!clazz.isAssignableFrom(input.getClass())) {
                throw new DomainException("error.ExecutionInterval.unexpected", clazz.getSimpleName(),
                        input.getClass().getSimpleName());
            }
            result = (T) input;
        }
        return result;
    }

    @Deprecated
    public java.util.Date getBeginDate() {
        org.joda.time.YearMonthDay ymd = getBeginDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    public LocalDate getBeginLocalDate() {
        final YearMonthDay result = getBeginDateYearMonthDay();
        return result == null ? null : result.toLocalDate();
    }

    @Deprecated
    public void setBeginDate(java.util.Date date) {
        if (date == null) {
            setBeginDateYearMonthDay(null);
        } else {
            setBeginDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.YearMonthDay ymd = getEndDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    public LocalDate getEndLocalDate() {
        final YearMonthDay result = getEndDateYearMonthDay();
        return result == null ? null : result.toLocalDate();
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateYearMonthDay(null);
        } else {
            setEndDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    public abstract <E extends ExecutionInterval> E convert(final Class<E> concreteExecution);
}
