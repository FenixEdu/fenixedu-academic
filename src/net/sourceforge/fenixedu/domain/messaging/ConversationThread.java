package net.sourceforge.fenixedu.domain.messaging;

import java.util.Calendar;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;

public class ConversationThread extends ConversationThread_Base {

    public static final Comparator CONVERSATION_THREAD_COMPARATOR_BY_CREATION_DATE = new BeanComparator(
            "creationDate");

    public ConversationThread() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCreationDate(Calendar.getInstance().getTime());
    }

    public ConversationThread(Forum forum, Person creator, String subject) {
        this();
        init(forum, creator, subject);
    }

    private void init(Forum forum, Person creator, String subject) {
        setForum(forum);
        setSubject(subject);
        setCreator(creator);

    }

    @Override
    public void setForum(Forum forum) {
        if (forum == null) {
            throw new DomainException("conversationThread.forum.cannot.be.null");
        }

        super.setForum(forum);
    }

    @Override
    public void setSubject(String subject) {
        if (subject == null) {
            throw new DomainException("conversationThread.subject.cannot.be.null");
        }

        super.setSubject(subject);
    }

    @Override
    public void setCreator(Person creator) {
        if (creator == null) {
            throw new DomainException("conversationThread.creator.cannot.be.null");
        }

        super.setCreator(creator);
    }

    public void delete() {
        for (; !getConversationMessages().isEmpty(); getConversationMessages().get(0).delete())
            ;

        removeForum();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    @Override
    public void removeForum() {
        super.setForum(null);
    }

    public void checkIfPersonCanWrite(Person person) {
        getForum().checkIfPersonCanWrite(person);
    }

    public ConversationMessage createConversationMessage(Person creator, String body) {
        checkIfPersonCanWrite(creator);
        ConversationMessage conversationMessage = new ConversationMessage(this, creator, body);

        return conversationMessage;
    }

    public ConversationMessage getNextToLastConversationMessage() {
        ConversationMessage lastConversationMessage = null;
        ConversationMessage nextToLastConversationMessage = null;

        for (ConversationMessage conversationMessage : getConversationMessages()) {
            if (lastConversationMessage == null) {
                lastConversationMessage = conversationMessage;
            } else if (conversationMessage.getCreationDateDateTime().compareTo(
                    lastConversationMessage.getCreationDateDateTime()) > 1) {
                nextToLastConversationMessage = lastConversationMessage;
                lastConversationMessage = conversationMessage;
            } else if (nextToLastConversationMessage == null) {
                nextToLastConversationMessage = conversationMessage;
            } else if (conversationMessage.getCreationDateDateTime().compareTo(
                    nextToLastConversationMessage.getCreationDateDateTime()) > 1) {
                nextToLastConversationMessage = conversationMessage;
            }
        }

        return nextToLastConversationMessage;
    }

}