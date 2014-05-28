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

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;

public class SearchPeopleByNameOrISTID extends SearchPeople {

    @Override
    protected Collection<Person> search(final String value, final int size) {
        if (value.length() > 3 && value.substring(0, 3).equals("ist")) {
            ArrayList<Person> result = new ArrayList<Person>();
            Person person = Person.readPersonByUsername(value);
            if (person != null) {
                result.add(person);
            }
            return result;
        } else {
            return super.search(value, size);

        }
    }

}
