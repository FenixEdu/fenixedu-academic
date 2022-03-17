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
package org.fenixedu.academic.domain.space;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.List;
import java.util.SortedSet;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.FrequencyType;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.OccupationPeriod;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.SpacePredicates;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import com.google.common.collect.Lists;

public class LessonSpaceOccupation extends LessonSpaceOccupation_Base {

    public LessonSpaceOccupation(Space allocatableSpace, Lesson lesson) {
//        check(this, SpacePredicates.checkPermissionsToManageLessonSpaceOccupations);

        super();

        if (lesson != null && lesson.getLessonSpaceOccupation() != null) {
            throw new DomainException("error.lesson.already.has.lessonSpaceOccupation");
        }

        setLesson(lesson);

        if (getPeriod() != null) {
            if (allocatableSpace != null /* && !allocatableSpace.isFree(this) */
                    && !allocatableSpace.isFree(Lists.newArrayList(lesson.getAllLessonIntervalsWithoutInstanceDates()))) {
                throw new DomainException("error.LessonSpaceOccupation.room.is.not.free", allocatableSpace.getName(),
                        getPeriod().getStartYearMonthDay().toString("dd-MM-yyy"),
                        getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay().toString("dd-MM-yyy"));
            }
        }

        setResource(allocatableSpace);
    }

    public void edit(Space allocatableSpace) {
        check(this, SpacePredicates.checkPermissionsToManageLessonSpaceOccupations);

        if (getPeriod() != null) {
            final SortedSet<Interval> allLessonIntervalsWithoutInstanceDates =
                    getLesson().getAllLessonIntervalsWithoutInstanceDates();
            if (allocatableSpace != null /* && !allocatableSpace.isFree(this) */
                    && !allocatableSpace.isFree(Lists.newArrayList(allLessonIntervalsWithoutInstanceDates))) {
                throw new DomainException("error.LessonSpaceOccupation.room.is.not.free", allocatableSpace.getName(),
                        getPeriod().getStartYearMonthDay().toString("dd-MM-yyy"),
                        getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay().toString("dd-MM-yyy"));
            }
        }

        setResource(allocatableSpace);
    }

    @Override
    public void delete() {
        check(this, SpacePredicates.checkPermissionsToDeleteLessonSpaceOccupations);
        super.setLesson(null);
        super.delete();
    }

    public OccupationPeriod getPeriod() {
        return getLesson() == null ? null : getLesson().getPeriod();
    }

    @Override
    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
        return getPeriod() != null && getPeriod().nestedOccupationPeriodsIntersectDates(startDate, endDate);
    }

    @Override
    public List<Interval> getEventSpaceOccupationIntervals(YearMonthDay startDateToSearch, YearMonthDay endDateToSearch) {
        return Lists.newArrayList(getLesson().getAllLessonIntervalsWithoutInstanceDates());
    }

    @Override
    public void setLesson(Lesson lesson) {
        if (lesson == null) {
            throw new DomainException("error.LessonSpaceOccupation.empty.lesson");
        }
        super.setLesson(lesson);
    }

    @Override
    public FrequencyType getFrequency() {
        return getLesson().getFrequency();
    }

    @Override
    public Group getAccessGroup() {
        return getSpace().getOccupationsGroupWithChainOfResponsability();
    }

    @Override
    public YearMonthDay getBeginDate() {
        return getPeriod() != null ? getPeriod().getStartYearMonthDay() : null;
    }

    @Override
    public YearMonthDay getEndDate() {
        return getPeriod() != null ? getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay() : null;
    }

    @Override
    public DiaSemana getDayOfWeek() {
        return getLesson().getDiaSemana();
    }

    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
        return getLesson().getBeginHourMinuteSecond();
    }

    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
        return getLesson().getEndHourMinuteSecond();
    }

    @Override
    public Boolean getDailyFrequencyMarkSaturday() {
        return null;
    }

    @Override
    public Boolean getDailyFrequencyMarkSunday() {
        return null;
    }

    @Override
    public boolean isOccupiedByExecutionCourse(final ExecutionCourse executionCourse, final DateTime start, final DateTime end) {
        final Lesson lesson = getLesson();
        if (lesson.getExecutionCourse() == executionCourse) {
            final List<Interval> intervals =
                    getEventSpaceOccupationIntervals(start.toYearMonthDay(), end.toYearMonthDay().plusDays(1));
            for (final Interval interval : intervals) {
                if (start.isBefore(interval.getEnd()) && end.isAfter(interval.getStart())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getUrl() {
        Lesson li = getLesson();
        if (li == null) {
            return "";
        }

        return li.getShift().getExecutionCourse().getSiteUrl();
    }

    @Override
    public String getInfo() {
        String asd = getLesson().getShift().getCourseLoadsSet().iterator().next().getType().getFullNameTipoAula();
        for (YearMonthDay ymd : getLesson().getAllLessonDates()) {
            asd += "\n" + ymd.toString();
        }
        return asd;
    }

    @Override
    public String getPresentationString() {
        return getLesson().getShift().getExecutionCourse().getSigla();
    }

    @Override
    protected boolean overlaps(final Interval interval) {
        return getLesson().overlaps(interval);
    }

}
