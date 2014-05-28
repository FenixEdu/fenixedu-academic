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
package net.sourceforge.fenixedu.domain.util.email;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class MessageId extends MessageId_Base {

    public MessageId() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setSendTime(new DateTime());
    }

    public MessageId(final Message message, final String messageID) {
        this();
        setMessage(message);
        setId(messageID);
    }

    public void delete() {
        setMessage(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMessage() {
        return getMessage() != null;
    }

    @Deprecated
    public boolean hasSendTime() {
        return getSendTime() != null;
    }

    @Deprecated
    public boolean hasId() {
        return getId() != null;
    }

}
