package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.domain.Bennu;

public class TeacherPersonalExpectationPeriod extends TeacherPersonalExpectationPeriod_Base {

    public TeacherPersonalExpectationPeriod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void init(Department department, ExecutionYear executionYear, YearMonthDay startDate, YearMonthDay endDate) {
        if (department != null && executionYear != null
                && department.getTeacherPersonalExpectationPeriodForExecutionYear(executionYear, getClass()) != null) {
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

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDateYearMonthDay() {
        return getEndDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasDepartment() {
        return getDepartment() != null;
    }

    @Deprecated
    public boolean hasStartDateYearMonthDay() {
        return getStartDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
