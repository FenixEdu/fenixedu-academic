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