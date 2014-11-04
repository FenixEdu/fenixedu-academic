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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.ConclusionValue;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;

public class RaidesGraduationReportFile extends RaidesGraduationReportFile_Base {

    private static final Logger logger = LoggerFactory.getLogger(RaidesGraduationReportFile.class);

    public RaidesGraduationReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem RAIDES - Graduação";
    }

    @Override
    protected String getPrefix() {
        return "graduationRAIDES";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        ExecutionYear executionYear = getExecutionYear();
        createHeaders(spreadsheet);

        logger.info("BEGIN report for " + getDegreeType().name());

        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansToProcess(executionYear)) {
            final Registration registration = studentCurricularPlan.getRegistration();

            if (registration != null && !registration.isTransition() && !registration.isSchoolPartConcluded()) {

                for (final CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
                    final CycleCurriculumGroup cycleCGroup = studentCurricularPlan.getRoot().getCycleCurriculumGroup(cycleType);
                    if (cycleCGroup != null && !cycleCGroup.isExternal()) {

                        final RegistrationConclusionBean registrationConclusionBean =
                                new RegistrationConclusionBean(registration, cycleCGroup);

                        if (cycleCGroup.isConcluded()) {
                            final ExecutionYear conclusionYear = registrationConclusionBean.getConclusionYear();

                            if (conclusionYear != executionYear && conclusionYear != executionYear.getPreviousExecutionYear()) {
                                continue;
                            }

                        }

                        boolean isToAddRegistration = false;
                        for (RegistrationState state : registration.getRegistrationStates(executionYear)) {
                            if (state.isActive() || state.getStateType() == RegistrationStateType.CONCLUDED) {
                                isToAddRegistration = true;
                                break;
                            }
                        }
                        if (isToAddRegistration
                                && (cycleCGroup.isConcluded(executionYear.getPreviousExecutionYear()) == ConclusionValue.CONCLUDED)) {
                            reportRaidesGraduate(spreadsheet, registration, getFullRegistrationPath(registration), executionYear,
                                    cycleType, true, registrationConclusionBean.getConclusionDate(),
                                    registrationConclusionBean.getAverage());
                        } else if (isToAddRegistration
                                && registration.getLastDegreeCurricularPlan().hasExecutionDegreeFor(executionYear)) {
                            reportRaidesGraduate(spreadsheet, registration, getFullRegistrationPath(registration), executionYear,
                                    cycleType, false, null, registrationConclusionBean.getAverage());
                        }
                    }
                }
            }
        }

        logger.info("END report for " + getDegreeType().name());
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
            for (StudentCurricularPlan studentCurricularPlan : executionDegree.getDegreeCurricularPlan()
                    .getStudentCurricularPlansSet()) {
                if (!studentCurricularPlan.getStartDateYearMonthDay().isAfter(executionYear.getEndDateYearMonthDay())) {
                    result.add(studentCurricularPlan);
                }
            }
        }
    }

    private void createHeaders(final Spreadsheet spreadsheet) {
        RaidesCommonReportFieldsWrapper.createHeaders(spreadsheet);
    }

    private void reportRaidesGraduate(final Spreadsheet sheet, final Registration registration,
            List<Registration> registrationPath, ExecutionYear executionYear, final CycleType cycleType, final boolean concluded,
            final YearMonthDay conclusionDate, BigDecimal average) {
        RaidesCommonReportFieldsWrapper.reportRaidesFields(sheet, registration, registrationPath, executionYear, cycleType,
                concluded, conclusionDate, average, true);
    }
}
