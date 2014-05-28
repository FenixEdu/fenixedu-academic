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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

public class PersonAutoCompleteProvider implements AutoCompleteProvider<Person> {

    @Override
    public Collection<Person> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final String searchValue = StringNormalizer.normalize(value);

        final List<Person> result = new ArrayList<Person>();
        result.addAll(Person.findInternalPerson(value));
        result.add(Person.findByUsername(searchValue));
        return result;
    }

    private boolean match(final String stringToMatch, final String content) {
        final String normalizedContent = StringNormalizer.normalize(content);
        return normalizedContent.indexOf(stringToMatch) >= 0;
    }

}
