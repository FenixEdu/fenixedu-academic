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
	private InfoProfessorShip infoProfessorship = null;
	private InfoShift infoShift = null;
	private Double percentage = null;

	/**
	 * @return
	 */
	public InfoProfessorShip getInfoProfessorship() {
		return infoProfessorship;
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
	public void setInfoProfessorship(InfoProfessorShip ship) {
		infoProfessorship = ship;
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
