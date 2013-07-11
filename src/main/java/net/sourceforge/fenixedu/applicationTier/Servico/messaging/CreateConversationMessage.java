package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationMessageBean;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import pt.ist.fenixframework.Atomic;

public class CreateConversationMessage extends ForumService {

    public CreateConversationMessage() {
        super();
    }

    protected void run(CreateConversationMessageBean createConversationMessageBean) {

        ConversationMessage conversationMessage =
                createConversationMessageBean.getConversationThread().createConversationMessage(
                        createConversationMessageBean.getCreator(), createConversationMessageBean.getBody());
        super.sendNotifications(conversationMessage);

    }

    // Service Invokers migrated from Berserk

    private static final CreateConversationMessage serviceInstance = new CreateConversationMessage();

    @Atomic
    public static void runCreateConversationMessage(CreateConversationMessageBean createConversationMessageBean) {
        serviceInstance.run(createConversationMessageBean);
    }

}