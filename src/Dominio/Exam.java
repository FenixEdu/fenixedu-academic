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
public class Exam extends WrittenEvaluation implements IExam {

	protected Season season;
	protected List associatedRooms;

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
	public List getAssociatedRooms() {
		return associatedRooms;
	}

	/**
	 * @param rooms
	 */
	public void setAssociatedRooms(List rooms) {
		associatedRooms = rooms;
	}

	

}