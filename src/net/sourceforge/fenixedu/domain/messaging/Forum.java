package net.sourceforge.fenixedu.domain.messaging;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class Forum extends Forum_Base {

    public Forum() {
	super();
    }

    public Forum(Person owner, String name, String description, Group readersGroup, Group writersGroup) {
	super();
	init(owner, name, description);
    }

    public void init(Person owner, String name, String description) {
	setCreationDate(Calendar.getInstance().getTime());
	setOjbConcreteClass(this.getClass().getName());
	setRootDomainObject(RootDomainObject.getInstance());
	setOwner(owner);
	setName(name);
	setDescription(description);
    }

    public void createConversationThread(Person creator, String subject) {
	checkIfCanAddConversationThreadWithSubject(subject);
	ConversationThread conversationThread = new ConversationThread(creator, subject);
	this.addConversationThreads(conversationThread);

    }

    private boolean hasConversationThreadWithSubject(String subject) {
	for (ConversationThread conversationThread : getConversationThreads()) {
	    if (conversationThread.getSubject().equalsIgnoreCase(subject)) {
		return true;
	    }
	}
	return false;
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

    @Override
    public void addConversationThreads(ConversationThread conversationThread) {
	checkIfCanAddConversationThreadWithSubject(conversationThread.getSubject());
	super.addConversationThreads(conversationThread);
    }
}
