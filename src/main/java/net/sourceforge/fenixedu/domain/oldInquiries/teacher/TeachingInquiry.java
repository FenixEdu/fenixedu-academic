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
package net.sourceforge.fenixedu.domain.oldInquiries.teacher;

import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeachingInquiryDTO;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class TeachingInquiry extends TeachingInquiry_Base {

    public TeachingInquiry() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setResponseDateTime(new DateTime());
    }

    public void setAnswers(final TeachingInquiryDTO inquiryDTO) {
        Map<String, InquiriesQuestion> answersMap = inquiryDTO.buildAnswersMap(false);

        setLoadAndClassTypeContributionToFullfilmentOfCUProgram(answersMap.get(
                "loadAndClassTypeContributionToFullfilmentOfCUProgram").getValueAsInteger());
        setTeacherNumberSuitableForCUOperation(answersMap.get("teacherNumberSuitableForCUOperation").getValueAsInteger());
        setEstablishedScheduleSuitable(answersMap.get("establishedScheduleSuitable").getValueAsInteger());
        setEstablishedScheduleNotSuitableReason(answersMap.get("establishedScheduleNotSuitableReason").getValue());
        setAvailableInfrastructureSuitable(answersMap.get("availableInfrastructureSuitable").getValueAsInteger());
        setAvailableInfrastructureSuitableReason(answersMap.get("availableInfrastructureSuitableReason").getValue());

        setStudentsReadyForFollowingCU(answersMap.get("studentsReadyForFollowingCU").getValueAsInteger());
        setEvaluationMethodSuitableForCUTeachingTypeAndObjective(answersMap.get(
                "evaluationMethodSuitableForCUTeachingTypeAndObjective").getValueAsInteger());
        setDisturbingEventsInClasses(answersMap.get("disturbingEventsInClasses").getValueAsInteger());
        setDisturbingEventsInClassesReasonLowAssiduity(answersMap.get("disturbingEventsInClassesReasonLowAssiduity")
                .getValueAsBoolean());
        setDisturbingEventsInClassesReasonIndiscipline(answersMap.get("disturbingEventsInClassesReasonIndiscipline")
                .getValueAsBoolean());
        setDisturbingEventsInClassesReasonIntermediateEvaluations(answersMap.get(
                "disturbingEventsInClassesReasonIntermediateEvaluations").getValueAsBoolean());
        setDisturbingEventsInClassesDescription(answersMap.get("disturbingEventsInClassesDescription").getValue());

        setSemesterStartAverageStudentNumberInTheorical(answersMap.get("semesterStartAverageStudentNumberInTheorical").getValue());
        setSemesterMiddleAverageStudentNumberInTheorical(answersMap.get("semesterMiddleAverageStudentNumberInTheorical")
                .getValue());
        setSemesterEndAverageStudentNumberInTheorical(answersMap.get("semesterEndAverageStudentNumberInTheorical").getValue());
        setSemesterStartActiveAndInteressedStudentsRatioInTheorical(answersMap.get(
                "semesterStartActiveAndInteressedStudentsRatioInTheorical").getValue());
        setSemesterMiddleActiveAndInteressedStudentsRatioInTheorica(answersMap.get(
                "semesterMiddleActiveAndInteressedStudentsRatioInTheorica").getValue());
        setSemesterEndActiveAndInteressedStudentsRatioInTheorical(answersMap.get(
                "semesterEndActiveAndInteressedStudentsRatioInTheorical").getValue());

        setSemesterStartAverageStudentNumberInProblems(answersMap.get("semesterStartAverageStudentNumberInProblems").getValue());
        setSemesterMiddleAverageStudentNumberInProblems(answersMap.get("semesterMiddleAverageStudentNumberInProblems").getValue());
        setSemesterEndAverageStudentNumberInProblems(answersMap.get("semesterEndAverageStudentNumberInProblems").getValue());
        setSemesterStartActiveAndInteressedStudentsRatioInProblems(answersMap.get(
                "semesterStartActiveAndInteressedStudentsRatioInProblems").getValue());
        setSemesterMiddleActiveAndInteressedStudentsRatioInProblems(answersMap.get(
                "semesterMiddleActiveAndInteressedStudentsRatioInProblems").getValue());
        setSemesterEndActiveAndInteressedStudentsRatioInProblems(answersMap.get(
                "semesterEndActiveAndInteressedStudentsRatioInProblems").getValue());

        setSemesterStartAverageStudentNumberInLabs(answersMap.get("semesterStartAverageStudentNumberInLabs").getValue());
        setSemesterMiddleAverageStudentNumberInLabs(answersMap.get("semesterMiddleAverageStudentNumberInLabs").getValue());
        setSemesterEndAverageStudentNumberInLabs(answersMap.get("semesterEndAverageStudentNumberInLabs").getValue());
        setSemesterStartActiveAndInteressedStudentsRatioInLabs(answersMap.get(
                "semesterStartActiveAndInteressedStudentsRatioInLabs").getValue());
        setSemesterMiddleActiveAndInteressedStudentsRatioInLabs(answersMap.get(
                "semesterMiddleActiveAndInteressedStudentsRatioInLabs").getValue());
        setSemesterEndActiveAndInteressedStudentsRatioInLabs(answersMap.get("semesterEndActiveAndInteressedStudentsRatioInLabs")
                .getValue());

        setSemesterStartAverageStudentNumberInSeminary(answersMap.get("semesterStartAverageStudentNumberInSeminary").getValue());
        setSemesterMiddleAverageStudentNumberInSeminary(answersMap.get("semesterMiddleAverageStudentNumberInSeminary").getValue());
        setSemesterEndAverageStudentNumberInSeminary(answersMap.get("semesterEndAverageStudentNumberInSeminary").getValue());
        setSemesterStartActiveAndInteressedStudentsRatioInSeminary(answersMap.get(
                "semesterStartActiveAndInteressedStudentsRatioInSeminary").getValue());
        setSemesterMiddleActiveAndInteressedStudentsRatioInSeminary(answersMap.get(
                "semesterMiddleActiveAndInteressedStudentsRatioInSeminary").getValue());
        setSemesterEndActiveAndInteressedStudentsRatioInSeminary(answersMap.get(
                "semesterEndActiveAndInteressedStudentsRatioInSeminary").getValue());

        setSemesterStartAverageStudentNumberInProject(answersMap.get("semesterStartAverageStudentNumberInProject").getValue());
        setSemesterMiddleAverageStudentNumberInProject(answersMap.get("semesterMiddleAverageStudentNumberInProject").getValue());
        setSemesterEndAverageStudentNumberInProject(answersMap.get("semesterEndAverageStudentNumberInProject").getValue());
        setSemesterStartActiveAndInteressedStudentsRatioInProject(answersMap.get(
                "semesterStartActiveAndInteressedStudentsRatioInProject").getValue());
        setSemesterMiddleActiveAndInteressedStudentsRatioInProject(answersMap.get(
                "semesterMiddleActiveAndInteressedStudentsRatioInProject").getValue());
        setSemesterEndActiveAndInteressedStudentsRatioInProject(answersMap.get(
                "semesterEndActiveAndInteressedStudentsRatioInProject").getValue());

        setReportDisclosureToAcademicComunity(answersMap.get("reportDisclosureToAcademicComunity").getValueAsBoolean());
        setResultsDisclosureToAcademicComunity(answersMap.get("resultsDisclosureToAcademicComunity").getValueAsBoolean());

        setClarificationOfDoubtsOutsideClasses(answersMap.get("clarificationOfDoubtsOutsideClasses").getValueAsInteger());
        setAutonomousWork(answersMap.get("autonomousWork").getValueAsInteger());
        setStudentsPerformance(answersMap.get("studentsPerformance").getValueAsInteger());
        setClassesAndOtherActivitiesFrequency(answersMap.get("classesAndOtherActivitiesFrequency").getValueAsInteger());
        setClassesAndOtherActivitiesPonctuality(answersMap.get("classesAndOtherActivitiesPonctuality").getValueAsInteger());
        setGlobalQualityOfTeachingInCU(answersMap.get("globalQualityOfTeachingInCU").getValueAsInteger());
        setPedagogicalActivitiesDeveloped(answersMap.get("pedagogicalActivitiesDeveloped").getValueAsInteger());
        setRelativePedagogicalInitiatives(answersMap.get("relativePedagogicalInitiatives").getValue());
        setGeneralCommentToCUOperation(answersMap.get("generalCommentToCUOperation").getValue());

        setNumberOfExams(answersMap.get("numberOfExams").getValueAsInteger());
        setNumberOfTests(answersMap.get("numberOfTests").getValueAsInteger());
        setNumberOfQuizzesAndMiniTests(answersMap.get("numberOfQuizzesAndMiniTests").getValueAsInteger());
        setNumberOfElectronicQuizzes(answersMap.get("numberOfElectronicQuizzes").getValueAsInteger());
        setNumberOfStudyVisitsOrOtherActivitiesReports(answersMap.get("numberOfStudyVisitsOrOtherActivitiesReports")
                .getValueAsInteger());
        setNumberOfWorksOrProjects(answersMap.get("numberOfWorksOrProjects").getValueAsInteger());
        setTeachingLanguage(answersMap.get("teachingLanguage").getValue());
        setOtherTeachingLanguage(answersMap.get("otherTeachingLanguage").getValue());
        setWorkLoadClassification(answersMap.get("workLoadClassification").getValueAsInteger());
        setWorkLoadClassificationReasons(answersMap.get("workLoadClassificationReasons").getValue());
        setPositionOfCUInStudentCurricularPlan(answersMap.get("positionOfCUInStudentCurricularPlan").getValueAsInteger());
        setComprehensionAndKnowledgeOfCU(answersMap.get("comprehensionAndKnowledgeOfCU").getValueAsInteger());
        setComprehensionApplicationOfCU(answersMap.get("comprehensionApplicationOfCU").getValueAsInteger());
        setCriticalSenseAndReflexiveSpirit(answersMap.get("criticalSenseAndReflexiveSpirit").getValueAsInteger());
        setCooperationAndCommunicationCapacity(answersMap.get("cooperationAndCommunicationCapacity").getValueAsInteger());
        setIncreaseAutonoumousLearningCapacity(answersMap.get("increaseAutonoumousLearningCapacity").getValueAsInteger());
        setSocialAndProfessionalContextAnalysis(answersMap.get("socialAndProfessionalContextAnalysis").getValueAsInteger());

        setGlobalClassificationOfThisCU(answersMap.get("globalClassificationOfThisCU").getValueAsInteger());
        setWeakPointsOfCUTeachingProcess(answersMap.get("weakPointsOfCUTeachingProcess").getValue());
        setStrongPointsOfCUTeachingProcess(answersMap.get("strongPointsOfCUTeachingProcess").getValue());
        setFinalCommentsAndImproovements(answersMap.get("finalCommentsAndImproovements").getValue());
        setNegativeResultsResolutionAndImproovementPlanOfAction(answersMap.get(
                "negativeResultsResolutionAndImproovementPlanOfAction").getValue());

        setResponsibleTeacherReportDisclosureToAcademicComunity(answersMap.get(
                "responsibleTeacherReportDisclosureToAcademicComunity").getValueAsBoolean());
    }

    @Override
    public Boolean getResultsDisclosureToAcademicComunity() {
        return super.getResultsDisclosureToAcademicComunity() != null && super.getResultsDisclosureToAcademicComunity();
    }

    public void delete() {
        setProfessorship(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getResponse() {
        org.joda.time.DateTime dt = getResponseDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setResponse(java.util.Date date) {
        if (date == null) {
            setResponseDateTime(null);
        } else {
            setResponseDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasDisturbingEventsInClassesReasonIndiscipline() {
        return getDisturbingEventsInClassesReasonIndiscipline() != null;
    }

    @Deprecated
    public boolean hasSemesterStartAverageStudentNumberInProblems() {
        return getSemesterStartAverageStudentNumberInProblems() != null;
    }

    @Deprecated
    public boolean hasNumberOfQuizzesAndMiniTests() {
        return getNumberOfQuizzesAndMiniTests() != null;
    }

    @Deprecated
    public boolean hasNumberOfExams() {
        return getNumberOfExams() != null;
    }

    @Deprecated
    public boolean hasSemesterStartActiveAndInteressedStudentsRatioInTheorical() {
        return getSemesterStartActiveAndInteressedStudentsRatioInTheorical() != null;
    }

    @Deprecated
    public boolean hasStrongPointsOfCUTeachingProcess() {
        return getStrongPointsOfCUTeachingProcess() != null;
    }

    @Deprecated
    public boolean hasWeakPointsOfCUTeachingProcess() {
        return getWeakPointsOfCUTeachingProcess() != null;
    }

    @Deprecated
    public boolean hasEstablishedScheduleSuitable() {
        return getEstablishedScheduleSuitable() != null;
    }

    @Deprecated
    public boolean hasSemesterStartActiveAndInteressedStudentsRatioInLabs() {
        return getSemesterStartActiveAndInteressedStudentsRatioInLabs() != null;
    }

    @Deprecated
    public boolean hasGlobalClassificationOfThisCU() {
        return getGlobalClassificationOfThisCU() != null;
    }

    @Deprecated
    public boolean hasSemesterEndActiveAndInteressedStudentsRatioInProblems() {
        return getSemesterEndActiveAndInteressedStudentsRatioInProblems() != null;
    }

    @Deprecated
    public boolean hasClassesAndOtherActivitiesFrequency() {
        return getClassesAndOtherActivitiesFrequency() != null;
    }

    @Deprecated
    public boolean hasDisturbingEventsInClassesReasonLowAssiduity() {
        return getDisturbingEventsInClassesReasonLowAssiduity() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleAverageStudentNumberInProject() {
        return getSemesterMiddleAverageStudentNumberInProject() != null;
    }

    @Deprecated
    public boolean hasSemesterStartAverageStudentNumberInLabs() {
        return getSemesterStartAverageStudentNumberInLabs() != null;
    }

    @Deprecated
    public boolean hasResponseDateTime() {
        return getResponseDateTime() != null;
    }

    @Deprecated
    public boolean hasStudentsReadyForFollowingCU() {
        return getStudentsReadyForFollowingCU() != null;
    }

    @Deprecated
    public boolean hasDisturbingEventsInClassesReasonIntermediateEvaluations() {
        return getDisturbingEventsInClassesReasonIntermediateEvaluations() != null;
    }

    @Deprecated
    public boolean hasSemesterEndAverageStudentNumberInTheorical() {
        return getSemesterEndAverageStudentNumberInTheorical() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleAverageStudentNumberInTheorical() {
        return getSemesterMiddleAverageStudentNumberInTheorical() != null;
    }

    @Deprecated
    public boolean hasSemesterStartAverageStudentNumberInSeminary() {
        return getSemesterStartAverageStudentNumberInSeminary() != null;
    }

    @Deprecated
    public boolean hasNumberOfWorksOrProjects() {
        return getNumberOfWorksOrProjects() != null;
    }

    @Deprecated
    public boolean hasSemesterStartAverageStudentNumberInTheorical() {
        return getSemesterStartAverageStudentNumberInTheorical() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleActiveAndInteressedStudentsRatioInTheorica() {
        return getSemesterMiddleActiveAndInteressedStudentsRatioInTheorica() != null;
    }

    @Deprecated
    public boolean hasActiveAndInteressedStudentsRatio() {
        return getActiveAndInteressedStudentsRatio() != null;
    }

    @Deprecated
    public boolean hasDisturbingEventsInClassesDescription() {
        return getDisturbingEventsInClassesDescription() != null;
    }

    @Deprecated
    public boolean hasGeneralCommentToCUOperation() {
        return getGeneralCommentToCUOperation() != null;
    }

    @Deprecated
    public boolean hasComprehensionApplicationOfCU() {
        return getComprehensionApplicationOfCU() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleAverageStudentNumberInProblems() {
        return getSemesterMiddleAverageStudentNumberInProblems() != null;
    }

    @Deprecated
    public boolean hasResponsibleTeacherReportDisclosureToAcademicComunity() {
        return getResponsibleTeacherReportDisclosureToAcademicComunity() != null;
    }

    @Deprecated
    public boolean hasEvaluationMethodSuitableForCUTeachingTypeAndObjective() {
        return getEvaluationMethodSuitableForCUTeachingTypeAndObjective() != null;
    }

    @Deprecated
    public boolean hasDisturbingEventsInClasses() {
        return getDisturbingEventsInClasses() != null;
    }

    @Deprecated
    public boolean hasAnswerDuration() {
        return getAnswerDuration() != null;
    }

    @Deprecated
    public boolean hasWorkLoadClassificationReasons() {
        return getWorkLoadClassificationReasons() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleActiveAndInteressedStudentsRatioInProject() {
        return getSemesterMiddleActiveAndInteressedStudentsRatioInProject() != null;
    }

    @Deprecated
    public boolean hasReportDisclosureToAcademicComunity() {
        return getReportDisclosureToAcademicComunity() != null;
    }

    @Deprecated
    public boolean hasStudentsPerformance() {
        return getStudentsPerformance() != null;
    }

    @Deprecated
    public boolean hasSemesterStartActiveAndInteressedStudentsRatioInProblems() {
        return getSemesterStartActiveAndInteressedStudentsRatioInProblems() != null;
    }

    @Deprecated
    public boolean hasPositionOfCUInStudentCurricularPlan() {
        return getPositionOfCUInStudentCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasClassesAndOtherActivitiesPonctuality() {
        return getClassesAndOtherActivitiesPonctuality() != null;
    }

    @Deprecated
    public boolean hasSemesterEndActiveAndInteressedStudentsRatioInLabs() {
        return getSemesterEndActiveAndInteressedStudentsRatioInLabs() != null;
    }

    @Deprecated
    public boolean hasOtherTeachingLanguage() {
        return getOtherTeachingLanguage() != null;
    }

    @Deprecated
    public boolean hasGlobalQualityOfTeachingInCU() {
        return getGlobalQualityOfTeachingInCU() != null;
    }

    @Deprecated
    public boolean hasSemesterEndAverageStudentNumberInProject() {
        return getSemesterEndAverageStudentNumberInProject() != null;
    }

    @Deprecated
    public boolean hasSemesterStartActiveAndInteressedStudentsRatioInProject() {
        return getSemesterStartActiveAndInteressedStudentsRatioInProject() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleAverageStudentNumberInSeminary() {
        return getSemesterMiddleAverageStudentNumberInSeminary() != null;
    }

    @Deprecated
    public boolean hasSemesterStartActiveAndInteressedStudentsRatioInSeminary() {
        return getSemesterStartActiveAndInteressedStudentsRatioInSeminary() != null;
    }

    @Deprecated
    public boolean hasSemesterEndActiveAndInteressedStudentsRatioInTheorical() {
        return getSemesterEndActiveAndInteressedStudentsRatioInTheorical() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumberOfElectronicQuizzes() {
        return getNumberOfElectronicQuizzes() != null;
    }

    @Deprecated
    public boolean hasFinalCommentsAndImproovements() {
        return getFinalCommentsAndImproovements() != null;
    }

    @Deprecated
    public boolean hasPedagogicalActivitiesDeveloped() {
        return getPedagogicalActivitiesDeveloped() != null;
    }

    @Deprecated
    public boolean hasCooperationAndCommunicationCapacity() {
        return getCooperationAndCommunicationCapacity() != null;
    }

    @Deprecated
    public boolean hasIncreaseAutonoumousLearningCapacity() {
        return getIncreaseAutonoumousLearningCapacity() != null;
    }

    @Deprecated
    public boolean hasRelativePedagogicalInitiatives() {
        return getRelativePedagogicalInitiatives() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleActiveAndInteressedStudentsRatioInSeminary() {
        return getSemesterMiddleActiveAndInteressedStudentsRatioInSeminary() != null;
    }

    @Deprecated
    public boolean hasNumberOfStudyVisitsOrOtherActivitiesReports() {
        return getNumberOfStudyVisitsOrOtherActivitiesReports() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleActiveAndInteressedStudentsRatioInLabs() {
        return getSemesterMiddleActiveAndInteressedStudentsRatioInLabs() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleAverageStudentNumberInLabs() {
        return getSemesterMiddleAverageStudentNumberInLabs() != null;
    }

    @Deprecated
    public boolean hasCriticalSenseAndReflexiveSpirit() {
        return getCriticalSenseAndReflexiveSpirit() != null;
    }

    @Deprecated
    public boolean hasNumberOfTests() {
        return getNumberOfTests() != null;
    }

    @Deprecated
    public boolean hasSemesterMiddleActiveAndInteressedStudentsRatioInProblems() {
        return getSemesterMiddleActiveAndInteressedStudentsRatioInProblems() != null;
    }

    @Deprecated
    public boolean hasSemesterEndAverageStudentNumberInLabs() {
        return getSemesterEndAverageStudentNumberInLabs() != null;
    }

    @Deprecated
    public boolean hasSemesterEndAverageStudentNumberInSeminary() {
        return getSemesterEndAverageStudentNumberInSeminary() != null;
    }

    @Deprecated
    public boolean hasAvailableInfrastructureSuitableReason() {
        return getAvailableInfrastructureSuitableReason() != null;
    }

    @Deprecated
    public boolean hasSemesterEndActiveAndInteressedStudentsRatioInSeminary() {
        return getSemesterEndActiveAndInteressedStudentsRatioInSeminary() != null;
    }

    @Deprecated
    public boolean hasAutonomousWork() {
        return getAutonomousWork() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasTeachingLanguage() {
        return getTeachingLanguage() != null;
    }

    @Deprecated
    public boolean hasLoadAndClassTypeContributionToFullfilmentOfCUProgram() {
        return getLoadAndClassTypeContributionToFullfilmentOfCUProgram() != null;
    }

    @Deprecated
    public boolean hasDisturbingEventsInClassesReasonInfrastructure() {
        return getDisturbingEventsInClassesReasonInfrastructure() != null;
    }

    @Deprecated
    public boolean hasClarificationOfDoubtsOutsideClasses() {
        return getClarificationOfDoubtsOutsideClasses() != null;
    }

    @Deprecated
    public boolean hasNegativeResultsResolutionAndImproovementPlanOfAction() {
        return getNegativeResultsResolutionAndImproovementPlanOfAction() != null;
    }

    @Deprecated
    public boolean hasEstablishedScheduleNotSuitableReason() {
        return getEstablishedScheduleNotSuitableReason() != null;
    }

    @Deprecated
    public boolean hasComprehensionAndKnowledgeOfCU() {
        return getComprehensionAndKnowledgeOfCU() != null;
    }

    @Deprecated
    public boolean hasTeacherNumberSuitableForCUOperation() {
        return getTeacherNumberSuitableForCUOperation() != null;
    }

    @Deprecated
    public boolean hasSemesterStartAverageStudentNumberInProject() {
        return getSemesterStartAverageStudentNumberInProject() != null;
    }

    @Deprecated
    public boolean hasSocialAndProfessionalContextAnalysis() {
        return getSocialAndProfessionalContextAnalysis() != null;
    }

    @Deprecated
    public boolean hasWorkLoadClassification() {
        return getWorkLoadClassification() != null;
    }

    @Deprecated
    public boolean hasResultsDisclosureToAcademicComunity() {
        return getResultsDisclosureToAcademicComunity() != null;
    }

    @Deprecated
    public boolean hasSemesterEndAverageStudentNumberInProblems() {
        return getSemesterEndAverageStudentNumberInProblems() != null;
    }

    @Deprecated
    public boolean hasAvailableInfrastructureSuitable() {
        return getAvailableInfrastructureSuitable() != null;
    }

    @Deprecated
    public boolean hasSemesterEndActiveAndInteressedStudentsRatioInProject() {
        return getSemesterEndActiveAndInteressedStudentsRatioInProject() != null;
    }

}
