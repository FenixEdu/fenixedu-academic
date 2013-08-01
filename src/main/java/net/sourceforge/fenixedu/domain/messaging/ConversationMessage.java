package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.contents.DateOrderedNode;
import net.sourceforge.fenixedu.domain.contents.IDateContent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ConversationMessage extends ConversationMessage_Base implements IDateContent {

    public ConversationMessage() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCreationDate(new DateTime());
    }

    public ConversationMessage(ConversationThread conversationThread, Person creator, MultiLanguageString body) {
        this();
        init(conversationThread, creator, body);
    }

    private void init(ConversationThread conversationThread, Person creator, MultiLanguageString body) {
        setConversationThread(conversationThread);
        setBody(body);
        setCreator(creator);
    }

    public void removeCreator() {
        super.setCreator(null);
    }

    @Override
    public void setCreator(Person creator) {
        if (creator == null) {
            throw new DomainException("conversationMessage.creator.cannot.be.null");
        }

        super.setCreator(creator);
    }

    @Override
    public void setBody(MultiLanguageString body) {
        if (body == null) {
            throw new DomainException("conversationMessage.body.cannot.be.null");
        }

        super.setBody(body);
    }

    public void setConversationThread(ConversationThread conversationThread) {
        if (conversationThread == null) {
            throw new DomainException("conversationMessage.conversationThread.cannot.be.null");
        }

        if (!hasAnyParents()) {
            new DateOrderedNode(conversationThread, this, Boolean.TRUE);
        } else {
            getUniqueParentNode().setParent(conversationThread);
        }
    }

    public ConversationThread getConversationThread() {
        return (ConversationThread) getUniqueParentContainer();
    }

    @Override
    public DateTime getContentDate() {
        return getCreationDate();
    }

}
