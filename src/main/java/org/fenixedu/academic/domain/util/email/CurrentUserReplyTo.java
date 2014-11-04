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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.predicate.AccessControl;

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
