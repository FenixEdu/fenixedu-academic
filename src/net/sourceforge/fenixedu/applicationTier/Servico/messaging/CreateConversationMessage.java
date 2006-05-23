package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationMessageBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;

public class CreateConversationMessage extends ForumService {
        
    public CreateConversationMessage() {
	super();
    }

    public void run(CreateConversationMessageBean createConversationMessageBean) {
	String body = createConversationMessageBean.getBody();
	Person creator = createConversationMessageBean.getCreator();
	ConversationThread conversationThread = createConversationMessageBean.getConversationThread();

	ConversationMessage conversationMessage = conversationThread.createConversationMessage(creator, body);
	super.sendNotifications(conversationMessage);

    }

}