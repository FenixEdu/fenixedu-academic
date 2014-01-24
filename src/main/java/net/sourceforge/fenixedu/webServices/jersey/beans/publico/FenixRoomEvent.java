package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCourse;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = FenixRoomEvent.LessonEvent.class, name = "LESSON"),
        @JsonSubTypes.Type(value = FenixRoomEvent.WrittenEvaluationEvent.TestEvent.class, name = "TEST"),
        @JsonSubTypes.Type(value = FenixRoomEvent.WrittenEvaluationEvent.ExamEvent.class, name = "EXAM"),
        @JsonSubTypes.Type(value = FenixRoomEvent.GenericEvent.class, name = "GENERIC") })
public abstract class FenixRoomEvent {

    public static class LessonEvent extends FenixRoomEvent {
        public String info;
        public FenixCourse course;

        public LessonEvent(String start, String end, String weekday, String info, FenixCourse course) {
            super(start, end, weekday);
            this.info = info;
            this.course = course;
        }

    }

    public static abstract class WrittenEvaluationEvent extends FenixRoomEvent {

        public static class TestEvent extends FenixRoomEvent.WrittenEvaluationEvent {
            public String description;

            public TestEvent(String start, String end, String weekday, List<FenixCourse> courses, String description) {
                super(start, end, weekday, courses);
                this.description = description;
            }

        }

        public static class ExamEvent extends FenixRoomEvent.WrittenEvaluationEvent {
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

    public static class GenericEvent extends FenixRoomEvent {
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

    public FenixRoomEvent(String start, String end, String weekday) {
        super();
        this.start = start;
        this.end = end;
        this.weekday = weekday;
    }

}