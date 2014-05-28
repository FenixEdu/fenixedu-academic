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
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListByCurricularCourseAuthorizationFilter extends Filtro {

    public static final StudentListByCurricularCourseAuthorizationFilter instance =
            new StudentListByCurricularCourseAuthorizationFilter();

    /**
     * 
     */
    public StudentListByCurricularCourseAuthorizationFilter() {
        super();
    }

    public void execute(String curricularCourseID) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        if ((id != null && id.getPerson().getPersonRolesSet() != null && !containsRoleType(id.getPerson().getPersonRolesSet()))
                || (id != null && id.getPerson().getPersonRolesSet() != null && !hasPrivilege(id, curricularCourseID))
                || (id == null) || (id.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasPrivilege(User id, String curricularCourseID) {

        CurricularCourse curricularCourse = null;

        // Read The DegreeCurricularPlan
        try {
            curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseID);
        } catch (Exception e) {
            return false;
        }

        if (curricularCourse == null) {
            return false;
        }

        if (id.getPerson().hasRole(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            if (curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
                return true;
            }
            return false;

        }

        if (id.getPerson().hasRole(RoleType.COORDINATOR)) {
            Collection executionDegrees = curricularCourse.getDegreeCurricularPlan().getExecutionDegrees();
            if (executionDegrees == null || executionDegrees.isEmpty()) {
                return false;
            }
            // IMPORTANT: It's assumed that the coordinator for a Degree is
            // ALWAYS the same
            // modified by Tânia Pousão
            Collection<Coordinator> coodinatorsList =
                    ((ExecutionDegree) executionDegrees.iterator().next()).getCoordinatorsList();
            if (coodinatorsList == null) {
                return false;
            }
            Iterator<Coordinator> listIterator = coodinatorsList.iterator();
            while (listIterator.hasNext()) {
                Coordinator coordinator = listIterator.next();

                if (coordinator.getPerson() == id.getPerson()) {
                    return true;
                }
            }

            return false;
        }
        return false;
    }
}