/*
 * Created on 17/Set/2003
 *
 */
package DataBeans;

import java.io.UnsupportedEncodingException;
import java.io.Serializable;

/**
 *fenix-head
 *DataBeans
 * @author João Mota
 *17/Set/2003
 *
 */
public class InfoLink implements Serializable {
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
			this.link = new String(link.getBytes("ISO-8859-1"), "ISO-8859-1");
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
			this.linkName =
				new String(linkName.getBytes("ISO-8859-1"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			this.linkName = linkName;
		}

	}

}
