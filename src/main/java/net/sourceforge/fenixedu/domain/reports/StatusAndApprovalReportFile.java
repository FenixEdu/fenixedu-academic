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
package net.sourceforge.fenixedu.domain.reports;

import java.util.HashMap;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.dataTransferObject.student.StudentStatuteBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class StatusAndApprovalReportFile extends StatusAndApprovalReportFile_Base {

    public StatusAndApprovalReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Estatutos e aprovações desde 2003/2004";
    }

    public static class EnrolmentAndAprovalCounter {
        private int enrolments = 0;

        private int aprovals = 0;

        public void count(final Enrolment enrolment) {
            enrolments++;
            if (enrolment.isApproved()) {
                aprovals++;
            }
        }

        public int getEnrolments() {
            return enrolments;
        }

        public int getAprovals() {
            return aprovals;
        }
    }

    public static class EnrolmentAndAprovalCounterMap extends HashMap<ExecutionSemester, EnrolmentAndAprovalCounter> {

        private final ExecutionSemester firstExecutionSemester;

        private final ExecutionSemester lastExecutionSemester;

        public EnrolmentAndAprovalCounterMap(final ExecutionSemester firstExecutionSemester,
                final ExecutionSemester lastExecutionSemester) {
            this.firstExecutionSemester = firstExecutionSemester;
            this.lastExecutionSemester = lastExecutionSemester;
        }

        public EnrolmentAndAprovalCounterMap(final ExecutionSemester firstExecutionSemester,
                final ExecutionSemester lastExecutionSemester, final Registration registration) {
            this(firstExecutionSemester, lastExecutionSemester);
            for (Registration current : getFullRegistrationPath(registration)) {
                for (final StudentCurricularPlan studentCurricularPlan : current.getStudentCurricularPlansSet()) {
                    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                        count(enrolment);
                    }
                }
                for (RegistrationState state : current.getRegistrationStates()) {
                    if (state.isActive()) {
                        // ensure the entries exist if there is an active state
                        // in the target years
                        ExecutionSemester first = state.getExecutionYear().getFirstExecutionPeriod();
                        ExecutionSemester second = state.getExecutionYear().getLastExecutionPeriod();
                        if (firstExecutionSemester.isBeforeOrEquals(first) && first.isBeforeOrEquals(lastExecutionSemester)) {
                            get(first);
                        }
                        if (firstExecutionSemester.isBeforeOrEquals(second) && second.isBeforeOrEquals(lastExecutionSemester)) {
                            get(second);
                        }
                    }
                }
            }
        }

        public void count(final Enrolment enrolment) {
            final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
            if (firstExecutionSemester.isBeforeOrEquals(executionSemester)
                    && executionSemester.isBeforeOrEquals(lastExecutionSemester)) {
                final EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = get(executionSemester);
                enrolmentAndAprovalCounter.count(enrolment);
            }
        }

        @Override
        public EnrolmentAndAprovalCounter get(final Object key) {
            EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = super.get(key);
            if (enrolmentAndAprovalCounter == null) {
                enrolmentAndAprovalCounter = new EnrolmentAndAprovalCounter();
                put((ExecutionSemester) key, enrolmentAndAprovalCounter);
            }
            return enrolmentAndAprovalCounter;
        }

    }

    @Override
    protected String getPrefix() {
        return "statusAndAproval";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        spreadsheet.setHeader("número aluno");
        spreadsheet.setHeader("ano lectivo");
        spreadsheet.setHeader("semestre");
        setDegreeHeaders(spreadsheet);
        spreadsheet.setHeader("estatuto");
        spreadsheet.setHeader("ciclo de estudos");
        spreadsheet.setHeader("regime");
        spreadsheet.setHeader("número inscricoes");
        spreadsheet.setHeader("número aprovacoes");

        final ExecutionSemester firstExecutionSemester =
                ExecutionYear.readExecutionYearByName("2003/2004").getFirstExecutionPeriod();
        final ExecutionSemester lastExecutionSemester = getExecutionYear().getLastExecutionPeriod();
        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (checkDegreeType(getDegreeType(), degree)) {
                if (isActive(degree)) {
                    for (final Registration registration : degree.getRegistrationsSet()) {
                        if (registration.isRegistered(getExecutionYear())) {
                            final EnrolmentAndAprovalCounterMap map =
                                    new EnrolmentAndAprovalCounterMap(firstExecutionSemester, lastExecutionSemester, registration);
                            for (final Entry<ExecutionSemester, EnrolmentAndAprovalCounter> entry : map.entrySet()) {
                                final ExecutionSemester executionSemester = entry.getKey();
                                final EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = entry.getValue();

                                final Row row = spreadsheet.addRow();
                                row.setCell(registration.getNumber().toString());
                                row.setCell(executionSemester.getExecutionYear().getYear());
                                row.setCell(executionSemester.getSemester().toString());
                                setDegreeCells(row, degree);
                                final StringBuilder stringBuilder = new StringBuilder();
                                for (final StudentStatuteBean studentStatuteBean : registration.getStudent().getStatutes(
                                        executionSemester)) {
                                    if (stringBuilder.length() > 0) {
                                        stringBuilder.append(", ");
                                    }
                                    stringBuilder.append(studentStatuteBean.getStudentStatute().getStatuteType());
                                }
                                row.setCell(stringBuilder.toString());
                                CycleType cycleType = registration.getCycleType(executionSemester.getExecutionYear());
                                row.setCell(cycleType != null ? cycleType.getDescription() : "");
                                row.setCell(registration.getRegimeType(executionSemester.getExecutionYear()).getLocalizedName());
                                row.setCell(Integer.toString(enrolmentAndAprovalCounter.getEnrolments()));
                                row.setCell(Integer.toString(enrolmentAndAprovalCounter.getAprovals()));
                            }
                        }
                    }
                }
            }
        }
    }
}
