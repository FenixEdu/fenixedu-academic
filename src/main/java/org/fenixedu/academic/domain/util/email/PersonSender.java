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
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.ist.fenixframework.Atomic;

public class PersonSender extends PersonSender_Base {

    public PersonSender() {
        super();
        setFromAddress(Sender.getNoreplyMail());
        addReplyTos(new CurrentUserReplyTo());
    }

    public PersonSender(final Person person) {
        this();
        setPerson(person);
        setMembers(UserGroup.of(person.getUser()));
        setFromName(createFromName());
    }

    public String createFromName() {
        return String.format("%s (%s)", Unit.getInstitutionAcronym(), getPerson().getName());
    }

    @Override
    public void delete() {
        setPerson(null);
        super.delete();
    }

    @Atomic
    public static PersonSender newInstance(final Person person) {
        return person.getSender() != null ? person.getSender() : new PersonSender(person);
    }

}
