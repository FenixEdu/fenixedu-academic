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
package org.fenixedu.academic.dto.messaging;

import java.io.Serializable;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.messaging.Forum;
import org.fenixedu.academic.util.MultiLanguageString;

public class CreateConversationThreadAndMessageBean implements Serializable {

    private MultiLanguageString subject;

    private MultiLanguageString body;

    private Person creatorReference;

    private Forum forumReference;

    public CreateConversationThreadAndMessageBean() {
        super();
        creatorReference = null;
        forumReference = null;
    }

    public MultiLanguageString getSubject() {
        return subject;
    }

    public void setSubject(MultiLanguageString subject) {
        this.subject = subject;
    }

    public MultiLanguageString getBody() {
        return body;
    }

    public void setBody(MultiLanguageString body) {
        this.body = body;
    }

    public Person getCreator() {
        return this.creatorReference;

    }

    public void setCreator(Person creator) {
        this.creatorReference = creator;
    }

    public Forum getForum() {
        return this.forumReference;

    }

    public void setForum(Forum forum) {
        this.forumReference = forum;
    }

}
