/*
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author joaosa & rmalo
 *
 */
public class InfoSiteStudentsAndGroups extends DataTranferObject implements ISiteComponent {

	private List infoSiteStudentsAndGroupsList;
	private InfoShift infoShift;
	
	
	/**
	* @return
	*/
	public List getInfoSiteStudentsAndGroupsList() {
		return infoSiteStudentsAndGroupsList;
	}

	/**
	 * @param list
	 */
	public void setInfoSiteStudentsAndGroupsList(List infoSiteStudentsAndGroupsList) {
		this.infoSiteStudentsAndGroupsList = infoSiteStudentsAndGroupsList;
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
	
	
	public boolean equals(Object objectToCompare) {
			boolean result = true;
			
			if(objectToCompare instanceof InfoSiteStudentsAndGroups)
				result = true;
				
			if (((InfoSiteStudentsAndGroups) objectToCompare).getInfoSiteStudentsAndGroupsList() == null
				&& this.getInfoSiteStudentsAndGroupsList() == null) {
				return true;
			}

			if (((InfoSiteStudentsAndGroups) objectToCompare).getInfoSiteStudentsAndGroupsList() == null
				|| this.getInfoSiteStudentsAndGroupsList() == null
				|| ((InfoSiteStudentsAndGroups) objectToCompare).getInfoSiteStudentsAndGroupsList().size()
					!= this.getInfoSiteStudentsAndGroupsList().size()) {

				return false;
			}

			
			ListIterator iter1 =
				((InfoSiteStudentsAndGroups) objectToCompare)
					.getInfoSiteStudentsAndGroupsList()
					.listIterator();
			ListIterator iter2 = this.getInfoSiteStudentsAndGroupsList().listIterator();
			while (result && iter1.hasNext()) {
				InfoSiteStudentAndGroup infoSiteStudentAndGroup1 = (InfoSiteStudentAndGroup) iter1.next();
				InfoSiteStudentAndGroup infoSiteStudentAndGroup2 = (InfoSiteStudentAndGroup) iter2.next();
				
				if (!infoSiteStudentAndGroup1.equals(infoSiteStudentAndGroup2)) {
					result = false;
				}
			}
			return result;
		}
}