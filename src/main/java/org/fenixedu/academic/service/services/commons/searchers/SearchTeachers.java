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
package org.fenixedu.academic.service.services.commons.searchers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import pt.ist.fenixframework.Atomic;

public class SearchTeachers implements AutoCompleteProvider<Teacher> {

    protected Collection<Teacher> search(final String value, final int size) {
        Set<Teacher> matches = new HashSet<>();
        User user = User.findByUsername(value);
        if (user != null && user.getProfile().getPerson() != null && user.getProfile().getPerson().getTeacher() != null) {
            matches.add(user.getProfile().getPerson().getTeacher());
        }
        matches.addAll(UserProfile.searchByName(value, size).map(UserProfile::getPerson).filter(Objects::nonNull)
                .map(p -> p.getTeacher()).filter(Objects::nonNull).collect(Collectors.toSet()));
        return matches;
    }

    @Override
    @Atomic
    public Collection<Teacher> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return search(value, maxCount);
    }

}
