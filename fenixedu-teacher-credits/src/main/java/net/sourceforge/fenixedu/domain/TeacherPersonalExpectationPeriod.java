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
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public class TeacherPersonalExpectationPeriod extends TeacherPersonalExpectationPeriod_Base {

    public TeacherPersonalExpectationPeriod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void init(Department department, ExecutionYear executionYear, YearMonthDay startDate, YearMonthDay endDate) {
        if (department != null && executionYear != null
                && getTeacherPersonalExpectationPeriodForExecutionYear(department, executionYear, getClass()) != null) {
            throw new DomainException("error.TeacherPersonalExpectationPeriod.already.exists");
        }
        setDepartment(department);
        setExecutionYear(executionYear);
        setTimeInterval(startDate, endDate);
    }

    public void edit(YearMonthDay startDate, YearMonthDay endDate) {
        setTimeInterval(startDate, endDate);
    }

    public void setTimeInterval(YearMonthDay start, YearMonthDay end) {
        if (start == null) {
            throw new DomainException("error.TeacherPersonalExpectationPeriod.empty.startDateYearMonthDay");
        }
        if (end == null) {
            throw new DomainException("error.TeacherPersonalExpectationPeriod.empty.endDateYearMonthDay");
        }
        if (!end.isAfter(start)) {
            throw new DomainException("error.begin.after.end");
        }
        super.setStartDateYearMonthDay(start);
        super.setEndDateYearMonthDay(end);
    }

    public Boolean isPeriodOpen() {
        YearMonthDay now = new YearMonthDay();
        return !getStartDateYearMonthDay().isAfter(now) && !getEndDateYearMonthDay().isBefore(now) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public void setStartDateYearMonthDay(YearMonthDay startDateYearMonthDay) {
        if (startDateYearMonthDay == null) {
            throw new DomainException("error.TeacherPersonalExpectationPeriod.empty.startDateYearMonthDay");
        }
        setTimeInterval(startDateYearMonthDay, getEndDateYearMonthDay());
    }

    @Override
    public void setEndDateYearMonthDay(YearMonthDay endDateYearMonthDay) {
        if (endDateYearMonthDay == null) {
            throw new DomainException("error.TeacherPersonalExpectationPeriod.empty.endDate");
        }
        setTimeInterval(getStartDateYearMonthDay(), endDateYearMonthDay);
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        if (executionYear == null) {
            throw new DomainException("error.TeacherPersonalExpectationPeriod.empty.executionYear");
        }
        super.setExecutionYear(executionYear);
    }

    @Override
    public void setDepartment(Department department) {
        if (department == null) {
            throw new DomainException("error.TeacherPersonalExpectationPeriod.empty.department");
        }
        super.setDepartment(department);
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.YearMonthDay ymd = getEndDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateYearMonthDay(null);
        } else {
            setEndDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartDate() {
        org.joda.time.YearMonthDay ymd = getStartDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setStartDate(java.util.Date date) {
        if (date == null) {
            setStartDateYearMonthDay(null);
        } else {
            setStartDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    public static TeacherPersonalExpectationPeriod getTeacherPersonalExpectationPeriodForExecutionYear(Department department,
            ExecutionYear executionYear, Class<? extends TeacherPersonalExpectationPeriod> clazz) {

        if (executionYear != null) {
            for (TeacherPersonalExpectationPeriod period : department.getTeacherPersonalExpectationPeriodsSet()) {
                if (period.getExecutionYear().equals(executionYear) && period.getClass().equals(clazz)) {
                    return period;
                }
            }
        }
        return null;
    }

    public static TeacherAutoEvaluationDefinitionPeriod getTeacherAutoEvaluationDefinitionPeriodForExecutionYear(
            Department department, ExecutionYear executionYear) {
        TeacherPersonalExpectationPeriod period =
                getTeacherPersonalExpectationPeriodForExecutionYear(department, executionYear,
                        TeacherAutoEvaluationDefinitionPeriod.class);
        return period != null ? (TeacherAutoEvaluationDefinitionPeriod) period : null;
    }

    public static TeacherExpectationDefinitionPeriod getTeacherExpectationDefinitionPeriodForExecutionYear(Department department,
            ExecutionYear executionYear) {
        TeacherPersonalExpectationPeriod period =
                getTeacherPersonalExpectationPeriodForExecutionYear(department, executionYear,
                        TeacherExpectationDefinitionPeriod.class);
        return period != null ? (TeacherExpectationDefinitionPeriod) period : null;
    }

    public static TeacherPersonalExpectationsVisualizationPeriod getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(
            Department department, ExecutionYear executionYear) {
        TeacherPersonalExpectationPeriod period =
                getTeacherPersonalExpectationPeriodForExecutionYear(department, executionYear,
                        TeacherPersonalExpectationsVisualizationPeriod.class);
        return period != null ? (TeacherPersonalExpectationsVisualizationPeriod) period : null;
    }

    public static TeacherPersonalExpectationsEvaluationPeriod getTeacherPersonalExpectationsEvaluationPeriodByExecutionYear(
            Department department, ExecutionYear executionYear) {
        TeacherPersonalExpectationPeriod period =
                getTeacherPersonalExpectationPeriodForExecutionYear(department, executionYear,
                        TeacherPersonalExpectationsEvaluationPeriod.class);
        return period != null ? (TeacherPersonalExpectationsEvaluationPeriod) period : null;
    }

}
