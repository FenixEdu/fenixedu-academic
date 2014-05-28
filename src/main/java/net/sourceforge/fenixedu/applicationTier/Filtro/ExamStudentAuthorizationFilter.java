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
 * Created on Nov 14, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 * 
 */
public class ExamStudentAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExamStudentAuthorizationFilter instance = new ExamStudentAuthorizationFilter();

    public ExamStudentAuthorizationFilter() {
        super();
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.STUDENT;
    }

    public void execute(String username, String writtenEvaluationOID) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        try {
            if ((id == null) || (id.getPerson().getPersonRolesSet() == null) || !id.getPerson().hasRole(getRoleType())
                    || !attendsEvaluationExecutionCourse(id, username, writtenEvaluationOID)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean attendsEvaluationExecutionCourse(User id, String studentUsername, String writtenEvaluationOID) {
        if (writtenEvaluationOID == null) {
            return false;
        }
        try {
            final Evaluation evaluation = FenixFramework.getDomainObject(writtenEvaluationOID);

            for (final ExecutionCourse executionCourse : evaluation.getAssociatedExecutionCourses()) {
                for (final Attends attend : executionCourse.getAttends()) {
                    if (attend.getRegistration().getPerson().getUsername().equals(studentUsername)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
}
