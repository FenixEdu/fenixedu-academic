package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationThreadAndMessageBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.Forum;

public class CreateConversationThreadAndMessage extends ForumService {
    public CreateConversationThreadAndMessage() {
	super();
    }

    public void run(CreateConversationThreadAndMessageBean createConversationThreadAndMessageBean) {
	Forum forum = createConversationThreadAndMessageBean.getForum();
	Person creator = createConversationThreadAndMessageBean.getCreator();
	String subject = createConversationThreadAndMessageBean.getSubject();
	String body = createConversationThreadAndMessageBean.getBody();
	ConversationThread conversationThread = forum.createConversationThread(creator, subject);

	ConversationMessage conversationMessage =  conversationThread.createConversationMessage(creator, body);
	super.sendNotifications(conversationMessage);

    }

}