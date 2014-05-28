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
/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 * 
 */
public class DegreeCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final DegreeCoordinatorAuthorizationFilter instance = new DegreeCoordinatorAuthorizationFilter();

    public DegreeCoordinatorAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(String executionDegreeId) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        try {
            if ((id == null) || (id.getPerson().getPersonRolesSet() == null) || !id.getPerson().hasRole(getRoleType())
                    || !isCoordinatorOfExecutionDegree(id, executionDegreeId)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean isCoordinatorOfExecutionDegree(User id, String executionDegreeId) {
        boolean result = false;
        if (executionDegreeId == null) {
            return result;
        }
        try {
            final Person person = id.getPerson();
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
            if (executionDegree == null) {
                return false;
            }
            Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

            result = coordinator != null;

        } catch (Exception e) {
            return false;
        }

        return result;
    }

}