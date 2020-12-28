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
package org.fenixedu.academic.domain.studentCurriculum;

import static org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult.createFalse;
import static org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult.createTrue;

import java.util.Collections;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.service.AcademicPermissionService;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.LocalDate;

public class StudentCurricularPlanEnrolmentPreConditions {

    static public class EnrolmentPreConditionResult {
        private boolean valid = false;
        private String message;
        private String[] args;

        private EnrolmentPreConditionResult valid(final boolean value) {
            this.valid = value;
            return this;
        }

        public boolean isValid() {
            return valid;
        }

        private EnrolmentPreConditionResult message(final String message, final String... args) {
            this.message = message;
            this.args = args;
            return this;
        }

        public String message() {
            return this.message;
        }

        public String[] args() {
            return this.args;
        }

        static public EnrolmentPreConditionResult createTrue() {
            return new EnrolmentPreConditionResult().valid(true);
        }

        static public EnrolmentPreConditionResult createFalse(final String message, final String... args) {
            return new EnrolmentPreConditionResult().valid(false).message(message, args);
        }
    }

    static public EnrolmentPreConditionResult checkPreConditionsToEnrol(StudentCurricularPlan scp, ExecutionInterval interval) {
        return checkDebts(scp);
    }

    /**
     *
     * Check if student has any debts that prevent him to enrol in curricular
     * courses
     *
     * @param scp
     * @return EnrolmentPreConditionResult
     */
    static EnrolmentPreConditionResult checkDebts(StudentCurricularPlan scp) {
        final Person authenticatedPerson = Authenticate.getUser().getPerson();
        final boolean hasAcademicalAuthorizationToEnrol =
                AcademicAccessRule.isMember(Authenticate.getUser(), AcademicOperationType.STUDENT_ENROLMENTS,
                        Collections.singleton(scp.getDegree()), Collections.singleton(scp.getAdministrativeOffice()))
                        || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_ENROLMENTS", scp.getDegree(), Authenticate.getUser());

        final boolean isStudentEnrolling = authenticatedPerson.getStudent() != null
                && authenticatedPerson.getStudent() == scp.getRegistration().getStudent();

        if (hasAcademicalAuthorizationToEnrol && !isStudentEnrolling) {
            return createTrue();
        }

        if (TreasuryBridgeAPIFactory.implementation().isAcademicalActsBlocked(scp.getPerson(), new LocalDate())) {
            return createFalse("error.StudentCurricularPlan.cannot.enrol.with.debts.for.previous.execution.years");
        }

        return createTrue();
    }

}
