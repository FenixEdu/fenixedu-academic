package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.joda.time.Interval;

public class FenixSchedule {

    public static class FenixCourseLoad {

        String type;
        BigDecimal totalQuantity;
        BigDecimal unitQuantity;

        public FenixCourseLoad(final CourseLoad courseLoad) {
            final ShiftType shiftType = courseLoad.getType();
            type = shiftType == null ? null : shiftType.name();
            totalQuantity = courseLoad.getTotalQuantity();
            unitQuantity = courseLoad.getUnitQuantity();
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

    public static class FenixRoom {

        String name;
        String id;

        public FenixRoom(final AllocatableSpace room) {
            this.name = room.getName();
            this.id = room.getExternalId();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public static class FenixLessonOccurence extends FenixInterval {

        FenixRoom room;

        public FenixLessonOccurence(final LessonInstance instance) {
            super(instance.getBeginDateTime(), instance.getEndDateTime());
            final AllocatableSpace room = instance.getRoom();
            this.room = room == null ? null : new FenixRoom(room);
        }

        public FenixLessonOccurence(final Interval interval, final AllocatableSpace room) {
            super(interval);
            this.room = room == null ? null : new FenixRoom(room);
        }

        public FenixRoom getRoom() {
            return room;
        }

        public void setRoom(FenixRoom room) {
            this.room = room;
        }

    }

    public static class FenixShift {

        String name;
        List<String> types = new ArrayList<>();
        List<FenixLessonOccurence> lessons = new ArrayList<>();

        public FenixShift(final Shift shift) {
            this.name = shift.getNome();
            for (CourseLoad courseLoad : shift.getCourseLoadsSet()) {
                final ShiftType type = courseLoad.getType();
                if (type != null) {
                    types.add(type.name());
                }
            }
            for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
                for (final LessonInstance lessonInstance : lesson.getLessonInstancesSet()) {
                    lessons.add(new FenixLessonOccurence(lessonInstance));
                }
                for (final Interval interval : lesson.getAllLessonIntervalsWithoutInstanceDates()) {
                    lessons.add(new FenixLessonOccurence(interval, lesson.getSala()));
                }
            }
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
