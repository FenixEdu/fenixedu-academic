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
			setRoom(lesson.getRoomOccupation().getRoom().getIdentification());
		} else if (lesson.getLastLessonInstance() != null && lesson.getLastLessonInstance().getRoom() != null) {
			setRoom(lesson.getLastLessonInstance().getRoom().getIdentification());
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
