package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.DateOrderedNode;
import net.sourceforge.fenixedu.domain.contents.IDateContent;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class ConversationMessage extends ConversationMessage_Base implements IDateContent {

    public ConversationMessage() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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

    public void delete() {
        removeConversationThread();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    
    public void removeConversationThread() {
	getParents().get(0).removeParent();
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

        if(getParents().isEmpty()) {
            new DateOrderedNode(conversationThread,this,Boolean.TRUE);
        }
        else {
            getParents().get(0).setParent(conversationThread);
        }
    }

    public ConversationThread getConversationThread() {
	return (getParents().isEmpty()) ? null : (ConversationThread) getParents().get(0).getParent();
    }

    public DateTime getContentDate() {
	return getCreationDate();
    }
    
}
