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

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

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
            return AccessControl.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL);
        }

    };

    public static final AccessControlPredicate<Thesis> isScientificCouncilOrCoordinatorAndNotOrientatorOrCoorientator =
            new AccessControlPredicate<Thesis>() {

                @Override
                public boolean evaluate(Thesis thesis) {
                    return isScientificCouncil.evaluate(thesis) || thesis.isCoordinatorAndNotOrientator();
                }

            };

    public static final AccessControlPredicate<Thesis> student = new AccessControlPredicate<Thesis>() {

        @Override
        public boolean evaluate(Thesis thesis) {
            Person person = AccessControl.getPerson();

            return person.getStudent() == thesis.getStudent() && thesis.isWaitingConfirmation();
        }

    };

    public static final AccessControlPredicate<Thesis> studentOrAcademicAdministrativeOfficeOrScientificCouncil =
            new AccessControlPredicate<Thesis>() {

                @Override
                public boolean evaluate(Thesis thesis) {
                    Person person = AccessControl.getPerson();
                    return (person.getStudent() == thesis.getStudent() && thesis.isWaitingConfirmation())
                            || (AcademicAuthorizationGroup.getProgramsForOperation(person, AcademicOperationType.MANAGE_MARKSHEETS).contains(thesis.getDegree()))
                            || person.hasRole(RoleType.SCIENTIFIC_COUNCIL);
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

    public static final AccessControlPredicate<Thesis> isScientificCommissionOrScientificCouncil =
            new AccessControlPredicate<Thesis>() {

                @Override
                public boolean evaluate(final Thesis thesis) {
                    return isScientificCommission.evaluate(thesis) || isScientificCouncil.evaluate(thesis);
                }

            };

}
