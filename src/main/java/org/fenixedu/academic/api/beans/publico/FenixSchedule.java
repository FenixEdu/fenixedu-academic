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
package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSpace.Room;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.Interval;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class FenixSchedule {

    public static class FenixCourseLoad {

        String type;
        BigDecimal totalQuantity;
        BigDecimal unitQuantity;

        public FenixCourseLoad(final CourseLoad courseLoad) {
            final ShiftType shiftType = courseLoad.getType();
            type = shiftType == null ? null : shiftType.name();
            totalQuantity = courseLoad.getTotalQuantity();
            unitQuantity = courseLoad.getUnitQuantity() == null ? courseLoad.getWeeklyHours() : courseLoad.getUnitQuantity();
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public BigDecimal getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(BigDecimal totalQuantity) {
            this.totalQuantity = totalQuantity;
        }

        public BigDecimal getUnitQuantity() {
            return unitQuantity;
        }

        public void setUnitQuantity(BigDecimal unitQuantity) {
            this.unitQuantity = unitQuantity;
        }
    }

    public static class FenixLessonOccurence extends FenixInterval {

        FenixSpace.Room room;

        public FenixLessonOccurence(final LessonInstance instance) {
            super(instance.getBeginDateTime(), instance.getEndDateTime());
            setRoom(instance.getRoom());
        }

        public FenixLessonOccurence(final Interval interval, final Space room) {
            super(interval);
            setRoom(room);
        }

        public FenixSpace.Room getRoom() {
            return room;
        }

        public void setRoom(Space room) {
            this.room = room == null ? null : new FenixSpace.Room(room, false, false, null);
        }

    }

    public static class FenixShift {

        public static class FenixShiftOccupation {
            public FenixShiftOccupation(Integer current, Integer max) {
                super();
                this.current = current;
                this.max = max;
            }

            Integer current;
            Integer max;

            public Integer getCurrent() {
                return current;
            }

            public void setCurrent(Integer current) {
                this.current = current;
            }

            public Integer getMax() {
                return max;
            }

            public void setMax(Integer max) {
                this.max = max;
            }

        }

        String name;
        FenixShiftOccupation occupation;
        List<String> types = new ArrayList<>();
        List<FenixLessonOccurence> lessons = new ArrayList<>();
        List<FenixSpace.Room> rooms = new ArrayList<>();

        public FenixShift(final Shift shift) {
            this.name = shift.getNome();
            setOccupation(new FenixShiftOccupation(shift.getStudentsSet().size(), shift.getLotacao()));
            for (CourseLoad courseLoad : shift.getCourseLoadsSet()) {
                final ShiftType type = courseLoad.getType();
                if (type != null) {
                    types.add(type.name());
                }
            }
            Set<Space> spaces = new HashSet<>();
            for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
                if (lesson.getSala() != null) {
                    spaces.add(lesson.getSala());
                }
                for (final LessonInstance lessonInstance : lesson.getLessonInstancesSet()) {
                    lessons.add(new FenixLessonOccurence(lessonInstance));
                }
                for (final Interval interval : lesson.getAllLessonIntervalsWithoutInstanceDates()) {
                    lessons.add(new FenixLessonOccurence(interval, lesson.getSala()));
                }
            }

            setRooms(spaces);

        }

        public void setRooms(Set<Space> rooms) {
            this.rooms = FluentIterable.from(rooms).transform(new Function<Space, FenixSpace.Room>() {

                @Override
                public Room apply(Space input) {
                    return new Room(input, false, true, null);
                }
            }).toList();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<FenixLessonOccurence> getLessons() {
            return lessons;
        }

        public void setLessons(List<FenixLessonOccurence> lessons) {
            this.lessons = lessons;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public FenixShiftOccupation getOccupation() {
            return occupation;
        }

        public void setOccupation(FenixShiftOccupation occupation) {
            this.occupation = occupation;
        }

        public List<FenixSpace.Room> getRooms() {
            return rooms;
        }
    }

    List<FenixInterval> lessonPeriods = new ArrayList<>();
    List<FenixCourseLoad> courseLoads = new ArrayList<>();
    List<FenixShift> shifts = new ArrayList<>();

    public FenixSchedule(final ExecutionCourse executionCourse) {
        for (final OccupationPeriod occupationPeriod : executionCourse.getLessonPeriods()) {
            for (final Interval interval : occupationPeriod.getIntervals()) {
                lessonPeriods.add(new FenixInterval(interval));
            }
        }
        for (final CourseLoad courseLoad : executionCourse.getCourseLoadsSet()) {
            courseLoads.add(new FenixCourseLoad(courseLoad));
            for (final Shift shift : courseLoad.getShiftsSet()) {
                shifts.add(new FenixShift(shift));
            }
        }
    }

    public List<FenixInterval> getLessonPeriods() {
        return lessonPeriods;
    }

    public void setLessonPeriods(List<FenixInterval> lessonPeriods) {
        this.lessonPeriods = lessonPeriods;
    }

    public List<FenixCourseLoad> getCourseLoads() {
        return courseLoads;
    }

    public void setCourseLoads(List<FenixCourseLoad> courseLoads) {
        this.courseLoads = courseLoads;
    }

    public List<FenixShift> getShifts() {
        return shifts;
    }

    public void setShifts(List<FenixShift> shifts) {
        this.shifts = shifts;
    }

}
