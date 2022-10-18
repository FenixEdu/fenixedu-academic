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

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

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
        if (!space.isFree(lessonInstance.getInterval())) {
            throw new DomainException("error.LessonInstanceSpaceOccupation.room.is.not.free", space.getName(),
                    lessonInstance.getDay().toString("dd-MM-yy"));
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
    public List<Interval> getIntervals() {
        return getLessonInstancesSet().stream().map(LessonInstance::getInterval).collect(Collectors.toList());
    }

    @Override
    public String getPresentationString() {
        if (!getLessonInstancesSet().isEmpty()) {
            return getLessonInstancesSet().iterator().next().getLesson().getShift().getExecutionCourse().getSigla();
        }
        return "";
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
        Set<LessonInstance> lessonInstancesSet = getLessonInstancesSet();
        if (!lessonInstancesSet.isEmpty()) {
            Lesson theLesson = lessonInstancesSet.iterator().next().getLesson();
            String asd = theLesson.getShift().getCourseLoadsSet().iterator().next().getType().getFullNameTipoAula();
            for (YearMonthDay ymd : theLesson.getAllLessonDates()) {
                asd += "\n" + ymd.toString();
            }
            return asd;
        }

        return "";
    }

    @Override
    public String getType() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.lesson");
    }

    @Override
    public DateTime getStart() {
        List<Interval> intervals = getIntervals();
        intervals.sort((i1, i2) -> i1.getStart().compareTo(i2.getStart()));
        if (intervals.size() > 0) {
            return intervals.get(0).getStart();
        }
        return null;
    }

    @Override
    public DateTime getEnd() {
        List<Interval> intervals = getIntervals();
        intervals.sort((i1, i2) -> i1.getEnd().compareTo(i2.getEnd()));
        if (intervals.size() > 0) {
            return intervals.get(intervals.size() - 1).getEnd();
        }
        return null;
    }

}
