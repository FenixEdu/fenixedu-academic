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
package org.fenixedu.academic.domain.util.email;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.domain.Bennu;

public abstract class ReplyTo extends ReplyTo_Base {

    public static final Comparator<ReplyTo> COMPARATOR_BY_ADDRESS = new Comparator<ReplyTo>() {

        @Override
        public int compare(final ReplyTo replyTo1, final ReplyTo replyTo2) {
            return DomainObjectUtil.COMPARATOR_BY_ID.compare(replyTo1, replyTo2);
            // No longer possible because we need the current user to check
            // this...
            // return
            // replyTo1.getReplyToAddress().compareTo(replyTo2.getReplyToAddress());
        }

    };

    public ReplyTo() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void safeDelete() {
        for (final Message message : getMessagesSet()) {
            removeMessages(message);
        }
        if (getSender() == null) {
            delete();
        }
    }

    public void delete() {
        setSender(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public abstract String getReplyToAddress(final Person person);

    public abstract String getReplyToAddress();

    public Collection<? extends ReplyTo> asCollection() {
        return Collections.singletonList(this);
    }

}
