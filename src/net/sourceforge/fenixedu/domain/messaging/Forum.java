package net.sourceforge.fenixedu.domain.messaging;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.DateOrderedNode;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public abstract class Forum extends Forum_Base {

    public Forum() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Forum(MultiLanguageString name, MultiLanguageString description) {
        this();
        init(name, description);
    }

    public void init(MultiLanguageString name, MultiLanguageString description) {
        setCreationDate(new DateTime());
        setName(name);
        setDescription(description);
    }

    public boolean hasConversationThreadWithSubject(MultiLanguageString subject) {
        ConversationThread conversationThread = getConversationThreadBySubject(subject);

        return (conversationThread != null) ? true : false;
    }

    public ConversationThread getConversationThreadBySubject(MultiLanguageString subject) {
        for (ConversationThread conversationThread : getConversationThreads()) {
            if (conversationThread.getTitle().equalInAnyLanguage(subject)) {
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

    public void checkIfCanAddConversationThreadWithSubject(MultiLanguageString subject) {
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

    public ConversationThread createConversationThread(Person creator, MultiLanguageString subject) {
        checkIfPersonCanWrite(creator);
        checkIfCanAddConversationThreadWithSubject(subject);

        return new ConversationThread(this, creator, subject);
    }

    public List<ConversationThread> getConversationThreads() {
	List <ConversationThread>  conversationThreads  = new ArrayList<ConversationThread>();
	for(Node node : getChildrenSet()) {
	    conversationThreads.add((ConversationThread) node.getChild());
	}
	return conversationThreads;
    }
    
    public void addConversationThreads(ConversationThread conversationThread) {
	conversationThread.setForum(this);
    }
    
    public void removeConversationThreads(ConversationThread conversationThread) {
	getConversationThreads().remove(conversationThread);
    }
    
    public int getConversationThreadsCount() {
	return getChildrenCount();
    }
    
    @Override
    protected Node createChildNode(Content childContent) {
        return new DateOrderedNode(this, childContent, Boolean.FALSE);
    }
    
    public abstract Group getReadersGroup();

    public abstract Group getWritersGroup();

    public abstract Group getAdminGroup();
}
