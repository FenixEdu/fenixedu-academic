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
public class InfoSiteAllGroups implements ISiteComponent {

	private List infoSiteGroupsByShiftList;
	
	/**
	* @return
	*/
	public List getInfoSiteGroupsByShiftList() {
		return infoSiteGroupsByShiftList;
	}

	/**
	 * @param list
	 */
	public void setInfoSiteGroupsByShiftList(List infoSiteGroupsByShiftList) {
		this.infoSiteGroupsByShiftList = infoSiteGroupsByShiftList;
	}
	
	public boolean equals(Object objectToCompare) {
			boolean result = true;
			
			if(objectToCompare instanceof InfoSiteAllGroups)
				result = true;
				
			if (((InfoSiteAllGroups) objectToCompare).getInfoSiteGroupsByShiftList() == null
				&& this.getInfoSiteGroupsByShiftList() == null) {
				return true;
			}

			if (((InfoSiteAllGroups) objectToCompare).getInfoSiteGroupsByShiftList() == null
				|| this.getInfoSiteGroupsByShiftList() == null
				|| ((InfoSiteAllGroups) objectToCompare).getInfoSiteGroupsByShiftList().size()
					!= this.getInfoSiteGroupsByShiftList().size()) {

				return false;
			}

			
			ListIterator iter1 =
				((InfoSiteAllGroups) objectToCompare)
					.getInfoSiteGroupsByShiftList()
					.listIterator();
			ListIterator iter2 = this.getInfoSiteGroupsByShiftList().listIterator();
			while (result && iter1.hasNext()) {
				InfoSiteGroupsByShift infoSiteGroupsByShift1 = (InfoSiteGroupsByShift) iter1.next();
				InfoSiteGroupsByShift infoSiteGroupsByShift2 = (InfoSiteGroupsByShift) iter2.next();
				
				if (!infoSiteGroupsByShift1.equals(infoSiteGroupsByShift2)) {
					result = false;
				}
			}
			return result;
		}
}
