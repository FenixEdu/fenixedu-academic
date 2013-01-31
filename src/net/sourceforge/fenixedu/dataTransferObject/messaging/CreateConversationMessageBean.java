package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateConversationMessageBean implements Serializable {

	private MultiLanguageString body;

	private Person creatorReference;

	private ConversationThread conversationThreadReference;

	public CreateConversationMessageBean() {
		super();
		creatorReference = null;
		conversationThreadReference = null;
	}

	public MultiLanguageString getBody() {
		return body;
	}

	public void setBody(MultiLanguageString body) {
		this.body = body;
	}

	public Person getCreator() {
		return this.creatorReference;

	}

	public void setCreator(Person creator) {
		this.creatorReference = creator;
	}

	public ConversationThread getConversationThread() {
		return this.conversationThreadReference;

	}

	public void setConversationThread(ConversationThread conversationThread) {
		this.conversationThreadReference = conversationThread;
	}

}
