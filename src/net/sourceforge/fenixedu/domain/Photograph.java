package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

import org.joda.time.DateTime;

public class Photograph extends Photograph_Base {
    public Photograph() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setSubmission(new DateTime());
	setRejectedAcknowledged(Boolean.FALSE);
    }

    public Photograph(ContentType contentType, ByteArray content, ByteArray compressed, PhotoType photoType) {
	this();
	setContentType(contentType);
	setRawContent(content);
	setContent(compressed);
	setPhotoType(photoType);
    }

    @Override
    public Person getPerson() {
	if (super.getPerson() != null)
	    return super.getPerson();
	if (hasNext())
	    return getNext().getPerson();
	return null;
    }

    @Override
    public void setPhotoType(PhotoType photoType) {
	super.setPhotoType(photoType);
	if (photoType == PhotoType.INSTITUTIONAL) {
	    setState(PhotoState.APPROVED);
	} else if (photoType == PhotoType.USER) {
	    setState(PhotoState.PENDING);
	} else {
	    setState(PhotoState.PENDING);
	}
    }

    @Override
    public void setState(PhotoState state) {
	super.setState(state);
	setStateChange(new DateTime());
	if (state == PhotoState.PENDING) {
	    setPendingHolder(RootDomainObject.getInstance());
	} else {
	    setPendingHolder(null);
	}
    }

    @Override
    public void setPrevious(Photograph previous) {
	if (previous.getState() == PhotoState.PENDING) {
	    previous.setState(PhotoState.REJECTED);
	    previous.setRejectedAcknowledged(Boolean.TRUE);
	}
	super.setPrevious(previous);
    }

    public byte[] getContents() {
	return this.getContent().getBytes();
    }

    public void delete() {
	this.removePerson();
	removeRootDomainObject();
	if (hasPrevious()) {
	    getPrevious().delete();
	}
	super.deleteDomainObject();
    }
}
