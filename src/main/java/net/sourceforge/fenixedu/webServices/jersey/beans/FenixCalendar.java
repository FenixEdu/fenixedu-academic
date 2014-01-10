package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixPeriod;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSpace;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;

public class FenixCalendar {

    public static class FenixCalendarEvent {
        FenixPeriod eventPeriod;
        Set<FenixSpace> location;
        String title;

        public FenixCalendarEvent(FenixPeriod eventPeriod, Set<FenixSpace> location, String title) {
            super();
            this.eventPeriod = eventPeriod;
            this.location = location;
            this.title = title;
        }

        public Set<FenixSpace> getLocation() {
            return location;
        }

        public void setLocation(Set<FenixSpace> location) {
            this.location = location;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public FenixPeriod getEventPeriod() {
            return eventPeriod;
        }

        public void setEventPeriod(FenixPeriod eventPeriod) {
            this.eventPeriod = eventPeriod;
        }

    }

    public static class FenixClassEvent extends FenixCalendarEvent {

        FenixCourse course;

        public FenixClassEvent(FenixPeriod eventPeriod, Set<FenixSpace> location, String title, FenixCourse course) {
            super(eventPeriod, location, title);
            setCourse(course);
        }

        @Override
        @JsonProperty("classPeriod")
        public FenixPeriod getEventPeriod() {
            return super.getEventPeriod();
        }

        public void setCourse(FenixCourse course) {
            this.course = course;
        }

        public FenixCourse getCourse() {
            return course;
        }
    }

    public static class FenixEvaluationEvent extends FenixCalendarEvent {

        Set<FenixCourse> courses;

        public FenixEvaluationEvent(FenixPeriod eventPeriod, Set<FenixSpace> location, String title, Set<FenixCourse> courses) {
            super(eventPeriod, location, title);
            setCourses(courses);
        }

        @Override
        @JsonProperty("evaluationPeriod")
        public FenixPeriod getEventPeriod() {
            return super.getEventPeriod();
        }

        public Set<FenixCourse> getCourses() {
            return courses;
        }

        public void setCourses(Set<FenixCourse> courses) {
            this.courses = courses;
        }

    }

    private String year;

    @JsonSerialize(typing = Typing.DYNAMIC)
    private List<FenixCalendarEvent> events;

    public FenixCalendar(String year, List<FenixCalendarEvent> events) {
        super();
        this.year = year;
        this.events = events;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<FenixCalendarEvent> getEvents() {
        return events;
    }

    public void setEvents(List<FenixCalendarEvent> events) {
        this.events = events;
    }

}
