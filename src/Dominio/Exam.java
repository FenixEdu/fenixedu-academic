/*
 * Created on 18/Mar/2003
 *
 */
package Dominio;

import java.util.Calendar;
import java.util.List;

import Util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class Exam extends DomainObject implements IExam {

	protected Calendar day;
	protected Calendar beginning;
	protected Calendar end;
	protected Season season;
	protected List associatedExecutionCourses;
	protected String comment;


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
			+ " day= '"             + this.getDay()             + "'\n"
			+ " beginning= '"       + this.getBeginning()       + "'\n"
			+ " end= '"             + this.getEnd()             + "'\n"
			+ " season= '"           + this.getSeason()           + "'\n"
			+ "";
	}


	public Exam(Calendar day, Calendar beginning, Calendar end, Season season) {
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
	public Calendar getDay() {
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
	public void setDay(Calendar day) {
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

	/**
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param string
	 */
	public void setComment(String string) {
		comment = string;
	}

}