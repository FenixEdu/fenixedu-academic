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

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplication;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacySubmission;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule.ConclusionValue;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        logger.info("BEGIN report for " + getDegreeType().getName().getContent());

        for (final Registration registration : RaidesCommonReportFieldsWrapper.getRegistrationsToProcess(executionYear,
                this.getDegreeType())) {

            if (registration != null && !registration.isTransition() && !registration.isSchoolPartConcluded()) {
                for (final CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
                    final StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(cycleType);
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

                        boolean isToAddRegistration = registration.getRegistrationStates(executionYear).stream()
                                .anyMatch(state -> state.isActive() || state.getStateType() == RegistrationStateType.CONCLUDED);

                        if (isToAddRegistration
                                && (cycleCGroup.isConcluded(executionYear.getPreviousExecutionYear()) == ConclusionValue.CONCLUDED)) {
                            reportRaidesGraduate(spreadsheet, registration, studentCurricularPlan,
                                    getFullRegistrationPath(registration), executionYear,
                                    cycleType, true, registrationConclusionBean.getConclusionDate(), registrationConclusionBean
                                            .getRawGrade().getNumericValue());
                        } else if (isToAddRegistration
                                && (registration.getLastDegreeCurricularPlan().hasExecutionDegreeFor(executionYear) || registration
                                        .hasAnyCurriculumLines(executionYear))) {
                            reportRaidesGraduate(spreadsheet, registration, studentCurricularPlan,
                                    getFullRegistrationPath(registration), executionYear,
                                    cycleType, false, null, registrationConclusionBean.getRawGrade().getNumericValue());
                        }
                    }
                }
            }
        }

        logger.info("END report for " + getDegreeType().getName().getContent());
    }

    private void createHeaders(final Spreadsheet spreadsheet) {
        RaidesCommonReportFieldsWrapper.createHeaders(spreadsheet);
    }

    private void reportRaidesGraduate(final Spreadsheet sheet, final Registration registration,
            StudentCurricularPlan studentCurricularPlan, List<Registration> registrationPath, ExecutionYear executionYear,
            final CycleType cycleType, final boolean concluded, final YearMonthDay conclusionDate, BigDecimal average) {
        final Row row = RaidesCommonReportFieldsWrapper.reportRaidesFields(sheet, registration, studentCurricularPlan, registrationPath,
                executionYear, cycleType, concluded, conclusionDate, average, true);

        registerMobility(registration, executionYear, row);
    }

    private void registerMobility(Registration registration, ExecutionYear executionYear, Row row) {
        boolean inMobility = registration.getIndividualCandidacy() != null && registration.getIndividualCandidacy().isErasmus();
        if (inMobility) {
            final MobilityIndividualApplication mia = (MobilityIndividualApplication) registration.getIndividualCandidacy();
            final MobilityIndividualApplicationProcess applicationProcess = mia.getCandidacyProcess();
            final ExecutionYear startExecutionYear = applicationProcess.getCandidacyExecutionInterval();
            final RegistrationState lastState = registration.getLastState();
            DateTime concludedStudiesDate = null;
            if (lastState.getStateType() == RegistrationStateType.SCHOOLPARTCONCLUDED) {
                concludedStudiesDate = lastState.getStateDate();
            }
            final MobilityApplicationProcess process = (MobilityApplicationProcess)applicationProcess.getCandidacyProcess();
            final ErasmusApplyForSemesterType forSemester = process.getForSemester();
            final Country precedentCountry = mia.getRefactoredPrecedentDegreeInformation().getPrecedentCountry();

            row.setCell("Ano lectivo MobilidadeIN",startExecutionYear.getName());
            row.setCell("Semestre MobilidadeIN", forSemester.getLocalizedName());
            row.setCell("Fim MobilidadeIN", concludedStudiesDate != null ? concludedStudiesDate.toString("dd-MM-yyyy") : "");
            row.setCell("País MobilidadeIN",precedentCountry.getName());
        }
        Optional<RegistrationState> optionalRegistrationState = registration.getRegistrationStates(executionYear).stream()
                .filter(rs -> rs.getStateType() == RegistrationStateType.MOBILITY)
                .findAny();
        boolean outMobility = optionalRegistrationState.isPresent();
        if (outMobility) {
            final RegistrationState registrationState = optionalRegistrationState.get();
            final ExecutionYear startExecutionYear = registrationState.getExecutionYear();
            DateTime concludedMobility = registrationState.getEndDate();
            final Optional<Country> anyCountry = registration.getSortedExternalEnrolments().stream()
                    .filter(e -> e.getExecutionYear() == executionYear)
                    .filter(e -> e.getExternalCurricularCourse().getUnit().getCountry() != Country.readDefault())
                    .map(e -> e.getExternalCurricularCourse().getUnit().getCountry())
                    .findAny();
            Country country = null;
            if (!anyCountry.isPresent()) {
                final Set<OutboundMobilityCandidacySubmission> candidacies = registration.getOutboundMobilityCandidacySubmissionSet().stream()
                        .filter(oc -> oc.getSelectedCandidacy() != null)
                        .collect(Collectors.toSet());
                if (candidacies.size() == 1) {
                    country = candidacies.iterator().next().getSelectedCandidacy().getOutboundMobilityCandidacyContest().getMobilityAgreement()
                            .getUniversityUnit().getCountry();
                }
            } else {
                country = anyCountry.get();
            }

            row.setCell("Ano lectivo MobilidadeOUT", startExecutionYear.getName());
            row.setCell("Semestre MobilidadeIN", ExecutionSemester.readByDateTime(registrationState.getStateDate()).getName());
            row.setCell("Fim MobilidadeOUT", concludedMobility != null ? concludedMobility.toString("dd-MM-yyyy") : "");
            row.setCell("País MobilidadeOUT", country != null ? country.getName() : "");
        }
    }
}
