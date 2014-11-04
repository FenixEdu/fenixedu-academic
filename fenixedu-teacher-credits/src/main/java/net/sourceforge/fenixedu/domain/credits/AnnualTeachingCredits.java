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
package org.fenixedu.academic.domain.credits;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherCredits;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonProfessionalData;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.phd.InternalPhdParticipant;
import org.fenixedu.academic.domain.teacher.DegreeProjectTutorialService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class AnnualTeachingCredits extends AnnualTeachingCredits_Base {

    public AnnualTeachingCredits(Teacher teacher, AnnualCreditsState annualCreditsState) {
        super();
        setTeacher(teacher);
        setAnnualCreditsState(annualCreditsState);
        setHasAnyLimitation(false);
        setCreationDate(new DateTime());
        setRootDomainObject(Bennu.getInstance());
    }

    public boolean isPastResume() {
        return getTeachingCredits() == null && getMasterDegreeThesesCredits() == null && getPhdDegreeThesesCredits() == null
                && getProjectsTutorialsCredits() == null && getManagementFunctionCredits() == null && getOthersCredits() == null
                && getCreditsReduction() == null && getServiceExemptionCredits() == null && getAnnualTeachingLoad() == null
                && getYearCredits() == null && getFinalCredits() == null;
    }

    public static AnnualTeachingCredits readByYearAndTeacher(ExecutionYear executionYear, Teacher teacher) {
        if (executionYear != null) {
            for (AnnualTeachingCredits annualTeachingCredits : teacher.getAnnualTeachingCreditsSet()) {
                if (annualTeachingCredits.getAnnualCreditsState().getExecutionYear().equals(executionYear)) {
                    return annualTeachingCredits;
                }
            }
        }
        return null;
    }

    public boolean isClosed() {
        return getAnnualCreditsState() != null ? getAnnualCreditsState().getIsCreditsClosed() : false;
    }

    @Atomic
    public void calculateCredits() {
        setMasterDegreeThesesCredits(calculateMasterDegreeThesesCredits(getTeacher(), getAnnualCreditsState().getExecutionYear()));
        setPhdDegreeThesesCredits(calculatePhdDegreeThesesCredits(getTeacher(), getAnnualCreditsState().getExecutionYear()));
        setProjectsTutorialsCredits(calculateProjectsTutorialsCredits(getTeacher(), getAnnualCreditsState().getExecutionYear()));

        BigDecimal teachingCredits = BigDecimal.ZERO;
        BigDecimal managementFunctionsCredits = BigDecimal.ZERO;
        BigDecimal reductionServiceCredits = BigDecimal.ZERO;
        BigDecimal serviceExemptionCredits = BigDecimal.ZERO;
        BigDecimal othersCredits = BigDecimal.ZERO;
        BigDecimal annualTeachingLoad = BigDecimal.ZERO;
        BigDecimal yearCredits = BigDecimal.ZERO;
        BigDecimal yearCreditsForFinalCredits = BigDecimal.ZERO;
        BigDecimal annualTeachingLoadFinalCredits = BigDecimal.ZERO;

        boolean hasOrientantionCredits = false;
        boolean hasFinalAndAccumulatedCredits = false;

        for (ExecutionSemester executionSemester : getAnnualCreditsState().getExecutionYear().getExecutionPeriodsSet()) {
            if (PersonProfessionalData.isTeacherActiveForSemester(getTeacher(), executionSemester)
                    || getTeacher().hasTeacherAuthorization(executionSemester.getAcademicInterval())) {
                BigDecimal thisSemesterManagementFunctionCredits =
                        new BigDecimal(TeacherCredits.calculateManagementFunctionsCredits(getTeacher(), executionSemester));
                managementFunctionsCredits = managementFunctionsCredits.add(thisSemesterManagementFunctionCredits);
                serviceExemptionCredits =
                        serviceExemptionCredits.add(new BigDecimal(TeacherCredits.calculateServiceExemptionCredits(getTeacher(),
                                executionSemester)));
                BigDecimal thisSemesterTeachingLoad =
                        new BigDecimal(TeacherCredits.calculateMandatoryLessonHours(getTeacher(), executionSemester));
                annualTeachingLoad = annualTeachingLoad.add(thisSemesterTeachingLoad).setScale(2, BigDecimal.ROUND_HALF_UP);
                TeacherService teacherService =
                        TeacherService.getTeacherServiceByExecutionPeriod(getTeacher(), executionSemester);
                BigDecimal thisSemesterCreditsReduction = BigDecimal.ZERO;
                if (teacherService != null) {
                    teachingCredits = teachingCredits.add(new BigDecimal(teacherService.getTeachingDegreeCredits()));
                    thisSemesterCreditsReduction = teacherService.getReductionServiceCredits();
                    othersCredits = othersCredits.add(new BigDecimal(teacherService.getOtherServiceCredits()));
                }
                reductionServiceCredits = reductionServiceCredits.add(thisSemesterCreditsReduction);
                BigDecimal reductionAndManagement = thisSemesterManagementFunctionCredits.add(thisSemesterCreditsReduction);
                BigDecimal thisSemesterYearCredits = thisSemesterTeachingLoad;
                if (thisSemesterTeachingLoad.compareTo(reductionAndManagement) > 0) {
                    thisSemesterYearCredits = reductionAndManagement;
                } else {
                    setHasAnyLimitation(true);
                }
                yearCredits = yearCredits.add(thisSemesterYearCredits);
                if (canHaveFinalCredits(executionSemester, getTeacher())) {
                    yearCreditsForFinalCredits = yearCreditsForFinalCredits.add(thisSemesterYearCredits);
                    annualTeachingLoadFinalCredits = annualTeachingLoadFinalCredits.add(thisSemesterTeachingLoad);
                    if (executionSemester.getSemester() == 2) {
                        hasFinalAndAccumulatedCredits = true;
                    } else {
                        hasOrientantionCredits = true;
                    }
                }
            }
        }
        setTeachingCredits(teachingCredits);
        setManagementFunctionCredits(managementFunctionsCredits);
        setCreditsReduction(reductionServiceCredits);
        setServiceExemptionCredits(serviceExemptionCredits);
        setOthersCredits(othersCredits);
        setAnnualTeachingLoad(annualTeachingLoad);

        yearCredits = yearCredits.add(teachingCredits).add(serviceExemptionCredits).add(othersCredits);
        yearCreditsForFinalCredits =
                yearCreditsForFinalCredits.add(teachingCredits).add(serviceExemptionCredits).add(othersCredits);
        if (hasOrientantionCredits) {
            yearCredits =
                    yearCredits.add(getMasterDegreeThesesCredits()).add(getPhdDegreeThesesCredits())
                            .add(getProjectsTutorialsCredits()).setScale(2, BigDecimal.ROUND_HALF_UP);
            yearCreditsForFinalCredits =
                    yearCreditsForFinalCredits.add(getMasterDegreeThesesCredits()).add(getPhdDegreeThesesCredits())
                            .add(getProjectsTutorialsCredits());
        }

        setYearCredits(yearCredits);
        BigDecimal accumulatedCredits = BigDecimal.ZERO;
        BigDecimal finalCredits = BigDecimal.ZERO;
        if (hasFinalAndAccumulatedCredits) {
            finalCredits =
                    yearCreditsForFinalCredits.subtract(annualTeachingLoadFinalCredits).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal lastYearAccumulated = getPreviousAccumulatedCredits();
            accumulatedCredits = finalCredits.add(lastYearAccumulated).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        setFinalCredits(finalCredits);
        setAccumulatedCredits(accumulatedCredits);
        setLastModifiedDate(new DateTime());

    }

    private boolean canHaveFinalCredits(ExecutionSemester executionSemester, Teacher teacher) {
        return PersonProfessionalData.isTeacherActiveForSemester(getTeacher(), executionSemester)
                && !ProfessionalCategory.isMonitor(getTeacher(), executionSemester);
    }

    private BigDecimal getPreviousAccumulatedCredits() {
        AnnualTeachingCredits previousAnnualTeachingCredits =
                readByYearAndTeacher(getAnnualCreditsState().getExecutionYear().getPreviousExecutionYear(), getTeacher());
        return previousAnnualTeachingCredits != null ? previousAnnualTeachingCredits.getAccumulatedCredits() : BigDecimal.ZERO;
    }

    public AnnualTeachingCreditsDocument getLastTeacherCreditsDocument(Boolean withConfidencialInformation) {
        AnnualTeachingCreditsDocument lastAnnualTeachingCreditsDocument = null;
        for (AnnualTeachingCreditsDocument annualTeachingCreditsDocument : getAnnualTeachingCreditsDocumentSet()) {
            if (annualTeachingCreditsDocument.getHasConfidencialInformation() == withConfidencialInformation
                    && (lastAnnualTeachingCreditsDocument == null || lastAnnualTeachingCreditsDocument.getUploadTime().isBefore(
                            annualTeachingCreditsDocument.getUploadTime()))) {
                lastAnnualTeachingCreditsDocument = annualTeachingCreditsDocument;
            }
        }
        return lastAnnualTeachingCreditsDocument;
    }

    public static BigDecimal calculateProjectsTutorialsCredits(Teacher teacher, ExecutionYear executionYear) {
        BigDecimal result = BigDecimal.ZERO;
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            final ExecutionSemester executionSemester1 = executionSemester;
            TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester1);
            if (teacherService != null) {
                for (DegreeProjectTutorialService degreeProjectTutorialService : teacherService
                        .getDegreeProjectTutorialServices()) {
                    result = result.add(degreeProjectTutorialService.getDegreeProjectTutorialServiceCredits());
                }
            }
        }
        return result.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal calculatePhdDegreeThesesCredits(Teacher teacher, ExecutionYear executionYear) {
        ExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();
        int guidedThesesNumber = 0;
        double assistantGuidedTheses = 0.0;

        if (!executionYear.getYear().equals("2011/2012")) {
            for (InternalPhdParticipant internalPhdParticipant : teacher.getPerson().getInternalParticipantsSet()) {
                ExecutionYear conclusionYear = internalPhdParticipant.getIndividualProcess().getConclusionYear();
                if (conclusionYear != null && conclusionYear.equals(previousExecutionYear)) {
                    if (internalPhdParticipant.getProcessForGuiding() != null) {
                        guidedThesesNumber++;
                    } else if (internalPhdParticipant.getProcessForAssistantGuiding() != null) {
                        assistantGuidedTheses =
                                assistantGuidedTheses
                                        + (0.5 / internalPhdParticipant.getProcessForAssistantGuiding().getAssistantGuidingsSet()
                                                .size());
                    }

                }
            }
        }
        return BigDecimal.valueOf(2 * (guidedThesesNumber + assistantGuidedTheses)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal calculateMasterDegreeThesesCredits(Teacher teacher, ExecutionYear executionYear) {
        double totalThesisValue = 0.0;
        if (!executionYear.getYear().equals("2011/2012")) {
            for (ThesisEvaluationParticipant participant : teacher.getPerson().getThesisEvaluationParticipantsSet()) {
                Thesis thesis = participant.getThesis();
                if (thesis.isEvaluated()
                        && thesis.hasFinalEnrolmentEvaluation()
                        && thesis.getEvaluation().getYear() == executionYear.getBeginCivilYear()
                        && (participant.getType() == ThesisParticipationType.ORIENTATOR || participant.getType() == ThesisParticipationType.COORIENTATOR)) {
                    totalThesisValue = totalThesisValue + participant.getParticipationCredits();
                }
            }
        }
        return (BigDecimal.valueOf(5).min(new BigDecimal(totalThesisValue * 0.5))).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
