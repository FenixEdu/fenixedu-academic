/*
 * Created on 17/Set/2003
 *
 */
package DataBeans;

/**
 *fenix-head
 *DataBeans
 * @author João Mota
 *17/Set/2003
 *
 */
public class InfoLink {
//note: this class does not extends InfoObject because it does not has a counterpart in the Domain. its just a DTO
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
		this.link = link;
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
		this.linkName = linkName;
	}

}
