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
package org.fenixedu.academic.domain.reports;

import java.util.List;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaidesDfaReportFile extends RaidesDfaReportFile_Base {

    private static final Logger logger = LoggerFactory.getLogger(RaidesDfaReportFile.class);

    public RaidesDfaReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem RAIDES - DFA";
    }

    @Override
    protected String getPrefix() {
        return "dfaRAIDES";
    }

    @Override
    public DegreeType getDegreeType() {
        return DegreeType.matching(DegreeType::isAdvancedFormationDiploma).get();
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

        ExecutionYear executionYear = getExecutionYear();
        createHeaders(spreadsheet);

        logger.info("BEGIN report for " + getDegreeType().getName().getContent());
        for (final Registration registration : RaidesCommonReportFieldsWrapper.getRegistrationsToProcess(executionYear,
                this.getDegreeType())) {

            if (registration != null && !registration.isTransition()) {

                for (final CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
                    final StudentCurricularPlan studentCurricularPlan =
                            registration.getStudentCurricularPlan(cycleType);
                    final CycleCurriculumGroup cycleCGroup = studentCurricularPlan.getRoot().getCycleCurriculumGroup(cycleType);
                    if (cycleCGroup != null && !cycleCGroup.isExternal()) {
                        final RegistrationConclusionBean registrationConclusionBean =
                                new RegistrationConclusionBean(registration, cycleCGroup);

                        ExecutionYear conclusionYear = null;
                        if (cycleCGroup.isConcluded()) {
                            conclusionYear = registrationConclusionBean.getConclusionYear();

                            if (conclusionYear != executionYear && conclusionYear != executionYear.getPreviousExecutionYear()) {
                                continue;
                            }

                        }
                        boolean isToAddRegistration = false;
                        boolean isActive = false;
                        for (RegistrationState state : registration.getRegistrationStates(executionYear)) {
                            if (state.isActive() || state.getStateType() == RegistrationStateType.CONCLUDED) {
                                isToAddRegistration = true;
                            }
                            isActive |= state.isActive();
                        }
                        if (isToAddRegistration && conclusionYear != null) {
                            reportRaides(spreadsheet, registration, studentCurricularPlan, getFullRegistrationPath(registration),
                                    executionYear, cycleType, true, registrationConclusionBean.getConclusionDate());
                        } else if (isActive) {
                            reportRaides(spreadsheet, registration, studentCurricularPlan, getFullRegistrationPath(registration),
                                    executionYear, cycleType, false, null);
                        }
                    }
                }
            }
        }
    }

    private void createHeaders(Spreadsheet spreadsheet) {
        RaidesCommonReportFieldsWrapper.createHeaders(spreadsheet);
        spreadsheet.setHeader("Total ECTS necessários para a conclusão");
    }

    private void reportRaides(final Spreadsheet sheet, final Registration registration,
            StudentCurricularPlan studentCurricularPlan, List<Registration> registrationPath,
            ExecutionYear executionYear, final CycleType cycleType, final boolean concluded, final YearMonthDay conclusionDate) {

        final Row row =
                RaidesCommonReportFieldsWrapper.reportRaidesFields(sheet, registration, studentCurricularPlan, registrationPath,
                        executionYear, cycleType, concluded, conclusionDate, null, false);

        // Total de ECTS concluídos até ao fim do ano lectivo anterior ao que se referem os dados  no curso actual
        double totalEctsConcludedUntilPreviousYear = studentCurricularPlan.getInternalCycleCurriculumGrops().stream()
                .mapToDouble(cycleCurriculumGroup -> cycleCurriculumGroup.getCreditsConcluded(executionYear.getPreviousExecutionYear()))
                .sum();

        // Total de ECTS necessários para a conclusão
        if (concluded) {
            row.setCell(0);
        } else {
            row.setCell(studentCurricularPlan.getRoot().getDefaultEcts(executionYear) - totalEctsConcludedUntilPreviousYear);
        }
    }
}
