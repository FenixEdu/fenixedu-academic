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
package org.fenixedu.academic.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

@GroupOperator("specificSpaceOccupationManagersGroup")
public class SpecificSpaceOccupationManagersGroup extends FenixGroupStrategy {

    private static final long serialVersionUID = 1L;

    @Override
    public Set<User> getMembers() {
        return SpaceUtils.allocatableSpaces().map(s -> s.getOccupationsGroupWithChainOfResponsability()).filter(g -> g != null)
                .flatMap(g -> g.getMembers().stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean isMember(User user) {
        if (user != null && RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(user)) {
            return true;
        }
        return Space.getSpaces().filter(Space::isActive).anyMatch(space -> space.isOccupationMember(user));
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }
}
