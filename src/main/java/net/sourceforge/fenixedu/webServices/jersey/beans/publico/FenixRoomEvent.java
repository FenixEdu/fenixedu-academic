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

        public LessonEvent(String start, String end, String weekday, String day, FenixPeriod period, String info,
                FenixCourse course) {
            super(start, end, weekday, day, period);
            this.info = info;
            this.course = course;
        }

    }

    public static abstract class WrittenEvaluationEvent extends FenixRoomEvent {

        public static class TestEvent extends FenixRoomEvent.WrittenEvaluationEvent {
            public String description;

            public TestEvent(String start, String end, String weekday, String day, FenixPeriod period, List<FenixCourse> courses,
                    String description) {
                super(start, end, weekday, day, period, courses);
                this.description = description;
            }

        }

        public static class ExamEvent extends FenixRoomEvent.WrittenEvaluationEvent {
            public Integer season;

            public ExamEvent(String start, String end, String weekday, String day, FenixPeriod period, List<FenixCourse> courses,
                    Integer season) {
                super(start, end, weekday, day, period, courses);
                this.season = season;
            }

        }

        public List<FenixCourse> courses;

        public WrittenEvaluationEvent(String start, String end, String weekday, String day, FenixPeriod period,
                List<FenixCourse> courses) {
            super(start, end, weekday, day, period);
            this.courses = courses;
        }

    }

    public static class GenericEvent extends FenixRoomEvent {
        public String description;
        public String title;

        public GenericEvent(String start, String end, String weekday, String day, FenixPeriod period, String description,
                String title) {
            super(start, end, weekday, day, period);
            this.description = description;
            this.title = title;
        }

    }

    public String start;
    public String end;
    public String weekday;
    public String day;
    public FenixPeriod period;

    public FenixRoomEvent(String start, String end, String weekday, String day, FenixPeriod period) {
        super();
        this.start = start;
        this.end = end;
        this.weekday = weekday;
        this.day = day;
        this.period = period;
    }

}