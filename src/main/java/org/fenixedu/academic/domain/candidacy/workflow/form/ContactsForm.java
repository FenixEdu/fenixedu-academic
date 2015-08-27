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
package org.fenixedu.academic.domain.candidacy.workflow.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.fenixedu.academic.domain.EmergencyContact;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.util.workflow.Form;
import org.fenixedu.academic.util.LabelFormatter;

public class ContactsForm extends Form {

    private String phoneNumber;

    private String mobileNumber;

    private String email;

    private String webAddress;

    private String emergencyContact;

    private boolean isEmailAvailable;

    private boolean isHomepageAvailable;

    public ContactsForm() {
        super();
    }

    public static ContactsForm createFromPerson(Person person) {
        final boolean availableEmail = person.isDefaultEmailVisible();
        final boolean availableWebSite = person.isDefaultWebAddressVisible();
        final String emergencyContact =
                Optional.ofNullable(person.getProfile().getEmergencyContact()).map(EmergencyContact::getContact).orElse(null);

        return new ContactsForm(person.getEmail(), person.getDefaultWebAddressUrl(), availableEmail, availableWebSite,
                person.getDefaultMobilePhoneNumber(), person.getDefaultPhoneNumber(), emergencyContact);
    }

    private ContactsForm(String email, String homepage, boolean isEmailAvailable, boolean isHomepageAvailable,
            String mobileNumber, String phoneNumber, String emergencyContact) {
        this();
        this.email = email;
        this.webAddress = homepage;
        this.isEmailAvailable = isEmailAvailable;
        this.isHomepageAvailable = isHomepageAvailable;
        this.mobileNumber = mobileNumber;
        this.phoneNumber = phoneNumber;
        this.emergencyContact = emergencyContact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String homepage) {
        this.webAddress = homepage;
    }

    public boolean isEmailAvailable() {
        return isEmailAvailable;
    }

    public void setEmailAvailable(boolean isEmailAvailable) {
        this.isEmailAvailable = isEmailAvailable;
    }

    public boolean isHomepageAvailable() {
        return isHomepageAvailable;
    }

    public void setHomepageAvailable(boolean isHomepageAvailable) {
        this.isHomepageAvailable = isHomepageAvailable;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    @Override
    public List<LabelFormatter> validate() {
        return new ArrayList<LabelFormatter>();
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.contactsForm";
    }

}