/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package DataBeans.teacher.credits;

import DataBeans.InfoObject;
import DataBeans.InfoProfessorShip;
import DataBeans.InfoShift;

/**
 * @author jpvl
 */
public class InfoTeacherShiftPercentage extends InfoObject {
	private InfoProfessorShip infoProfesorShip = null;
	private InfoShift infoShift = null;
	private Double percentage = null;

	/**
	 * @return
	 */
	public InfoProfessorShip getInfoProfesorShip() {
		return infoProfesorShip;
	}

	/**
	 * @return
	 */
	public InfoShift getInfoShift() {
		return infoShift;
	}

	/**
	 * @return
	 */
	public Double getPercentage() {
		return percentage;
	}

	/**
	 * @param ship
	 */
	public void setInfoProfesorShip(InfoProfessorShip ship) {
		infoProfesorShip = ship;
	}

	/**
	 * @param shift
	 */
	public void setInfoShift(InfoShift shift) {
		infoShift = shift;
	}

	/**
	 * @param double1
	 */
	public void setPercentage(Double double1) {
		percentage = double1;
	}

}
