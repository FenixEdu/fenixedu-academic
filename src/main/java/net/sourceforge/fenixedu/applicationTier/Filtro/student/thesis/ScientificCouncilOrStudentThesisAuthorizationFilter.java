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
package net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class ScientificCouncilOrStudentThesisAuthorizationFilter extends StudentThesisAuthorizationFilter {

    public static final ScientificCouncilOrStudentThesisAuthorizationFilter instance =
            new ScientificCouncilOrStudentThesisAuthorizationFilter();

    @Override
    public void execute(Thesis thesis) throws NotAuthorizedException {
        final User userView = Authenticate.getUser();
        if (!userView.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
            super.execute(thesis);
        }
    }

}
