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
package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.applicationTier.Servico.person.PersonSearcher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

import org.joda.time.YearMonthDay;

public class SearchAllActiveParties extends SearchParties<Party> {

    @Override
    protected Collection<Party> search(String value, int size) {
        Set<Party> result = new HashSet<Party>();
        result.addAll(new PersonSearcher().bestEffortQuery(value).search(size).collect(Collectors.toSet()));
        YearMonthDay currentDate = new YearMonthDay();
        for (UnitName unitName : UnitName.find(value, size)) {
            if (unitName.getUnit().isActive(currentDate)) {
                result.add(unitName.getUnit());
            }
        }

        return result;
    }

}
