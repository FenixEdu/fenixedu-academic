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
package org.fenixedu.academic.service.services.contacts;

import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.dto.contacts.PartyContactBean;

import pt.ist.fenixframework.Atomic;

public class EditPartyContact {

    @Atomic
    public static Boolean run(PartyContactBean contactBean, final boolean toBeValidated) {
        // return type true if value changed
        Boolean wasChanged = contactBean.edit();
        if (wasChanged) {
            final PartyContact contact = contactBean.getContact();
            if (toBeValidated) {
                contact.triggerValidationProcessIfNeeded();
            } else {
                if (contact instanceof PhysicalAddress || contact instanceof WebAddress) {
                    contact.setValid();
                }
            }
        }
        return wasChanged;
    }
}
