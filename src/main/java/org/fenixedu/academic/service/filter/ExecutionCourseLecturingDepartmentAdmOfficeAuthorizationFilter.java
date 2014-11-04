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

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.dto.SummariesManagementBean;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class ExecutionCourseLecturingDepartmentAdmOfficeAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExecutionCourseLecturingDepartmentAdmOfficeAuthorizationFilter instance =
            new ExecutionCourseLecturingDepartmentAdmOfficeAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE;
    }

    public void execute(SummariesManagementBean bean) throws NotAuthorizedException {
        User id = Authenticate.getUser();

        try {
            if ((id == null) || !id.getPerson().hasRole(getRoleType()) || !lecturesExecutionCourse(id, bean)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean lecturesExecutionCourse(User id, SummariesManagementBean bean) {
        final ExecutionCourse executionCourse = bean.getExecutionCourse();
        final Person person = bean.getProfessorshipLogged().getPerson();
        return person.getProfessorshipByExecutionCourse(executionCourse) != null;
    }
}
