/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *
 * 
 */
public class InfoSiteAnnouncement implements ISiteComponent {

	private List announcements;

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
