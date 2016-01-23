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
package org.fenixedu.academic.service.services.accounting;

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.events.dfa.DfaRegistrationEvent;
import org.fenixedu.academic.predicate.AcademicPredicates;

import pt.ist.fenixframework.Atomic;

public class AccountingEventsCreator {

    @Atomic
    static public void createInsuranceEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        studentCurricularPlan.getRegistration().getStudent().createInsuranceEvent(studentCurricularPlan, executionYear);
    }

    @Atomic
    static public void createGratuityEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        studentCurricularPlan.getRegistration().getStudent().createGratuityEvent(studentCurricularPlan, executionYear);
    }

    @Atomic
    static public void createAdministrativeOfficeFeeAndInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        studentCurricularPlan.getRegistration().getStudent()
                .createAdministrativeOfficeFeeEvent(studentCurricularPlan, executionYear);
    }

    @Atomic
    static public void createEnrolmentOutOfPeriodEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester, final Integer numberOfDelayDays) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        studentCurricularPlan.getRegistration().getStudent()
                .createEnrolmentOutOfPeriodEvent(studentCurricularPlan, executionSemester, numberOfDelayDays);
    }

    @Atomic
    @Deprecated
    static public void createDfaRegistrationEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        new DfaRegistrationEvent(studentCurricularPlan.getDegree().getAdministrativeOffice(), studentCurricularPlan.getPerson(),
                studentCurricularPlan.getRegistration(), executionYear);
    }

}