package net.sourceforge.fenixedu.domain.util.email;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class Message extends Message_Base {
    
    public Message() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Message(final Sender sender, final Collection<Recipient> recipients, final String subject, final String body, final String bccs) {
        super();
        final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
        setRootDomainObject(rootDomainObject);
        setRootDomainObjectFromPendingRelation(rootDomainObject);
        setSender(sender);
        if (recipients != null) {
            for (final Recipient recipient : recipients) {
        	addRecipients(recipient);
            }
        }
        setSubject(subject);
        setBody(body);
        setBccs(bccs);
        final Person person = AccessControl.getPerson();
        setPerson(person);
        setCreated(new DateTime());
    }

    @Service
    public void safeDelete() {
	if (getSent() == null) {
	    delete();
	}
    }

    public void delete() {
	for (final Recipient recipient : getRecipientsSet()) {
	    removeRecipients(recipient);
	}
	removeSender();
	removePerson();
	removeRootDomainObjectFromPendingRelation();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public String getRecipientsAsText() {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final Recipient recipient : getRecipientsSet()) {
	    if (stringBuilder.length() > 0) {
		stringBuilder.append("\n");
	    }
	    stringBuilder.append(recipient.getToName());
	}
	if (getBccs() != null && !getBccs().isEmpty()) {
	    if (stringBuilder.length() > 0) {
		stringBuilder.append("\n");
	    }
	    stringBuilder.append(getBccs());
	}
	return stringBuilder.toString();
    }

    protected Set<String> getDestinationEmailAddresses() {
	final Set<String> emailAddresses = new HashSet<String>();
	if (getBccs() != null && !getBccs().isEmpty()) {
	    for (final String emailAddress : getBccs().replace(',', ' ').replace(';', ' ').split(" ")) {
		final String trimmed = emailAddress.trim();
		if (!trimmed.isEmpty()) {
		    emailAddresses.add(emailAddress);
		}
	    }
	}
	for (final Recipient recipient : getRecipientsSet()) {
	    recipient.addDestinationEmailAddresses(emailAddresses);
	}
	return emailAddresses;
    }

    public void dispatch() {
	final Sender sender = getSender();
	new Email(sender.getFromName(), sender.getFromAddress(), new String[0], Collections.EMPTY_SET,
		Collections.EMPTY_SET, getDestinationEmailAddresses(), getSubject(), getBody());
	removeRootDomainObjectFromPendingRelation();
	setSent(new DateTime());
    }

}
