package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreateConversationMessageBean implements Serializable {

    private MultiLanguageString body;

    private DomainReference<Person> creatorReference;

    private DomainReference<ConversationThread> conversationThreadReference;

    public CreateConversationMessageBean() {
	super();
	creatorReference = new DomainReference<Person>(null);
	conversationThreadReference = new DomainReference<ConversationThread>(null);
    }

    public MultiLanguageString getBody() {
	return body;
    }

    public void setBody(MultiLanguageString body) {
	this.body = body;
    }

    public Person getCreator() {
	return this.creatorReference.getObject();

    }

    public void setCreator(Person creator) {
	this.creatorReference = new DomainReference<Person>(creator);
    }

    public ConversationThread getConversationThread() {
	return this.conversationThreadReference.getObject();

    }

    public void setConversationThread(ConversationThread conversationThread) {
	this.conversationThreadReference = new DomainReference<ConversationThread>(conversationThread);
    }

}
