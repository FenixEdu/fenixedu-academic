package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = FenixSpace.Campus.class, name = "CAMPUS"),
        @JsonSubTypes.Type(value = FenixSpace.Building.class, name = "BUILDING"),
        @JsonSubTypes.Type(value = FenixSpace.Floor.class, name = "FLOOR"),
        @JsonSubTypes.Type(value = FenixSpace.Room.class, name = "ROOM") })
public class FenixSpace {

    public static class Campus extends FenixSpace {
        @JsonSerialize(contentAs = FenixSpace.class, typing = Typing.STATIC)
        @JsonInclude(Include.NON_EMPTY)
        public List<FenixSpace> buildings;

        public Campus(String id, String name) {
            super(id, name);
        }

        public Campus(String id, String name, List<FenixSpace> buildings) {
            super(id, name);
            this.buildings = buildings;
        }

    }

    public static class Building extends FenixSpace {

        @JsonSerialize(as = FenixSpace.class)
        @JsonInclude(Include.NON_EMPTY)
        public Campus campus;

        @JsonSerialize(contentAs = FenixSpace.class, typing = Typing.STATIC)
        @JsonInclude(Include.NON_EMPTY)
        public List<FenixSpace> floors;

        public Building(String id, String name) {
            super(id, name);
        }

        public Building(String id, String name, Campus campus, List<FenixSpace> floors) {
            super(id, name);
            this.campus = campus;
            this.floors = floors;
        }

    }

    public static class Floor extends FenixSpace {
        @JsonSerialize(contentAs = FenixSpace.class)
        @JsonInclude(Include.NON_EMPTY)
        public List<Room> rooms;

        @JsonSerialize(as = FenixSpace.class)
        @JsonInclude(Include.NON_EMPTY)
        public Building building;

        public Floor(String id, String name) {
            super(id, name);
        }

        public Floor(String id, String name, Building building, List<Room> rooms) {
            super(id, name);
            this.building = building;
            this.rooms = rooms;
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
                public WrittenEvaluationEvent.ExecutionCourse course;

                public LessonEvent(String start, String end, String weekday, String info,
                        WrittenEvaluationEvent.ExecutionCourse course) {
                    super(start, end, weekday);
                    this.info = info;
                    this.course = course;
                }

            }

            public static abstract class WrittenEvaluationEvent extends RoomEvent {

                public static class ExecutionCourse {
                    public String sigla;
                    public String name;
                    public String id;

                    public ExecutionCourse(String sigla, String name, String id) {
                        super();
                        this.sigla = sigla;
                        this.name = name;
                        this.id = id;
                    }

                }

                public static class TestEvent extends WrittenEvaluationEvent {
                    public String description;

                    public TestEvent(String start, String end, String weekday, List<ExecutionCourse> courses, String description) {
                        super(start, end, weekday, courses);
                        this.description = description;
                    }

                }

                public static class ExamEvent extends WrittenEvaluationEvent {
                    public Integer season;

                    public ExamEvent(String start, String end, String weekday, List<ExecutionCourse> courses, Integer season) {
                        super(start, end, weekday, courses);
                        this.season = season;
                    }

                }

                public List<WrittenEvaluationEvent.ExecutionCourse> courses;

                public WrittenEvaluationEvent(String start, String end, String weekday, List<ExecutionCourse> courses) {
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

        @JsonSerialize(as = FenixSpace.class)
        public FenixSpace floor;
        public String description;
        public Integer normalCapacity;
        public Integer examCapacity;

        @JsonInclude(Include.NON_EMPTY)
        public List<RoomEvent> events;

        public Room(String id, String name) {
            super(id, name);
        }

        public Room(String id, String name, Floor floor, String description, Integer normalCapacity, Integer examCapacity,
                List<RoomEvent> events) {
            super(id, name);
            this.floor = floor;
            this.description = description;
            this.normalCapacity = normalCapacity;
            this.examCapacity = examCapacity;
            this.events = events;
        }

    }

    public String id;
    public String name;

    public FenixSpace(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

}