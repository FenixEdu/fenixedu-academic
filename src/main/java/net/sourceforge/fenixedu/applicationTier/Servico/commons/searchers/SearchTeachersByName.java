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
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import com.google.common.base.Predicate;

public class SearchTeachersByName implements AutoCompleteProvider<Teacher> {

    protected Collection<Teacher> search(final String value, final int size) {
        final Predicate<Person> predicate = new Predicate<Person>() {
            @Override
            public boolean apply(final Person person) {
                return person.hasTeacher();
            }
        };
        final Collection<Person> people = Person.findPerson(value, size, predicate);
        final List<Teacher> teachers = new ArrayList<Teacher>();
        for (final Person person : people) {
            final Teacher teacher = person.getTeacher();
            teachers.add(teacher);
        }
        return teachers;
    }

    @Override
    public Collection<Teacher> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return search(value, maxCount);
    }

}
