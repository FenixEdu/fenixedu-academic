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
/*
 * Created on 19/Mai/2003
 * 
 * 
 */
package org.fenixedu.academic.service.filter;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.SummariesManagementBean;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExecutionCourseLecturingTeacherAuthorizationFilter instance =
            new ExecutionCourseLecturingTeacherAuthorizationFilter();

    public ExecutionCourseLecturingTeacherAuthorizationFilter() {

    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(String executionCourseCode) throws NotAuthorizedException {
        execute(getExecutionCourse(executionCourseCode));
    }

    public void execute(SummariesManagementBean executionCourseCode) throws NotAuthorizedException {
        execute(getExecutionCourse(executionCourseCode));
    }

    public void execute(ExecutionCourse executionCourse) throws NotAuthorizedException {
        User id = Authenticate.getUser();

        try {
            if ((id == null) || !lecturesExecutionCourse(id, executionCourse)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean lecturesExecutionCourse(User id, ExecutionCourse executionCourse) {
        if (executionCourse == null) {
            return false;
        }

        final Person person = id.getPerson();
        if (person == null) {
            return false;
        }
        for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
            if (professorship.getPerson() == id.getPerson()) {
                return true;
            }
        }
        return false;
    }

    private ExecutionCourse getExecutionCourse(Object argument) {
        if (argument == null) {
            return null;

        } else if (argument instanceof ExecutionCourse) {
            return (ExecutionCourse) argument;

        } else if (argument instanceof InfoExecutionCourse) {
            final InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) argument;
            return FenixFramework.getDomainObject(infoExecutionCourse.getExternalId());

        } else if (argument instanceof String) {
            final String executionCourseID = (String) argument;
            return FenixFramework.getDomainObject(executionCourseID);

        } else if (argument instanceof SummariesManagementBean) {
            return ((SummariesManagementBean) argument).getExecutionCourse();

        } else {
            return null;
        }
    }

}