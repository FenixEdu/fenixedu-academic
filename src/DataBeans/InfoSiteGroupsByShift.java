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

public class InfoSiteGroupsByShift implements ISiteComponent{

	private InfoShift infoShift;
	private List infoStudentGroupsList;

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFO_SITE_GROUPS_BY_SHIFT";
		result += " infoShift=" + getInfoShift();
		result += ", infoStudentGroupsList=" + getInfoStudentGroupsList();
		result += "]";
		return result;
	}

	public boolean equals(Object objectToCompare) {
		boolean result = false;
		if (objectToCompare instanceof InfoSiteSections
			&& (((((InfoSiteGroupsByShift) objectToCompare).getInfoShift() != null
				&& this.getInfoShift() != null
				&& ((InfoSiteGroupsByShift) objectToCompare).getInfoShift().equals(
					this.getInfoShift()))
				|| ((InfoSiteGroupsByShift) objectToCompare).getInfoShift() == null
				&& this.getInfoShift() == null))) {
			result = true;
		}

		if (((InfoSiteGroupsByShift) objectToCompare).getInfoStudentGroupsList() == null
			&& this.getInfoStudentGroupsList() == null
			&& result == true) {
			return true;
		}
		if (((InfoSiteGroupsByShift) objectToCompare).getInfoStudentGroupsList() == null
			|| this.getInfoStudentGroupsList() == null
			|| ((InfoSiteGroupsByShift) objectToCompare).getInfoStudentGroupsList().size()
				!= this.getInfoStudentGroupsList().size()) {

			return false;
		}
		ListIterator iter1 =
			((InfoSiteGroupsByShift) objectToCompare).getInfoStudentGroupsList().listIterator();
		ListIterator iter2 = this.getInfoStudentGroupsList().listIterator();
		while (result && iter1.hasNext()) {
			Integer groupNumber1 = (Integer) iter1.next();
			Integer groupNumber2 = (Integer) iter2.next();
			if (!groupNumber1.equals(groupNumber2)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * @return
	 */
	public InfoShift getInfoShift() {
		return infoShift;
	}

	/**
	 * @param infoShift
	 */
	public void setInfoShift(InfoShift infoShift) {
		this.infoShift = infoShift;
	}
	/**
		 * @return
		 */
	public List getInfoStudentGroupsList() {
		return infoStudentGroupsList;
	}

	/**
	 * @param list
	 */
	public void setInfoStudentGroupsList(List infoStudentGroupsList) {
		this.infoStudentGroupsList = infoStudentGroupsList;
	}
}
