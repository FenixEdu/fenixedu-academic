/*
 * Announcement.java
 */
package Dominio;

import java.sql.Timestamp;

/**
 * @author Ivo Brandão
 */
public class Announcement extends DomainObject implements IAnnouncement {

    private String title;

    private Timestamp creationDate;

    private Timestamp lastModifiedDate;

    private String information;

    private ISite site;

    private Integer keySite;

    /**
     * Construtor
     */
    public Announcement() {
    }

    /**
     * Construtor
     */
    public Announcement(Integer announcementCode) {
        setIdInternal(announcementCode);
    }

    /**
     * Construtor
     */
    public Announcement(String title, Timestamp date, Timestamp lastModifiedDate, String information,
            ISite site) {

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
    //	public boolean equals(Object arg0) {
    //		boolean result = false;
    //		if (arg0 instanceof IAnnouncement
    //			&& (((((Announcement) arg0).getCreationDate() != null
    //				&& this.getCreationDate() != null
    //				&& ((Announcement) arg0).getCreationDate().equals(
    //					this.getCreationDate()))
    //				|| ((Announcement) arg0).getCreationDate() == null
    //				&& this.getCreationDate() == null))
    //			&& (((((Announcement) arg0).getSite() != null
    //				&& this.getSite() != null
    //				&& ((Announcement) arg0).getSite().equals(this.getSite()))
    //				|| ((Announcement) arg0).getSite() == null
    //				&& this.getSite() == null))
    //			&& (((Announcement) arg0).getTitle() != null
    //				&& this.getTitle() != null
    //				&& this.getTitle().equals(((Announcement) arg0).getTitle()))) {
    //			result = true;
    //		}
    //		return result;
    //	}
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[ANNOUNCEMENT";

        result += ", creationDate=" + getCreationDate();
        result += ", lastModifiedDate=" + getLastModifiedDate();
        result += ", information=" + getInformation();
        result += ", site=" + getSite();
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
     * 
     * @param creationDate
     *            The creationDate to set
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Sets the information.
     * 
     * @param information
     *            The information to set
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * Sets the keySite.
     * 
     * @param keySite
     *            The keySite to set
     */
    public void setKeySite(Integer keySite) {
        this.keySite = keySite;
    }

    /**
     * Sets the lastModifiedDate.
     * 
     * @param lastModifiedDate
     *            The lastModifiedDate to set
     */
    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Sets the site.
     * 
     * @param site
     *            The site to set
     */
    public void setSite(ISite site) {
        this.site = site;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

}