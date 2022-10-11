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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.joda.time.DateTime;

public class Phone extends Phone_Base {

    static {
        setResolver(Phone.class, (pc) -> ((Phone) pc).getNumber());
    }

    public static Comparator<Phone> COMPARATOR_BY_NUMBER = new Comparator<Phone>() {
        @Override
        public int compare(Phone contact, Phone otherContact) {
            final String number = contact.getNumber();
            final String otherNumber = otherContact.getNumber();
            int result = 0;
            if (number != null && otherNumber != null) {
                result = number.compareTo(otherNumber);
            } else if (number != null) {
                result = 1;
            } else if (otherNumber != null) {
                result = -1;
            }
            return (result == 0) ? COMPARATOR_BY_TYPE.compare(contact, otherContact) : result;
        }
    };

    public static Phone createPhone(Party party, String number, PartyContactType type, Boolean isDefault, Boolean visibleToPublic,
            Boolean visibleToStudents, Boolean visibleToStaff) {
        Phone result = null;
        if (!StringUtils.isEmpty(number)) {
            result = new Phone(party, type, visibleToPublic, visibleToStudents, visibleToStaff, isDefault, number);
        }
        return result;
    }

    public static Phone createPhone(Party party, String number, PartyContactType type, boolean isDefault) {
        // for (Phone phone : party.getPhones()) {
        // if (phone.getNumber().equals(number))
        // return phone;
        // }
        return (!StringUtils.isEmpty(number)) ? new Phone(party, type, isDefault, number) : null;
    }

    protected Phone() {
        super();
        new PhoneValidation(this);
    }

    protected Phone(final Party party, final PartyContactType type, final boolean defaultContact, final String number) {
        this();
        super.init(party, type, defaultContact);
        checkParameters(number);
        super.setNumber(number);
    }

    protected Phone(final Party party, final PartyContactType type, final boolean visibleToPublic,
            final boolean visibleToStudents, final boolean visibleToStaff, final boolean defaultContact, final String number) {
        this();
        super.init(party, type, visibleToPublic, visibleToStudents, visibleToStaff, defaultContact);
        checkParameters(number);
        super.setNumber(number);
    }

    private void checkParameters(final String number) {
        if (StringUtils.isEmpty(number)) {
            throw new DomainException("error.contacts.Phone.invalid.number");
        }
    }

    @Override
    public boolean isPhone() {
        return true;
    }

    public void edit(final String number) {
        if (!StringUtils.equals(getNumber(), number)) {
            super.setNumber(number);
            if (!waitsValidation()) {
                new PhoneValidation(this);
            }
            setLastModifiedDate(new DateTime());
        }

    }

    public boolean hasNumber() {
        return getNumber() != null && !getNumber().isEmpty();
    }

    @Override
    public boolean hasValue(String value) {
        return hasNumber() && getNumber().equals(value);
    }

    @Override
    public void logCreate(Person person) {
        logCreateAux(person, "label.partyContacts.Phone");
    }

    @Override
    public void logEdit(Person person, boolean propertiesChanged, boolean valueChanged, boolean createdNewContact,
            String newValue) {
        logEditAux(person, propertiesChanged, valueChanged, createdNewContact, newValue, "label.partyContacts.Phone");
    }

    @Override
    public void logDelete(Person person) {
        logDeleteAux(person, "label.partyContacts.Phone");
    }

    @Override
    public void logValid(Person person) {
        logValidAux(person, "label.partyContacts.Phone");
    }

    @Override
    public void logRefuse(Person person) {
        logRefuseAux(person, "label.partyContacts.Phone");
    }

    @Override
    public boolean isToBeValidated() {
        return false;
    }

}
