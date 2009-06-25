package net.sourceforge.fenixedu.domain.util.email;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.domain.util.EmailAddressList;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class Message extends Message_Base {

    public Message() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Message(final Sender sender, final Collection<? extends ReplyTo> replyTos, final Collection<Recipient> tos,
	    final Collection<Recipient> ccs, final Collection<Recipient> recipientsBccs, final String subject, final String body,
	    final Set<String> bccs) {
	this(sender, replyTos, recipientsBccs, subject, body, bccs);
	if (tos != null) {
	    for (final Recipient recipient : tos) {
		addTos(recipient);
	    }
	}
	if (ccs != null) {
	    for (final Recipient recipient : ccs) {
		addCcs(recipient);
	    }
	}
    }

    public Message(final Sender sender, final Collection<? extends ReplyTo> replyTos, final Collection<Recipient> recipients,
	    final String subject, final String body, final Set<String> bccs) {
	this(sender, replyTos, recipients, subject, body, new EmailAddressList(bccs).toString());
    }

    public Message(final Sender sender, final Collection<? extends ReplyTo> replyTos, final Collection<Recipient> recipients,
	    final String subject, final String body, final String bccs) {
	super();
	final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
	setRootDomainObject(rootDomainObject);
	setRootDomainObjectFromPendingRelation(rootDomainObject);
	setSender(sender);
	if (replyTos != null) {
	    for (final ReplyTo replyTo : replyTos) {
		addReplyTos(replyTo);
	    }
	}
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

    public void safeDelete() {
	if (getSent() == null) {
	    delete();
	}
    }

    public void delete() {
	for (final Recipient recipient : getRecipientsSet()) {
	    removeRecipients(recipient);
	}
	for (final ReplyTo replyTo : getReplyTosSet()) {
	    replyTo.safeDelete();
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

    protected Set<String> getRecipientAddresses(Set<Recipient> recipients) {
	final Set<String> emailAddresses = new HashSet<String>();
	for (final Recipient recipient : recipients) {
	    recipient.addDestinationEmailAddresses(emailAddresses);
	}
	return emailAddresses;
    }

    protected Set<String> getDestinationBccs() {
	final Set<String> emailAddresses = new HashSet<String>();
	if (getBccs() != null && !getBccs().isEmpty()) {
	    for (final String emailAddress : getBccs().replace(',', ' ').replace(';', ' ').split(" ")) {
		final String trimmed = emailAddress.trim();
		if (!trimmed.isEmpty()) {
		    emailAddresses.add(emailAddress);
		}
	    }
	}
	emailAddresses.addAll(getRecipientAddresses(getRecipientsSet()));
	return emailAddresses;
    }

    protected String[] getReplyToAddresses(final Person person) {
	final String[] replyToAddresses = new String[getReplyTosCount()];
	int i = 0;
	for (final ReplyTo replyTo : getReplyTosSet()) {
	    replyToAddresses[i++] = replyTo.getReplyToAddress(person);
	}
	return replyToAddresses;
    }

    public void dispatch() {
	final Sender sender = getSender();
	final Person person = getPerson();
	new Email(sender.getFromName(person), sender.getFromAddress(), getReplyToAddresses(person),
		getRecipientAddresses(getTosSet()), getRecipientAddresses(getCcsSet()), getDestinationBccs(), getSubject(),
		getBody());
	removeRootDomainObjectFromPendingRelation();
	setSent(new DateTime());
    }
}
