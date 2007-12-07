package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationThreadAndMessageBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;

public class CreateConversationThreadAndMessage extends ForumService {
    public CreateConversationThreadAndMessage() {
	super();
    }

    public void run(CreateConversationThreadAndMessageBean createConversationThreadAndMessageBean) {

	Person creator = createConversationThreadAndMessageBean.getCreator();

	ConversationThread conversationThread = createConversationThreadAndMessageBean.getForum()
		.createConversationThread(creator, createConversationThreadAndMessageBean.getSubject());

	ConversationMessage conversationMessage = conversationThread.createConversationMessage(creator,
		createConversationThreadAndMessageBean.getBody());
	super.sendNotifications(conversationMessage);

    }

}