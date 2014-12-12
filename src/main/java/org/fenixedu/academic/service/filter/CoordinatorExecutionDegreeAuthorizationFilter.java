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
package org.fenixedu.academic.service.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class CoordinatorExecutionDegreeAuthorizationFilter extends Filtro {

    public static final CoordinatorExecutionDegreeAuthorizationFilter instance =
            new CoordinatorExecutionDegreeAuthorizationFilter();

    public void execute(String executionDegreeId) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        if ((id != null && !containsRoleType(id)) || (id != null && !hasPrivilege(id, executionDegreeId)) || (id == null)) {
            throw new NotAuthorizedException();
        }
    }

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.RESOURCE_ALLOCATION_MANAGER);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    private boolean hasPrivilege(User id, String executionDegreeId) {
        if (RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(id.getPerson().getUser())) {
            return true;
        }

        if (RoleType.COORDINATOR.isMember(id.getPerson().getUser())) {
            String executionDegreeID = executionDegreeId;

            if (executionDegreeID == null) {
                return false;
            }
            final Person person = id.getPerson();
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
            if (executionDegree == null) {
                return false;
            }
            Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

            if (coordinator != null) {
                return true;
            }
        }
        return false;
    }

}