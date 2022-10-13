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

import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.domain.organizationalStructure.Party;

public class WebAddressBean extends PartyContactBean {

    private static final String CONTACT_NAME = "WebAddress";

    private static final long serialVersionUID = 232589021187587867L;

    public WebAddressBean(Party party) {
        super(party);
    }

    public WebAddressBean(WebAddress partyContact) {
        super(partyContact);
        setValue(partyContact.getUrl());
    }

    @Override
    public String getContactName() {
        return CONTACT_NAME;
    }

    @Override
    public PartyContact createNewContact() {
        return WebAddress.createWebAddress(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
                getVisibleToStudents(), getVisibleToStaff());
    }

    @Override
    public boolean isValueChanged() {
        return !((WebAddress) getContact()).getUrl().equals(getValue());
    }

}
