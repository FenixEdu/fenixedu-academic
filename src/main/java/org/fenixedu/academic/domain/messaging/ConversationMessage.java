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
package org.fenixedu.academic.domain.messaging;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.MultiLanguageString;
import org.joda.time.DateTime;

public class ConversationMessage extends ConversationMessage_Base implements Comparable<ConversationMessage> {

    public ConversationMessage(ConversationThread conversationThread, Person creator, MultiLanguageString body) {
        super();
        setCreationDate(new DateTime());
        init(conversationThread, creator, body);
    }

    private void init(ConversationThread conversationThread, Person creator, MultiLanguageString body) {
        setConversationThread(conversationThread);
        setBody(body);
        setCreator(creator);
    }

    @Override
    public void setCreator(Person creator) {
        if (creator == null) {
            throw new DomainException("conversationMessage.creator.cannot.be.null");
        }

        super.setCreator(creator);
    }

    @Override
    public void setBody(MultiLanguageString body) {
        if (body == null) {
            throw new DomainException("conversationMessage.body.cannot.be.null");
        }

        super.setBody(body);
    }

    @Override
    public void setConversationThread(ConversationThread conversationThread) {
        if (conversationThread == null) {
            throw new DomainException("conversationMessage.conversationThread.cannot.be.null");
        }
        super.setConversationThread(conversationThread);
    }

    public void delete() {
        setCreator(null);
        setConversationThread(null);
        deleteDomainObject();
    }

    @Override
    public int compareTo(ConversationMessage o) {
        return this.getCreationDate().compareTo(o.getCreationDate());
    }

}
