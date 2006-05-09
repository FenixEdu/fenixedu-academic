package net.sourceforge.fenixedu.domain.messaging;

import java.util.Calendar;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public class ConversationThread extends ConversationThread_Base {

    public static final Comparator CONVERSATION_THREAD_COMPARATOR_BY_CREATION_DATE = new BeanComparator(
	    "creationDate");

    public ConversationThread() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDate(Calendar.getInstance().getTime());
    }

    public ConversationThread(Person creator, String subject) {
	this();

	setCreationDate(Calendar.getInstance().getTime());
	setCreator(creator);
	setSubject(subject);
    }

    public void createConversationMessage(Person creator, String body) {
	ConversationMessage conversationMessage = new ConversationMessage(creator, body);
	this.addConversationMessages(conversationMessage);
    }

    @Override
    public void setForum(Forum forum) {
	if (this.getSubject() != null) {
	    forum.checkIfCanAddConversationThreadWithSubject(this.getSubject());
	}
	super.setForum(forum);
    }

    @Override
    public void setSubject(String subject) {
	if (this.getForum() != null) {
	    this.getForum().checkIfCanAddConversationThreadWithSubject(subject);
	}
	super.setSubject(subject);
    }

}