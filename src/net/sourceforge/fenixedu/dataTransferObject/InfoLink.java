/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.io.UnsupportedEncodingException;

import net.sourceforge.fenixedu._development.PropertiesManager;

/**
 * fenix-head DataBeans
 * 
 * @author João Mota 17/Set/2003
 * 
 */
public class InfoLink extends DataTranferObject {
	private String linkName;

	private String link;

	/**
	 * @return
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 */
	public void setLink(String link) {
		try {
			this.link = new String(link.getBytes(PropertiesManager.DEFAULT_CHARSET), PropertiesManager.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			this.link = link;
		}

	}

	/**
	 * @return
	 */
	public String getLinkName() {
		return linkName;
	}

	/**
	 * @param linkName
	 */
	public void setLinkName(String linkName) {
		try {
			this.linkName = new String(linkName.getBytes(PropertiesManager.DEFAULT_CHARSET), PropertiesManager.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			this.linkName = linkName;
		}

	}

}