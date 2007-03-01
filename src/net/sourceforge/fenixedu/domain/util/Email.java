package net.sourceforge.fenixedu.domain.util;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Email extends Email_Base {

    public Email(final String fromName, final String fromAddress, final Collection<String> replyTos,
	    final Collection<String> toAddresses, final Collection<String> ccAddresses,
	    final Collection<String> bccAddresses, final String subject, final String body) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setFromName(fromName);
	setFromAddress(fromAddress);
	setReplyTos(new EmailAddressList(replyTos));
	setToAddresses(new EmailAddressList(toAddresses));
	setCcAddresses(new EmailAddressList(ccAddresses));
	setBccAddresses(new EmailAddressList(bccAddresses));
	setSubject(subject);
	setBody(body);
    }

    public Email() {
        super();
    }

    public void delete() {
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public String[] replyTos() {
	return getToAddresses().toArray();
    }

    public Collection<String> toAddresses() {
	return getToAddresses().toCollection();
    }

    public Collection<String> ccAddresses() {
	return getToAddresses().toCollection();
    }

    public Collection<String> bccAddresses() {
	return getToAddresses().toCollection();
    }

}
