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
public class Exam extends Evaluation implements IExam {

	protected Calendar day;
	protected Calendar beginning;
	protected Calendar end;
	protected Season season;
//	protected List associatedExecutionCourses;
	protected List associatedRooms;
	protected Calendar enrollmentBeginDay;
	protected Calendar enrollmentBeginTime;
	protected Calendar enrollmentEndDay;
	protected Calendar enrollmentEndTime;

	public Exam() {
	}

	public Exam(Integer idInternal) {
		setIdInternal(idInternal);
	}

	public boolean equals(Object obj) {
		if (obj instanceof Exam) {
			Exam examObj = (Exam) obj;
			return this.getIdInternal().equals(examObj.getIdInternal());
		}

		return false;
	}

	public String toString() {
		return "[EXAM:"
			+ " id= '"
			+ this.getIdInternal()
			+ "'\n"
			+ " day= '"
			+ this.getDay()
			+ "'\n"
			+ " beginning= '"
			+ this.getBeginning()
			+ "'\n"
			+ " end= '"
			+ this.getEnd()
			+ "'\n"
			+ " season= '"
			+ this.getSeason()
			+ "'\n"
			+ "";
	}

	public Exam(
		Calendar day,
		Calendar beginning,
		Calendar end,
		Season season) {
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

//	/**
//	 * @return
//	 */
//	public List getAssociatedExecutionCourses() {
//		return associatedExecutionCourses;
//	}
//
//	/**
//	 * @param list
//	 */
//	public void setAssociatedExecutionCourses(List list) {
//		associatedExecutionCourses = list;
//	}

	/**
	 * @return
	 */
	public List getAssociatedRooms() {
		return associatedRooms;
	}

	/**
	 * @param rooms
	 */
	public void setAssociatedRooms(List rooms) {
		associatedRooms = rooms;
	}

	

	/**
	 * @return
	 */
	public Calendar getEnrollmentBeginDay() {
		return enrollmentBeginDay;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentEndDay() {
		return enrollmentEndDay;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentBeginDay(Calendar calendar) {
		enrollmentBeginDay = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentEndDay(Calendar calendar) {
		enrollmentEndDay = calendar;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentBeginTime() {
		return enrollmentBeginTime;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentEndTime() {
		return enrollmentEndTime;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentBeginTime(Calendar calendar) {
		enrollmentBeginTime = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentEndTime(Calendar calendar) {
		enrollmentEndTime = calendar;
	}

}