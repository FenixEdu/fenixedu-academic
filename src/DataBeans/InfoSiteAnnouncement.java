/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author João Mota
 *
 * 
 */
public class InfoSiteAnnouncement extends DataTranferObject implements ISiteComponent {

	private List announcements;

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFOSITEANNOUNCEMENT";
		result += ", announcements=" + getAnnouncements();
		result += "]";
		return result;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoSiteAnnouncement) {
			result = true;
		}

		if (((InfoSiteAnnouncement) obj).getAnnouncements() == null && this.getAnnouncements() == null) {
			return true;
		}
		if (((InfoSiteAnnouncement) obj).getAnnouncements() == null
			|| this.getAnnouncements() == null
			|| ((InfoSiteAnnouncement) obj).getAnnouncements().size() != this.getAnnouncements().size()) {
			return false;
		} 
//		if (((InfoSiteAnnouncement) obj).getAnnouncements().containsAll(this.getAnnouncements())
//			&& this.getAnnouncements().containsAll(((InfoSiteAnnouncement) obj).getAnnouncements())){
//				result = true;
//			}
			
			
			ListIterator iter1 = ((InfoSiteAnnouncement) obj).getAnnouncements().listIterator();
		ListIterator iter2 = this.getAnnouncements().listIterator();
		while (result && iter1.hasNext()) {
			InfoAnnouncement infoAnnouncement1 = (InfoAnnouncement) iter1.next();
			InfoAnnouncement infoAnnouncement2 = (InfoAnnouncement) iter2.next();
			if (!infoAnnouncement1.equals(infoAnnouncement2)) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * @return
	 */
	public List getAnnouncements() {
		return announcements;
	}

	/**
	 * @param list
	 */
	public void setAnnouncements(List list) {
		announcements = list;
	}

}
