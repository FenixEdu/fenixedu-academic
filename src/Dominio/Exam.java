/*
 * Created on 18/Mar/2003
 *
 */
package Dominio;

import java.util.Calendar;
import java.util.Date;

import Util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class Exam extends DomainObject implements IExam {

	protected Date day;
	protected Calendar beginning;
	protected Calendar end;
	protected Season season;
	protected IDisciplinaExecucao executionCourse;

	private Integer keyExecutionCourse;

	public Exam() {}

	public boolean equals(Object obj) {
		if (obj instanceof Exam) {
			Exam examObj = (Exam) obj;
			return this.getDay().equals(examObj.getDay())
				   && this.getBeginning().get(Calendar.HOUR_OF_DAY) == examObj.getBeginning().get(Calendar.HOUR_OF_DAY)
				   && this.getBeginning().get(Calendar.MINUTE) == examObj.getBeginning().get(Calendar.MINUTE)
			       && this.getBeginning().get(Calendar.SECOND) == examObj.getBeginning().get(Calendar.SECOND)
				   && this.getEnd().get(Calendar.HOUR_OF_DAY) == examObj.getEnd().get(Calendar.HOUR_OF_DAY)
				   && this.getEnd().get(Calendar.MINUTE) == examObj.getEnd().get(Calendar.MINUTE)
				   && this.getEnd().get(Calendar.SECOND) == examObj.getEnd().get(Calendar.SECOND)
				   && this.getSeason().equals(examObj.getSeason())
				   && this.getExecutionCourse().equals(examObj.getExecutionCourse());
		}

		return false;
	}

	public String toString() {
		return "[EXAM:"
			+ " day= '"             + this.getDay()             + "'"
			+ " beginning= '"       + this.getBeginning()       + "'"
			+ " end= '"             + this.getEnd()             + "'"
			+ " season= '"           + this.getSeason()           + "'"			
			+ " executionCourse= '" + this.getExecutionCourse() + "'"
			+ "";
	}


	public Exam(Date day, Calendar beginning, Calendar end, Season season, IDisciplinaExecucao executionCourse) {
		this.setDay(day);
		this.setBeginning(beginning);
		this.setEnd(end);
		this.setSeason(season);
		this.setExecutionCourse(executionCourse);
	}


	/**
	 * @return Calendar
	 */
	public Calendar getBeginning() {
		return beginning;
	}

	/**
	 * @return Calendar
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
	 * @return Integer
	 */
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
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
	 * Sets the keyExecutionCourse.
	 * @param keyExecutionCourse The keyExecutionCourse to set
	 */
	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse = keyExecutionCourse;
	}

	/**
	 * @return IDisciplinaExecucao
	 */
	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}

	/**
	 * Sets the executionCourse.
	 * @param executionCourse The executionCourse to set
	 */
	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse = executionCourse;
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