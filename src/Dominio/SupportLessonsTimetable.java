package Dominio;

import java.sql.Time;

import Util.DiaSemana;

/**
 * @author Fernanda Quitério
 * 17/10/2003
 *
 */
public class SupportLessonsTimetable extends DomainObject implements ISupportLessonsTimetable {

	private DiaSemana weekDay;
	private Time startTime;
	private Time endTime;
	private String place;
	private Integer keyExecutionCourse;
	private Integer keyTeacher;
	
	private ITeacher teacher;
	private IDisciplinaExecucao executionCourse;

	public SupportLessonsTimetable() {
	}

	public SupportLessonsTimetable(Integer idInternal) {
		setIdInternal(idInternal);
	}

	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse = keyExecutionCourse;
	}

	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}

	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse = executionCourse;
	}

	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof ISupportLessonsTimetable) {
			ISupportLessonsTimetable supportLessonsTimetable = (ISupportLessonsTimetable) arg0;

			if (elementsAreEqual(supportLessonsTimetable.getExecutionCourse(), this.getExecutionCourse())
			&& elementsAreEqual(supportLessonsTimetable.getTeacher(), this.getTeacher())
				&& elementsAreEqual(supportLessonsTimetable.getStartTime(), this.getStartTime())
				&& elementsAreEqual(supportLessonsTimetable.getEndTime(), this.getEndTime())
				&& elementsAreEqual(supportLessonsTimetable.getPlace(), this.getPlace())
				&& elementsAreEqual(supportLessonsTimetable.getWeekDay(), this.getWeekDay())) {
				result = true;
			}
		}
		return result;
	}

	private boolean elementsAreEqual(Object element1, Object element2) {
		boolean result = false;
		if ((element1 == null && element2 == null) || (element1 != null && element2 != null && element1.equals(element2))) {
			result = true;
		}
		return result;
	}

	public String toString() {
		String result = "[SUPPORT LESSONS TIMETABLE";
		result += ", codInt=" + getIdInternal();
		result += ", professor=" + getTeacher();
		result += ", dia=" + getWeekDay();
		result += ", inicio=" + getStartTime().toString();
		result += ", fim=" + getEndTime().toString();
		result += ", local=" + getPlace();
		result += "]";
		return result;
	}
	/**
	 * @return
	 */
	public Time getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 */
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return
	 */
	public DiaSemana getWeekDay() {
		return weekDay;
	}

	/**
	 * @param weekDay
	 */
	public void setWeekDay(DiaSemana weekDay) {
		this.weekDay = weekDay;
	}

	/**
	 * @return
	 */
	public Time getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 */
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return
	 */
	public Integer getKeyTeacher() {
		return keyTeacher;
	}

	/**
	 * @param keyTeacher
	 */
	public void setKeyTeacher(Integer keyTeacher) {
		this.keyTeacher = keyTeacher;
	}

	/**
	 * @return
	 */
	public ITeacher getTeacher() {
		return teacher;
	}

	/**
	 * @param teacher
	 */
	public void setTeacher(ITeacher teacher) {
		this.teacher = teacher;
	}

}
