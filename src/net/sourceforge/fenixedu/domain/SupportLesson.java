package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.WeekDay;
import net.sourceforge.fenixedu.util.date.TimePeriod;

/**
 * @author Fernanda Quit�rio 17/10/2003
 * @author jpvl
 * @author Ricardo Rodrigues
 */
public class SupportLesson extends SupportLesson_Base implements ICreditsEventOriginator {

    public SupportLesson() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete(){
        removeProfessorship();
        removeRootDomainObject();
        deleteDomainObject();
    }
    
    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    public boolean belongsToExecutionPeriod(ExecutionPeriod executionPeriod) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

    public void verifyOverlappings() {

        Teacher teacher = getProfessorship().getTeacher();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(getProfessorship()
                .getExecutionCourse().getExecutionPeriod());

        teacherService.verifyOverlappingWithInstitutionWorkingTime(getStartTime(), getEndTime(), WeekDay
                .getWeekDay(getWeekDay()));
        teacherService.verifyOverlappingWithTeachingService(getStartTime(), getEndTime(), WeekDay
                .getWeekDay(getWeekDay()));

        verifyOverlappingWithOtherSupportLessons(teacherService);
    }

    private void verifyOverlappingWithOtherSupportLessons(TeacherService teacherService) {
        for (SupportLesson supportLesson : teacherService.getSupportLessons()) {
            if (supportLesson != this) {
                if (supportLesson.getWeekDay().equals(getWeekDay())) {
                    Date supportLessonStart = supportLesson.getStartTime();
                    Date supportLessonEnd = supportLesson.getEndTime();
                    if (CalendarUtil.intersectTimes(getStartTime(), getEndTime(), supportLessonStart,
                            supportLessonEnd)) {
                        throw new DomainException("message.overlapping.support.lesson.period");
                    }
                }
            }
        }
    }    
}
