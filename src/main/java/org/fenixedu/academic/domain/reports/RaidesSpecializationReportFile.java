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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaidesSpecializationReportFile extends RaidesSpecializationReportFile_Base {

    private static final Logger logger = LoggerFactory.getLogger(RaidesSpecializationReportFile.class);

    public RaidesSpecializationReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem RAIDES - Especialização";
    }

    @Override
    protected String getPrefix() {
        return "specializationRAIDES";
    }

    @Override
    public void setDegreeType(DegreeType type) {
        if(!type.isSpecializationDegree()) {
            throw new IllegalArgumentException(BundleUtil.getString(Bundle.GEP,"error.reports.raides.specialization.degree.type"));
        }
        super.setDegreeType(type);
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

        ExecutionYear executionYear = getExecutionYear();
        createHeaders(spreadsheet);

        logger.info("BEGIN report for " + getDegreeType().getName().getContent());
        int count = 0;

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

                        if ((registration.isActive() || registration.isConcluded()) && conclusionYear != null) {
                            reportRaides(spreadsheet, registration, getFullRegistrationPath(registration), executionYear,
                                    cycleType, true, registrationConclusionBean.getConclusionDate());
                        } else if (registration.isActive()) {
                            reportRaides(spreadsheet, registration, getFullRegistrationPath(registration), executionYear,
                                    cycleType, false, null);
                        }
                    }
                }
                count++;
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
            result.addAll(executionDegree.getDegreeCurricularPlan().getStudentCurricularPlansSet());
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
