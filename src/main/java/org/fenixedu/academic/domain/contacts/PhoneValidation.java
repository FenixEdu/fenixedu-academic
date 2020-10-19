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

import org.apache.commons.lang.RandomStringUtils;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.struts.action.externalServices.PhoneValidationUtils;
import org.fenixedu.academic.util.PhoneUtil;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.messaging.smsdispatch.SMSMessage;

import pt.ist.fenixframework.Atomic;

public class PhoneValidation extends PhoneValidation_Base {

    public PhoneValidation(PartyContact contact) {
        super();
        super.init(contact);
        assert (contact instanceof Phone || contact instanceof MobilePhone);
    }

    public String getNumber() {
        final PartyContact partyContact = getPartyContact();
        if (partyContact instanceof Phone) {
            return ((Phone) partyContact).getNumber();
        }
        if (partyContact instanceof MobilePhone) {
            return ((MobilePhone) partyContact).getNumber();
        }
        return null;
    }

    public void generateToken() {
        if (getToken() == null) {
            setToken(RandomStringUtils.random(4, false, true));
        }
    }

    @Override
    @Atomic
    public void triggerValidationProcess() {
        if (!isValid()) {
            generateToken();
            final String number = getNumber();
            final String token = getToken();
            final Person person = (Person) getPartyContact().getParty();
            final Country country = person.getCountry();
            final String language = Country.isCPLPCountry(country) ? "pt" : "en";

            if (getPartyContact() instanceof MobilePhone) {
                final String message = StringNormalizer.normalizePreservingCapitalizedLetters(BundleUtil.getString(
                        "resources.GlobalResources", "sms.confirmationCode", token));
                SMSMessage.getInstance().sendSMS(PhoneUtil.getInternacionalFormatNumber(number), message);
            } else if (getPartyContact() instanceof Phone) {
                PhoneValidationUtils.getInstance().makeCall(PhoneUtil.getInternacionalFormatNumber(number), token, language);
            }

            person.incValidationRequest();
        }
    }
}
