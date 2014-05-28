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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

//modified by gedl AT rnl dot IST dot uTl dot pT , September the 16th, 2003
//added the auth to a lecturing teacher

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class ReadShiftsByExecutionCourseIDAuthorizationFilter extends Filtro {

    public static final ReadShiftsByExecutionCourseIDAuthorizationFilter instance =
            new ReadShiftsByExecutionCourseIDAuthorizationFilter();

    public ReadShiftsByExecutionCourseIDAuthorizationFilter() {
    }

    public void execute(String executionCourseID) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        if ((((id != null && id.getPerson().getPersonRolesSet() != null && !containsRoleType(id.getPerson().getPersonRolesSet()))
                || (id != null && id.getPerson().getPersonRolesSet() != null && !hasPrivilege(id, executionCourseID))
                || (id == null) || (id.getPerson().getPersonRolesSet() == null)))
                && (!lecturesExecutionCourse(id, executionCourseID))) {
            throw new NotAuthorizedException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.RESOURCE_ALLOCATION_MANAGER);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasPrivilege(User id, String executionCourseID) {
        if (id.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            return true;
        }

        if (id.getPerson().hasRole(RoleType.COORDINATOR)) {

            final Person person = id.getPerson();

            ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

            // For all Associated Curricular Courses
            Iterator curricularCourseIterator = executionCourse.getAssociatedCurricularCourses().iterator();
            while (curricularCourseIterator.hasNext()) {
                CurricularCourse curricularCourse = (CurricularCourse) curricularCourseIterator.next();

                // Read All Execution Degrees for this Degree Curricular
                // Plan

                Collection executionDegrees = curricularCourse.getDegreeCurricularPlan().getExecutionDegrees();

                // Check if the Coordinator is the logged one
                Iterator executionDegreesIterator = executionDegrees.iterator();
                while (executionDegreesIterator.hasNext()) {
                    ExecutionDegree executionDegree = (ExecutionDegree) executionDegreesIterator.next();

                    // modified by Tânia Pousão
                    Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

                    if (coordinator != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean lecturesExecutionCourse(User id, String executionCourseID) {
        if (executionCourseID == null) {
            return false;
        }
        try {

            Teacher teacher = Teacher.readTeacherByUsername(id.getUsername());
            Professorship professorship = null;
            if (teacher != null) {
                ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
                teacher.getProfessorshipByExecutionCourse(executionCourse);
            }
            return professorship != null;

        } catch (Exception e) {
            return false;
        }
    }
}