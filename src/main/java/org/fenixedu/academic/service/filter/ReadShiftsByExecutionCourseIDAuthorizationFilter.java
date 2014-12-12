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
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
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
        if ((((id != null && !containsRoleType(id)) || (id != null && !hasPrivilege(id, executionCourseID)) || (id == null)))
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
        if (RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(id.getPerson().getUser())) {
            return true;
        }

        if (RoleType.COORDINATOR.isMember(id.getPerson().getUser())) {

            final Person person = id.getPerson();

            ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

            // For all Associated Curricular Courses
            Iterator curricularCourseIterator = executionCourse.getAssociatedCurricularCoursesSet().iterator();
            while (curricularCourseIterator.hasNext()) {
                CurricularCourse curricularCourse = (CurricularCourse) curricularCourseIterator.next();

                // Read All Execution Degrees for this Degree Curricular
                // Plan

                Collection executionDegrees = curricularCourse.getDegreeCurricularPlan().getExecutionDegreesSet();

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