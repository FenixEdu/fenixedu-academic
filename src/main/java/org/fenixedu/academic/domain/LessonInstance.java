/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.space.LessonInstanceSpaceOccupation;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
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

        Space room = lesson.getSala();

        HourMinuteSecond beginTime = lesson.getBeginHourMinuteSecond();
        HourMinuteSecond endTime = lesson.getEndHourMinuteSecond();
        DateTime beginDateTime =
                new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), beginTime.getHour(),
                        beginTime.getMinuteOfHour(), beginTime.getSecondOfMinute(), 0);
        DateTime endDateTime =
                new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), endTime.getHour(),
                        endTime.getMinuteOfHour(), endTime.getSecondOfMinute(), 0);

        setRootDomainObject(Bennu.getInstance());
        setBeginDateTime(beginDateTime);
        setEndDateTime(endDateTime);

        YearMonthDay nextPossibleDay = findNextPossibleDateAfter(day, lesson);

        setLesson(lesson);

//        summaryAndCourseLoadManagement(summary, lesson);
        setSummary(summary);
        lesson.refreshPeriodAndInstancesInSummaryCreation(nextPossibleDay);
        lessonInstanceSpaceOccupationManagement(room);
    }

    private YearMonthDay findNextPossibleDateAfter(YearMonthDay day, Lesson lesson) {
        for (YearMonthDay lessonDay : lesson.getAllLessonDatesWithoutInstanceDates()) {
            if (lessonDay.isAfter(day)) {
                return lessonDay;
            }
        }
        return lesson.isBiWeeklyOffset() ? day.plusDays(8) : day.plusDays(1);
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

        Space room = lesson.getSala();

        HourMinuteSecond beginTime = lesson.getBeginHourMinuteSecond();
        HourMinuteSecond endTime = lesson.getEndHourMinuteSecond();
        DateTime beginDateTime =
                new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), beginTime.getHour(),
                        beginTime.getMinuteOfHour(), beginTime.getSecondOfMinute(), 0);
        DateTime endDateTime =
                new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), endTime.getHour(),
                        endTime.getMinuteOfHour(), endTime.getSecondOfMinute(), 0);

        setRootDomainObject(Bennu.getInstance());
        setBeginDateTime(beginDateTime);
        setEndDateTime(endDateTime);
        setLesson(lesson);

        lessonInstanceSpaceOccupationManagement(room);
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        LessonInstanceSpaceOccupation occupation = getLessonInstanceSpaceOccupation();
        if (occupation != null) {
            occupation.removeLessonInstances(this);
            occupation.delete();
        }

//        super.setCourseLoad(null);
        super.setLesson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

//    public void summaryAndCourseLoadManagement(Summary summary, Lesson lesson) {
//        CourseLoad courseLoad = null;
//        if (lesson != null && summary != null) {
//            courseLoad = lesson.getExecutionCourse().getCourseLoadByShiftType(summary.getSummaryType());
//        }
//        setSummary(summary);
//        setCourseLoad(courseLoad);
//    }

    private int getUnitMinutes() {
        return Minutes.minutesBetween(getStartTime(), getEndTime()).getMinutes();
    }

    public BigDecimal getInstanceDurationInHours() {
        return BigDecimal.valueOf(getUnitMinutes()).divide(BigDecimal.valueOf(Lesson.NUMBER_OF_MINUTES_IN_HOUR), 2,
                RoundingMode.HALF_UP);
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (getSummary() != null) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.LessonInstance.cannot.be.deleted"));
        }
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateTimeInterval() {
        final DateTime start = getBeginDateTime();
        final DateTime end = getEndDateTime();
        return start != null && end != null && start.isBefore(end);
    }

    private void lessonInstanceSpaceOccupationManagement(Space space) {
        if (space != null) {
            final Lesson lesson = getLesson();
            LessonInstanceSpaceOccupation instanceSpaceOccupation =
                    (LessonInstanceSpaceOccupation) SpaceUtils.getFirstOccurrenceOfResourceAllocationByClass(space, lesson);

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

//    @Override
//    public void setCourseLoad(CourseLoad courseLoad) {
//        if (courseLoad == null) {
//            throw new DomainException("error.lessonInstance.empty.courseLoad");
//        }
//        super.setCourseLoad(courseLoad);
//    }

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
    
    public LocalDate getDate() {
        return getBeginDateTime().toLocalDate();
    }

    public HourMinuteSecond getStartTime() {
        return new HourMinuteSecond(getBeginDateTime().getHourOfDay(), getBeginDateTime().getMinuteOfHour(), getBeginDateTime()
                .getSecondOfMinute());
    }

    public HourMinuteSecond getEndTime() {
        return new HourMinuteSecond(getEndDateTime().getHourOfDay(), getEndDateTime().getMinuteOfHour(), getEndDateTime()
                .getSecondOfMinute());
    }

    public Space getRoom() {
        return getLessonInstanceSpaceOccupation() != null ? getLessonInstanceSpaceOccupation().getSpace() : null;
    }

    public DiaSemana getDayOfweek() {
        return new DiaSemana(DiaSemana.getDiaSemana(getDay()));
    }

    public String prettyPrint() {
        final StringBuilder result = new StringBuilder();
        result.append(getDayOfweek().getDiaSemanaString()).append(" (");
        result.append(getStartTime().toString("HH:mm")).append(" - ");
        result.append(getEndDateTime().toString("HH:mm")).append(") ");
        result.append(getRoom() != null ? getRoom().getName() : "");
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

    public Interval getInterval() {
        return new Interval(getBeginDateTime(), getEndDateTime());
    }

}
