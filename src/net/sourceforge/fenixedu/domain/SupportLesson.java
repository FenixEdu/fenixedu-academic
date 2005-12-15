package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.WeekDay;
import net.sourceforge.fenixedu.util.date.TimePeriod;

/**
 * @author Fernanda Quitério 17/10/2003
 * @author jpvl
 * @author Ricardo Rodrigues
 */
public class SupportLesson extends SupportLesson_Base implements ICreditsEventOriginator {

    public void delete(){
        setProfessorship(null);
        deleteDomainObject();
    }
    
    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

    public void verifyOverlappings() {

        ITeacher teacher = getProfessorship().getTeacher();
        ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(getProfessorship()
                .getExecutionCourse().getExecutionPeriod());

        teacherService.verifyOverlappingWithInstitutionWorkingTime(getStartTime(), getEndTime(), WeekDay
                .getWeekDay(getWeekDay()));
        teacherService.verifyOverlappingWithTeachingService(getStartTime(), getEndTime(), WeekDay
                .getWeekDay(getWeekDay()));

        verifyOverlappingWithOtherSupportLessons(teacherService);
    }

    private void verifyOverlappingWithOtherSupportLessons(ITeacherService teacherService) {
        for (ISupportLesson supportLesson : teacherService.getSupportLessons()) {
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
