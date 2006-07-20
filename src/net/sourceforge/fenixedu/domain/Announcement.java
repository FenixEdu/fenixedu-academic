package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Ivo Brand�o
 */
public class Announcement extends Announcement_Base {

    public Announcement() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Announcement(String title, Date creationDate, Date lastModifiedDate, String information,
            Site site) {
    	this();

        if (Math.pow(2, 16) < information.length()) {
            throw new DomainException("information.exceeds.storage.limit", Double.toString(Math.pow(2, 16)));
        }

        setTitle(title);
        setCreationDate(creationDate);
        setLastModifiedDate(lastModifiedDate);
        setInformation(information);
        setSite(site);
    }

    public void edit(final String newAnnouncementTitle, final String newAnnouncementInformation) {
        if (newAnnouncementTitle == null || newAnnouncementInformation == null) {
            throw new NullPointerException();
        }
        if (Math.pow(2, 16) < newAnnouncementInformation.length()) {
            throw new DomainException("information.exceeds.storage.limit", Double.toString(Math.pow(2, 16)));
        }
        setTitle(newAnnouncementTitle);
        setInformation(newAnnouncementInformation);
        setLastModifiedDate(Calendar.getInstance().getTime());
    }

    public void delete() {
        removeSite();        
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
