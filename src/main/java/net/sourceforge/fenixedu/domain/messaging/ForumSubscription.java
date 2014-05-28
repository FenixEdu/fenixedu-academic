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
package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.Person;

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

    @Deprecated
    public boolean hasFavorite() {
        return getFavorite() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasForum() {
        return getForum() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasReceivePostsByEmail() {
        return getReceivePostsByEmail() != null;
    }

}
