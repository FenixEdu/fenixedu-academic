/*
 * Announcement.java
 */
package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;

/**
 * @author Ivo Brandão
 */
public class Announcement extends Announcement_Base {
    private Timestamp creationDate;
    private Timestamp lastModifiedDate;

    
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

        setTitle(title);
        setCreationDate(date);
        setLastModifiedDate(lastModifiedDate);
        setInformation(information);
        setSite(site);
    }

    /**
     * Construtor
     */
    public Announcement(String title, Timestamp date, ISite site) {

        setTitle(title);
        setCreationDate(date);
        setSite(site);
        
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

    public Timestamp getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}