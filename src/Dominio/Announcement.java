/*
 * Advertisement.java
 * Mar 10, 2003
 */
package Dominio;

/**
 * @author Ivo Brandão
 */
public class Announcement implements IAnnouncement {

	private Integer internalCode;
	private String title;
	private Long date;
	private Long lastModifiedDate;
	private String information;
	private ISite site;
	private Integer keySite; 

	/** 
	 * Construtor
	 */
	public Announcement() {}

	/** 
	 * Construtor
	 */
	public Announcement(Integer internalCode, String title, Long date, 
		Long lastModifiedDate, String information, ISite site, Integer keySite) {
			
		this.internalCode = internalCode;
		this.title = title;
		this.date = date;
		this.lastModifiedDate = lastModifiedDate;
		this.information = information;
		this.site = site;
		this.keySite = keySite;
	}
	
	/**
	 * @return Long
	 */
	public Long getDate() {
		return date;
	}

	/**
	 * @return String
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * @return Integer
	 */
	public Integer getKeySite() {
		return keySite;
	}

	/**
	 * @return Long
	 */
	public Long getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @return ISite
	 */
	public ISite getSite() {
		return site;
	}

	/**
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the date.
	 * @param date The date to set
	 */
	public void setDate(Long date) {
		this.date = date;
	}

	/**
	 * Sets the information.
	 * @param information The information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * Sets the keySite.
	 * @param keySite The keySite to set
	 */
	public void setKeySite(Integer keySite) {
		this.keySite = keySite;
	}

	/**
	 * Sets the lastModifiedDate.
	 * @param lastModifiedDate The lastModifiedDate to set
	 */
	public void setLastModifiedDate(Long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * Sets the site.
	 * @param site The site to set
	 */
	public void setSite(ISite site) {
		this.site = site;
	}

	/**
	 * Sets the title.
	 * @param title The title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IAnnouncement) {
			result = (getTitle().equals(((IAnnouncement) arg0).getTitle()))&&
				(getDate().equals(((IAnnouncement) arg0).getDate()))&&
				(getLastModifiedDate().equals(((IAnnouncement) arg0).getLastModifiedDate()))&&
				(getSite().equals(((IAnnouncement) arg0).getSite()))&&				
				(getInformation().equals(((IAnnouncement) arg0).getInformation()));
		} 
		return result;		
	}

}
