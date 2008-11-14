package net.sourceforge.fenixedu.domain.util.email;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ReplyTo extends ReplyTo_Base {
    
    public static final Comparator<ReplyTo> COMPARATOR_BY_ADDRESS = new Comparator<ReplyTo>() {

	@Override
	public int compare(final ReplyTo replyTo1, final ReplyTo replyTo2) {
	    return replyTo1.getReplyToAddress().compareTo(replyTo2.getReplyToAddress());
	}
	
    };

    public ReplyTo() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void safeDelete() {
	if (getMessagesSet().isEmpty()) {
	    deleteDomainObject();
	}
	removeSender();
    }

    public void delete() {
	for (final Message message : getMessagesSet()) {
	    removeMessages(message);
	}
	removeSender();
	removeRootDomainObject();
	deleteDomainObject();
    }
    
}
