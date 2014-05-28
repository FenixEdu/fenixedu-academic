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
 * Created on Nov 12, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 * 
 */
public class ExecutionCourseAndExamLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExecutionCourseAndExamLecturingTeacherAuthorizationFilter instance =
            new ExecutionCourseAndExamLecturingTeacherAuthorizationFilter();

    public ExecutionCourseAndExamLecturingTeacherAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(String executionCourseID, String evaluationID) throws NotAuthorizedException {
        User id = Authenticate.getUser();

        try {
            if ((id == null) || (id.getPerson().getPersonRolesSet() == null) || !id.getPerson().hasRole(getRoleType())
                    || !lecturesExecutionCourse(id, executionCourseID)
                    || !examBelongsExecutionCourse(id, executionCourseID, evaluationID)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }

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
                professorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
            }
            return professorship != null;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean examBelongsExecutionCourse(User id, String executionCourseID, String evaluationID) {
        if (executionCourseID == null || evaluationID == null) {
            return false;
        }
        try {
            ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

            if (executionCourse != null && evaluationID != null) {
                for (Evaluation associatedEvaluation : executionCourse.getAssociatedEvaluations()) {
                    if (associatedEvaluation.getExternalId().equals(evaluationID)) {
                        return true;
                    }
                }
                return false;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

}
