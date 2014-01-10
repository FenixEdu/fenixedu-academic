package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCourse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class FenixSpace {

    public static class Campus extends FenixSpace {
        public Campus(String id, String name) {
            super(id, name, "CAMPUS");
        }

        public Campus(String id, String name, List<FenixSpace> containedSpaces, FenixSpace parentSpace) {
            super(id, name, "CAMPUS", containedSpaces, parentSpace);
        }
    }

    public static class Building extends FenixSpace {
        public Building(String id, String name) {
            super(id, name, "BUILDING");
        }

        public Building(String id, String name, List<FenixSpace> containedSpaces, FenixSpace parentSpace) {
            super(id, name, "BUILDING", containedSpaces, parentSpace);
        }

    }

    public static class Floor extends FenixSpace {
        public Floor(String id, String name) {
            super(id, name, "FLOOR");
        }

        public Floor(String id, String name, List<FenixSpace> containedSpaces, FenixSpace parentSpace) {
            super(id, name, "FLOOR", containedSpaces, parentSpace);
        }

    }

    public static class Room extends FenixSpace {

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
        @JsonSubTypes({ @JsonSubTypes.Type(value = RoomEvent.LessonEvent.class, name = "LESSON"),
                @JsonSubTypes.Type(value = RoomEvent.WrittenEvaluationEvent.TestEvent.class, name = "TEST"),
                @JsonSubTypes.Type(value = RoomEvent.WrittenEvaluationEvent.ExamEvent.class, name = "EXAM"),
                @JsonSubTypes.Type(value = RoomEvent.GenericEvent.class, name = "GENERIC") })
        public static abstract class RoomEvent {

            public static class LessonEvent extends RoomEvent {
                public String info;
                public FenixCourse course;

                public LessonEvent(String start, String end, String weekday, String info, FenixCourse course) {
                    super(start, end, weekday);
                    this.info = info;
                    this.course = course;
                }

            }

            public static abstract class WrittenEvaluationEvent extends RoomEvent {

                public static class TestEvent extends WrittenEvaluationEvent {
                    public String description;

                    public TestEvent(String start, String end, String weekday, List<FenixCourse> courses, String description) {
                        super(start, end, weekday, courses);
                        this.description = description;
                    }

                }

                public static class ExamEvent extends WrittenEvaluationEvent {
                    public Integer season;

                    public ExamEvent(String start, String end, String weekday, List<FenixCourse> courses, Integer season) {
                        super(start, end, weekday, courses);
                        this.season = season;
                    }

                }

                public List<FenixCourse> courses;

                public WrittenEvaluationEvent(String start, String end, String weekday, List<FenixCourse> courses) {
                    super(start, end, weekday);
                    this.courses = courses;
                }

            }

            public static class GenericEvent extends RoomEvent {
                public String description;
                public String title;

                public GenericEvent(String start, String end, String weekday, String description, String title) {
                    super(start, end, weekday);
                    this.description = description;
                    this.title = title;
                }

            }

            public String start;
            public String end;
            public String weekday;

            public RoomEvent(String start, String end, String weekday) {
                super();
                this.start = start;
                this.end = end;
                this.weekday = weekday;
            }

        }

        public String description;
        public Integer normalCapacity;
        public Integer examCapacity;

        @JsonInclude(Include.NON_NULL)
        public List<RoomEvent> events;

        public Room(String id, String name) {
            super(id, name, "ROOM");
        }

        public Room(String id, String name, FenixSpace parentSpace, String description, Integer normalCapacity,
                Integer examCapacity, List<RoomEvent> events) {
            super(id, name, "ROOM", null, parentSpace);
            this.description = description;
            this.normalCapacity = normalCapacity;
            this.examCapacity = examCapacity;
            if (events == null || events.isEmpty()) {
                events = new ArrayList<>();
            }
            this.events = events;
        }

    }

    public String id;
    public String name;
    @JsonInclude(Include.NON_NULL)
    public String type;
    @JsonInclude(Include.NON_NULL)
    public List<FenixSpace> containedSpaces;
    @JsonInclude(Include.NON_NULL)
    public FenixSpace parentSpace;

    public FenixSpace(String id, String name) {
        this(id, name, null);
    }

    public FenixSpace(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parentSpace = null;
        this.containedSpaces = null;
    }

    public FenixSpace(String id, String name, String type, List<FenixSpace> containedSpaces, FenixSpace parentSpace) {
        this(id, name, type);
        this.containedSpaces = containedSpaces;
        this.parentSpace = parentSpace;
    }
}