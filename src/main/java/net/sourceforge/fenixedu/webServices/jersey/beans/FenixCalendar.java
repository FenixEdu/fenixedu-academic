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

    private String academicTerm;

    @JsonSerialize(typing = Typing.DYNAMIC)
    private List<FenixCalendarEvent> events;

    public FenixCalendar(String academicTerm, List<FenixCalendarEvent> events) {
        super();
        this.academicTerm = academicTerm;
        this.events = events;
    }

    public String getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }

    public List<FenixCalendarEvent> getEvents() {
        return events;
    }

    public void setEvents(List<FenixCalendarEvent> events) {
        this.events = events;
    }

}
