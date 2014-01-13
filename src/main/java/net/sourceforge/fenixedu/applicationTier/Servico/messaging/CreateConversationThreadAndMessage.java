package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationThreadAndMessageBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import pt.ist.fenixframework.Atomic;

public class CreateConversationThreadAndMessage extends ForumService {
    public CreateConversationThreadAndMessage() {
        super();
    }

    protected void run(CreateConversationThreadAndMessageBean createConversationThreadAndMessageBean) {

        Person creator = createConversationThreadAndMessageBean.getCreator();

        ConversationThread conversationThread =
                createConversationThreadAndMessageBean.getForum().createConversationThread(creator,
                        createConversationThreadAndMessageBean.getSubject());

        ConversationMessage conversationMessage =
                conversationThread.createConversationMessage(creator, createConversationThreadAndMessageBean.getBody());
        super.sendNotifications(conversationMessage);

    }

    // Service Invokers migrated from Berserk

    private static final CreateConversationThreadAndMessage serviceInstance = new CreateConversationThreadAndMessage();

    @Atomic
    public static void runCreateConversationThreadAndMessage(
            CreateConversationThreadAndMessageBean createConversationThreadAndMessageBean) {
        serviceInstance.run(createConversationThreadAndMessageBean);
    }

}