package net.sourceforge.fenixedu.domain.oldInquiries.teacher;

import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeachingInquiryDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class TeachingInquiry extends TeachingInquiry_Base {

    public TeachingInquiry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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

}
