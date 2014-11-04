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

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.PhoneValidationUtils;

import org.apache.commons.lang.RandomStringUtils;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.PhoneUtil;

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

            if (PhoneUtil.isFixedNumber(number) || !PhoneUtil.isPortugueseNumber(number)) {
                PhoneValidationUtils.getInstance().makeCall(PhoneUtil.getInternacionalFormatNumber(number), token, language);
            } else if (PhoneUtil.isMobileNumber(number)) {
                PhoneValidationUtils.getInstance().sendSMS(PhoneUtil.getInternacionalFormatNumber(number), token);
            }

            person.incValidationRequest();
        }
    }
}
