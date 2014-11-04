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
package org.fenixedu.academic.domain.phd.migration.common.exceptions;

import java.util.Set;

import org.fenixedu.academic.domain.Person;

public class PossiblePersonCandidatesException extends ParseException {

    public PossiblePersonCandidatesException(Set<Person> possibleCandidates) {
        super(getPersonNames(possibleCandidates));
    }

    static private String getPersonNames(Set<Person> possibleCandidates) {
        StringBuilder builder = new StringBuilder();

        for (Person person : possibleCandidates) {
            builder.append(person.getName() + " ;");
        }

        return builder.toString();
    }
}
