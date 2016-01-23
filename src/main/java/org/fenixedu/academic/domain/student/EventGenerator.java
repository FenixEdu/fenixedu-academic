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
package org.fenixedu.academic.domain.student;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.events.AccountingEventsManager;

public class EventGenerator {

    public static void generateNecessaryEvents(StudentCurricularPlan studentCurricularPlan, Person person,
            ExecutionYear executionYear) {

        if (studentCurricularPlan.getDegree().getDegreeType().isAdvancedFormationDiploma()) {

            final AccountingEventsManager accountingEventsManager = new AccountingEventsManager();
            final ExecutionYear executionYearToCreateEvents =
                    executionYear != null ? executionYear : ExecutionYear.readCurrentExecutionYear();

            accountingEventsManager.createGratuityEvent(studentCurricularPlan, executionYearToCreateEvents, false);

            accountingEventsManager.createAdministrativeOfficeFeeAndInsuranceEvent(studentCurricularPlan,
                    executionYearToCreateEvents);
        }
    }
}
