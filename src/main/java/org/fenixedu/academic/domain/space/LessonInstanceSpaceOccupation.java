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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.FrequencyType;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.google.common.collect.Lists;

public class LessonInstanceSpaceOccupation extends LessonInstanceSpaceOccupation_Base {

    public LessonInstanceSpaceOccupation(Space allocatableSpace) {
//        check(this, SpacePredicates.checkPermissionsToManageLessonInstanceSpaceOccupationsWithTeacherCheck);

        super();

//        Occupation allocation =
//                SpaceUtils.getFirstOccurrenceOfResourceAllocationByClass(allocatableSpace, lesson);
//        if (allocation != null) {
//            throw new DomainException("error.LessonInstanceSpaceOccupation.occupation.for.this.space.already.exists");
//        }

        setResource(allocatableSpace);
    }

    public void edit(LessonInstance lessonInstance) {
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
        if (getDeletionBlockers().isEmpty()) {
            super.delete();
        }
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getLessonInstancesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.cannotDeleteLessonInstanceSpaceOccupation"));
        }
    }

    @Override
    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
        return true;
    }

    @Override
    public List<Interval> getEventSpaceOccupationIntervals(YearMonthDay startDateToSearch, YearMonthDay endDateToSearch) {

        List<Interval> result = new ArrayList<Interval>();
        Collection<LessonInstance> lessonInstances = getLessonInstancesSet();

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
        return getSpace().getOccupationsGroupWithChainOfResponsability();
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
        if (!getLessonInstancesSet().isEmpty()) {
            return getLessonInstancesSet().iterator().next().getLesson().getShift().getExecutionCourse().getSigla();
        }
        return getClass().getName();
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

    @Override
    public String getUrl() {
        LessonInstance lis = getLessonInstancesSet().iterator().next();
        if (lis == null) {
            return "";
        }
        Lesson li = lis.getLesson();

        return li.getShift().getExecutionCourse().getSiteUrl();
    }

    @Override
    public String getInfo() {
        Lesson theLesson = getLessonInstancesSet().iterator().next().getLesson();
        String asd = theLesson.getShift().getCourseLoadsSet().iterator().next().getType().getFullNameTipoAula();
        for (YearMonthDay ymd : theLesson.getAllLessonDates()) {
            asd += "\n" + ymd.toString();
        }
        return asd;
    }
    
    @Override
    public String getType() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.lesson");
    }
}
