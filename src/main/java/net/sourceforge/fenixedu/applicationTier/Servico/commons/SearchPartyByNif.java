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
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchParties;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;

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