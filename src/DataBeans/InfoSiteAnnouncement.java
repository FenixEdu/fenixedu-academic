/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

import java.util.List;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
