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
package org.fenixedu.academic.predicate;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.thesis.Thesis;

public class ThesisPredicates {

    public static final AccessControlPredicate<Thesis> waitingConfirmation = new AccessControlPredicate<Thesis>() {

        @Override
        public boolean evaluate(Thesis thesis) {
            return thesis.isWaitingConfirmation() || isScientificCouncil.evaluate(thesis);
        }

    };

    public static final AccessControlPredicate<Thesis> isScientificCouncil = new AccessControlPredicate<Thesis>() {

        @Override
        public boolean evaluate(Thesis thesis) {
            return RoleType.SCIENTIFIC_COUNCIL.isMember(AccessControl.getPerson().getUser());
        }

    };

    public static final AccessControlPredicate<Thesis> isScientificCouncilOrCoordinatorAndNotOrientatorOrCoorientator =
            new AccessControlPredicate<Thesis>() {

                @Override
                public boolean evaluate(Thesis thesis) {
                    return isScientificCouncil.evaluate(thesis) || thesis.isCoordinatorAndNotOrientator();
                }

            };

    public static final AccessControlPredicate<Thesis> studentOrAcademicAdministrativeOfficeOrScientificCouncil =
            new AccessControlPredicate<Thesis>() {

                @Override
                public boolean evaluate(Thesis thesis) {
                    Person person = AccessControl.getPerson();
                    return (person.getStudent() == thesis.getStudent() && thesis.isWaitingConfirmation())
                            || (AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_MARKSHEETS,
                                    thesis.getDegree(), person.getUser()))
                            || RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser());
                }

            };

    public static final AccessControlPredicate<Thesis> isScientificCommission = new AccessControlPredicate<Thesis>() {

        @Override
        public boolean evaluate(final Thesis thesis) {
            final Enrolment enrolment = thesis.getEnrolment();
            final ExecutionYear executionYear = enrolment.getExecutionYear();
            final DegreeCurricularPlan degreeCurricularPlan = enrolment.getDegreeCurricularPlanOfDegreeModule();
            return degreeCurricularPlan.isScientificCommissionMember(executionYear);
        }

    };

}
