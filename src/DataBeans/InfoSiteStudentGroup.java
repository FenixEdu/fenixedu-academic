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
public class InfoSiteStudentGroup implements ISiteComponent {

	private List infoSiteStudentInformationList;

	
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

		
	public boolean equals(Object objectToCompare) {

			boolean result = false;

			if (((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList() == null
				&& this.getInfoSiteStudentInformationList() == null) {
				return true;
			}

			if (((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList() == null
				|| this.getInfoSiteStudentInformationList() == null
				|| ((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList().size()
					!= this.getInfoSiteStudentInformationList().size()) {

				return false;
			}
			ListIterator iter1 =
				((InfoSiteStudentGroup) objectToCompare)
					.getInfoSiteStudentInformationList()
					.listIterator();
			ListIterator iter2 = this.getInfoSiteStudentInformationList().listIterator();
			while (result && iter1.hasNext()) {
				InfoSiteStudentInformation infoSiteStudentInformation1 = (InfoSiteStudentInformation) iter1.next();
				InfoSiteStudentInformation infoSiteStudentInformation2 = (InfoSiteStudentInformation) iter2.next();
				
				if (!infoSiteStudentInformation1.equals(infoSiteStudentInformation2)) {
					result = false;
				}
			}
			return result;
		}
}
