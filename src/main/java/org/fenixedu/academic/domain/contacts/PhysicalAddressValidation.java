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

import java.util.function.Predicate;

import pt.ist.fenixframework.Atomic;

public class PhysicalAddressValidation extends PhysicalAddressValidation_Base {

    public static final Predicate<PartyContactValidation> PREDICATE = new Predicate<PartyContactValidation>() {

        @Override
        public boolean test(PartyContactValidation t) {
            return t instanceof PhysicalAddressValidation;
        }

    };

    public static final Predicate<PartyContactValidation> PREDICATE_FILE = new Predicate<PartyContactValidation>() {

        @Override
        public boolean test(PartyContactValidation t) {
            return PREDICATE.test(t) && ((PhysicalAddressValidation) t).getFile() != null;
        }

    };

    public PhysicalAddressValidation(PhysicalAddress physicalAddress) {
        super();
        setPartyContact(physicalAddress);
    }

    @Atomic
    public void setFile(String filename, String displayName, byte[] content) {
        new PhysicalAddressValidationFile(this, filename, displayName, content);
    }

}
