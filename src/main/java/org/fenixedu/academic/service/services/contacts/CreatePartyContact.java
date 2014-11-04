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
package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import pt.ist.fenixframework.Atomic;

public class CreatePartyContact {

    @Atomic
    public static PartyContact run(PartyContactBean contactBean, final boolean toBeValidated) {
        if (contactBean.hasPartyContact()) {
            return null;
        }
        final PartyContact createNewContact = contactBean.createNewContact();
        createNewContact.getParty().logCreateContact(createNewContact);
        if (toBeValidated) {
            createNewContact.triggerValidationProcessIfNeeded();
        } else {
            if (createNewContact instanceof PhysicalAddress) {
                ((PhysicalAddress) createNewContact).setValid();
            }
        }
        contactBean.setContact(createNewContact);
        return createNewContact;
    }
}
