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
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.util.PhoneUtil;

import pt.ist.fenixframework.Atomic;

public class PhoneBean extends PartyContactBean {

    private static final String CONTACT_NAME = "Phone";

    private static final long serialVersionUID = 7318803302492544891L;

    public PhoneBean(Party party) {
        super(party);
    }

    public PhoneBean(Phone partyContact) {
        super(partyContact);
        setValue(partyContact.getNumber());
    }

    @Override
    public String getContactName() {
        return CONTACT_NAME;
    }

    @Override
    @Atomic
    public boolean edit() {
        boolean isValueChanged = super.edit();
        if (isValueChanged) {
            if (!getType().equals(PartyContactType.INSTITUTIONAL)) {
                ((Phone) getContact()).edit(getValue());
            }
        }
        return isValueChanged;
    }

    @Override
    public PartyContact createNewContact() {
        return Phone.createPhone(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
                getVisibleToStudents(), getVisibleToStaff());
    }

    @Override
    public String getValidationMessageKey() {
        if (PhoneUtil.isMobileNumber(getValue())) {
            return "label.contact.validation.message.MobilePhone";
        }
        if (PhoneUtil.isFixedNumber(getValue())) {
            return "label.contact.validation.message.Phone";
        }
        return super.getValidationMessageKey();
    }

    @Override
    public boolean isValueChanged() {
        return !getValue().equals(((Phone) getContact()).getNumber());
    }

    @Override
    public boolean isToBeValidated() {
        return false;
    }
}
