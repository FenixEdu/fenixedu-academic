/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package DataBeans.teacher.credits;

import java.util.ArrayList;
import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoShift;

/**
 * @author jpvl
 */
public class InfoShiftPercentage extends DataTranferObject{
	private List lessons;
	private InfoShift shift;
	private Double availablePercentage;
	private List infoShiftProfessorshipList;

	public InfoShiftPercentage(){
		infoShiftProfessorshipList = new ArrayList();
	}
	
	/**
	 * @return
	 */
	public Double getAvailablePercentage() {
		return availablePercentage;
	}

	/**
	 * @return
	 */
	public InfoShift getShift() {
		return shift;
	}

	/**
	 * @return
	 */
	public List getInfoShiftProfessorshipList() {
		return infoShiftProfessorshipList;
	}

	/**
	 * @param double1
	 */
	public void setAvailablePercentage(Double double1) {
		availablePercentage = double1;
	}

	/**
	 * @param shift
	 */
	public void setShift(InfoShift shift) {
		this.shift = shift;
	}
	
	public void addInfoShiftProfessorship(InfoShiftProfessorship infoTeacherShiftPercentage){
		infoShiftProfessorshipList.add(infoTeacherShiftPercentage);	
	}

	public void setInfoLessons(List lessons) {
		this.lessons = lessons;
	}
	
	public List getInfoLessons(){
		return this.lessons;
	}	
}
