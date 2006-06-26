package net.sourceforge.fenixedu.domain.messaging;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class Forum extends Forum_Base {

    public Forum() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Forum(String name, String description) {
        this();
        init(name, description);
    }

    public void init(String name, String description) {
        setCreationDate(Calendar.getInstance().getTime());
        setName(name);
        setDescription(description);
    }

    public boolean hasConversationThreadWithSubject(String subject) {
        ConversationThread conversationThread = getConversationThreadBySubject(subject);

        return (conversationThread != null) ? true : false;
    }

    public ConversationThread getConversationThreadBySubject(String subject) {
        for (ConversationThread conversationThread : getConversationThreads()) {
            if (conversationThread.getSubject().equalsIgnoreCase(subject)) {
                return conversationThread;
            }
        }

        return null;
    }

    public void checkIfPersonCanWrite(Person person) {
        if (!getWritersGroup().isMember(person)) {
            throw new DomainException("forum.person.cannot.write");
        }
    }

    public void checkIfCanAddConversationThreadWithSubject(String subject) {
        if (hasConversationThreadWithSubject(subject)) {
            throw new DomainException("forum.already.existing.conversation.thread");
        }
    }

    public int getConversationMessagesCount() {
        int total = 0;

        for (ConversationThread conversationThread : getConversationThreads()) {
            total += conversationThread.getConversationMessagesCount();
        }

        return total;
    }

    public void delete() {
        for (; !getConversationThreads().isEmpty(); getConversationThreads().get(0).delete())
            ;

        for (; !getForumSubscriptions().isEmpty(); getForumSubscriptions().get(0).delete())
            ;

        removeRootDomainObject();
        deleteDomainObject();

    }

    public void addEmailSubscriber(Person person) {
        if (!getReadersGroup().isMember(person)) {
            throw new DomainException("forum.cannot.subscribe.person.because.does.not.belong.to.readers");
        }

        ForumSubscription subscription = getPersonSubscription(person);
        if (subscription == null) {
            subscription = new ForumSubscription(person, this);
        }

        subscription.setReceivePostsByEmail(true);

    }

    public void removeEmailSubscriber(Person person) {
        ForumSubscription subscription = getPersonSubscription(person);

        if (subscription != null) {
            if (subscription.getFavorite() == false) {
                removeForumSubscriptions(subscription);
                subscription.delete();
            } else {
                subscription.setReceivePostsByEmail(false);
            }
        }

    }

    public ForumSubscription getPersonSubscription(Person person) {
        for (ForumSubscription subscription : getForumSubscriptions()) {
            if (subscription.getPerson() == person) {
                return subscription;
            }
        }

        return null;
    }

    public boolean isPersonReceivingMessagesByEmail(Person person) {
        ForumSubscription subscription = getPersonSubscription(person);

        return (subscription != null) ? subscription.getReceivePostsByEmail() : false;
    }

    public ConversationThread createConversationThread(Person creator, String subject) {
        checkIfPersonCanWrite(creator);
        checkIfCanAddConversationThreadWithSubject(subject);

        return new ConversationThread(this, creator, subject);
    }

    public abstract Group getReadersGroup();

    public abstract Group getWritersGroup();

    public abstract Group getAdminGroup();
}
