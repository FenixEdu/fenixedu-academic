package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.WeekDay;
import net.sourceforge.fenixedu.util.date.TimePeriod;

/**
 * @author Fernanda Quit�rio 17/10/2003
 * @author jpvl
 * @author Ricardo Rodrigues
 */
public class SupportLesson extends SupportLesson_Base implements ICreditsEventOriginator {

    public static final Comparator<SupportLesson> SUPPORT_LESSON_COMPARATOR_BY_HOURS_AND_WEEK_DAY =
            new Comparator<SupportLesson>() {

                @Override
                public int compare(SupportLesson o1, SupportLesson o2) {
                    final int c = o1.getWeekDay().getDiaSemana().compareTo(o2.getWeekDay().getDiaSemana());
                    return c == 0 ? o1.getStartTimeHourMinuteSecond().compareTo(o2.getStartTimeHourMinuteSecond()) : c;
                }

            };

    public SupportLesson(SupportLessonDTO supportLessonDTO, Professorship professorship, RoleType roleType) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setProfessorship(professorship);
        update(supportLessonDTO, roleType);
    }

    public void delete(RoleType roleType) {
        getProfessorship().getExecutionCourse().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        setProfessorship(null);
        setRootDomainObject(null);
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

    @Override
    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionSemester);
    }

    public void verifyOverlappings() {

        Teacher teacher = getProfessorship().getTeacher();
        TeacherService teacherService =
                teacher.getTeacherServiceByExecutionPeriod(getProfessorship().getExecutionCourse().getExecutionPeriod());

        teacherService
                .verifyOverlappingWithInstitutionWorkingTime(getStartTime(), getEndTime(), WeekDay.getWeekDay(getWeekDay()));
        teacherService.verifyOverlappingWithTeachingService(getStartTime(), getEndTime(), WeekDay.getWeekDay(getWeekDay()));

        verifyOverlappingWithOtherSupportLessons(teacherService);
    }

    private void verifyOverlappingWithOtherSupportLessons(TeacherService teacherService) {
        for (SupportLesson supportLesson : teacherService.getSupportLessons()) {
            if (supportLesson != this) {
                if (supportLesson.getWeekDay().equals(getWeekDay())) {
                    Date supportLessonStart = supportLesson.getStartTime();
                    Date supportLessonEnd = supportLesson.getEndTime();
                    if (CalendarUtil.intersectTimes(getStartTime(), getEndTime(), supportLessonStart, supportLessonEnd)) {
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
        setProfessorship(null);
        setRootDomainObject(null);
        deleteDomainObject();
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

    public WeekDay getWeekDayObject() {
        final DiaSemana diaSemana = getWeekDay();
        return diaSemana == null ? null : WeekDay.getWeekDay(diaSemana);
    }

    @Deprecated
    public boolean hasPlace() {
        return getPlace() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndTimeHourMinuteSecond() {
        return getEndTimeHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
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
