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
package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
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
    static public void createDfaRegistrationEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        new DfaRegistrationEvent(studentCurricularPlan.getDegree().getAdministrativeOffice(), studentCurricularPlan.getPerson(),
                studentCurricularPlan.getRegistration(), executionYear);
    }

}