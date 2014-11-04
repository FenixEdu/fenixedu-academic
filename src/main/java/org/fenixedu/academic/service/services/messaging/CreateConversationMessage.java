/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.service.services.messaging;

import org.fenixedu.academic.domain.messaging.ConversationMessage;
import org.fenixedu.academic.dto.messaging.CreateConversationMessageBean;

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