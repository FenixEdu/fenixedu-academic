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

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.predicates.Predicate;

public class PhysicalAddressValidation extends PhysicalAddressValidation_Base {

    public static final Predicate<PartyContactValidation> PREDICATE = new Predicate<PartyContactValidation>() {

        @Override
        public boolean eval(PartyContactValidation t) {
            return t instanceof PhysicalAddressValidation;
        }

    };

    public static final Predicate<PartyContactValidation> PREDICATE_FILE = new Predicate<PartyContactValidation>() {

        @Override
        public boolean eval(PartyContactValidation t) {
            return PREDICATE.eval(t) && ((PhysicalAddressValidation) t).hasFile();
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

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasFile() {
        return getFile() != null;
    }

}
