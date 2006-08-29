package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.DomainReference;

/**
 * @author EP 15
 * @author João Mota
 * @author Ivo Brandão
 */

public class InfoAnnouncement extends InfoObject implements Comparable, ISiteComponent {

    private final DomainReference<Announcement> announcementDomainReference;

    public InfoAnnouncement(final Announcement announcement) {
	   announcementDomainReference = new DomainReference<Announcement>(announcement);
    }

    public Announcement getAnnouncement() {
        return announcementDomainReference == null ? null : announcementDomainReference.getObject();
    }

    public boolean equals(Object obj) {
	return obj != null && getAnnouncement() == ((InfoAnnouncement) obj).getAnnouncement();
    }

    public String toString() {
	return getAnnouncement().toString();
    }

    public Date getCreationDate() {
	return getAnnouncement().getCreationDate();
    }

    public String getInformation() {
	return getAnnouncement().getInformation();
    }

    public InfoSite getInfoSite() {
	return InfoSite.newInfoFromDomain(getAnnouncement().getSite());
    }

    public Date getLastModifiedDate() {
	return getAnnouncement().getLastModifiedDate();
    }

    public String getTitle() {
	return getAnnouncement().getTitle();
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
	return getAnnouncement().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

}