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
 */
package org.fenixedu.academic.service.filter;

import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author João Mota
 */
public class SummaryManagementToTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final SummaryManagementToTeacherAuthorizationFilter instance =
            new SummaryManagementToTeacherAuthorizationFilter();

    public SummaryManagementToTeacherAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(Summary summary, Professorship professorshipLogged) throws NotAuthorizedException {

        try {
            User userViewLogged = Authenticate.getUser();

            boolean executionCourseResponsibleLogged = professorshipLogged.isResponsibleFor();

            if (userViewLogged == null || professorshipLogged == null) {
                throw new NotAuthorizedException("error.summary.not.authorized");
            }
            if (executionCourseResponsibleLogged
                    && (summary.getProfessorship() != null && (!summary.getProfessorship().equals(professorshipLogged)))) {
                throw new NotAuthorizedException("error.summary.not.authorized");

            } else if (!executionCourseResponsibleLogged
                    && (summary.getProfessorship() == null || (!summary.getProfessorship().equals(professorshipLogged)))) {
                throw new NotAuthorizedException("error.summary.not.authorized");
            }

        } catch (RuntimeException ex) {
            throw new NotAuthorizedException("error.summary.not.authorized");
        }
    }

}
