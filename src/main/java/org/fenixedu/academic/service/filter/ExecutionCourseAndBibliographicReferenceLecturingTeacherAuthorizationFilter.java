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

import java.util.Iterator;

import org.fenixedu.academic.domain.BibliographicReference;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter instance =
            new ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter();

    public ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter() {

    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(String bibliographicReferenceID) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        if ((id == null) || !getRoleType().isMember(id.getPerson().getUser())
                || !bibliographicReferenceBelongsToTeacherExecutionCourse(id, bibliographicReferenceID)) {
            throw new NotAuthorizedException();
        }
    }

    private boolean bibliographicReferenceBelongsToTeacherExecutionCourse(User id, String bibliographicReferenceID) {
        if (bibliographicReferenceID == null) {
            return false;
        }

        boolean result = false;
        final BibliographicReference bibliographicReference = FenixFramework.getDomainObject(bibliographicReferenceID);
        final Teacher teacher = Teacher.readTeacherByUsername(id.getUsername());

        if (bibliographicReference != null && teacher != null) {
            final ExecutionCourse executionCourse = bibliographicReference.getExecutionCourse();
            final Iterator associatedProfessorships = teacher.getProfessorshipsIterator();
            // Check if Teacher has a professorship to ExecutionCourse
            // BibliographicReference
            while (associatedProfessorships.hasNext()) {
                Professorship professorship = (Professorship) associatedProfessorships.next();
                if (professorship.getExecutionCourse().equals(executionCourse)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

}