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
package org.fenixedu.academic.domain;

import org.fenixedu.bennu.core.domain.UserProfile;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class EmergencyContact extends EmergencyContact_Base {

    protected EmergencyContact() {
        super();
    }

    protected EmergencyContact(UserProfile profile, String contact) {
        super();
        setContact(contact);
        setProfile(profile);
    }

    @Atomic(mode = TxMode.WRITE)
    public static void updateEmergencyContact(UserProfile profile, String contact) {
        if (profile.getEmergencyContact() == null) {
            new EmergencyContact(profile, contact);
        } else {
            profile.getEmergencyContact().setContact(contact);
        }
    }
}
