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
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.SpacePredicates;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.google.common.collect.Lists;

public class LessonInstanceSpaceOccupation extends LessonInstanceSpaceOccupation_Base {

    public LessonInstanceSpaceOccupation(Space allocatableSpace) {
//        check(this, SpacePredicates.checkPermissionsToManageLessonInstanceSpaceOccupationsWithTeacherCheck);

        super();

        Occupation allocation =
                SpaceUtils.getFirstOccurrenceOfResourceAllocationByClass(allocatableSpace, LessonInstanceSpaceOccupation.class);
        if (allocation != null) {
            throw new DomainException("error.LessonInstanceSpaceOccupation.occupation.for.this.space.already.exists");
        }

        setResource(allocatableSpace);
    }

    public void edit(LessonInstance lessonInstance) {
        check(this, SpacePredicates.checkPermissionsToManageLessonInstanceSpaceOccupationsWithTeacherCheck);

        if (getLessonInstancesSet().contains(lessonInstance)) {
            removeLessonInstances(lessonInstance);
        }

        Space space = getSpace();
        //final ExecutionCourse executionCourse = lessonInstance.getLesson().getExecutionCourse();
        if (/*!space.isOccupiedByExecutionCourse(executionCourse, lessonInstance.getBeginDateTime(),
                lessonInstance.getEndDateTime())
                &&*//*!space.isFree(lessonInstance.getDay(), lessonInstance.getDay(), lessonInstance.getStartTime(),
                    lessonInstance.getEndTime(), lessonInstance.getDayOfweek(), null, null, null)*/
        !space.isFree(Lists.newArrayList(new Interval[] { new Interval(lessonInstance.getBeginDateTime(), lessonInstance
                .getEndDateTime()) }))) {

            throw new DomainException("error.LessonInstanceSpaceOccupation.room.is.not.free", space.getName(), lessonInstance
                    .getDay().toString("dd-MM-yy"));
        }

        addLessonInstances(lessonInstance);
    }

    @Override
    public void delete() {
        check(this, SpacePredicates.checkPermissionsToManageLessonInstanceSpaceOccupations);
        if (canBeDeleted()) {
            super.delete();
        }
    }

    private boolean canBeDeleted() {
        return !hasAnyLessonInstances();
    }

    @Override
    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
        return true;
    }

    @Override
    public List<Interval> getEventSpaceOccupationIntervals(YearMonthDay startDateToSearch, YearMonthDay endDateToSearch) {

        List<Interval> result = new ArrayList<Interval>();
        Collection<LessonInstance> lessonInstances = getLessonInstances();

        DateTime startDateTime = startDateToSearch != null ? startDateToSearch.toDateTimeAtMidnight() : null;
        DateTime endDateTime = endDateToSearch != null ? endDateToSearch.toDateTime(new TimeOfDay(23, 59, 59)) : null;

        for (LessonInstance lessonInstance : lessonInstances) {
            if (startDateTime == null
                    || (!lessonInstance.getEndDateTime().isBefore(startDateTime) && !lessonInstance.getBeginDateTime().isAfter(
                            endDateTime))) {

                result.add(new Interval(lessonInstance.getBeginDateTime(), lessonInstance.getEndDateTime()));
            }
        }
        return result;
    }

    @Override
    public YearMonthDay getBeginDate() {
        return null;
    }

    @Override
    public YearMonthDay getEndDate() {
        return null;
    }

    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
        return null;
    }

    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
        return null;
    }

    @Override
    public FrequencyType getFrequency() {
        return null;
    }

    @Override
    public DiaSemana getDayOfWeek() {
        return null;
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
    public Group getAccessGroup() {
        return null;
    }

    @Override
    public boolean isOccupiedByExecutionCourse(final ExecutionCourse executionCourse, final DateTime start, final DateTime end) {
        for (final LessonInstance lessonInstance : getLessonInstancesSet()) {
            final Lesson lesson = lessonInstance.getLesson();
            if (lesson.getExecutionCourse() == executionCourse && start.isBefore(lessonInstance.getEndDateTime())
                    && end.isAfter(lessonInstance.getBeginDateTime())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPresentationString() {
        if (hasAnyLessonInstances()) {
            return getLessonInstances().iterator().next().getLesson().getShift().getExecutionCourse().getSigla();
        }
        return getClass().getName();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LessonInstance> getLessonInstances() {
        return getLessonInstancesSet();
    }

    @Deprecated
    public boolean hasAnyLessonInstances() {
        return !getLessonInstancesSet().isEmpty();
    }

    @Override
    protected boolean overlaps(final Interval interval) {
        for (final LessonInstance instance : getLessonInstancesSet()) {
            final Interval lessonInterval = instance.getInterval();
            if (interval.overlaps(lessonInterval)) {
                return true;
            }
        }
        return false;
    }

}
