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

import org.fenixedu.academic.domain.contacts.MobilePhone;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.ui.struts.action.externalServices.PhoneValidationUtils;

import pt.ist.fenixframework.Atomic;

public class MobilePhoneBean extends PartyContactBean {

    private static final String CONTACT_NAME = "MobilePhone";

    private static final long serialVersionUID = -17019887608353092L;

    public MobilePhoneBean(Party party) {
        super(party);
    }

    public MobilePhoneBean(MobilePhone partyContact) {
        super(partyContact);
        setValue(partyContact.getNumber());
    }

    @Override
    public String getContactName() {
        return CONTACT_NAME;
    }

    @Override
    @Atomic
    public Boolean edit() {
        boolean isValueChanged = super.edit();
        if (isValueChanged) {
            if (!getType().equals(PartyContactType.INSTITUTIONAL)) {
                ((MobilePhone) getContact()).edit(getValue());
            }
        }
        return isValueChanged;
    }

    @Override
    public PartyContact createNewContact() {
        return MobilePhone.createMobilePhone(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
                getVisibleToStudents(), getVisibleToStaff());
    }

    @Override
    public boolean isValueChanged() {
        return !((MobilePhone) getContact()).getNumber().equals(getValue());
    }

    @Override
    public String getValidationMessageKey() {
        if (!PhoneValidationUtils.getInstance().shouldRun()) {
            return "label.contact.validation.message.MobilePhoneValidated";
        }
        return super.getValidationMessageKey();
    }

    @Override
    public boolean isToBeValidated() {
        return MobilePhone.requiresValidation();
    }
}
