package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.WeekDay;
import net.sourceforge.fenixedu.util.date.TimePeriod;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Fernanda Quit�rio 17/10/2003
 * @author jpvl
 * @author Ricardo Rodrigues
 */
public class SupportLesson extends SupportLesson_Base implements ICreditsEventOriginator {

    public static final Comparator SUPPORT_LESSON_COMPARATOR_BY_HOURS_AND_WEEK_DAY = new ComparatorChain();
    static {
        ((ComparatorChain) SUPPORT_LESSON_COMPARATOR_BY_HOURS_AND_WEEK_DAY).addComparator(new BeanComparator("weekDay.diaSemana"));
        ((ComparatorChain) SUPPORT_LESSON_COMPARATOR_BY_HOURS_AND_WEEK_DAY).addComparator(new BeanComparator("startTimeHourMinuteSecond"));       
    }    
        
    public SupportLesson(SupportLessonDTO supportLessonDTO, Professorship professorship,
            RoleType roleType) {
        super();       
        setRootDomainObject(RootDomainObject.getInstance());
        setProfessorship(professorship);
        update(supportLessonDTO, roleType);
    }

    public void delete(RoleType roleType) {
        getProfessorship().getExecutionCourse().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        removeProfessorship();
        removeRootDomainObject();
        deleteDomainObject();
    }

    public void update(SupportLessonDTO supportLessonDTO, RoleType roleType) {
        getProfessorship().getExecutionCourse().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        setEndTime(supportLessonDTO.getEndTime());
        setStartTime(supportLessonDTO.getStartTime());
        setPlace(supportLessonDTO.getPlace());
        setWeekDay(supportLessonDTO.getWeekDay());
        verifyOverlappings();
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

    @Override
    public void setPlace(String place) {
        final int maxPlaceChars = 50;
        if (place != null && place.length() > maxPlaceChars) {
            throw new DomainException("error.place.cannot.have.more.than.characters", Integer.toString(maxPlaceChars));
        }
        super.setPlace(place);
    }

    private SupportLesson() {
        super();       
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public static SupportLesson create(SupportLessonDTO supportLessonDTO, Professorship professorship, RoleType roleType) {
        final SupportLesson supportLesson = new SupportLesson();
        supportLesson.setProfessorship(professorship);
        supportLesson.getProfessorship().getExecutionCourse().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        supportLesson.setEndTime(supportLessonDTO.getEndTime());
        supportLesson.setStartTime(supportLessonDTO.getStartTime());
        supportLesson.setPlace(supportLessonDTO.getPlace());
        supportLesson.setWeekDay(supportLessonDTO.getWeekDay());
        return supportLesson;
    }

    public void delete() {
        removeProfessorship();
        removeRootDomainObject();
        deleteDomainObject();
    }

}
