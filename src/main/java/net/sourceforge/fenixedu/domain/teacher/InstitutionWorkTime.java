package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.WeekDay;
import net.sourceforge.fenixedu.util.date.TimePeriod;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.Atomic;

public class InstitutionWorkTime extends InstitutionWorkTime_Base {

    public InstitutionWorkTime(TeacherService teacherService, Date startTime, Date endTime, WeekDay weekDay) {
        super();
        if (teacherService == null || startTime == null || endTime == null || weekDay == null) {
            throw new DomainException("arguments can't be null");
        }
        setTeacherService(teacherService);
        getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(getUserRoleType());
        setStartTime(startTime);
        setEndTime(endTime);
        setWeekDay(weekDay);
        verifyOverlappings();
        log("label.teacher.schedule.institutionWorkTime.create");
    }

    private void log(final String key) {
        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources", key));
        log.append(getWeekDay().getLabel());
        log.append(" ");
        log.append(getStartTime().getHours());
        log.append(":");
        log.append(getStartTime().getMinutes());
        log.append(" - ");
        log.append(getEndTime().getHours());
        log.append(":");
        log.append(getEndTime().getMinutes());
        new TeacherServiceLog(getTeacherService(), log.toString());
    }

    private RoleType getUserRoleType() {
        Person person = ((IUserView) UserView.getUser()).getPerson();
        if (person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
            return RoleType.SCIENTIFIC_COUNCIL;
        } else if (person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
            return RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE;
        } else if (person.hasRole(RoleType.DEPARTMENT_MEMBER)) {
            return RoleType.DEPARTMENT_MEMBER;
        }
        return null;
    }

    @Atomic
    public void delete(RoleType roleType) {
        getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        log("label.teacher.schedule.institutionWorkTime.delete");
        setTeacherService(null);
        super.delete();
    }

    public double getHours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    public void update(InstitutionWorkTimeDTO institutionWorkTimeDTO, RoleType roleType) {
        getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        setWeekDay(institutionWorkTimeDTO.getWeekDay());
        setStartTime(institutionWorkTimeDTO.getStartTime());
        setEndTime(institutionWorkTimeDTO.getEndTime());
        verifyOverlappings();
        log("label.teacher.schedule.institutionWorkTime.edit");
    }

    public void update(WeekDay weekDay, Date startTime, Date endTime) {
        setWeekDay(weekDay);
        setStartTime(startTime);
        setEndTime(endTime);
        verifyOverlappings();
        log("label.teacher.schedule.institutionWorkTime.edit");
    }

    private void verifyOverlappings() {
        getTeacherService().verifyOverlappingWithSupportLesson(getStartTime(), getEndTime(), getWeekDay());
        getTeacherService().verifyOverlappingWithTeachingService(getStartTime(), getEndTime(), getWeekDay());
        verifyOverlappingWithOtherInstitutionWorkingTimes();
    }

    private void verifyOverlappingWithOtherInstitutionWorkingTimes() {
        for (InstitutionWorkTime teacherInstitutionWorkTime : getTeacherService().getInstitutionWorkTimes()) {
            if (this != teacherInstitutionWorkTime) {
                if (teacherInstitutionWorkTime.getWeekDay().equals(getWeekDay())) {
                    Date startWorkTime = teacherInstitutionWorkTime.getStartTime();
                    Date endWorkTime = teacherInstitutionWorkTime.getEndTime();
                    if (CalendarUtil.intersectTimes(getStartTime(), getEndTime(), startWorkTime, endWorkTime)) {
                        throw new DomainException("message.overlapping.institution.working.period");
                    }
                }
            }
        }
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
    public boolean hasEndTimeHourMinuteSecond() {
        return getEndTimeHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasStartTimeHourMinuteSecond() {
        return getStartTimeHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasWeekDay() {
        return getWeekDay() != null;
    }

}
