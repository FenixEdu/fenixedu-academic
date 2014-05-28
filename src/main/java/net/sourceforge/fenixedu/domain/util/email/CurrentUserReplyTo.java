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

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CurrentUserReplyTo extends CurrentUserReplyTo_Base {

    public CurrentUserReplyTo() {
        super();
    }

    @Override
    public String getReplyToAddress(final Person person) {
        final Person currentUser = AccessControl.getPerson();
        final Person toUse = person == null ? currentUser : person;
        final EmailAddress emailAddress = toUse == null ? null : toUse.getDefaultEmailAddress();
        return emailAddress == null ? "" : emailAddress.getValue();
    }

    @Override
    public String getReplyToAddress() {
        final Person currentUser = AccessControl.getPerson();
        final EmailAddress emailAddress = currentUser == null ? null : currentUser.getDefaultEmailAddress();
        return emailAddress == null ? "" : emailAddress.getValue();
    }

}
