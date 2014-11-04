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
package org.fenixedu.academic.service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.ui.services.UserInformationService;
import org.springframework.stereotype.Service;

@Service
public class FenixUserInformationService implements UserInformationService {

    @Override
    public Set<Group> getGroups(User user) {
        // TODO Implement this in a better way
        return Collections.emptySet();
//        Set<Role> personRolesSet = user.getPerson().getMainPersonRoles();
//        return personRolesSet.stream().map(Role::getRoleGroup).map(PersistentGroup::toGroup).collect(Collectors.toSet());
    }

    @Override
    public String getEmail(User user) {
        return user.getPerson().getEmailForSendingEmails();
    }

    @Override
    public String getContacts(User user) {
        return user.getPerson().getPartyContacts(Phone.class).stream()
                .filter(p -> p.getVisibleToStaff() != null && p.getVisibleToStaff()).map(c -> ((Phone) c).getNumber())
                .collect(Collectors.joining(","));
    }
}
