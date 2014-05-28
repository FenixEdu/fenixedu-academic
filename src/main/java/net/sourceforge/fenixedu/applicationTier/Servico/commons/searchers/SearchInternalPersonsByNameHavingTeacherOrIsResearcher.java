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

import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class SearchInternalPersonsByNameHavingTeacherOrIsResearcher extends SearchParties<PersonName> {

    @Override
    protected Collection<PersonName> search(String value, int size) {
        final Collection<PersonName> result = new HashSet<PersonName>();
        for (final PersonName personName : PersonName.findInternalPerson(value, size)) {
            if (personName.getPerson().hasUser()
                    && (personName.getPerson().hasTeacher() || personName.getPerson().hasRole(RoleType.RESEARCHER))) {
                result.add(personName);
            }
        }
        return result;
    }

}
