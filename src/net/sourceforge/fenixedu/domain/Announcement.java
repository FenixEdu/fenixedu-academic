/*
 * Announcement.java
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * @author Ivo Brandão
 */
public class Announcement extends Announcement_Base {

    public Announcement() {
    }

    public Announcement(Integer announcementCode) {
        setIdInternal(announcementCode);
    }

    public Announcement(String title, Date creationDate, Date lastModifiedDate, String information,
            ISite site) {

        setTitle(title);
        setCreationDate(creationDate);
        setLastModifiedDate(lastModifiedDate);
        setInformation(information);
        setSite(site);
    }
   
    public Announcement(String title, Date date, ISite site) {
        setTitle(title);
        setCreationDate(date);
        setSite(site);
    }

    public String toString() {
        String result = "[ANNOUNCEMENT";

        result += ", creationDate=" + getCreationDate();
        result += ", lastModifiedDate=" + getLastModifiedDate();
        result += ", information=" + getInformation();
        result += ", site=" + getSite();
        result += "]";
        return result;
    }
}