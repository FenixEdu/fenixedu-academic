/*
 * InfoExam.java
 *
 * Created on 2003/03/19
 */

package DataBeans;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.Date;

import Util.Season;

public class InfoExam {
	protected Date day;
	protected Calendar beginning;
	protected Calendar end;
	protected Season season;	
	protected InfoExecutionCourse infoExecutionCourse;

	public InfoExam() {
	}

	public InfoExam(Date day, Calendar beginning, Calendar end, Season season, InfoExecutionCourse infoExecutionCourse) {
		this.setDay(day);
		this.setBeginning(beginning);
		this.setEnd(end);
		this.setSeason(season);
		this.setInfoExecutionCourse(infoExecutionCourse);
	}

	public boolean equals(Object obj) {
		if (obj instanceof InfoExam) {
			InfoExam examObj = (InfoExam) obj;
			return this.getDay().equals(examObj.getDay())
				   && this.getBeginning().get(Calendar.HOUR_OF_DAY) == examObj.getBeginning().get(Calendar.HOUR_OF_DAY)
				   && this.getBeginning().get(Calendar.MINUTE) == examObj.getBeginning().get(Calendar.MINUTE)
				   && this.getBeginning().get(Calendar.SECOND) == examObj.getBeginning().get(Calendar.SECOND)
				   && this.getEnd().get(Calendar.HOUR_OF_DAY) == examObj.getEnd().get(Calendar.HOUR_OF_DAY)
				   && this.getEnd().get(Calendar.MINUTE) == examObj.getEnd().get(Calendar.MINUTE)
				   && this.getEnd().get(Calendar.SECOND) == examObj.getEnd().get(Calendar.SECOND)
				   && this.getSeason().equals(examObj.getSeason())
				   && this.getInfoExecutionCourse().equals(examObj.getInfoExecutionCourse());
		}

		return false;
	}

	public String toString() {
		return "[INFOEXAM:"
			+ " day= '"             + this.getDay()             + "'"
			+ " beginning= '"       + this.getBeginning()       + "'"
			+ " end= '"				+ this.getEnd()       + "'"
			+ " season= '"          + this.getSeason()          + "'"
			+ " executionCourse= '" + this.getInfoExecutionCourse() + "'"
			+ "";
	}


	/**
	 * @return Calendar
	 */
	public Calendar getBeginning() {
		return beginning;
	}

	/**
	 * @return Date
	 */
	public Date getDay() {
		return day;
	}

	/**
	 * @return Calendar
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * @return InfoExecutionCourse
	 */
	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	/**
	 * Sets the beginning.
	 * @param beginning The beginning to set
	 */
	public void setBeginning(Calendar beginning) {
		this.beginning = beginning;
	}

	/**
	 * Sets the day.
	 * @param day The day to set
	 */
	public void setDay(Date day) {
		this.day = day;
	}

	/**
	 * Sets the end.
	 * @param end The end to set
	 */
	public void setEnd(Calendar end) {
		this.end = end;
	}

	/**
	 * Sets the infoExecutionCourse.
	 * @param infoExecutionCourse The infoExecutionCourse to set
	 */
	public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		this.infoExecutionCourse = infoExecutionCourse;
	}

	/**
	 * @return
	 */
	public Season getSeason() {
		return season;
	}

	/**
	 * @param season
	 */
	public void setSeason(Season season) {
		this.season = season;
	}

}