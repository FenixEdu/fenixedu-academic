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
package org.fenixedu.academic.domain.space;

import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.spaces.domain.Space;

public abstract class SpaceOccupation extends SpaceOccupation_Base {

    protected SpaceOccupation() {
        super();
    }

//    public abstract Group getAccessGroup();

//    public void checkPermissionsToManageSpaceOccupations() {
//        User user = Authenticate.getUser();
//        Space r = getSpace();
//        if (SpaceUtils.personIsSpacesAdministrator(user.getPerson()) || r.getManagementGroupWithChainOfResponsability() != null
//                && r.getManagementGroupWithChainOfResponsability().isMember(user)) {
//            return;
//        }
//
//        final Group group = getAccessGroup();
//        if (group != null && group.isMember(user)) {
//            return;
//        }
//
//        throw new DomainException("error.logged.person.not.authorized.to.make.operation");
//    }

//    public void checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManager() {
//        User user = Authenticate.getUser();
//        if (getSpace() == null) {
//            return;
//        }
//        final Group group = getAccessGroup();
//        if (group != null && group.isMember(user)) {
//            return;
//        }
//
//        throw new DomainException("error.logged.person.not.authorized.to.make.operation");
//    }

    public Space getSpace() {
        Set<Space> spaces = getSpaces();
        return spaces.isEmpty() ? null : spaces.iterator().next();
    }
}
