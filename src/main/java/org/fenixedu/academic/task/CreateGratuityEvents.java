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
package org.fenixedu.academic.task;

import java.util.Locale;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.events.AccountingEventsManager;
import org.fenixedu.academic.util.InvocationResult;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@Deprecated
@Task(englishTitle = "CreateGratuityEvents", readOnly = true)
public class CreateGratuityEvents extends CronTask {

    private int GratuityEvent_TOTAL_CREATED = 0;

    private void generateGratuityEventsForAllStudents(final ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            for (final StudentCurricularPlan studentCurricularPlan : executionDegree.getDegreeCurricularPlan()
                    .getStudentCurricularPlansSet()) {
                generateGratuityEvents(executionYear, studentCurricularPlan);
            }
        }
    }

    private void generateGratuityEvents(final ExecutionYear executionYear, final StudentCurricularPlan studentCurricularPlan) {

        if (!studentCurricularPlan.isBolonhaDegree() || !studentCurricularPlan.hasRegistration()) {
            return;
        }

        generateGratuityEvent(studentCurricularPlan, executionYear);
    }

    @Atomic(mode = TxMode.WRITE)
    private void generateGratuityEvent(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
        try {
            final AccountingEventsManager manager = new AccountingEventsManager();

            final InvocationResult result = manager.createGratuityEvent(studentCurricularPlan, executionYear);

            if (result.isSuccess()) {
                GratuityEvent_TOTAL_CREATED++;
            }
        } catch (Exception e) {
            taskLog("Exception on student curricular plan with oid : %s\n", studentCurricularPlan.getExternalId());
            e.printStackTrace();
        }
    }

    @Override
    public void runTask() {
        I18N.setLocale(new Locale("PT", "pt"));
        generateGratuityEventsForAllStudents(ExecutionYear.readCurrentExecutionYear());
        taskLog("Created %s GratuityEvent events\n", GratuityEvent_TOTAL_CREATED);
    }
}
