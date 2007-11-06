package net.sourceforge.fenixedu.domain.messaging;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;

public class ConversationMessage extends ConversationMessage_Base {

    public static class DateComparatorOldestFirst implements Comparator<java.util.Date> {
	public int compare(Date o1, Date o2) {
	    return -o1.compareTo(o2);
	}
    }

    public static final Comparator CONVERSATION_MESSAGE_COMPARATOR_BY_CREATION_DATE = new BeanComparator("creationDate",
	    new DateComparatorOldestFirst());

    public ConversationMessage() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDate(Calendar.getInstance().getTime());
    }

    public ConversationMessage(ConversationThread conversationThread, Person creator, String body) {
	this();
	init(conversationThread, creator, body);
    }

    private void init(ConversationThread conversationThread, Person creator, String body) {
	setConversationThread(conversationThread);
	setBody(body);
	setCreator(creator);
    }

    public void delete() {
	removeConversationThread();
	removeRootDomainObject();
	removeCreator();
	super.deleteDomainObject();
    }

    @Override
    public void removeConversationThread() {
	super.setConversationThread(null);
    }

    @Override
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
    public void setBody(String body) {
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

}
