package net.sourceforge.fenixedu.domain.util.email;

import pt.ist.bennu.core.domain.Bennu;

import org.joda.time.DateTime;

public class MessageId extends MessageId_Base {

    public MessageId() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
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
