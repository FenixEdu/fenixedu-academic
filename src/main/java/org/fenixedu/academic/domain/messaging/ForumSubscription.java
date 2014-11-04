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
import org.fenixedu.bennu.core.domain.Bennu;

public class ForumSubscription extends ForumSubscription_Base {

    public ForumSubscription() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setReceivePostsByEmail(false);
        setFavorite(false);

    }

    public ForumSubscription(Person person, Forum forum) {
        this();
        setPerson(person);
        setForum(forum);
    }

    public void delete() {
        setPerson(null);
        setForum(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void removePerson() {
        super.setPerson(null);
    }

    public void removeForum() {
        super.setForum(null);
    }

}
