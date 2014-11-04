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
package org.fenixedu.academic.service.services.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.PartySocialSecurityNumber;
import org.fenixedu.academic.service.services.commons.searchers.SearchParties;
import org.fenixedu.bennu.core.domain.Bennu;

public class SearchPartyByNif extends SearchParties<Party> {

    @Override
    protected Collection<Party> search(String value, int size) {
        List<Party> result = new ArrayList<Party>();
        Collection<PartySocialSecurityNumber> partySocialSecurityNumbers = Bennu.getInstance().getPartySocialSecurityNumbersSet();
        for (PartySocialSecurityNumber partySocialSecurityNumber : partySocialSecurityNumbers) {
            if (partySocialSecurityNumber.getSocialSecurityNumber().startsWith(value)) {
                result.add(partySocialSecurityNumber.getParty());
            }
        }

        return result;
    }
}