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

}
