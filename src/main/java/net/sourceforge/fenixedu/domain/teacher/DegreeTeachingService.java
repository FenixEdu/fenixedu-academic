package net.sourceforge.fenixedu.domain.teacher;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

public class DegreeTeachingService extends DegreeTeachingService_Base {

    public static final Comparator<DegreeTeachingService> DEGREE_TEACHING_SERVICE_COMPARATOR_BY_SHIFT =
            new Comparator<DegreeTeachingService>() {
                @Override
                public int compare(DegreeTeachingService degreeTeachingService1, DegreeTeachingService degreeTeachingService2) {
                    return Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS.compare(degreeTeachingService1.getShift(),
                            degreeTeachingService2.getShift());
                }
            };

    public DegreeTeachingService(TeacherService teacherService, Professorship professorship, Shift shift, Double percentage,
            RoleType roleType) {
        super();
        if (teacherService == null || professorship == null || shift == null || percentage == null) {
            throw new DomainException("arguments can't be null");
        }
        if (percentage > 100 || percentage < 0) {
            throw new DomainException("message.invalid.professorship.percentage");
        }
        setTeacherService(teacherService);
        getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        setProfessorship(professorship);
        setShift(shift);

        Double availablePercentage = getShift().getAvailableShiftPercentage(getProfessorship());

        if (percentage > availablePercentage) {
            throw new DomainException("message.exceeded.professorship.percentage");
        }

        if (percentage == 100) {
            verifyAnyOverLapPeriod();
        }

        setPercentage(percentage);
    }

    @Override
    public void delete() {
        new TeacherServiceLog(getTeacherService(), BundleUtil.getStringFromResourceBundle(
                "resources.TeacherCreditsSheetResources", "label.teacher.schedule.delete", getTeacherService().getTeacher()
                        .getPerson().getNickname(), getShift().getPresentationName(), getPercentage().toString()));
        setTeacherService(null);
        setShift(null);
        setProfessorship(null);
        super.delete();
    }

    public void delete(RoleType roleType) {
        getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        delete();
    }

    public void updatePercentage(Double percentage, RoleType roleType) {
        getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        if (percentage == null || percentage > 100 || percentage < 0) {
            throw new DomainException("message.invalid.professorship.percentage");
        }
        if (percentage == 0) {
            delete(roleType);
        } else {
            Double availablePercentage = getShift().getAvailableShiftPercentage(getProfessorship());
            if (percentage > availablePercentage) {
                throw new DomainException("message.exceeded.professorship.percentage");
            }
            if (percentage == 100) {
                verifyAnyOverLapPeriod();
            }
            setPercentage(percentage);
        }
    }

    private void verifyAnyOverLapPeriod() {
        verifyOverlapLessonPeriods();
        for (Lesson lesson : getShift().getAssociatedLessons()) {
            WeekDay lessonWeekDay = WeekDay.getWeekDay(lesson.getDiaSemana());
            Date lessonStart = lesson.getBegin();
            Date lessonEnd = lesson.getEnd();
            getTeacherService().verifyOverlappingWithInstitutionWorkingTime(lessonStart, lessonEnd, lessonWeekDay);
            getTeacherService().verifyOverlappingWithSupportLesson(lessonStart, lessonEnd, lessonWeekDay);
        }
    }

    // TODO verify with other teachingServices
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

    public double getHours() {
        double totalHours = 0;
        final ExecutionCourse executionCourse = getProfessorship().getExecutionCourse();
        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
        if (getProfessorship().getTeacher().isTeacherProfessorCategory(executionSemester)) {
            double hoursAfter20PM = getShift().getHoursOnSaturdaysOrNightHours(20);
            double hoursBefore20PM = getShift().getUnitHours().doubleValue() - hoursAfter20PM;
            totalHours += hoursBefore20PM * (getPercentage().doubleValue() / 100);
            totalHours += (hoursAfter20PM * (getPercentage().doubleValue() / 100)) * 1.5;
        } else {
            double hours = getShift().getUnitHours().doubleValue();
            totalHours += (hours * (getPercentage().doubleValue() / 100));
        }
        return totalHours;
    }

    public double getEfectiveLoad() {
        double afterHeightFactor =
                getProfessorship().getTeacher().isTeacherProfessorCategory(
                        getProfessorship().getExecutionCourse().getExecutionPeriod()) ? 1.5 : 1;

        double weeklyHoursAfter20 = getTotalHoursAfter20AndSaturdays() / 14;
        double weeklyHoursBefore20 = (getShift().getCourseLoadWeeklyAverage().doubleValue() - weeklyHoursAfter20);

        BigDecimal percentage = new BigDecimal(getPercentage() / 100).setScale(4, BigDecimal.ROUND_HALF_UP);
        return (new BigDecimal((weeklyHoursBefore20 + (weeklyHoursAfter20 * afterHeightFactor))).multiply(percentage)).setScale(
                2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getTotalHoursAfter20AndSaturdays() {
        int minutesAfter20AndSaturday = 0;
        for (Lesson lesson : getShift().getAssociatedLessons()) {
            for (Interval lessonInterval : lesson.getAllLessonIntervals()) {
                if (lessonInterval.getStart().getDayOfWeek() == DateTimeConstants.SATURDAY) {
                    minutesAfter20AndSaturday +=
                            Minutes.minutesBetween(lessonInterval.getStart(), lessonInterval.getEnd()).getMinutes();
                } else {
                    DateTime dateTimeAfter20 = lessonInterval.getStart().toLocalDate().toDateTime(new LocalTime(20, 0, 0));
                    if (dateTimeAfter20.isBefore(lessonInterval.getEnd())) {
                        if (!dateTimeAfter20.isAfter(lessonInterval.getStart())) {
                            minutesAfter20AndSaturday +=
                                    Minutes.minutesBetween(lessonInterval.getStart(), lessonInterval.getEnd()).getMinutes();
                        } else {
                            minutesAfter20AndSaturday +=
                                    Minutes.minutesBetween(dateTimeAfter20, lessonInterval.getEnd()).getMinutes();
                        }
                    }

                }
            }
        }
        return minutesAfter20AndSaturday / DateTimeConstants.MINUTES_PER_HOUR;
    }

    public double calculateCredits() {
        return getEfectiveLoad() * getProfessorship().getExecutionCourse().getUnitCreditValue().doubleValue();
    }

    @Deprecated
    public boolean hasPercentage() {
        return getPercentage() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

}
