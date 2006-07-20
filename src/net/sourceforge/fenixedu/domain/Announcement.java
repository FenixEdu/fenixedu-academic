package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class Announcement extends Announcement_Base {

    protected Announcement() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Announcement(final String title, final DateTime creationDate, final String information, final Site site) {
    	this();
        setSite(site);
        setCreationDateDateTime(creationDate);
        edit(title, information, creationDate);
    }


    public void edit(final String title, final String information) {
        edit(title, information, new DateTime());
    }

    private void edit(final String title, final String information, final DateTime lastModifiedDate) {
        if (title == null || information == null) {
            throw new NullPointerException();
        }
        if (Math.pow(2, 16) < information.length()) {
            throw new DomainException("information.exceeds.storage.limit", Integer.toString(Double.valueOf(Math.pow(2, 16)).intValue()));
        }
        setTitle(title);
        setInformation(information);
        setLastModifiedDateDateTime(lastModifiedDate);
    }

    public void delete() {
        removeSite();        
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
