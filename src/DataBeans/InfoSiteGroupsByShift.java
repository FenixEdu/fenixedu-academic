/*
 * Created on 4/Ago/2003
 *
 */
package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author asnr and scpo
 *
 */

public class InfoSiteGroupsByShift extends DataTranferObject implements ISiteComponent {

	private List infoSiteStudentGroupsList;
	private InfoSiteShift infoSiteShift;

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFO_SITE_GROUPS_BY_SHIFT";
		result += ", infoStudentGroupsList=" + getInfoSiteStudentGroupsList();
		result += ", infoSiteShift=" + getInfoSiteShift();

		result += "]";
		return result;
	}

	public boolean equals(Object objectToCompare) {
		boolean result = false;
		if (objectToCompare instanceof InfoSiteGroupsByShift) {

			result = (this.getInfoSiteShift().equals(((InfoSiteGroupsByShift) objectToCompare).getInfoSiteShift()));
		}

		if (((InfoSiteGroupsByShift) objectToCompare).getInfoSiteStudentGroupsList() == null
			&& this.getInfoSiteStudentGroupsList() == null
			&& result == true) {
			return true;
		}
		if (((InfoSiteGroupsByShift) objectToCompare).getInfoSiteStudentGroupsList() == null
			|| this.getInfoSiteStudentGroupsList() == null
			|| ((InfoSiteGroupsByShift) objectToCompare).getInfoSiteStudentGroupsList().size()
				!= this.getInfoSiteStudentGroupsList().size()) {
			return false;
		}

		ListIterator iter1 = ((InfoSiteGroupsByShift) objectToCompare).getInfoSiteStudentGroupsList().listIterator();
		ListIterator iter2 = this.getInfoSiteStudentGroupsList().listIterator();
		while (result && iter1.hasNext()) {
			InfoSiteStudentGroup infoSiteStudentGroup1 = (InfoSiteStudentGroup) iter1.next();
			InfoSiteStudentGroup infoSiteStudentGroup2 = (InfoSiteStudentGroup) iter2.next();
			if (!infoSiteStudentGroup1.equals(infoSiteStudentGroup2)) {
				result = false;
			}
		}
		return result;
	}

	/**
		 * @return
		 */
	public List getInfoSiteStudentGroupsList() {
		return infoSiteStudentGroupsList;
	}

	/**
	 * @param list
	 */
	public void setInfoSiteStudentGroupsList(List infoStudentGroupsList) {
		this.infoSiteStudentGroupsList = infoStudentGroupsList;
	}

	/**
			 * @return
			 */
	public InfoSiteShift getInfoSiteShift() {
		return infoSiteShift;
	}

	/**
	 * @param list
	 */
	public void setInfoSiteShift(InfoSiteShift infoSiteShift) {
		this.infoSiteShift = infoSiteShift;
	}

}
