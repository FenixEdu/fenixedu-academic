package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationMessageBean;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;

public class CreateConversationMessage extends ForumService {

    public CreateConversationMessage() {
	super();
    }

    public void run(CreateConversationMessageBean createConversationMessageBean) {

	ConversationMessage conversationMessage = createConversationMessageBean.getConversationThread()
		.createConversationMessage(createConversationMessageBean.getCreator(),
			createConversationMessageBean.getBody());
	super.sendNotifications(conversationMessage);

    }

}