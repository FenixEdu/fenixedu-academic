/*
 * Created on 8/Jan/2005
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author joaosa & rmalo
 *
 */
public class InfoSiteStudentGroupAndStudents extends DataTranferObject implements ISiteComponent {

	private List infoSiteStudentsAndShiftByStudentGroupList;
	private InfoSiteShiftsAndGroups infoSiteShiftsAndGroups;
	
	
	/**
	* @return
	*/
	public List getInfoSiteStudentsAndShiftByStudentGroupList() {
		return infoSiteStudentsAndShiftByStudentGroupList;
	}

	/**
	 * @param list
	 */
	public void setInfoSiteStudentsAndShiftByStudentGroupList(List infoSiteStudentsAndShiftByStudentGroupList) {
		this.infoSiteStudentsAndShiftByStudentGroupList = infoSiteStudentsAndShiftByStudentGroupList;
	}
	

	/**
	* @return
	*/
	public InfoSiteShiftsAndGroups getInfoSiteShiftsAndGroups() {
		return infoSiteShiftsAndGroups;
	}

	/**
	 * @param list
	 */
	public void setInfoSiteShiftsAndGroups(InfoSiteShiftsAndGroups infoSiteShiftsAndGroups) {
		this.infoSiteShiftsAndGroups = infoSiteShiftsAndGroups;
	}
	
}