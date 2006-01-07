package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.WeekDay;

public class DegreeTeachingService extends DegreeTeachingService_Base {

    public DegreeTeachingService(TeacherService teacherService, Professorship professorship,
            Shift shift, Double percentage) {
        super();
        if (teacherService == null || professorship == null || shift == null || percentage == null) {
            throw new DomainException("arguments can't be null");
            
        } 
        
        if (percentage == 0 || percentage > 100 || percentage < 0) {
            throw new DomainException("error.invalid.teachingDegree.percentage");
        }
        
        setTeacherService(teacherService);
        setProfessorship(professorship);
        setShift(shift);
        
        if (percentage == 100) {
            verifyAnyOverLapPeriod();
        }
        setPercentage(percentage);
    }

    public void delete(){
        setTeacherService(null);
        setShift(null);
        setProfessorship(null);
        deleteDomainObject();
    }
    
    public void updatePercentage(Double percentage) {
        if (percentage == null || percentage > 100 || percentage < 0) {
            throw new DomainException("message.invalid.professorship.percentage");
        }
        if (percentage == 0) {
            delete();
        } else {
            if (percentage == 100) {
                verifyAnyOverLapPeriod();
            }
            setPercentage(percentage);
        }
    }

    /**
     * 
     */
    private void verifyAnyOverLapPeriod() {
        verifyOverlapLessonPeriods();
        for (Lesson lesson : getShift().getAssociatedLessons()) {
            WeekDay lessonWeekDay = WeekDay.getWeekDay(lesson.getDiaSemana());
            Date lessonStart = lesson.getBegin();
            Date lessonEnd = lesson.getEnd();
            getTeacherService().verifyOverlappingWithInstitutionWorkingTime(lessonStart, lessonEnd,
                    lessonWeekDay);
            getTeacherService()
                    .verifyOverlappingWithSupportLesson(lessonStart, lessonEnd, lessonWeekDay);
        }
    }

    // TODO verify with other teachingServices

    /**
     * 
     */
    private void verifyOverlapLessonPeriods() {
        List<Lesson> lessons = getShift().getAssociatedLessons();
        for (Lesson lesson : lessons) {
            DiaSemana lessonWeekDay = lesson.getDiaSemana();
            Date lessonStart = lesson.getBegin();
            Date lessonEnd = lesson.getEnd();
            int fromIndex = lessons.indexOf(lesson) + 1;
            int toIndex = lessons.size();
            for (Lesson otherLesson : lessons.subList(fromIndex, toIndex)) {
                if (otherLesson.getDiaSemana().equals(lessonWeekDay)) {
                    Date otherStart = otherLesson.getBegin();
                    Date otherEnd = otherLesson.getEnd();
                    if (CalendarUtil.intersectTimes(lessonStart, lessonEnd, otherStart, otherEnd)) {
                        throw new DomainException("message.overlapping.lesson.period");
                    }
                }
            }
        }
    }
}
