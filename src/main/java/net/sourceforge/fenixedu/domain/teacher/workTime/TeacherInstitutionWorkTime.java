package net.sourceforge.fenixedu.domain.teacher.workTime;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.util.date.TimePeriod;

public class TeacherInstitutionWorkTime extends TeacherInstitutionWorkTime_Base implements ICreditsEventOriginator {

    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    @Override
    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getExecutionPeriod().equals(executionSemester);
    }

    public void delete() {
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getEndTime() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEndTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEndTime(java.util.Date date) {
        if (date == null) {
            setEndTimeHourMinuteSecond(null);
        } else {
            setEndTimeHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartTime() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getStartTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setStartTime(java.util.Date date) {
        if (date == null) {
            setStartTimeHourMinuteSecond(null);
        } else {
            setStartTimeHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndTimeHourMinuteSecond() {
        return getEndTimeHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasStartTimeHourMinuteSecond() {
        return getStartTimeHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasWeekDay() {
        return getWeekDay() != null;
    }

}
