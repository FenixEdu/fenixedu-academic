package DataBeans.gesdis;

import java.util.Date;

/**
 * @author EP 15
 * @author jmota
 * @author Ivo Brandão
 */

public class InfoAnnouncement {

	private String title;
	private Date creationDate;
	private Date lastModificationDate;
	private String information;
	private InfoSite site;	

	public InfoAnnouncement() {
	}

	public InfoAnnouncement(String title, Date creationDate, Date lastModifiedDate, String information, InfoSite site) {
		this.title = title;
		this.creationDate = creationDate;
		this.lastModificationDate = lastModifiedDate;
		this.information = information;
		this.site = site;
	}

	public boolean equals(Object obj) {

		boolean resultado = false;
		if (obj != null && obj instanceof InfoAnnouncement) {
			resultado =
				getTitle().equals(((InfoAnnouncement) obj).getTitle())
					&& getCreationDate().equals(((InfoAnnouncement) obj).getCreationDate())
					&& getLastModificationDate().equals(((InfoAnnouncement) obj).getLastModificationDate())
					&& getInformation().equals(((InfoAnnouncement) obj).getInformation())
					&& getSite().equals(((InfoAnnouncement) obj).getSite());
		}
		return resultado;
	}


	 public String toString() {
	    String result = "[INFOANNONCEMENT";
	    result += ", title=" + getTitle();
	    result += ", information=" + getInformation();
		result += ", creationDate=" + getCreationDate();
		result += ", lastModificationDate=" + getLastModificationDate();
		result += ", site=" + getSite();
	    result += "]";
	    return result;
	}

	/**
	 * @return Date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @return String
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @return Date
	 */
	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	/**
	 * @return InfoSite
	 */
	public InfoSite getSite() {
		return site;
	}

	/**
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the creationDate.
	 * @param creationDate The creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Sets the information.
	 * @param information The information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}

	/**
	 * Sets the lastModificationDate.
	 * @param lastModificationDate The lastModificationDate to set
	 */
	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	/**
	 * Sets the site.
	 * @param site The site to set
	 */
	public void setSite(InfoSite site) {
		this.site = site;
	}

	/**
	 * Sets the title.
	 * @param title The title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
