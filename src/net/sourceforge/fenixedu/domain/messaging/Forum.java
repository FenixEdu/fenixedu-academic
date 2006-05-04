package net.sourceforge.fenixedu.domain.messaging;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class Forum extends Forum_Base {

    public Forum() {
        super();
    }

    public Forum(Person owner, String name, String description, Group readersGroup, Group writersGroup) {
        super();
        init(owner, name, description, readersGroup, writersGroup);
    }

    public void init(Person owner, String name, String description, Group readersGroup,
            Group writersGroup) {
        setCreationDate(Calendar.getInstance().getTime());
        setOwner(owner);
        setName(name);
        setDescription(description);
        setReadersGroup(readersGroup);
        setWritersGroup(writersGroup);
    }

    public void createConversationThread(Person creator, String subject) {
        if (hasConversationThreadWithSubject(subject)) {
            throw new DomainException("forum.already.existing.conversation.thread");
        } else {
            ConversationThread conversationThread = new ConversationThread(creator, subject);
            this.addConversationThreads(conversationThread);
        }

    }

    private boolean hasConversationThreadWithSubject(String subject) {
        for (ConversationThread conversationThread : getConversationThreads()) {
            if (conversationThread.getSubject().equalsIgnoreCase(subject)) {
                return true;
            }
        }

        return false;
    }
    

}
