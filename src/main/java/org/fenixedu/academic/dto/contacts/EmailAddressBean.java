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
package org.fenixedu.academic.dto.contacts;

import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.organizationalStructure.Party;

public class EmailAddressBean extends PartyContactBean {

    private static final String CONTACT_NAME = "EmailAddress";

    private static final long serialVersionUID = 8521361651957831331L;

    public EmailAddressBean(Party party) {
        super(party);
    }

    public EmailAddressBean(EmailAddress email) {
        super(email);
        setValue(email.getValue());
    }

    @Override
    public String getContactName() {
        return CONTACT_NAME;
    }

    @Override
    public boolean isValueChanged() {
        return !((EmailAddress) getContact()).getValue().equals(getValue());
    }

    @Override
    public PartyContact createNewContact() {
        return EmailAddress.createEmailAddress(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
                getVisibleToStudents(), getVisibleToStaff());
    }

}
