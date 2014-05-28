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
package pt.utl.ist.scripts.process.cron.accounting;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.InvocationResult;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@Task(englishTitle = "CreateAdministrativeOfficeFeeEvent", readOnly = true)
public class CreateAdministrativeOfficeFeeEvent extends CronTask {
    private int AdministrativeOfficeFee_TOTAL_CREATED = 0;
    private int InsuranceEvent_TOTAL_CREATED = 0;

    @Atomic(mode = TxMode.WRITE)
    private void createAdministrativeOfficeFeeEvent(StudentCurricularPlan scp, ExecutionYear executionYear) {
        try {

            final AccountingEventsManager manager = new AccountingEventsManager();
            final InvocationResult result;
            if (scp.getAdministrativeOffice().isDegree()) {
                result = manager.createAdministrativeOfficeFeeAndInsuranceEvent(scp, executionYear);

            } else if (scp.getAdministrativeOffice().isMasterDegree()) {
                result = manager.createInsuranceEvent(scp, executionYear);

            } else {
                throw new RuntimeException();
            }

            if (result.isSuccess()) {
                AdministrativeOfficeFee_TOTAL_CREATED++;
            }
        } catch (Exception e) {
            taskLog("Exception on student curricular plan with oid : %s\n", scp.getExternalId());
            e.printStackTrace(getTaskLogWriter());
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void createInsuranceEvent(Person person, ExecutionYear executionYear) {
        try {
            final AccountingEventsManager manager = new AccountingEventsManager();
            final InvocationResult result = manager.createInsuranceEvent(person, executionYear);

            if (result.isSuccess()) {
                InsuranceEvent_TOTAL_CREATED++;
            }
        } catch (Exception e) {
            taskLog("Exception on person with oid : %s\n", person.getExternalId());
            e.printStackTrace(getTaskLogWriter());
        }
    }

    @Override
    public void runTask() {
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        for (final Student student : Bennu.getInstance().getStudentsSet()) {
            for (final Registration registration : student.getRegistrationsSet()) {
                final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
                if (studentCurricularPlan != null) {
                    createAdministrativeOfficeFeeEvent(studentCurricularPlan, executionYear);
                }
            }
            if (student.getPerson() != null) {
                for (final PhdIndividualProgramProcess process : student.getPerson().getPhdIndividualProgramProcessesSet()) {
                    if (process.getActiveState() == PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
                        createInsuranceEvent(student.getPerson(), executionYear);
                        break;
                    }
                }
            }
        }

        taskLog("Created %s AdministrativeOfficeFee events\n", AdministrativeOfficeFee_TOTAL_CREATED);
        taskLog("Created %s InsuranceEvent events\n", InsuranceEvent_TOTAL_CREATED);
    }

}