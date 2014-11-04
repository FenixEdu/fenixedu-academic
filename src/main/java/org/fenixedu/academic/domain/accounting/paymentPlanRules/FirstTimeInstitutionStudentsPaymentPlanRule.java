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
package org.fenixedu.academic.domain.accounting.paymentPlanRules;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;

public class FirstTimeInstitutionStudentsPaymentPlanRule implements PaymentPlanRule {

    FirstTimeInstitutionStudentsPaymentPlanRule() {
    }

    @Override
    public boolean isEvaluatedInNotSpecificPaymentRules() {
        return true;
    }

    @Override
    public boolean isAppliableFor(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {

        if (studentCurricularPlan.getRegistration().getStartExecutionYear() != executionYear) {
            return false;
        }

        final Student student = studentCurricularPlan.getRegistration().getStudent();
        return hasOnlyOneValidRegistration(student, studentCurricularPlan.getRegistration());
    }

    private boolean hasOnlyOneValidRegistration(final Student student, final Registration current) {

        if (student.getRegistrationsSet().size() > 1) {

            for (final Registration registration : student.getRegistrationsSet()) {

                if (registration != current) {

                    if (registration.getActiveStateType() != RegistrationStateType.CANCELED) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
