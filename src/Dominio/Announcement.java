/*
 * Announcement.java
 */
package Dominio;

import java.sql.Timestamp;

/**
 * @author Ivo Brandão
 */
public class Announcement implements IAnnouncement {

	private Integer internalCode;
	private String title;
	private Timestamp creationDate;
	private Timestamp lastModifiedDate;
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
	public Announcement(String title, Timestamp date, 
		Timestamp lastModifiedDate, String information, ISite site) {
			
		this.title = title;
		this.creationDate = date;
		this.lastModifiedDate = lastModifiedDate;
		this.information = information;
		this.site = site;
	}
	
	/** 
	 * Construtor
	 */
	public Announcement(String title, Timestamp date, ISite site) {
			
		this.title = title;
		this.creationDate = date;
		this.site = site;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IAnnouncement) {
			result = (getTitle().equals(((IAnnouncement) arg0).getTitle()))&&
				(getCreationDate().equals(((IAnnouncement) arg0).getCreationDate()))&&
				(getLastModifiedDate().equals(((IAnnouncement) arg0).getLastModifiedDate()))&&
				(getSite().equals(((IAnnouncement) arg0).getSite()))&&				
				(getInformation().equals(((IAnnouncement) arg0).getInformation()));
		} 
		return result;		
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[ANNOUNCEMENT";
		result += ", codInt=" + getInternalCode();
		result += ", creationDate=" + getCreationDate();
		result += ", lastModifiedDate=" + getLastModifiedDate();
		result += ", information=" + getInformation();
//		result += ", site=" + getSite();
		result += "]";
		return result;
	}

	/**
	 * @return Timestamp
	 */
	public Timestamp getCreationDate() {
		return creationDate;
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
	 * @return Timestamp
	 */
	public Timestamp getLastModifiedDate() {
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
	 * Sets the creationDate.
	 * @param creationDate The creationDate to set
	 */
	public void setCreationDate(Timestamp creationDate) {
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
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
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

}
