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
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class CreditsPredicates {

    static public final AccessControlPredicate<Credits> DELETE = new AccessControlPredicate<Credits>() {

        @Override
        public boolean evaluate(final Credits credits) {

            boolean authorizedIfConcluded =
                    AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION).contains(
                            credits.getStudentCurricularPlan().getDegree());

            for (final Dismissal dismissal : credits.getDismissalsSet()) {
                if (dismissal.getParentCycleCurriculumGroup() != null && dismissal.getParentCycleCurriculumGroup().isConcluded()
                        && !authorizedIfConcluded) {
                    return false;
                }
            }

            return true;
        }
    };

}
