package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Announcement;

/**
 * @author EP 15
 * @author João Mota
 * @author Ivo Brandão
 */

public class InfoAnnouncement extends InfoObject implements Comparable, ISiteComponent {

    private final Announcement announcement;

    public InfoAnnouncement(final Announcement announcement) {
	this.announcement = announcement;
    }

    public boolean equals(Object obj) {
	return obj != null && announcement == ((InfoAnnouncement) obj).announcement;
    }

    public String toString() {
	return announcement.toString();
    }

    public Date getCreationDate() {
	return announcement.getCreationDate();
    }

    public String getInformation() {
	return announcement.getInformation();
    }

    public InfoSite getInfoSite() {
	return InfoSite.newInfoFromDomain(announcement.getSite());
    }

    public Date getLastModifiedDate() {
	return announcement.getLastModifiedDate();
    }

    public String getTitle() {
	return announcement.getTitle();
    }

    public int compareTo(Object arg0) {
	if (getYoungerDate().after(((InfoAnnouncement) arg0).getYoungerDate())) {
	    return -1;
	}
	if (getYoungerDate().before(((InfoAnnouncement) arg0).getYoungerDate())) {
	    return 1;
	}
	return 0;

    }

    public String getCreationDateFormatted() {
	String result = getCreationDate().toString();
	return result.substring(0, result.indexOf("."));
    }

    public String getLastModifiedDateFormatted() {
	String result = getLastModifiedDate().toString();
	return result.substring(0, result.indexOf("."));
    }

    private Date getYoungerDate() {
	if (getLastModifiedDate() != null && getLastModifiedDate().after(getCreationDate())) {
	    return getLastModifiedDate();
	}
	return getCreationDate();

    }

    public static InfoAnnouncement newInfoFromDomain(Announcement announcement) {
	return announcement == null ? null : new InfoAnnouncement(announcement);
    }

    @Override
    public Integer getIdInternal() {
	return announcement.getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

}