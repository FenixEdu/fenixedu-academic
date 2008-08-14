package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.WeekDay;
import net.sourceforge.fenixedu.util.date.TimePeriod;

public class InstitutionWorkTime extends InstitutionWorkTime_Base {

    public InstitutionWorkTime(TeacherService teacherService, Date startTime, Date endTime, WeekDay weekDay, RoleType roleType) {

	super();

	if (teacherService == null || startTime == null || endTime == null || weekDay == null) {
	    throw new DomainException("arguments can't be null");
	}

	setTeacherService(teacherService);
	getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
	setStartTime(startTime);
	setEndTime(endTime);
	setWeekDay(weekDay);
	verifyOverlappings();
    }

    public void delete(RoleType roleType) {
	getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
	removeTeacherService();
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
}
