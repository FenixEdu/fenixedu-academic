/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.SpacePredicates;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

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

        if (getPeriod() == null) {
            throw new DomainException("error.LessonSpaceOccupation.empty.period");
        }

        if (allocatableSpace != null /* && !allocatableSpace.isFree(this) */
                && !allocatableSpace.isFree(Lists.newArrayList(lesson.getAllLessonIntervalsWithoutInstanceDates()))) {
            throw new DomainException("error.LessonSpaceOccupation.room.is.not.free", allocatableSpace.getName(), getPeriod()
                    .getStartYearMonthDay().toString("dd-MM-yyy"), getPeriod().getLastOccupationPeriodOfNestedPeriods()
                    .getEndYearMonthDay().toString("dd-MM-yyy"));
        }

        setResource(allocatableSpace);
    }

    public void edit(Space allocatableSpace) {
        check(this, SpacePredicates.checkPermissionsToManageLessonSpaceOccupations);

        if (getPeriod() == null) {
            throw new DomainException("error.LessonSpaceOccupation.empty.period");
        }

        final SortedSet<Interval> allLessonIntervalsWithoutInstanceDates =
                getLesson().getAllLessonIntervalsWithoutInstanceDates();
        if (allocatableSpace != null /* && !allocatableSpace.isFree(this) */
                && !allocatableSpace.isFree(Lists.newArrayList(allLessonIntervalsWithoutInstanceDates))) {
            throw new DomainException("error.LessonSpaceOccupation.room.is.not.free", allocatableSpace.getName(), getPeriod()
                    .getStartYearMonthDay().toString("dd-MM-yyy"), getPeriod().getLastOccupationPeriodOfNestedPeriods()
                    .getEndYearMonthDay().toString("dd-MM-yyy"));
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

        List<Interval> result = new ArrayList<Interval>();
        OccupationPeriod occupationPeriod = getPeriod();

        if (getPeriod() != null) {

            result.addAll(generateEventSpaceOccupationIntervals(occupationPeriod.getStartYearMonthDay(),
                    occupationPeriod.getEndYearMonthDay(), getStartTimeDateHourMinuteSecond(), getEndTimeDateHourMinuteSecond(),
                    getFrequency(), getDayOfWeek(), getDailyFrequencyMarkSaturday(), getDailyFrequencyMarkSunday(),
                    startDateToSearch, endDateToSearch));

            while (occupationPeriod.getNextPeriod() != null) {
                result.addAll(generateEventSpaceOccupationIntervals(occupationPeriod.getNextPeriod().getStartYearMonthDay(),
                        occupationPeriod.getNextPeriod().getEndYearMonthDay(), getStartTimeDateHourMinuteSecond(),
                        getEndTimeDateHourMinuteSecond(), getFrequency(), getDayOfWeek(), getDailyFrequencyMarkSaturday(),
                        getDailyFrequencyMarkSunday(), startDateToSearch, endDateToSearch));

                occupationPeriod = occupationPeriod.getNextPeriod();
            }
        }

        return result;
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
    public String getPresentationString() {
        return getLesson().getShift().getExecutionCourse().getSigla();
    }

    @Deprecated
    public boolean hasLesson() {
        return getLesson() != null;
    }

    @Override
    protected boolean overlaps(final Interval interval) {
        for (final Interval lessonInterval : getLesson().getAllLessonIntervalsWithoutInstanceDates()) {
            if (interval.overlaps(lessonInterval)) {
                return true;
            }
        }
        return false;
    }

}
