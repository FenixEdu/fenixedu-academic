package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;

public class CreateConversationMessageBean implements Serializable {

    private String body;

    private DomainReference<Person> creatorReference;

    private DomainReference<ConversationThread> conversationThreadReference;

    public CreateConversationMessageBean() {
        super();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Person getCreator() {
        return (this.creatorReference != null) ? this.creatorReference.getObject() : null;

    }

    public void setCreator(Person creator) {
        this.creatorReference = (creator != null) ? new DomainReference<Person>(creator) : null;
    }

    public ConversationThread getConversationThread() {
        return (this.conversationThreadReference != null) ? this.conversationThreadReference.getObject()
                : null;

    }

    public void setConversationThread(ConversationThread conversationThread) {
        this.conversationThreadReference = (conversationThread != null) ? new DomainReference<ConversationThread>(
                conversationThread)
                : null;
    }

}
