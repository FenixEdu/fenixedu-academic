package net.sourceforge.fenixedu.domain.messaging;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ConversationThread extends ConversationThread_Base implements Comparable<ConversationThread> {

    public ConversationThread(Forum forum, Person creator, MultiLanguageString subject) {
        setCreationDate(new DateTime());
        init(forum, creator, subject);
    }

    private void init(Forum forum, Person creator, MultiLanguageString subject) {
        setForum(forum);
        setTitle(subject);
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
    public void setTitle(MultiLanguageString subject) {
        if (subject == null || subject.getContent() == null || subject.getContent().trim().isEmpty()) {
            throw new DomainException("conversationThread.subject.cannot.be.null");
        }
        super.setTitle(subject);
    }

    @Override
    public void setCreator(Person creator) {
        if (creator == null) {
            throw new DomainException("conversationThread.creator.cannot.be.null");
        }
        super.setCreator(creator);
    }

    public void checkIfPersonCanWrite(Person person) {
        getForum().checkIfPersonCanWrite(person);
    }

    public ConversationMessage createConversationMessage(Person creator, MultiLanguageString body) {
        checkIfPersonCanWrite(creator);
        return new ConversationMessage(this, creator, body);
    }

    public ConversationMessage getNextToLastConversationMessage() {
        ConversationMessage lastConversationMessage = null;
        ConversationMessage nextToLastConversationMessage = null;

        for (ConversationMessage conversationMessage : getMessageSet()) {
            if (lastConversationMessage == null) {
                lastConversationMessage = conversationMessage;
            } else if (conversationMessage.getCreationDate().compareTo(lastConversationMessage.getCreationDate()) > 1) {
                nextToLastConversationMessage = lastConversationMessage;
                lastConversationMessage = conversationMessage;
            } else if (nextToLastConversationMessage == null) {
                nextToLastConversationMessage = conversationMessage;
            } else if (conversationMessage.getCreationDate().compareTo(nextToLastConversationMessage.getCreationDate()) > 1) {
                nextToLastConversationMessage = conversationMessage;
            }
        }

        return nextToLastConversationMessage;
    }

    public void addConversationMessages(ConversationMessage conversationMessage) {
        conversationMessage.setConversationThread(this);
    }

    @ConsistencyPredicate
    public final boolean checkTitle() {
        return getTitle() != null && getTitle().getContent() != null && !getTitle().getContent().trim().isEmpty();
    }

    public void delete() {
        for (final ConversationMessage message : getMessageSet()) {
            message.delete();
        }
        setCreator(null);
        setForum(null);
        deleteDomainObject();
    }

    @Override
    public int compareTo(ConversationThread o) {
        return this.getCreationDate().compareTo(o.getCreationDate());
    }

}