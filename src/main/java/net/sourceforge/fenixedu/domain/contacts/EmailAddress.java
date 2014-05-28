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
package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class EmailAddress extends EmailAddress_Base {

    public static Comparator<EmailAddress> COMPARATOR_BY_EMAIL = new Comparator<EmailAddress>() {
        @Override
        public int compare(EmailAddress contact, EmailAddress otherContact) {
            final String value = contact.getValue();
            final String otherValue = otherContact.getValue();
            int result = 0;
            if (value != null && otherValue != null) {
                result = value.compareTo(otherValue);
            } else if (value != null) {
                result = 1;
            } else if (otherValue != null) {
                result = -1;
            }
            return (result == 0) ? COMPARATOR_BY_TYPE.compare(contact, otherContact) : result;
        }
    };

    public static EmailAddress createEmailAddress(Party party, String email, PartyContactType type, Boolean isDefault,
            Boolean visibleToPublic, Boolean visibleToStudents, Boolean visibleToTeachers, Boolean visibleToEmployees,
            Boolean visibleToAlumni) {

        EmailAddress result = null;
        if (!StringUtils.isEmpty(email)) {
            result =
                    new EmailAddress(party, type, visibleToPublic, visibleToStudents, visibleToTeachers, visibleToEmployees,
                            visibleToAlumni, isDefault, email);
        }
        return result;
    }

    public static EmailAddress createEmailAddress(Party party, String email, PartyContactType type, boolean isDefault) {
        for (EmailAddress emailAddress : party.getEmailAddresses()) {
            if (emailAddress.getValue().equals(email)) {
                return emailAddress;
            }
        }
        return (!StringUtils.isEmpty(email)) ? new EmailAddress(party, type, isDefault, email) : null;
    }

    protected EmailAddress() {
        super();
        new EmailValidation(this);
    }

    protected EmailAddress(final Party party, final PartyContactType type, final boolean defaultContact, final String value) {
        this();
        super.init(party, type, defaultContact);
        checkParameters(value);
        super.setValue(value);
    }

    protected EmailAddress(final Party party, final PartyContactType type, final boolean visibleToPublic,
            final boolean visibleToStudents, final boolean visibleToTeachers, final boolean visibleToEmployees,
            final boolean visibleToAlumni, final boolean defaultContact, final String value) {
        this();
        super.init(party, type, visibleToPublic, visibleToStudents, visibleToTeachers, visibleToEmployees, visibleToAlumni,
                defaultContact);
        checkParameters(value);
        super.setValue(value);
    }

    private void checkParameters(final String value) {
        if (!EmailValidator.getInstance().isValid(value)) {
            throw new DomainException("error.domain.contacts.EmailAddress.invalid.format", value);
        }
    }

    @Override
    public void setType(PartyContactType type) {
        if (PartyContactType.INSTITUTIONAL.equals(type)) {
            throw new DomainException("error.domain.contacts.EmailAddress.can.only.have.one.institutional.emailAddress");
        }
        super.setType(type);
    }

    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    public boolean hasValue(final String emailAddressString) {
        return hasValue() && getValue().equalsIgnoreCase(emailAddressString);
    }

    @Override
    public boolean isEmailAddress() {
        return true;
    }

    @Atomic
    public void edit(final String value) {
        if (!isInstitutionalType()) {
            if (!StringUtils.equals(value, getValue())) {
                super.setValue(value);
                if (!waitsValidation()) {
                    new EmailValidation(this);
                }
            }
            setLastModifiedDate(new DateTime());
        }
    }

    @Override
    protected void checkRulesToDelete() {
        if (isInstitutionalType()) {
            throw new DomainException("error.domain.contacts.EmailAddress.cannot.delete.institution.emailAddress", getValue());
        }
        if (getParty().getPartyContacts(getClass()).size() == 1) {
            throw new DomainException("error.domain.contacts.EmailAddress.cannot.remove.last.emailAddress");
        }
    }

    static public EmailAddress find(final String emailAddressString) {
        for (final PartyContact contact : Bennu.getInstance().getPartyContactsSet()) {
            if (contact.isEmailAddress()) {
                final EmailAddress emailAddress = (EmailAddress) contact;
                if (emailAddress.hasValue(emailAddressString)) {
                    return emailAddress;
                }
            }
        }
        return null;
    }

    @Override
    public String getPresentationValue() {
        return getValue();
    }

    @Override
    public void logCreate(Person person) {
        logCreateAux(person, "label.partyContacts.EmailAddress");
    }

    @Override
    public void logEdit(Person person, boolean propertiesChanged, boolean valueChanged, boolean createdNewContact, String newValue) {
        logEditAux(person, propertiesChanged, valueChanged, createdNewContact, newValue, "label.partyContacts.EmailAddress");
    }

    @Override
    public void logDelete(Person person) {
        logDeleteAux(person, "label.partyContacts.EmailAddress");
    }

    @Override
    public void logValid(Person person) {
        logValidAux(person, "label.partyContacts.EmailAddress");
    }

    @Override
    public void logRefuse(Person person) {
        logRefuseAux(person, "label.partyContacts.EmailAddress");
    }

}
