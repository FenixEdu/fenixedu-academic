package DataBeans;

import java.sql.Time;

import Util.DiaSemana;


/**
 * @author Fernanda Quitério
 * 17/10/2003
 * 
 */
public class InfoSupportLessonsTimetable extends InfoObject{
	private DiaSemana weekDay;
	private Time startTime;
	private Time endTime;
	private String place;
	
	private InfoTeacher infoTeacher;
	private InfoExecutionCourse infoExecutionCourse;


	public InfoSupportLessonsTimetable() {
	}
	

	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof InfoSupportLessonsTimetable) {
			InfoSupportLessonsTimetable infoSupportLessonsTimetable = (InfoSupportLessonsTimetable) arg0;

			if (elementsAreEqual(infoSupportLessonsTimetable.getInfoExecutionCourse(), this.getInfoExecutionCourse())
			&& elementsAreEqual(infoSupportLessonsTimetable.getInfoTeacher(), this.getInfoTeacher())
				&& elementsAreEqual(infoSupportLessonsTimetable.getStartTime(), this.getStartTime())
				&& elementsAreEqual(infoSupportLessonsTimetable.getEndTime(), this.getEndTime())
				&& elementsAreEqual(infoSupportLessonsTimetable.getPlace(), this.getPlace())
				&& elementsAreEqual(infoSupportLessonsTimetable.getWeekDay(), this.getWeekDay())) {
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
		
	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}
	
	public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		this.infoExecutionCourse = infoExecutionCourse;	
	}			


	/**
	 * @return
	 */
	public InfoTeacher getInfoTeacher() {
		return infoTeacher;
	}

	/**
	 * @param infoTeacher
	 */
	public void setInfoTeacher(InfoTeacher infoTeacher) {
		this.infoTeacher = infoTeacher;
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

}
