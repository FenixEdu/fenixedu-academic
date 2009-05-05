package net.sourceforge.fenixedu.domain.util.email;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;

public class Sender extends Sender_Base {

    public static Comparator<Sender> COMPARATOR_BY_FROM_NAME = new Comparator<Sender>() {

	@Override
	public int compare(final Sender sender1, final Sender sender2) {
	    final int c = sender1.getFromName().compareTo(sender2.getFromName());
	    return c == 0 ? COMPARATOR_BY_ID.compare(sender1, sender2) : c;
	}

    };

    public Sender() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Sender(final String fromName, final String fromAddress, final Group members) {
	this();
	setFromName(fromName);
	setFromAddress(fromAddress);
	setMembers(members);
    }

    public void delete() {
	for (final Message message : getMessagesSet()) {
	    message.delete();
	}
	for (final Recipient recipient : getRecipientsSet()) {
	    if (recipient.getSendersCount() == 1) {
		recipient.delete();
	    } else {
		removeRecipients(recipient);
	    }
	}
	removeRootDomainObject();
	deleteDomainObject();
    }

    public static Set<Sender> getAvailableSenders() {
	final IUserView userView = UserView.getUser();

	final Set<Sender> senders = new TreeSet<Sender>(Sender.COMPARATOR_BY_FROM_NAME);
	for (final Sender sender : RootDomainObject.getInstance().getUtilEmailSendersSet()) {
	    if (sender.getMembers().allows(userView) || (userView != null && userView.hasRoleType(RoleType.MANAGER))) {
		senders.add(sender);
	    }
	}

	return senders;
    }

    public static boolean userHasRecipients() {
	Set<Sender> senders = getAvailableSenders();
	if (senders != null && !senders.isEmpty()) {
	    for (final Sender sender : senders) {
		if (sender.hasAnyRecipients())
		    return true;
	    }
	}
	return false;
    }

    @Service
    public List<ReplyTo> getConcreteReplyTos() {
	List<ReplyTo> replyTos = new ArrayList<ReplyTo>();
	for (ReplyTo replyTo : getReplyTos()) {
	    if (replyTo instanceof CurrentUserReplyTo) {
		if (AccessControl.getPerson().getReplyTo() == null) {
		    ReplyTo concreteReplyTo = new PersonReplyTo(AccessControl.getPerson());
		    replyTos.add(concreteReplyTo);
		} else {
		    replyTos.add(AccessControl.getPerson().getReplyTo());
		}
	    } else {
		replyTos.add(replyTo);
	    }
	}
	return replyTos;
    }

    public String getFromName(Person person) {
	return getFromName();
    }
}
