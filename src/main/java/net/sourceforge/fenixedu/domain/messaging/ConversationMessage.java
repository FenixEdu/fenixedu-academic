package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ConversationMessage extends ConversationMessage_Base implements Comparable<ConversationMessage> {

    public ConversationMessage(ConversationThread conversationThread, Person creator, MultiLanguageString body) {
        super();
        setCreationDate(new DateTime());
        init(conversationThread, creator, body);
    }

    private void init(ConversationThread conversationThread, Person creator, MultiLanguageString body) {
        setConversationThread(conversationThread);
        setBody(body);
        setCreator(creator);
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

    @Override
    public void setConversationThread(ConversationThread conversationThread) {
        if (conversationThread == null) {
            throw new DomainException("conversationMessage.conversationThread.cannot.be.null");
        }
        super.setConversationThread(conversationThread);
    }

    public void delete() {
        setCreator(null);
        setConversationThread(null);
        deleteDomainObject();
    }

    @Override
    public int compareTo(ConversationMessage o) {
        return this.getCreationDate().compareTo(o.getCreationDate());
    }

}
