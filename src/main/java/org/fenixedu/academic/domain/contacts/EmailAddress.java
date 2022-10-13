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
package org.fenixedu.academic.domain.contacts;

import java.util.Comparator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.joda.time.DateTime;

public class EmailAddress extends EmailAddress_Base {

    static {
        setResolver(EmailAddress.class, (pc) -> ((EmailAddress) pc).getValue());
    }

    public static Comparator<EmailAddress> COMPARATOR_BY_EMAIL = new Comparator<EmailAddress>() {
        @Override
        public int compare(final EmailAddress contact, final EmailAddress otherContact) {
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
            return result == 0 ? COMPARATOR_BY_TYPE.compare(contact, otherContact) : result;
        }
    };

    public static EmailAddress createEmailAddress(final Party party, final String email, final PartyContactType type,
            final Boolean isDefault, final Boolean visibleToPublic, final Boolean visibleToStudents,
            final Boolean visibleToStaff) {
        final Supplier<EmailAddress> supplier =
                () -> new EmailAddress(party, type, visibleToPublic, visibleToStudents, visibleToStaff, isDefault, email);
        return createEmailAddress(supplier, party, email);
    }

    public static EmailAddress createEmailAddress(final Party party, final String email, final PartyContactType type,
            final boolean isDefault) {
        final Supplier<EmailAddress> supplier = () -> new EmailAddress(party, type, isDefault, email);
        return createEmailAddress(supplier, party, email);
    }

    private static EmailAddress createEmailAddress(final Supplier<EmailAddress> supplier, final Party party, final String email) {
        return StringUtils.isEmpty(email) ? null : party.getEmailAddressStream()
                .filter(ea -> email.equalsIgnoreCase(ea.getValue())).findAny().orElseGet(supplier);
    }

    protected EmailAddress() {
        super();
        new EmailValidation(this);
    }

    protected EmailAddress(final Party party, final PartyContactType type, final boolean defaultContact, final String value) {
        this();
        super.init(party, type, defaultContact);
        checkParameters(value);
        setValue(value);
    }

    protected EmailAddress(final Party party, final PartyContactType type, final boolean visibleToPublic,
            final boolean visibleToStudents, final boolean visibleToStaff, final boolean defaultContact, final String value) {
        this();
        super.init(party, type, visibleToPublic, visibleToStudents, visibleToStaff, defaultContact);
        checkParameters(value);
        setValue(value);
    }

    private void checkParameters(final String value) {
        if (!EmailValidator.getInstance().isValid(value)) {
            throw new DomainException("error.domain.contacts.EmailAddress.invalid.format", value);
        }
    }

    @Override
    public void setDefaultContact(final Boolean defaultContact) {
        super.setDefaultContact(defaultContact);
        updateProfileEmail();
    }

    @Override
    public void setValue(final String value) {
        super.setValue(value);
        updateProfileEmail();
    }

    @Override
    public void setType(final PartyContactType type) {
        if (PartyContactType.INSTITUTIONAL.equals(type)) {
            throw new DomainException("error.domain.contacts.EmailAddress.can.only.have.one.institutional.emailAddress");
        }
        super.setType(type);
        updateProfileEmail();
    }

    @Override
    public void setValid() {
        super.setValid();
        updateProfileEmail();
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

    public void edit(final String value) {
        if (!isInstitutionalType()) {
            if (!StringUtils.equals(value, getValue())) {
                setValue(value);
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

    @Override
    protected void processDelete() {
        updateProfileEmail();
        super.processDelete();
    }

    static public EmailAddress find(final String emailAddressString) {
        for (final PartyContact contact : ContactRoot.getInstance().getPartyContactsSet()) {
            if (contact.isEmailAddress()) {
                final EmailAddress emailAddress = (EmailAddress) contact;
                if (emailAddress.hasValue(emailAddressString)) {
                    return emailAddress;
                }
            }
        }
        return null;
    }

    public static Stream<EmailAddress> findAllActiveAndValid(final String emailAddressString) {
        return ContactRoot.getInstance().getPartyContactsSet().stream().filter(partyContact -> partyContact.isEmailAddress())
                .map(partyContact -> (EmailAddress) partyContact)
                .filter(emailAddress -> emailAddress.hasValue(emailAddressString))
                .filter(emailAddress -> emailAddress.isActiveAndValid());
    }

    private void updateProfileEmail() {
        if (getParty() != null && getParty() instanceof Person) {
            Person person = (Person) getParty();
            person.getProfile().setEmail(person.getEmailForSendingEmails());
        }
    }

    @Override
    public void logCreate(final Person person) {
        logCreateAux(person, "label.partyContacts.EmailAddress");
    }

    @Override
    public void logEdit(final Person person, final boolean propertiesChanged, final boolean valueChanged,
            final boolean createdNewContact, final String newValue) {
        logEditAux(person, propertiesChanged, valueChanged, createdNewContact, newValue, "label.partyContacts.EmailAddress");
    }

    @Override
    public void logDelete(final Person person) {
        logDeleteAux(person, "label.partyContacts.EmailAddress");
    }

    @Override
    public void logValid(final Person person) {
        logValidAux(person, "label.partyContacts.EmailAddress");
    }

    @Override
    public void logRefuse(final Person person) {
        logRefuseAux(person, "label.partyContacts.EmailAddress");
    }

}
