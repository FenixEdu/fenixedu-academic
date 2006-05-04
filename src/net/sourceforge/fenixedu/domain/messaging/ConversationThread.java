package net.sourceforge.fenixedu.domain.messaging;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Person;

public class ConversationThread extends ConversationThread_Base {

    public ConversationThread() {
        super();
    }

    public ConversationThread(Person creator, String subject) {
        super();
        setCreationDate(Calendar.getInstance().getTime());
        setCreator(creator);
        setSubject(subject);
    }

    public void createConversationMessage(Person creator, String body) {
        ConversationMessage conversationMessage = new ConversationMessage(creator, body);
        this.addConversationMessages(conversationMessage);
    }

}
