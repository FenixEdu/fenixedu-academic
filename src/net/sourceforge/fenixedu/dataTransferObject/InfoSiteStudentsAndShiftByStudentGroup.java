/*
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author joaosa & rmalo
 *
 */
public class InfoSiteStudentsAndShiftByStudentGroup extends DataTranferObject implements ISiteComponent {

	private List infoSiteStudentInformationList;
	private InfoStudentGroup infoStudentGroup;
	private InfoShift infoShift;
	
	
	/**
	* @return
	*/
	public List getInfoSiteStudentInformationList() {
		return infoSiteStudentInformationList;
	}

	/**
	 * @param list
	 */
	public void setInfoSiteStudentInformationList(List infoSiteStudentInformationList) {
		this.infoSiteStudentInformationList = infoSiteStudentInformationList;
	}
	

	/**
	* @return
	*/
	public InfoStudentGroup getInfoStudentGroup() {
		return infoStudentGroup;
	}

	/**
	 * @param list
	 */
	public void setInfoStudentGroup(InfoStudentGroup infoStudentGroup) {
		this.infoStudentGroup = infoStudentGroup;
	}
	
	/**
	* @return
	*/
	public InfoShift getInfoShift() {
		return infoShift;
	}

	/**
	 * @param list
	 */
	public void setInfoShift(InfoShift infoShift) {
		this.infoShift = infoShift;
	}
	
}
