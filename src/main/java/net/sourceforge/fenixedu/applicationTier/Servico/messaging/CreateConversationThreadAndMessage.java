/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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