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

	public InfoExam() {
	}

	public InfoExam(Date day, Calendar beginning, Calendar end, Season season) {
		this.setDay(day);
		this.setBeginning(beginning);
		this.setEnd(end);
		this.setSeason(season);
	}


	public String toString() {
		return "[INFOEXAM:"
			+ " day= '"             + this.getDay()             + "'"
			+ " beginning= '"       + this.getBeginning()       + "'"
			+ " end= '"				+ this.getEnd()       + "'"
			+ " season= '"          + this.getSeason()          + "'"
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

}