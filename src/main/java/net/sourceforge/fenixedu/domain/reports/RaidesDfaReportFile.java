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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

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
        return DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

        ExecutionYear executionYear = getExecutionYear();
        createHeaders(spreadsheet);

        logger.info("BEGIN report for " + getDegreeType().name());
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansToProcess(executionYear)) {
            final Registration registration = studentCurricularPlan.getRegistration();

            if (registration != null && !registration.isTransition()) {

                for (final CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
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
                            reportRaides(spreadsheet, registration, getFullRegistrationPath(registration), executionYear,
                                    cycleType, true, registrationConclusionBean.getConclusionDate());
                        } else if (isActive) {
                            reportRaides(spreadsheet, registration, getFullRegistrationPath(registration), executionYear,
                                    cycleType, false, null);
                        }
                    }
                }
            }
        }
    }

    private Set<StudentCurricularPlan> getStudentCurricularPlansToProcess(ExecutionYear executionYear) {
        final Set<StudentCurricularPlan> result = new HashSet<StudentCurricularPlan>();

        collectStudentCurricularPlansFor(executionYear, result);

        if (executionYear.getPreviousExecutionYear() != null) {
            collectStudentCurricularPlansFor(executionYear.getPreviousExecutionYear(), result);
        }

        return result;
    }

    private void collectStudentCurricularPlansFor(final ExecutionYear executionYear, final Set<StudentCurricularPlan> result) {
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesByType(this.getDegreeType())) {
            result.addAll(executionDegree.getDegreeCurricularPlan().getStudentCurricularPlans());
        }
    }

    private void createHeaders(Spreadsheet spreadsheet) {
        RaidesCommonReportFieldsWrapper.createHeaders(spreadsheet);
        spreadsheet.setHeader("Total ECTS necessários para a conclusão");
    }

    private void reportRaides(final Spreadsheet sheet, final Registration registration, List<Registration> registrationPath,
            ExecutionYear executionYear, final CycleType cycleType, final boolean concluded, final YearMonthDay conclusionDate) {

        final Row row =
                RaidesCommonReportFieldsWrapper.reportRaidesFields(sheet, registration, registrationPath, executionYear,
                        cycleType, concluded, conclusionDate, null, false);

        // Total de ECTS concluídos até ao fim do ano lectivo anterior ao que se referem os dados  no curso actual
        double totalEctsConcludedUntilPreviousYear = 0d;
        for (final CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan()
                .getInternalCycleCurriculumGrops()) {
            totalEctsConcludedUntilPreviousYear +=
                    cycleCurriculumGroup.getCreditsConcluded(executionYear.getPreviousExecutionYear());
        }

        // Total de ECTS necessários para a conclusão
        if (concluded) {
            row.setCell(0);
        } else {
            row.setCell(registration.getLastStudentCurricularPlan().getRoot().getDefaultEcts(executionYear)
                    - totalEctsConcludedUntilPreviousYear);
        }
    }
}
