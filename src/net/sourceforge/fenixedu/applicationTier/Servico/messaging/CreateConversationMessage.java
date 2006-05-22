package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.Servico.renderers.CreateObjects;
import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationMessageBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;

public class CreateConversationMessage extends CreateObjects {

    public CreateConversationMessage() {
        super();
    }

    public void run(CreateConversationMessageBean createConversationMessageBean) {
        String body = createConversationMessageBean.getBody();
        Person creator = createConversationMessageBean.getCreator();
        ConversationThread conversationThread = createConversationMessageBean.getConversationThread();

        conversationThread.createConversationMessage(creator, body, true);

    }

}
