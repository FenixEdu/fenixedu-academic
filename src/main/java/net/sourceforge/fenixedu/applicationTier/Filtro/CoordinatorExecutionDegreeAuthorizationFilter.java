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
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

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
        if ((id != null && id.getPerson().getPersonRolesSet() != null && !containsRoleType(id.getPerson().getPersonRolesSet()))
                || (id != null && id.getPerson().getPersonRolesSet() != null && !hasPrivilege(id, executionDegreeId))
                || (id == null) || (id.getPerson().getPersonRolesSet() == null)) {
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
        if (id.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            return true;
        }

        if (id.getPerson().hasRole(RoleType.COORDINATOR)) {
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