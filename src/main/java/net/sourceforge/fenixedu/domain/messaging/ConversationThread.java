package net.sourceforge.fenixedu.domain.messaging;

import java.util.ArrayList;
import java.util.List;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.DateOrderedNode;
import net.sourceforge.fenixedu.domain.contents.IDateContent;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ConversationThread extends ConversationThread_Base implements IDateContent {

    public ConversationThread() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCreationDate(new DateTime());
    }

    public ConversationThread(Forum forum, Person creator, MultiLanguageString subject) {
        this();
        init(forum, creator, subject);
    }

    private void init(Forum forum, Person creator, MultiLanguageString subject) {
        setForum(forum);
        setTitle(subject);
        setCreator(creator);

    }

    public void setForum(Forum forum) {
        if (forum == null) {
            throw new DomainException("conversationThread.forum.cannot.be.null");
        }

        if (!hasAnyParents()) {
            new DateOrderedNode(forum, this, Boolean.FALSE);
        } else {
            getUniqueParentNode().setParent(forum);
        }
    }

    @Override
    public void setTitle(MultiLanguageString subject) {
        if (subject == null || subject.getContent() == null || subject.getContent().trim().isEmpty()) {
            throw new DomainException("conversationThread.subject.cannot.be.null");
        }

        super.setTitle(subject);
    }

    public void removeCreator() {
        super.setCreator(null);
    }

    @Override
    public void setCreator(Person creator) {
        if (creator == null) {
            throw new DomainException("conversationThread.creator.cannot.be.null");
        }

        super.setCreator(creator);
    }

    public Forum getForum() {
        return (Forum) getUniqueParentContainer();
    }

    public void checkIfPersonCanWrite(Person person) {
        getForum().checkIfPersonCanWrite(person);
    }

    public ConversationMessage createConversationMessage(Person creator, MultiLanguageString body) {
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

    public List<ConversationMessage> getConversationMessages() {
        List<ConversationMessage> conversationMessages = new ArrayList<ConversationMessage>();
        for (Node node : getChildrenSet()) {
            conversationMessages.add((ConversationMessage) node.getChild());
        }
        return conversationMessages;
    }

    public void addConversationMessages(ConversationMessage conversationMessage) {
        conversationMessage.setConversationThread(this);
    }

    public int getConversationMessagesCount() {
        return getChildrenSet().size();
    }

    @Override
    public DateTime getContentDate() {
        return getCreationDate();
    }

    @Override
    protected Node createChildNode(Content childContent) {
        return new DateOrderedNode(this, childContent, Boolean.TRUE);
    }

    @ConsistencyPredicate
    public final boolean checkTitle() {
        return getRootDomainObject() == null
                || (getTitle() != null && getTitle().getContent() != null && !getTitle().getContent().trim().isEmpty());
    }

}