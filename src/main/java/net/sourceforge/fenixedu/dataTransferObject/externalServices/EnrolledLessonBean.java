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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Lesson;

public class EnrolledLessonBean {

    private String courseAcronym;
    private String lessonType;
    private String room;
    private String weekDay;
    private Date begin;
    private Date end;

    public EnrolledLessonBean(final Lesson lesson) {
        setCourseAcronym(lesson.getExecutionCourse().getSigla());
        setLessonType(lesson.getShift().getShiftTypesCodePrettyPrint());
        setWeekDay(lesson.getDiaSemana().getDiaSemanaString());
        setBegin(lesson.getBeginHourMinuteSecond().toDateTimeAtCurrentTime().toDate());
        setEnd(lesson.getEndHourMinuteSecond().toDateTimeAtCurrentTime().toDate());
        if (lesson.getRoomOccupation() != null) {
            setRoom(lesson.getRoomOccupation().getRoom().getName());
        } else if (lesson.getLastLessonInstance() != null && lesson.getLastLessonInstance().getRoom() != null) {
            setRoom(lesson.getLastLessonInstance().getRoom().getName());
        }
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setCourseAcronym(String courseAcronym) {
        this.courseAcronym = courseAcronym;
    }

    public String getCourseAcronym() {
        return courseAcronym;
    }
}
