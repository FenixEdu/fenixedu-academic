/*
 * Created on 18/Mar/2003
 *
 */
package Dominio;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class Exam extends DomainObject implements IExam {

	protected Date day;
	protected Calendar beginning;
	protected Calendar end;
	protected Season season;
	protected List associatedExecutionCourses;


	public Exam() {}

	public boolean equals(Object obj) {
		if (obj instanceof Exam) {
			Exam examObj = (Exam) obj;
			return this.getIdInternal().equals(examObj.getIdInternal());
		}

		return false;
	}

	public String toString() {
		return "[EXAM:"
			+ " day= '"             + this.getDay()             + "'"
			+ " beginning= '"       + this.getBeginning()       + "'"
			+ " end= '"             + this.getEnd()             + "'"
			+ " season= '"           + this.getSeason()           + "'"			
			+ "";
	}


	public Exam(Date day, Calendar beginning, Calendar end, Season season) {
		this.setDay(day);
		this.setBeginning(beginning);
		this.setEnd(end);
		this.setSeason(season);
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

	/**
	 * @return
	 */
	public List getAssociatedExecutionCourses() {
		return associatedExecutionCourses;
	}

	/**
	 * @param list
	 */
	public void setAssociatedExecutionCourses(List list) {
		associatedExecutionCourses = list;
	}

}