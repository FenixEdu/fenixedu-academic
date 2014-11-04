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
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public class EventGenerator {

    public static void generateNecessaryEvents(StudentCurricularPlan studentCurricularPlan, Person person,
            ExecutionYear executionYear) {

        final AdministrativeOffice administrativeOffice = studentCurricularPlan.getDegree().getAdministrativeOffice();

        switch (studentCurricularPlan.getDegree().getDegreeType()) {

        case BOLONHA_ADVANCED_FORMATION_DIPLOMA:

            final AccountingEventsManager accountingEventsManager = new AccountingEventsManager();
            final ExecutionYear executionYearToCreateEvents =
                    executionYear != null ? executionYear : ExecutionYear.readCurrentExecutionYear();

            accountingEventsManager.createGratuityEvent(studentCurricularPlan, executionYearToCreateEvents, false);

            new DfaRegistrationEvent(administrativeOffice, person, studentCurricularPlan.getRegistration());

            accountingEventsManager.createInsuranceEvent(studentCurricularPlan, executionYearToCreateEvents, false);

            break;
        default:
            break;

        }

    }
}
