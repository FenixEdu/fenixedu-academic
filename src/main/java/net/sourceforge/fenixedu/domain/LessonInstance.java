package net.sourceforge.fenixedu.domain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation;
import net.sourceforge.fenixedu.predicates.ResourceAllocationRolePredicates;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.YearMonthDay;

public class LessonInstance extends LessonInstance_Base {

    public static final Comparator<LessonInstance> COMPARATOR_BY_BEGIN_DATE_TIME = new Comparator<LessonInstance>() {

        @Override
        public int compare(LessonInstance o1, LessonInstance o2) {
            final int c = o1.getBeginDateTime().compareTo(o2.getBeginDateTime());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    public LessonInstance(Summary summary, Lesson lesson) {
//        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageLessonInstancesWithTeacherCheck);

        super();

        if (summary == null) {
            throw new DomainException("error.LessonInstance.empty.summary");
        }

        if (lesson == null) {
            throw new DomainException("error.LessonInstance.empty.lesson");
        }

        YearMonthDay day = summary.getSummaryDateYearMonthDay();

        LessonInstance lessonInstance = lesson.getLessonInstanceFor(day);
        if (lessonInstance != null) {
            throw new DomainException("error.lessonInstance.already.exist");
        }

        AllocatableSpace room = lesson.getSala();

        HourMinuteSecond beginTime = lesson.getBeginHourMinuteSecond();
        HourMinuteSecond endTime = lesson.getEndHourMinuteSecond();
        DateTime beginDateTime =
                new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), beginTime.getHour(),
                        beginTime.getMinuteOfHour(), beginTime.getSecondOfMinute(), 0);
        DateTime endDateTime =
                new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), endTime.getHour(),
                        endTime.getMinuteOfHour(), endTime.getSecondOfMinute(), 0);

        setRootDomainObject(RootDomainObject.getInstance());
        setBeginDateTime(beginDateTime);
        setEndDateTime(endDateTime);
        setLesson(lesson);

        summaryAndCourseLoadManagement(summary, lesson);
        lesson.refreshPeriodAndInstancesInSummaryCreation(day.plusDays(1));
        lessonInstanceSpaceOccupationManagement(room);
    }

    public LessonInstance(Lesson lesson, YearMonthDay day) {
//        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageLessonInstancesWithTeacherCheck);

        super();

        if (day == null) {
            throw new DomainException("error.LessonInstance.empty.day");
        }

        if (lesson == null) {
            throw new DomainException("error.LessonInstance.empty.Lesson");
        }

        LessonInstance lessonInstance = lesson.getLessonInstanceFor(day);
        if (lessonInstance != null) {
            throw new DomainException("error.lessonInstance.already.exist");
        }

        AllocatableSpace room = lesson.getSala();

        HourMinuteSecond beginTime = lesson.getBeginHourMinuteSecond();
        HourMinuteSecond endTime = lesson.getEndHourMinuteSecond();
        DateTime beginDateTime =
                new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), beginTime.getHour(),
                        beginTime.getMinuteOfHour(), beginTime.getSecondOfMinute(), 0);
        DateTime endDateTime =
                new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), endTime.getHour(),
                        endTime.getMinuteOfHour(), endTime.getSecondOfMinute(), 0);

        setRootDomainObject(RootDomainObject.getInstance());
        setBeginDateTime(beginDateTime);
        setEndDateTime(endDateTime);
        setLesson(lesson);

        lessonInstanceSpaceOccupationManagement(room);
    }

    public void delete() {
        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageLessonInstances);

        if (!canBeDeleted()) {
            throw new DomainException("error.LessonInstance.cannot.be.deleted");
        }

        LessonInstanceSpaceOccupation occupation = getLessonInstanceSpaceOccupation();
        if (occupation != null) {
            occupation.removeLessonInstances(this);
            occupation.delete();
        }

        super.setCourseLoad(null);
        super.setLesson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void summaryAndCourseLoadManagement(Summary summary, Lesson lesson) {
        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageLessonInstancesWithTeacherCheck);
        CourseLoad courseLoad = null;
        if (lesson != null && summary != null) {
            courseLoad = lesson.getExecutionCourse().getCourseLoadByShiftType(summary.getSummaryType());
        }
        setSummary(summary);
        setCourseLoad(courseLoad);
    }

    private int getUnitMinutes() {
        return Minutes.minutesBetween(getStartTime(), getEndTime()).getMinutes();
    }

    public BigDecimal getInstanceDurationInHours() {
        return BigDecimal.valueOf(getUnitMinutes()).divide(BigDecimal.valueOf(Lesson.NUMBER_OF_MINUTES_IN_HOUR), 2,
                RoundingMode.HALF_UP);
    }

    private boolean canBeDeleted() {
        return !hasSummary();
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateTimeInterval() {
        final DateTime start = getBeginDateTime();
        final DateTime end = getEndDateTime();
        return start != null && end != null && start.isBefore(end);
    }

    private void lessonInstanceSpaceOccupationManagement(AllocatableSpace space) {
        if (space != null) {
            LessonInstanceSpaceOccupation instanceSpaceOccupation =
                    (LessonInstanceSpaceOccupation) space
                            .getFirstOccurrenceOfResourceAllocationByClass(LessonInstanceSpaceOccupation.class);

            instanceSpaceOccupation =
                    instanceSpaceOccupation == null ? new LessonInstanceSpaceOccupation(space) : instanceSpaceOccupation;
            instanceSpaceOccupation.edit(this);
        }
    }

    @Override
    public void setSummary(Summary summary) {
        if (summary == null) {
            throw new DomainException("error.LessonInstance.empty.summary");
        }
        super.setSummary(summary);
    }

    @Override
    public void setCourseLoad(CourseLoad courseLoad) {
        if (courseLoad == null) {
            throw new DomainException("error.lessonInstance.empty.courseLoad");
        }
        super.setCourseLoad(courseLoad);
    }

    @Override
    public void setLesson(Lesson lesson) {
        if (lesson == null) {
            throw new DomainException("error.lessonInstance.empty.lesson");
        }
        super.setLesson(lesson);
    }

    public YearMonthDay getDay() {
        return getBeginDateTime().toYearMonthDay();
    }

    public HourMinuteSecond getStartTime() {
        return new HourMinuteSecond(getBeginDateTime().getHourOfDay(), getBeginDateTime().getMinuteOfHour(), getBeginDateTime()
                .getSecondOfMinute());
    }

    public HourMinuteSecond getEndTime() {
        return new HourMinuteSecond(getEndDateTime().getHourOfDay(), getEndDateTime().getMinuteOfHour(), getEndDateTime()
                .getSecondOfMinute());
    }

    public AllocatableSpace getRoom() {
        return hasLessonInstanceSpaceOccupation() ? getLessonInstanceSpaceOccupation().getRoom() : null;
    }

    public DiaSemana getDayOfweek() {
        return new DiaSemana(DiaSemana.getDiaSemana(getDay()));
    }

    public String prettyPrint() {
        final StringBuilder result = new StringBuilder();
        result.append(getDayOfweek().getDiaSemanaString()).append(" (");
        result.append(getStartTime().toString("HH:mm")).append(" - ");
        result.append(getEndDateTime().toString("HH:mm")).append(") ");
        result.append(getRoom() != null ? getRoom().getIdentification() : "");
        return result.toString();
    }

    @Deprecated
    public java.util.Date getBegin() {
        org.joda.time.DateTime dt = getBeginDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setBegin(java.util.Date date) {
        if (date == null) {
            setBeginDateTime(null);
        } else {
            setBeginDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getEnd() {
        org.joda.time.DateTime dt = getEndDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setEnd(java.util.Date date) {
        if (date == null) {
            setEndDateTime(null);
        } else {
            setEndDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasLessonInstanceSpaceOccupation() {
        return getLessonInstanceSpaceOccupation() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCourseLoad() {
        return getCourseLoad() != null;
    }

    @Deprecated
    public boolean hasEndDateTime() {
        return getEndDateTime() != null;
    }

    @Deprecated
    public boolean hasLesson() {
        return getLesson() != null;
    }

    @Deprecated
    public boolean hasSummary() {
        return getSummary() != null;
    }

    @Deprecated
    public boolean hasBeginDateTime() {
        return getBeginDateTime() != null;
    }

}
