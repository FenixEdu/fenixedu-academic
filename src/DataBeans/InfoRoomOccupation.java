/*
 * Created on 31/Out/2003
 *
  */
package DataBeans;

import java.util.Calendar;

import Util.DiaSemana;

/**
 * @author Ana e Ricardo
 *
 */
public class InfoRoomOccupation {

	protected Calendar startTime;
	protected Calendar endTime;
	protected DiaSemana dayOfWeek;
	protected InfoRoom infoRoom;
	protected InfoPeriod infoPeriod;
	
	
	/**
	 * @return
	 */
	public DiaSemana getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * @return
	 */
	public Calendar getEndTime() {
		return endTime;
	}

	/**
	 * @return
	 */
	public Calendar getStartTime() {
		return startTime;
	}

	/**
	 * @param semana
	 */
	public void setDayOfWeek(DiaSemana semana) {
		dayOfWeek = semana;
	}

	/**
	 * @param calendar
	 */
	public void setEndTime(Calendar calendar) {
		endTime = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setStartTime(Calendar calendar) {
		startTime = calendar;
	}

	/**
	 * @return Returns the infoPeriod.
	 */
	public InfoPeriod getInfoPeriod() {
		return infoPeriod;
	}

	/**
	 * @param infoPeriod The infoPeriod to set.
	 */
	public void setInfoPeriod(InfoPeriod infoPeriod) {
		this.infoPeriod = infoPeriod;
	}

	/**
	 * @return Returns the infoRoom.
	 */
	public InfoRoom getInfoRoom() {
		return infoRoom;
	}

	/**
	 * @param infoRoom The infoRoom to set.
	 */
	public void setInfoRoom(InfoRoom infoRoom) {
		this.infoRoom = infoRoom;
	}

}
