/*
 * Created on 08/Set/2003
 *
 * 
 */
package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author asnr and scpo
 *
 * 
 */
public class InfoSiteProjectShifts extends DataTranferObject implements ISiteComponent {

	private List infoSiteShifts;
	

	/**
	 * @return
	 */
	public List getInfoSiteShifts() {
		return infoSiteShifts;
	}

	/**
	 * @param list
	 */
	public void setInfoSiteShifts(List infoSiteShifts) {
		this.infoSiteShifts = infoSiteShifts;
	}


	public boolean equals(Object objectToCompare) {
			boolean result = false;
			if (objectToCompare instanceof InfoSiteProjectShifts)
				result = true;

			if (((InfoSiteProjectShifts) objectToCompare).getInfoSiteShifts() == null
				&& this.getInfoSiteShifts() == null) {
				return true;
			}

			if (((InfoSiteProjectShifts) objectToCompare).getInfoSiteShifts() == null
				|| this.getInfoSiteShifts() == null
				|| ((InfoSiteProjectShifts) objectToCompare).getInfoSiteShifts().size()
					!= this.getInfoSiteShifts().size()) {

				return false;
			}

			ListIterator iter1 = ((InfoSiteProjectShifts) objectToCompare).getInfoSiteShifts().listIterator();
			ListIterator iter2 = this.getInfoSiteShifts().listIterator();
			while (result && iter1.hasNext()) {
				InfoSiteShift infoSiteShift1 = (InfoSiteShift) iter1.next();
				InfoSiteShift infoSiteShift2 = (InfoSiteShift) iter2.next();

				if (!infoSiteShift1.equals(infoSiteShift2)) {
					result = false;
				}
			}
			return result;
		}


}
