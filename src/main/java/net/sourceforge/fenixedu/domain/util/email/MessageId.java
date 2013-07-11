package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class MessageId extends MessageId_Base {

    public MessageId() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setSendTime(new DateTime());
    }

    public MessageId(final Message message, final String messageID) {
        this();
        setMessage(message);
        setId(messageID);
    }

    public void delete() {
        setMessage(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMessage() {
        return getMessage() != null;
    }

    @Deprecated
    public boolean hasSendTime() {
        return getSendTime() != null;
    }

    @Deprecated
    public boolean hasId() {
        return getId() != null;
    }

}
