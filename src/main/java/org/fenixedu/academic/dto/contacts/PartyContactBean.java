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

import java.io.Serializable;

import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.MobilePhone;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.domain.organizationalStructure.Party;

public abstract class PartyContactBean implements Serializable {

    private Party party;

    private PartyContact contact;

    private String value;

    private PartyContactType type;

    private Boolean defaultContact = Boolean.FALSE;

    private Boolean visibleToPublic = Boolean.FALSE;

    private Boolean visibleToStudents = Boolean.FALSE;

    private Boolean visibleToStaff = Boolean.FALSE;

    private Boolean visibleToManagement = Boolean.TRUE;

    public PartyContactBean(Party party) {
        setParty(party);
    }

    public PartyContactBean(PartyContact partyContact) {
        init(partyContact);
    }

    private void init(PartyContact partyContact) {
        setParty(partyContact.getParty());
        setContact(partyContact);
        setType(partyContact.getType());
        setDefaultContact(partyContact.getDefaultContact());
        setVisibleToPublic(partyContact.getVisibleToPublic());
        setVisibleToStudents(partyContact.getVisibleToStudents());
        setVisibleToStaff(partyContact.getVisibleToStaff());
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public PartyContact getContact() {
        return this.contact;
    }

    public void setContact(PartyContact contact) {
        this.contact = contact;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PartyContactType getType() {
        return type;
    }

    public void setType(PartyContactType type) {
        this.type = type;
    }

    public Boolean getDefaultContact() {
        return defaultContact;
    }

    public void setDefaultContact(Boolean defaultContact) {
        this.defaultContact = defaultContact;
    }

    public Boolean getVisibleToPublic() {
        return visibleToPublic;
    }

    public void setVisibleToPublic(Boolean visibleToPublic) {
        this.visibleToPublic = visibleToPublic;
    }

    public Boolean getVisibleToStudents() {
        return visibleToStudents;
    }

    public void setVisibleToStudents(Boolean visibleToStudents) {
        this.visibleToStudents = visibleToStudents;
    }

    public Boolean getVisibleToStaff() {
        return visibleToStaff;
    }

    public void setVisibleToStaff(Boolean visibleToStaff) {
        this.visibleToStaff = visibleToStaff;
    }

    public Boolean getVisibleToManagement() {
        return visibleToManagement;
    }

    public void setVisibleToManagement(Boolean visibleToManagement) {
        this.visibleToManagement = visibleToManagement;
    }

    public abstract String getContactName();

    public abstract PartyContact createNewContact();

    public boolean isInstitutional() {
        return getType().equals(PartyContactType.INSTITUTIONAL);
    }

    public boolean edit() {
        boolean isValueChanged = isValueChanged();
        boolean createdNewContact = false;
        String newValue = getPresentationValue();

        if (isValueChanged) {
            if (!getContact().waitsValidation()) {
                PartyContact contact;
                if (getContact().getCurrentPartyContact() != null) { // has ongoing validation
                    contact = getContact().getCurrentPartyContact();
                    contact.getPartyContactValidation().reset();
                } else {
                    contact = createNewContact();

                    contact.setPrevPartyContact(getContact());
                    createdNewContact = true;
                }

                setContact(contact);
            }
        }

        boolean isChanged = setContactProperties();

        getParty().logEditContact(getContact(), isChanged, isValueChanged, createdNewContact, newValue);
        return isValueChanged;
    }

    protected String getPresentationValue() {
        return getValue();
    }

    private boolean setContactProperties() {
        boolean changes = false;
        final boolean isDefault = getDefaultContact().booleanValue();

        if (!isInstitutional()) {
            if (getContact().getType() != getType()) {
                changes = true;
            }
        }

        changes = changes || (getContact().getDefaultContact().booleanValue() != isDefault)
                || (getContact().getVisibleToPublic().booleanValue() != getVisibleToPublic().booleanValue())
                || (getContact().getVisibleToStudents().booleanValue() != getVisibleToStudents().booleanValue())
                || (getContact().getVisibleToStaff().booleanValue() != getVisibleToStaff().booleanValue());

        if (changes) {
            if (!isInstitutional()) {
                getContact().setType(getType());
            }
            getContact().setDefaultContactInformation(isDefault);
            getContact().setVisibleToPublic(getVisibleToPublic());
            getContact().setVisibleToStudents(getVisibleToStudents());
            getContact().setVisibleToStaff(getVisibleToStaff());
        }

        return changes;
    }

    public static PartyContactBean createFromDomain(PartyContact partyContact) {
        if (partyContact instanceof Phone) {
            return new PhoneBean((Phone) partyContact);
        }
        if (partyContact instanceof MobilePhone) {
            return new MobilePhoneBean((MobilePhone) partyContact);
        }
        if (partyContact instanceof EmailAddress) {
            return new EmailAddressBean((EmailAddress) partyContact);
        }
        if (partyContact instanceof WebAddress) {
            return new WebAddressBean((WebAddress) partyContact);
        }
        if (partyContact instanceof PhysicalAddress) {
            return new PhysicalAddressBean((PhysicalAddress) partyContact);
        }
        return null;
    }

    public abstract boolean isValueChanged();

    public String getValidationMessageKey() {
        return "label.contact.validation.message." + getContact().getClass().getSimpleName();
    }

}
