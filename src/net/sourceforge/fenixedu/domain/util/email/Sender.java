package net.sourceforge.fenixedu.domain.util.email;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;

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
        setRootDomainObject(RootDomainObject.getInstance());
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

}
