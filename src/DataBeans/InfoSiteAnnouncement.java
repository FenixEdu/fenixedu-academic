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
public class InfoSiteAnnouncement implements ISiteComponent {

	private List announcements;

	public boolean equals(Object siteView) {
		boolean result = false;
		if (siteView instanceof InfoSiteAnnouncement) {
			result = true;
		}

		if (((InfoSiteAnnouncement) siteView).getAnnouncements() == null && this.getAnnouncements() == null) {
			return true;
		}
		if (((InfoSiteAnnouncement) siteView).getAnnouncements() == null
			|| this.getAnnouncements() == null
			|| ((InfoSiteAnnouncement) siteView).getAnnouncements().size() != this.getAnnouncements().size()) {
			return false;
		}
		ListIterator iter1 = ((InfoSiteAnnouncement) siteView).getAnnouncements().listIterator();
		ListIterator iter2 = this.getAnnouncements().listIterator();
		while (result && iter1.hasNext()) {
			InfoAnnouncement infoAnnouncement1 = (InfoAnnouncement) iter1.next();
			InfoAnnouncement infoAnnouncement2 = (InfoAnnouncement) iter2.next();
			if(!infoAnnouncement1.equals(infoAnnouncement2)){
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
