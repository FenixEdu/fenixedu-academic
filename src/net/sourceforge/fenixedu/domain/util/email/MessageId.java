package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class MessageId extends MessageId_Base {

    public MessageId() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public MessageId(final Message message, final String messageID) {
	this();
	setMessage(message);
	setId(messageID);
    }

    public void delete() {
	removeMessage();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
